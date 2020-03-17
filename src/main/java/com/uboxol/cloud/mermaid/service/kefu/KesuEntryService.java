package com.uboxol.cloud.mermaid.service.kefu;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.uboxol.cloud.mermaid.api.req.EntryComplaint;
import com.uboxol.cloud.mermaid.api.req.Query;
import com.uboxol.cloud.mermaid.api.res.EntryResult;
import com.uboxol.cloud.mermaid.api.res.StatusResult;
import com.uboxol.cloud.mermaid.db.entity.bx.AlipayNotifyImage;
import com.uboxol.cloud.mermaid.db.entity.bx.AlipayOrder;
import com.uboxol.cloud.mermaid.db.entity.kefu.BOrder;
import com.uboxol.cloud.mermaid.db.entity.kefu.CustomerComplaint;
import com.uboxol.cloud.mermaid.db.entity.kefu.Image;
import com.uboxol.cloud.mermaid.db.repository.kefu.BOrderRepository;
import com.uboxol.cloud.mermaid.db.repository.kefu.CustomerComplaintRepository;
import com.uboxol.cloud.mermaid.db.repository.kefu.ImageRepository;
import com.uboxol.cloud.mermaid.service.bx.AlipayOrderService;
import com.uboxol.cloud.mermaid.utils.HyfUtils;
import com.uboxol.gen.api.Cds2APIService;
import com.uboxol.gen.api.Order;

import cn.ubox.cloud.node.NodeRemoteService;
import cn.ubox.cloud.node.data.NodeWithOrg;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class KesuEntryService {
	
	private final CustomerComplaintRepository customerComplaintRepository;
	private final BOrderRepository bOrderRepository;
	private final Cds2APIService.Iface apiService;
	private final NodeRemoteService.Iface nodeApiService;
	private final AlipayOrderService alipayOrderService;
	private final ImageRepository imageRepository;
	private final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");
	private static final String PATH = "https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=";
    
    
    @Autowired
    public KesuEntryService(final Cds2APIService.Iface apiService, final NodeRemoteService.Iface nodeApiService,final CustomerComplaintRepository customerComplaintRepository, 
    		final BOrderRepository bOrderRepository,final AlipayOrderService alipayOrderService,final ImageRepository imageRepository) {
    	this.apiService = apiService;
    	this.nodeApiService = nodeApiService;
        this.customerComplaintRepository = customerComplaintRepository;
        this.bOrderRepository = bOrderRepository;
        this.alipayOrderService = alipayOrderService;
        this.imageRepository = imageRepository;
    }
    
    public EntryResult entry(EntryComplaint req) {
    	EntryResult entryResult = new EntryResult();
    	List<AlipayOrder> alipayOrderlist = null;
    	BigDecimal amount = BigDecimal.ZERO;
    	List<String> ulist = new ArrayList<String>();
    	try {
    		Long orderId = req.getOrderId();
	    	if(orderId!=null) {
	    		BOrder bOrder = bOrderRepository.findByOrderId(orderId);
	    		logger.info("查询当前单号:"+orderId+"在b_order表里是否存在，BOrder结果"+bOrder);
	    		if(bOrder == null) {
	    			//如果表里没有,就查出trans_id把所有子单都入库，并且给当前子单子编号。其他不给。配置处理人时候  过滤没有子编号的单子
	    			AlipayOrder alipayOrder = alipayOrderService.findAlipayOrderByOrderId(orderId);
	    			if(alipayOrder==null) throw new RuntimeException("alipay_order表中没有对应的订单");
	    			String transactionId = alipayOrder.getTransactionId();
	    			
	    			Query query = new Query();query.setTransactionId(transactionId);
	    			alipayOrderlist = alipayOrderService.findAlipayOrder(query);
	    			List<Long> collect = alipayOrderlist.stream().map(AlipayOrder::getOrderId).collect(Collectors.toList());
	    			List<Order> orderList = apiService.getOrderListById(collect);
	    			if (orderList.size()==0) throw new RuntimeException("b_order表中没有对应的订单");
	    			
	    			logger.info("开始存入主单信息");
	    			CustomerComplaint customerComplaint = customerComplaintRepository.findByTransactionId(transactionId);
	    			if(customerComplaint == null) {
	    				customerComplaint = new CustomerComplaint();
	    			}
	    			customerComplaint.setKsNo(SDF.format(new Date()));
	    			customerComplaint.setTransactionId(transactionId);
	    			customerComplaint.setEductionTime(alipayOrder.getCreateTime());
	    			if(!StringUtil.isNullOrEmpty(alipayOrderService.findByTransactionId(transactionId))) {
	    				ulist.add(alipayOrderService.findByTransactionId(transactionId));
			    	}
	    			for (Order order : orderList) {
		            	if(order.getUid()>=0) {
		            		ulist.add(String.valueOf(order.getUid()));
		    	    	}
		            }
	    			ulist = HyfUtils.removeDuplicate(ulist);
	    			for(int i=0;i<ulist.size();i++) {
	    				if(i==ulist.size()-1) {
	    					customerComplaint.setUId2(ulist.get(i));
	    				}else {
	    					customerComplaint.setUId1(ulist.get(i));
	    				}
	    			}
	    			customerComplaint.setUids(HyfUtils.listToString(ulist));//uid去除，并转为字符串类型入库
	    			customerComplaint.setOrderNum(orderList.size());
	    			customerComplaint.setTradeNo(alipayOrder.getTradeNo());
	    			
	    			List<BigDecimal> costs = alipayOrderlist.stream().map(AlipayOrder::getCost).collect(Collectors.toList());
		            for (BigDecimal cost : costs) {
		                amount = amount.add(cost);
		            }
	    			customerComplaint.setEductionMoney(amount);
	    			customerComplaint.setCcTime(new Timestamp(new Date().getTime()));
	    			customerComplaint.setMachineNo(alipayOrder.getVmId());
	    			
	    			NodeWithOrg nodeWithOrg=null;
					try {
						if(orderList.get(0).getNodeId()>=0L) {
							nodeWithOrg = nodeApiService.getNodeWithOrgById(orderList.get(0).getNodeId());
						}
					} catch (Exception e) {
						logger.error("点位获取分公司"+e.getMessage(),e);
					}
		            if(nodeWithOrg == null || nodeWithOrg.getOrgs()==null) {
		            	customerComplaint.setBranchCompany("");//分公司
		            }else {
		            	customerComplaint.setBranchCompany(nodeWithOrg.getOrgs().getOrg4Name());//分公司
		            }
	    			
	    			customerComplaintRepository.saveAndFlush(customerComplaint);
	    			
	    			logger.info("开始存入所有子单信息");
	    			for(Order o : orderList) {
	    				bOrder = new BOrder();
	    				bOrder.setTransactionId(transactionId);
	    				if(o.getOrderId()==orderId) {
	    					bOrder.setKsSubNo(SDF.format(new Date())+"-"+orderId);
	    					bOrder.setContact(req.getPhone());
	    		    		bOrder.setCcPhenomenon(req.getReason().equals("4")?"多扣款":req.getReason().equals("1")?"未出货":"其他");//4多扣款 1未出货 3其他
	    		    		bOrder.setCcAppeal(req.getRemark());
	    		    		bOrder.setCcChannel(req.getChannelId().equals("1")?"支付宝小程序":req.getChannelId());
	    		    		bOrder.setCcStatus(1);
		    				bOrder.setCcTime(new Timestamp(new Date().getTime()));
		    				bOrder.setHandleStatus(1);
	    				}
//	    				bOrder.setHandleTime(new Timestamp(new Date().getTime()));
//	    				bOrder.setVisitStatus(1);
//	    				bOrder.setVisitTime(new Timestamp(new Date().getTime()));
		    			bOrder.setOrderId(o.getOrderId());
			    		bOrder.setOrderTime(new Timestamp(new Date().getTime()));
			    		bOrder.setProductPrice(new BigDecimal(o.getCost()));
			    		bOrder.setProductName(o.getProductFullName());
			    		bOrder.setUid(o.getUid());
			    		
						bOrderRepository.saveAndFlush(bOrder);
	    			}
	    			
	    			logger.info("开始存入开柜前图片信息");
			    	List<AlipayNotifyImage> list = alipayOrderService.findPictures(transactionId, "1");
			    	for (int i = 0; i < list.size(); i++) {
			    		Image image = imageRepository.findByTransactionIdAndImageIdAndFlag(transactionId, PATH+list.get(i).getImageId(), "1");
			            if(image==null) {
			            	image = new Image();
			            }
			            image.setTransactionId(transactionId);
			            image.setImageId(PATH+list.get(i).getImageId());
			            image.setNum(i+1);
			            image.setFlag("1");
			            imageRepository.saveAndFlush(image);
			        }
			    	
			    	logger.info("开始存入开柜后图片信息");
					List<AlipayNotifyImage> list2 = alipayOrderService.findPictures(transactionId, "2");
			    	for (int i = 0; i < list2.size(); i++) {
			    		Image image = imageRepository.findByTransactionIdAndImageIdAndFlag(transactionId, PATH+list2.get(i).getImageId(), "2");
			            if(image==null) {
			            	image = new Image();
			            }
			            image.setTransactionId(transactionId);
			            image.setImageId(PATH+list2.get(i).getImageId());
			            image.setNum(i+1);
			            image.setFlag("2");
			            imageRepository.saveAndFlush(image);
			        }
			    	
			    	//更新主单信息状态
			    	//客诉状态 (1进行中 9已闭环)
			    	List<BOrder> bOrderList = bOrderRepository.findByTransactionId(transactionId);
			    	List<Integer> ksStatus = bOrderList.stream().map(BOrder::getCcStatus).collect(Collectors.toList());
					ksStatus.removeAll(Collections.singleton(null)); 
					if(ksStatus.size()>0) {
						if(ksStatus.contains(1)) {
							customerComplaint.setCcStatus(1);
						}else {
							if(ksStatus.contains(9)) {
								customerComplaint.setCcStatus(9);
							}
						}
					}
					//客诉时间
					List<Timestamp> times = bOrderList.stream().map(BOrder::getCcTime).collect(Collectors.toList());
					times.removeAll(Collections.singleton(null)); 
					if(times.size()>0) {
						customerComplaint.setCcTime(times.get(times.size()-1));
					}
					//回访状态 （1需回访 2回访中 3已回访）
					//回访时间
					//处理状态（1待处理 2处理中 3已处理）
					List<Integer> clStatus = bOrderList.stream().map(BOrder::getHandleStatus).collect(Collectors.toList());
					clStatus.removeAll(Collections.singleton(null)); 
					if(clStatus.size()>0) {
						if(clStatus.contains(2)) {
							customerComplaint.setHandleStatus(2);
						}else {
							if(clStatus.contains(1)) {
								customerComplaint.setHandleStatus(1);
							}else {
								customerComplaint.setHandleStatus(3);
							}
						}
					}
					
					//处理时间
					customerComplaintRepository.saveAndFlush(customerComplaint);
					
			    	entryResult.setCode("200");
	    			entryResult.setMsg("成功");
	    			entryResult.setCustomerComplaint(customerComplaint);
	    		}else {
	    			if(StringUtils.isEmpty(bOrder.getKsSubNo())) {
    					bOrder.setKsSubNo(SDF.format(new Date())+"-"+orderId);
    					bOrder.setCcTime(new Timestamp(new Date().getTime()));
    				}
	    			bOrder.setCcStatus(1);
	    			bOrder.setHandleStatus(1);
		    		bOrder.setContact(req.getPhone());
		    		bOrder.setCcPhenomenon(req.getReason().equals("4")?"多扣款":req.getReason().equals("1")?"未出货":"其他");//4多扣款 1未出货 3其他
		    		bOrder.setCcAppeal(req.getRemark());
		    		bOrder.setCcChannel(req.getChannelId().equals("1")?"支付宝小程序":req.getChannelId());
					bOrderRepository.saveAndFlush(bOrder);
	    			
	    			String transactionId = bOrder.getTransactionId();
	    			CustomerComplaint customerComplaint = customerComplaintRepository.findByTransactionId(transactionId);
	    			if(customerComplaint==null) throw new RuntimeException("customer_complaint表中没有对应的订单");
	    			//查询表里有，状态什么都没设置
	    			//主单又怎么更新状态
	    			//更新主单信息状态
			    	//客诉状态 (1进行中 9已闭环)
			    	List<BOrder> bOrderList = bOrderRepository.findByTransactionId(transactionId);
			    	List<Integer> ksStatus = bOrderList.stream().map(BOrder::getCcStatus).collect(Collectors.toList());
					ksStatus.removeAll(Collections.singleton(null)); 
					if(ksStatus.size()>0) {
						if(ksStatus.contains(1)) {
							customerComplaint.setCcStatus(1);
						}else {
							if(ksStatus.contains(9)) {
								customerComplaint.setCcStatus(9);
							}
						}
					}
					//客诉时间
					List<Timestamp> times = bOrderList.stream().map(BOrder::getCcTime).collect(Collectors.toList());
					times.removeAll(Collections.singleton(null)); 
					if(times.size()>0) {
						customerComplaint.setCcTime(times.get(times.size()-1));
					}
					//回访状态 （1需回访 2回访中 3已回访）
					//回访时间
					//处理状态（1待处理 2处理中 3已处理）
					List<Integer> clStatus = bOrderList.stream().map(BOrder::getHandleStatus).collect(Collectors.toList());
					clStatus.removeAll(Collections.singleton(null)); 
					if(clStatus.size()>0) {
						if(clStatus.contains(2)) {
							customerComplaint.setHandleStatus(2);
						}else {
							if(clStatus.contains(1)) {
								customerComplaint.setHandleStatus(1);
							}else {
								customerComplaint.setHandleStatus(3);
							}
						}
					}
					
					//处理时间
					customerComplaintRepository.saveAndFlush(customerComplaint);
	    			
	    			
	    			entryResult.setCode("200");
	    			entryResult.setMsg("成功(客诉已经存在)");
	    			entryResult.setCustomerComplaint(customerComplaint);
	    		}
	    		
			}else {
				entryResult.setCode("201");
    			entryResult.setMsg("传入的order_id值为空");
			}
    	 } catch (Exception e) {
             logger.error("推送多平台录入客诉出错:{}", e.getMessage(), e);
		 }
		return entryResult;
    }
    
    public StatusResult status(EntryComplaint req) {
    	StatusResult statusResult = new StatusResult();
    	BOrder bOrder = null; String code = ""; String msg ="";
    	try {
    		Long orderId = req.getOrderId();
    		logger.info("当前单号:"+orderId);
	    	if(orderId!=null) {
	    		bOrder = bOrderRepository.findByOrderId(orderId);
	    		if(bOrder==null) {
	    			code = "201";
	    			msg = "表里没有查到当前子单";
	    		}else {
	    			code = "200";
	    			msg = "成功";
	    			logger.info("查询当前单号:"+orderId+"在b_order表里存在，客诉状态"+bOrder.getCcStatus()+"(1进行中,9已闭环)");
	    		}
			}else {
				code = "201";
    			msg = "请求传入的子单为空";
			}
	    	statusResult.setCode(code);
	    	statusResult.setMsg(msg);
	    	statusResult.setBOrder(bOrder);
	    	
    	 } catch (Exception e) {
             logger.error("推送多平台录入客诉出错:{}", e.getMessage(), e);
		 }
		return statusResult;
    }
	
}
