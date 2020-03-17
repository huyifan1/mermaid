package com.uboxol.cloud.mermaid.service.kefu;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uboxol.cloud.mermaid.api.req.DistributionQuery;
import com.uboxol.cloud.mermaid.api.req.User;
import com.uboxol.cloud.mermaid.api.res.LoginBack;
import com.uboxol.cloud.mermaid.db.entity.kefu.BOrder;
import com.uboxol.cloud.mermaid.db.entity.kefu.WorkOrder;
import com.uboxol.cloud.mermaid.db.repository.kefu.BOrderRepository;
import com.uboxol.cloud.mermaid.db.repository.kefu.WorkOrderRepository;
import com.uboxol.cloud.mermaid.utils.HyfUtils;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class WorkOrderService {
	
	private final BOrderRepository bOrderRepository;
    private final WorkOrderRepository workOrderRepository;
    
    
    @Autowired
    public WorkOrderService(final BOrderRepository bOrderRepository,final WorkOrderRepository workOrderRepository) {
        this.bOrderRepository = bOrderRepository;
        this.workOrderRepository = workOrderRepository;
    }
    
    public List<WorkOrder> query(DistributionQuery query) {
    	List<WorkOrder> list = new ArrayList<WorkOrder>();
    	
    	String beginTime = query.getBeginTime();
    	String endTime = query.getEndTime();
    	String account = query.getAccount();
    	if(StringUtil.isNullOrEmpty(account)) {
    		list = workOrderRepository.findAccounts(beginTime, endTime);
    	}else {
    		list = workOrderRepository.findAccount(beginTime, endTime, account);
    	}
    	
		return list;
    }
    
	
	public List<WorkOrder> build(List<User> list) {
		List<WorkOrder> wlist = new ArrayList<WorkOrder>();
		try {
			for(User o : list) {
				String account = o.getAccount();
				String name = o.getName();
				int subNum = o.getSubNum();
				String begin = o.getBeginTime();
				String  end = o.getEndTime();
				String  operator = o.getOperator();
				
				List<BOrder> borders = bOrderRepository.findid(subNum);
				
				if(borders.size()!=0) {
					Set<String> setlist = new HashSet<String>();
					//把子单对应分配数目的单子存上处理人
					for(BOrder bOrder : borders) {
						setlist.add(bOrder.getTransactionId());//TransactionId父单唯一
						bOrder.setHandle(account);
						bOrder.setTime(new Timestamp(new java.util.Date().getTime()));
						bOrderRepository.saveAndFlush(bOrder);
					}
					
					//把能分配子单的人入库
					WorkOrder workOrder = new WorkOrder();
					workOrder.setAccount(account);
					workOrder.setName(name);
					workOrder.setSubNum(borders.size());//子单数目
					workOrder.setParentNum(setlist.size());//父单数目
					workOrder.setOperator(operator);
					workOrder.setBeginTime(begin);
					workOrder.setEndTime(end);
					workOrder.setTime(new Timestamp(new java.util.Date().getTime()));
					workOrderRepository.saveAndFlush(workOrder);
					
					wlist.add(workOrder);
				}
				
			}
		
		} catch (Exception e) {
            logger.error("客诉处理出错:{}", e.getMessage(), e);
		 }
		
		return wlist;
	}

	public LoginBack verify(User user) {
		LoginBack loginBack = new LoginBack();
		try {
			List<WorkOrder> list = workOrderRepository.findByAccount(user.getAccount());
			if(list.size()==0) {
				loginBack.setMsg("统一账号有误，请重新输入!");
			}else {
				//int num = 0;
				loginBack.setUser(user.getAccount());
				loginBack.setMsg("验证成功!");
				
				List<BOrder> list1 = bOrderRepository.findOne(HyfUtils.getCurrentDate(), user.getAccount(), 9);
				List<BOrder> list2 = bOrderRepository.findTwo(HyfUtils.getCurrentDate(), user.getAccount());
				List<BOrder> list3 = bOrderRepository.findThree(HyfUtils.getMonthFirstday(HyfUtils.getCurrentDate()), HyfUtils.getMonthLastday(HyfUtils.getCurrentDate()), user.getAccount(), 9);
				List<BOrder> list4 = bOrderRepository.findFour(HyfUtils.getMonthFirstday(HyfUtils.getCurrentDate()), HyfUtils.getMonthLastday(HyfUtils.getCurrentDate()), user.getAccount());
				List<BOrder> list5 = bOrderRepository.findFive(HyfUtils.getCurrentDate(), 9);
				List<BOrder> list6 = bOrderRepository.findSix(HyfUtils.getCurrentDate());
				List<BOrder> list7 = bOrderRepository.findSeven(HyfUtils.getMonthFirstday(HyfUtils.getCurrentDate()), HyfUtils.getMonthLastday(HyfUtils.getCurrentDate()), 9);
				List<BOrder> list8 = bOrderRepository.findEight(HyfUtils.getMonthFirstday(HyfUtils.getCurrentDate()), HyfUtils.getMonthLastday(HyfUtils.getCurrentDate()));
				
				loginBack.setOne(list1==null?0:list1.size());
				loginBack.setTwo(list2==null?0:list2.size());
				loginBack.setThree(list3==null?0:list3.size());
				loginBack.setFour(list4==null?0:list4.size());
				loginBack.setFive(list5==null?0:list5.size());
				loginBack.setSix(list6==null?0:list6.size());
				loginBack.setSeven(list7==null?0:list7.size());
				loginBack.setEight(list8==null?0:list8.size());
				
				
//				loginBack.setUser(list.get(0).getAccount());
//				for(WorkOrder w : list) {
//					num+=w.getSubNum();
//				}
//				loginBack.setSubNum(num);
			}
		} catch (Exception e) {
            logger.error("登录账号验证出错:{}", e.getMessage(), e);
		 }
		
		return loginBack;
	}
	
	public static void main(String[] args) {
	}

	
}
