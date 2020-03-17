package com.uboxol.cloud.mermaid.db.repository.bx;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.uboxol.cloud.mermaid.db.entity.bx.AlipayNotifyImage;

@Repository
public interface AlipayNotifyImageRepository extends JpaRepository<AlipayNotifyImage, Long> {
	/**
     * @param transactionID 
     * @param shootMoment 
     *
     * @return AlipayNotifyImage
     */
	List<AlipayNotifyImage> findByTransactionIdAndShootMomentOrderByCamIdAsc(String transactionId,String shootMoment);
	
}
