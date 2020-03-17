package com.uboxol.cloud.mermaid.db.repository.kefu;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.uboxol.cloud.mermaid.db.entity.kefu.CustomerComplaint;

@Repository
public interface CustomerComplaintRepository extends JpaRepository<CustomerComplaint, Long> {
	/**
     * transactionID查询 
     *
     * @param transactionID 
     *
     * @return AlipayOrder
     */
	CustomerComplaint findByTransactionId(String transactionId);
	CustomerComplaint findByTradeNo(String tradeNo);
	
	@Query(value = "select * FROM customer_complaint c where c.transaction_id in (SELECT DISTINCT b.transaction_id FROM `b_order` b where b.handle=?1) and c.cc_status='1' ",nativeQuery = true)
	List<CustomerComplaint> get(String user);
	
	@Query(value = "select * FROM customer_complaint c where c.transaction_id in (SELECT DISTINCT b.transaction_id FROM `b_order` b where b.handle=?1) and c.cc_status='1' ",nativeQuery = true)
	List<CustomerComplaint> refresh(String user);
}
