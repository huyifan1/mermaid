package com.uboxol.cloud.mermaid.service.bx;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uboxol.cloud.mermaid.api.req.Query;
import com.uboxol.cloud.mermaid.db.entity.bx.AlipayNotifyImage;
import com.uboxol.cloud.mermaid.db.entity.bx.AlipayOrder;
import com.uboxol.cloud.mermaid.db.entity.bx.AlipayVisionpayInfo;
import com.uboxol.cloud.mermaid.db.repository.bx.AlipayNotifyImageRepository;
import com.uboxol.cloud.mermaid.db.repository.bx.AlipayOrderRepository;
import com.uboxol.cloud.mermaid.db.repository.bx.AlipayVisionpayInfoRepository;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class AlipayOrderService {
	private final AlipayOrderRepository alipayOrderRepository;
	private final AlipayVisionpayInfoRepository alipayVisionpayInfoRepository;
	private final AlipayNotifyImageRepository alipayNotifyImageRepository;
	@Autowired
    public AlipayOrderService(AlipayOrderRepository alipayOrderRepository,
    		AlipayVisionpayInfoRepository alipayVisionpayInfoRepository,
    		AlipayNotifyImageRepository alipayNotifyImageRepository) {
        this.alipayOrderRepository = alipayOrderRepository;
        this.alipayVisionpayInfoRepository = alipayVisionpayInfoRepository;
        this.alipayNotifyImageRepository = alipayNotifyImageRepository;
    }
	
	
	public List<AlipayOrder> findAlipayOrder(Query req) {
		List<AlipayOrder> alipayOrderlist = null;
		if(!StringUtil.isNullOrEmpty(req.getTransactionId())) {
			alipayOrderlist = alipayOrderRepository.findByTransactionId(req.getTransactionId());
		}
		if(!StringUtil.isNullOrEmpty(req.getTradeNo())) {
			alipayOrderlist = alipayOrderRepository.findByTradeNo(req.getTradeNo());
		}
		if(req.getOrderId()!=null) {
			alipayOrderlist = alipayOrderRepository.findByOrderId(req.getOrderId());
		}
		return alipayOrderlist;
	}
	
	public AlipayOrder findAlipayOrderByOrderId(Long orderId) {
		AlipayOrder alipayOrder = null;
		if(orderId!=null) {
			List<AlipayOrder> alipayOrderlist = alipayOrderRepository.findByOrderId(orderId);
			if(alipayOrderlist.size()>0) {
				alipayOrder = alipayOrderlist.get(0);
			}
		}
		return alipayOrder;
	}
	
	public String findByTransactionId(String transactionId) {
		String uid = "";
		try {
			AlipayVisionpayInfo alipayVisionpayInfo = alipayVisionpayInfoRepository.findByTransactionId(transactionId);
			if(alipayVisionpayInfo!=null) {
				uid = alipayVisionpayInfo.getUserId();
			}
		 } catch (Exception e) {
            logger.error("查询alipay_visionpay_info表信息出错:{}", e.getMessage(), e);
		 }
		return uid;
	}
	
	public List<AlipayNotifyImage> findPictures(String transactionId,String shootMoment) {
		List<AlipayNotifyImage>  list = null;
		try {
			list = alipayNotifyImageRepository.findByTransactionIdAndShootMomentOrderByCamIdAsc(transactionId, shootMoment);
		 } catch (Exception e) {
            logger.error("查询alipay_notify_image表信息出错:{}", e.getMessage(), e);
		 }
		return list;
	}

}
