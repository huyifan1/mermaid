package com.uboxol.cloud.mermaid.db.repository.bx;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.uboxol.cloud.mermaid.db.entity.bx.AlipayOrder;

@Repository
public interface AlipayOrderRepository extends JpaRepository<AlipayOrder, Long> {
	/**
     * transactionID查询 
     *
     * @param transactionID 
     *
     * @return AlipayOrder
     */
	List<AlipayOrder> findByTransactionId(String transactionId);
	
	/**
     * tradeNo查询 
     *
     * @param tradeNo 支付宝交易单号
     *
     * @return AlipayOrder
     */
	List<AlipayOrder> findByTradeNo(String tradeNo);
	
	/**
     * orderId查询 
     *
     * @param orderId 友宝订单号
     *
     * @return AlipayOrder
     */
	List<AlipayOrder> findByOrderId(Long orderId);
}
