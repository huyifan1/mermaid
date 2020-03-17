package com.uboxol.cloud.mermaid.service.kefu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uboxol.cloud.mermaid.api.req.Query;
import com.uboxol.cloud.mermaid.api.req.SubOrder;
import com.uboxol.cloud.mermaid.api.res.BasicInformation;
import com.uboxol.cloud.mermaid.api.res.QueryBack;
import com.uboxol.cloud.mermaid.db.entity.kefu.BOrder;
import com.uboxol.cloud.mermaid.db.entity.kefu.CustomerComplaint;
import com.uboxol.cloud.mermaid.db.entity.kefu.Image;
import com.uboxol.cloud.mermaid.db.repository.kefu.BOrderRepository;
import com.uboxol.cloud.mermaid.db.repository.kefu.CustomerComplaintRepository;
import com.uboxol.cloud.mermaid.db.repository.kefu.ImageRepository;
import com.uboxol.cloud.mermaid.utils.HyfUtils;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class KesuQueryService {
	
	private final CustomerComplaintRepository customerComplaintRepository;
	private final BOrderRepository bOrderRepository;
    private final ImageRepository imageRepository;
    
    private static final String PATH = "https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=";
    
    @Autowired
    public KesuQueryService(final CustomerComplaintRepository customerComplaintRepository, final BOrderRepository bOrderRepository,final ImageRepository imageRepository) {
        this.customerComplaintRepository = customerComplaintRepository;
        this.bOrderRepository = bOrderRepository;
        this.imageRepository = imageRepository;
    }
    
    public QueryBack findAll(Query req) {
    	QueryBack queryBack = new QueryBack();
		try {
			if(!StringUtil.isNullOrEmpty(req.getTransactionId())) {
				CustomerComplaint c = customerComplaintRepository.findByTransactionId(req.getTransactionId());
				if(c==null) {
					queryBack = null;
				}else {
					queryBack = reback(c);
				}
			}else if(!StringUtil.isNullOrEmpty(req.getTradeNo())) {
				CustomerComplaint c = customerComplaintRepository.findByTradeNo(req.getTradeNo());
				if(c==null) {
					queryBack = null;
				}else {
					queryBack = reback(c);
				}
			}else if(req.getOrderId()!=null) {
				BOrder bOrder = bOrderRepository.findByOrderId(req.getOrderId());
//				String transactionId = bOrder.getTransactionId();
//				CustomerComplaint c = customerComplaintRepository.findByTransactionId(transactionId);
				if(bOrder==null) {
					queryBack = null;
				}else {
					String transactionId = bOrder.getTransactionId();
					CustomerComplaint c = customerComplaintRepository.findByTransactionId(transactionId);
					if(c==null) {
						queryBack = null;
					}else {
						queryBack = reback(c);
					}
				}
			}
		} catch (Exception e) {
            logger.error("录入客诉插叙本地数据信息出错:{}", e.getMessage(), e);
		 }
		return queryBack;
	}
    
    public QueryBack reback(CustomerComplaint c) {
    	QueryBack queryBack = new QueryBack();
    	BasicInformation basicInformation = new BasicInformation();
    	String[] imageBefore;
		String[] imageAfter;
    	try {
    		basicInformation.setTransactionId(c.getTransactionId());//transactionID
        	basicInformation.setTradeNo(c.getTradeNo());//支付宝交易单号
        	basicInformation.setVmId(c.getMachineNo());//机器编号(b_order表里也有)
        	basicInformation.setCreateTime(c.getCcTime());//发生时间
        	basicInformation.setAmount(c.getEductionMoney());//订单金额
        	List<String> uids = new ArrayList<String>();
        	if(c.getUids().contains(",")==true) {
        		String[] us = c.getUids().split(",");
        		for(String s : us) {
        			uids.add(s);
        		}
        	}else {
        		uids.add(c.getUids());
        	}
        	basicInformation.setUserUID(uids);//用户UID
        	basicInformation.setDeductionTime(c.getEductionTime());
        	basicInformation.setBrachCompany(c.getBranchCompany());//分公司
        	
            queryBack.setTrade(basicInformation);
            queryBack.setOrderNum(c.getOrderNum());
            List<BOrder> list = bOrderRepository.findByTransactionId(c.getTransactionId());
            List<Integer> kesuList = list.stream().map(BOrder::getCcStatus).collect(Collectors.toList());
            int kesuNums = 0;
            for(int i = 0;i<kesuList.size();i++) {
            	if(kesuList.get(i)!=null) {
            		kesuNums +=1;
            	}
            }
            queryBack.setKesuNum(kesuNums);
            List<SubOrder> subOrderList = HyfUtils.convertList2List(list, SubOrder.class);
            queryBack.setSubOrderList(subOrderList);
            
          //开柜前图片
        	List<Image> iList = imageRepository.findByTransactionIdAndFlagOrderByNumAsc(c.getTransactionId(), "1");
        	imageBefore = new String[iList.size()];
        	for (int i = 0; i < iList.size(); i++) {
        		Image image = iList.get(i);
        		imageBefore[i]=PATH+image.getImageId();
            }
        	queryBack.setImageBefore(imageBefore);
        	
        	//开柜后图片
        	List<Image> list2 = imageRepository.findByTransactionIdAndFlagOrderByNumAsc(c.getTransactionId(), "2");
    		imageAfter = new String[list2.size()];
        	for (int i = 0; i < list2.size(); i++) {
        		Image image = list2.get(i);
        		imageAfter[i]=PATH+image.getImageId();
            }
        	queryBack.setImageAfter(imageAfter);
    	} catch (Exception e) {
            logger.error("录入客诉插叙本地数据信息出错:{}", e.getMessage(), e);
		 }
		return queryBack;
    }
    
}
