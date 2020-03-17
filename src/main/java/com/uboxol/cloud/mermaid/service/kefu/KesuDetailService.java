package com.uboxol.cloud.mermaid.service.kefu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uboxol.cloud.mermaid.api.req.SubOrder;
import com.uboxol.cloud.mermaid.api.req.SubOrderAll;
import com.uboxol.cloud.mermaid.api.res.BasicInformation;
import com.uboxol.cloud.mermaid.api.res.QueryBack;
import com.uboxol.cloud.mermaid.db.entity.kefu.BOrder;
import com.uboxol.cloud.mermaid.db.entity.kefu.CustomerComplaint;
import com.uboxol.cloud.mermaid.db.entity.kefu.Image;
import com.uboxol.cloud.mermaid.db.repository.kefu.BOrderRepository;
import com.uboxol.cloud.mermaid.db.repository.kefu.CustomerComplaintRepository;
import com.uboxol.cloud.mermaid.db.repository.kefu.ImageRepository;
import com.uboxol.cloud.util.BeanUtils;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class KesuDetailService {
	
	private final CustomerComplaintRepository customerComplaintRepository;
	private final BOrderRepository bOrderRepository;
    private final ImageRepository imageRepository;
    
    private static final String PATH = "https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=";
    
    
    @Autowired
    public KesuDetailService(final CustomerComplaintRepository customerComplaintRepository, final BOrderRepository bOrderRepository,final ImageRepository imageRepository) {
        this.customerComplaintRepository = customerComplaintRepository;
        this.bOrderRepository = bOrderRepository;
        this.imageRepository = imageRepository;
    }
	
	public QueryBack detail(String transactionId) {
		QueryBack queryBack = new QueryBack();
		try {
			CustomerComplaint customerComplaint = customerComplaintRepository.findByTransactionId(transactionId);
			if(customerComplaint==null) throw new RuntimeException("customer_complaint表中没有对应的订单");
			BasicInformation basicInformation = new BasicInformation();
			basicInformation.setTransactionId(transactionId);//transactionID
	    	basicInformation.setTradeNo(customerComplaint.getTradeNo());//支付宝交易单号
	    	basicInformation.setVmId(customerComplaint.getMachineNo());//机器编号(b_order表里也有)
	    	basicInformation.setCreateTime(customerComplaint.getHandleTime());//发生时间
	    	basicInformation.setDeductionTime(customerComplaint.getEductionTime());
	    	basicInformation.setAmount(customerComplaint.getEductionMoney());//订单金额
	    	String[] s= customerComplaint.getUids().split(",");
	    	List<String> slist = new ArrayList<String>();
	    	for (int a = 0; a < s.length; a++) {
	    		slist.add(s[a]);
	        }
	    	basicInformation.setUserUID(slist);//用户UID
	    	basicInformation.setBrachCompany(customerComplaint.getBranchCompany());//分公司
	    	
	    	queryBack.setKsNo(customerComplaint.getKsNo());
	    	queryBack.setTrade(basicInformation);
	    	
	    	List<SubOrder> subOrderList = new ArrayList<SubOrder>();
	    	List<BOrder> bOrderList = bOrderRepository.findByTransactionId(transactionId);
	    	
	    	for(BOrder b : bOrderList) {
	    		SubOrder so = new SubOrder();
	    		BeanUtils.copyProperties(b, so);
	    		subOrderList.add(so);
	    	}
	    	
	    	List<Integer> ksStatus = bOrderList.stream().map(BOrder::getCcStatus).collect(Collectors.toList());
	    	int kesuNums = 0;
            for(int i = 0;i<ksStatus.size();i++) {
            	if(ksStatus.get(i)!=null) {
            		kesuNums +=1;
            	}
            }
            queryBack.setKesuNum(kesuNums);
	    	queryBack.setOrderNum(customerComplaint.getOrderNum());
	    	queryBack.setSubOrderList(subOrderList);
	    	
	    	String[] imageBefore;
			String[] imageAfter;
	    	List<Image> beforeList = imageRepository.findByTransactionIdAndFlagOrderByNumAsc(transactionId, "1");
	    	imageBefore = new String[beforeList.size()];
	    	for (int i = 0; i < beforeList.size(); i++) {
	    		Image image = beforeList.get(i);
	    		imageBefore[i]=PATH+image.getImageId();
	        }
	    	List<Image> afterList = imageRepository.findByTransactionIdAndFlagOrderByNumAsc(transactionId, "2");
	    	imageAfter = new String[afterList.size()];
	    	for (int i = 0; i < afterList.size(); i++) {
	    		Image image = afterList.get(i);
	    		imageAfter[i]=PATH+image.getImageId();
	        }
	    	queryBack.setImageBefore(imageBefore);
	    	queryBack.setImageAfter(imageAfter);
		
		} catch (Exception e) {
            logger.error("客诉信息查询出错:{}", e.getMessage(), e);
		 }
		
		return queryBack;
	}
	
	public QueryBack detailAll(String transactionId) {
		QueryBack queryBack = new QueryBack();
		try {
			CustomerComplaint customerComplaint = customerComplaintRepository.findByTransactionId(transactionId);
			if(customerComplaint==null) throw new RuntimeException("customer_complaint表中没有对应的订单");
	    	
	    	List<SubOrderAll> subOrderAllList = new ArrayList<SubOrderAll>();
	    	List<BOrder> bOrderList = bOrderRepository.findByTransactionId(transactionId);
	    	
	    	for(BOrder b : bOrderList) {
	    		SubOrderAll so = new SubOrderAll();
	    		BeanUtils.copyProperties(b, so);
	    		
	    		so.setTransactionId(transactionId);
	    		so.setThreeSerialNo(customerComplaint.getThreeSerialNo());
	    		so.setEductionTime(customerComplaint.getEductionTime());
	    		so.setMachineNo(customerComplaint.getMachineNo());
	    		so.setBranchCompany(customerComplaint.getBranchCompany());
	    		so.setUids(customerComplaint.getUids());
	    		so.getServiceReminder();
	    		
	    		subOrderAllList.add(so);
	    	}
	    	
	    	List<Integer> ksStatus = bOrderList.stream().map(BOrder::getCcStatus).collect(Collectors.toList());
	    	int kesuNums = 0;
            for(int i = 0;i<ksStatus.size();i++) {
            	if(ksStatus.get(i)!=null) {
            		kesuNums +=1;
            	}
            }
            queryBack.setKesuNum(kesuNums);
	    	queryBack.setOrderNum(customerComplaint.getOrderNum());
	    	queryBack.setSubOrderAllList(subOrderAllList);
		
		} catch (Exception e) {
            logger.error("客诉信息查询出错:{}", e.getMessage(), e);
		 }
		
		return queryBack;
	}

}
