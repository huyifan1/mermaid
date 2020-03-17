package com.uboxol.cloud.mermaid.db.repository.kefu;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.uboxol.cloud.mermaid.db.entity.kefu.BOrder;

@Repository
public interface BOrderRepository extends JpaRepository<BOrder, Long> {
	/**
     * orderId查询 
     *
     * @param orderId 
     *
     * @return BOrder
     */
	BOrder findByOrderId(Long orderId);
	
	/**
     * transactionId查询 
     *
     * @param transactionId 支付宝交易单号
     *
     * @return List<BOrder>
     */
	List<BOrder> findByTransactionId(String transactionId);
	
	
//	
//	List<BOrder> findAll();//部门今日待办 总数
//	List<BOrder> findByCcStatus(int ccStatus);//今日待办 已完成
	
	
	@Query(value = "select * from b_order b where b.time like CONCAT('%',?1,'%') and b.handle=?2  and b.cc_status=?3 ; ",nativeQuery = true)
	List<BOrder> findOne(String today,String handle,int ccStatus);//今日待办 已完成
	@Query(value = "select * from b_order b where b.time like CONCAT('%',?1,'%') and b.handle=?2 ; ",nativeQuery = true)
	List<BOrder> findTwo(String today,String handle);//今日待办 总数
	
	@Query(value = "select * from b_order b where b.time>=?1 and b.time<=?2 and b.handle=?3  and b.cc_status=?4  ; ",nativeQuery = true)
	List<BOrder> findThree(String beginTime,String endTime,String handle,int ccStatus);
	@Query(value = "select * from b_order b where b.time>=?1 and b.time<=?2 and b.handle=?3 ; ",nativeQuery = true)
	List<BOrder> findFour(String beginTime,String endTime,String handle);
	
	
	@Query(value = "select * from b_order b where b.time like CONCAT('%',?1,'%') and b.cc_status=?2 ; ",nativeQuery = true)
	List<BOrder> findFive(String today,int ccStatus);//部门统计 - 今日待办 已完成
	@Query(value = "select * from b_order b where b.time like CONCAT('%',?1,'%') ; ",nativeQuery = true)
	List<BOrder> findSix(String today);//部门统计 - 今日待办 总数

	@Query(value = "select * from b_order b where b.time>=?1 and b.time<=?2 and b.cc_status=?3  ; ",nativeQuery = true)
	List<BOrder> findSeven(String beginTime,String endTime,int ccStatus);
	@Query(value = "select * from b_order b where b.time>=?1 and b.time<=?2 ; ",nativeQuery = true)
	List<BOrder> findEight(String beginTime,String endTime);
	
	
	
	
	
	/**
     * 查询指定一天日期的没有处理人的客诉单
     *
     * @return List<BOrder>
     */
	//@Query(value = "select * from b_order b where b.cc_time like CONCAT('%',?1,'%')  and (b.ks_sub_no != '' or b.ks_sub_no <> null) and (b.handle is NULL or b.handle='') limit ?2 ; ",nativeQuery = true)
	@Query(value = "select * from b_order b where  (b.ks_sub_no != '' or b.ks_sub_no <> null) and (b.handle is NULL or b.handle='') limit ?1 ; ",nativeQuery = true)
	List<BOrder> findid(int num);
	
	/**
     * 查询一段时间范围内指定条数的没有处理人的客诉单
     *
     * @return List<BOrder>
     */
	//@Query(value = "select * from b_order b where b.cc_time>=?1 and b.cc_time<=?2 and  (b.ks_sub_no != '' or b.ks_sub_no <> null) and (b.handle is NULL or b.handle='') limit ?3 ; ",nativeQuery = true)
	@Query(value = "select * from b_order b where b.cc_time>=?1 and b.cc_time<=?2 and  (b.ks_sub_no != '' or b.ks_sub_no <> null) and (b.handle is NULL or b.handle='') limit ?3 ; ",nativeQuery = true)
	List<BOrder> findids(String beginTime,String endTime,int num);
	
}
