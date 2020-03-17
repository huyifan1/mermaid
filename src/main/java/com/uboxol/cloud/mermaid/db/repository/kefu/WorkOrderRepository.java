package com.uboxol.cloud.mermaid.db.repository.kefu;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.uboxol.cloud.mermaid.db.entity.kefu.WorkOrder;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
	
	//@Query(value = "select * from work_order w  where w.account in (select DISTINCT b.handle from b_order b  where b.cc_time>=?1 and b.cc_time<=?2 and b.handle=?3) ",nativeQuery = true)
	@Query(value = "select * from work_order w  where w.begin_time>=?1 and w.end_time<=?2 and w.account=?3 ",nativeQuery = true)
	List<WorkOrder> findAccount(String beginTime,String endTime,String account);
	
	//@Query(value = "select * from work_order w  where w.account in (select DISTINCT b.handle from b_order b  where b.cc_time>=?1 and b.cc_time<=?2) ",nativeQuery = true)
	@Query(value = "select * from work_order w  where w.begin_time>=?1 and w.end_time<=?2 ",nativeQuery = true)
	List<WorkOrder> findAccounts(String beginTime,String endTime);
	
	List<WorkOrder> findByAccount(String account);
	
	
}
