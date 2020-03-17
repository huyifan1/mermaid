package com.uboxol.cloud.mermaid.service.kefu;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uboxol.cloud.mermaid.api.req.SubOrder;
import com.uboxol.cloud.mermaid.api.res.SaveComplaint;
import com.uboxol.cloud.mermaid.db.entity.kefu.BOrder;
import com.uboxol.cloud.mermaid.db.entity.kefu.CustomerComplaint;
import com.uboxol.cloud.mermaid.db.repository.kefu.BOrderRepository;
import com.uboxol.cloud.mermaid.db.repository.kefu.CustomerComplaintRepository;
import com.uboxol.gen.api.Cds2APIService;
import com.uboxol.gen.api.Order;
import com.uboxol.gen.api.OrderStatus;
import com.uboxol.gen.api.Response;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class KesuDealService {
	private final Cds2APIService.Iface apiService;
	private final CustomerComplaintRepository customerComplaintRepository;
	private final BOrderRepository bOrderRepository;
	private final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");
    
    @Autowired
    public KesuDealService(final Cds2APIService.Iface apiService, final CustomerComplaintRepository customerComplaintRepository, final BOrderRepository bOrderRepository) {
    	this.apiService = apiService;
    	this.customerComplaintRepository = customerComplaintRepository;
        this.bOrderRepository = bOrderRepository;
    }
	
	public List<SaveComplaint> deal(List<SubOrder> dtoList) {
		List<SaveComplaint> slist = new ArrayList<SaveComplaint>();
		SaveComplaint saveComplaint = new SaveComplaint();
		try {
			if(dtoList.size()>0) {
				for(SubOrder dto : dtoList) {
					BOrder bOrder = bOrderRepository.findByOrderId(dto.getOrderId());
					if(bOrder==null) throw new RuntimeException("b_order表中没有对应的订单");
					
					//给一开始没录客诉，走代录A处理录入客诉的使用
					if(StringUtils.isEmpty(bOrder.getKsSubNo())) {
						bOrder.setKsSubNo(SDF.format(new Date())+"-"+dto.getOrderId());
						dto.setKsSubNo(SDF.format(new Date())+"-"+dto.getOrderId());
					}
					if(StringUtils.isEmpty(bOrder.getCcChannel())) {
						bOrder.setCcChannel("客服后台");
						dto.setCcChannel("客服后台");
					}
					if(bOrder.getCcTime()==null) {
						bOrder.setCcTime(new Timestamp(new Date().getTime()));
						dto.setCcTime(new Timestamp(new Date().getTime()));
					}
					
					bOrder.setHandleStatus(dto.getHandleStatus());//1	待处理 2	处理中 3	已处理
					if(bOrder.getHandleStatus()==3) {
						bOrder.setCcStatus(9);
						dto.setCcStatus(9);
					}else {
						bOrder.setCcStatus(1);
						dto.setCcStatus(1);
					}
					bOrder.setHandleWay(dto.getHandleWay());
					
					if(dto.getHandleWay().equals("退款")) {
						OrderStatus orderStatus = new OrderStatus();
						orderStatus.setOrderId(dto.getOrderId());
						orderStatus.setStatus(8);
						Order order = apiService.getOrderDetailById(dto.getOrderId());
						if (order==null) throw new RuntimeException("cds获取订单详情表中没有对应的该订单");
						int originaStatus = order.getStatus();
						orderStatus.setOriginaStatus(originaStatus);
						int reasonId = 8501;
						if(originaStatus==5) {
							reasonId = 8*1000+originaStatus*100+1;
						}else if(originaStatus==6) {
							reasonId = 8*1000+originaStatus*100+0;
						}else if(originaStatus==7) {
							reasonId = 8*1000+originaStatus*100+4;
						}else if(originaStatus==9) {
							reasonId = 8*1000+originaStatus*100+0;
						}
						orderStatus.setReasonId(reasonId);
						orderStatus.setDealLog("等待退款");
						orderStatus.setSysId(40);
						orderStatus.setVendoutType(0);
						Response response = apiService.updateOrderStatus(orderStatus);
						saveComplaint.setFlag(response.getFlag());
						saveComplaint.setMessage(response.getMessage());
					}
					
					//如果处理方式有值，会有提交人，判断当前子单是否已经有处理人，没有就取当前处理人入库当前单，有处理人就不改变
					if(StringUtils.isNotEmpty(dto.getHandleWay()) && StringUtils.isEmpty(bOrder.getHandle()) ) {
						bOrder.setHandle(dto.getHandle());
					}
					
					bOrder.setQuestionClassify(dto.getQuestionClassify());
					bOrder.setHandleRemark(dto.getHandleRemark());
					bOrder.setVisitor(dto.getVisitor());
					bOrder.setVisitStatus(dto.getVisitStatus());
					bOrder.setVisitAdvice(dto.getVisitAdvice());
					bOrder.setVisitRemark(dto.getVisitRemark());
					bOrder.setHandleTime(dto.getHandleTime());
					bOrder.setVisitTime(dto.getVisitTime());
					bOrderRepository.saveAndFlush(bOrder);
					
					List<BOrder> list = bOrderRepository.findByTransactionId(dto.getTransactionId());
					List<Integer> ccStatus = list.stream().map(BOrder::getCcStatus).collect(Collectors.toList());
					ccStatus.removeAll(Collections.singleton(null)); 
		            saveComplaint.setKesuNum(ccStatus.size());
					saveComplaint.setSubOrder(dto);
					
					//更新完子单后，要去更新主单的客诉状态
					CustomerComplaint customerComplaint = customerComplaintRepository.findByTransactionId(dto.getTransactionId());
					
					//客诉状态 (1进行中 9已闭环)
					if(ccStatus.size()>0) {
						if(ccStatus.contains(1)) {
							customerComplaint.setCcStatus(1);
						}else {
							if(ccStatus.contains(9)) {
								customerComplaint.setCcStatus(9);
							}
						}
					}
					//客诉时间
					List<Timestamp> times = list.stream().map(BOrder::getCcTime).collect(Collectors.toList());
					times.removeAll(Collections.singleton(null)); 
					if(times.size()>0) {
						customerComplaint.setCcTime(times.get(times.size()-1));
					}
					
					//回访状态
					List<Integer> hfStatus = list.stream().map(BOrder::getVisitStatus).collect(Collectors.toList());
					hfStatus.removeAll(Collections.singleton(null)); 
					if(hfStatus.size()>0) {
						if(hfStatus.contains(2)) {
							customerComplaint.setVisitStatus(2);
						}else {
							if(hfStatus.contains(1)) {
								customerComplaint.setVisitStatus(1);
							}else {
								customerComplaint.setVisitStatus(3);
							}
						}
					}
					
					//回访时间
					List<Timestamp> visitTimes = list.stream().map(BOrder::getVisitTime).collect(Collectors.toList());
					visitTimes.removeAll(Collections.singleton(null)); 
					if(visitTimes.size()>0) {
						customerComplaint.setVisitTime(visitTimes.get(visitTimes.size()-1));
					}
					
					//处理状态
					List<Integer> clStatus = list.stream().map(BOrder::getHandleStatus).collect(Collectors.toList());
					clStatus.removeAll(Collections.singleton(null)); 
					if(clStatus.size()>0) {
						if(clStatus.contains(2)) {
							customerComplaint.setHandleStatus(2);
						}else {
							if(clStatus.contains(1)) {
								customerComplaint.setHandleStatus(1);
							}else {
								customerComplaint.setHandleStatus(3);
							}
						}
					}
					
					//处理时间
					List<Timestamp> handleTimes = list.stream().map(BOrder::getHandleTime).collect(Collectors.toList());
					handleTimes.removeAll(Collections.singleton(null)); 
					if(handleTimes.size()>0) {
						customerComplaint.setHandleTime(handleTimes.get(handleTimes.size()-1));
					}
					
					List<String> ways = list.stream().map(BOrder::getHandleWay).collect(Collectors.toList());
					String way = "";
					for(int i=0;i<ways.size();i++) {
						if(i==ways.size()-1) {
							way += ways.get(i);
						}else {
							way += ways.get(i)+",";
						}
					}
					customerComplaint.setHandleWay(way);
					customerComplaintRepository.saveAndFlush(customerComplaint);
					
					slist.add(saveComplaint);
					
				}
			}
			
		} catch (Exception e) {
            logger.error("客诉处理出错:{}", e.getMessage(), e);
		 }
		
		return slist;
	}

}
