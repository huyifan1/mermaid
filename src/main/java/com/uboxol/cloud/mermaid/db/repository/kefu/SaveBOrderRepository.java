package com.uboxol.cloud.mermaid.db.repository.kefu;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.uboxol.cloud.mermaid.db.entity.kefu.SaveBOrder;

@Repository
public interface SaveBOrderRepository extends JpaRepository<SaveBOrder, Long> {
	/**
     * orderId查询 
     *
     * @param orderId 
     *
     * @return BOrder
     */
	SaveBOrder findByOrderId(Long orderId);
	
	/**
     * transactionId查询 
     *
     * @param transactionId 支付宝交易单号
     *
     * @return List<BOrder>
     */
	List<SaveBOrder> findByTransactionId(String transactionId);
	
}
