package com.uboxol.cloud.mermaid.db.repository.bx;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.uboxol.cloud.mermaid.db.entity.bx.AlipayVisionpayInfo;

@Repository
public interface AlipayVisionpayInfoRepository extends JpaRepository<AlipayVisionpayInfo, Long> {
	/**
     * transactionID查询 
     *
     * @param transactionID 
     *
     * @return AlipayOrder
     */
	AlipayVisionpayInfo findByTransactionId(String transactionId);
	
}
