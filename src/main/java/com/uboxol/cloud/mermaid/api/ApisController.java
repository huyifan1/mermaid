package com.uboxol.cloud.mermaid.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uboxol.cloud.mermaid.aop.UserParam;
import com.uboxol.cloud.mermaid.api.req.CustomerComplaintQuery;
import com.uboxol.cloud.mermaid.api.req.DistributionQuery;
import com.uboxol.cloud.mermaid.api.req.EntryComplaint;
import com.uboxol.cloud.mermaid.api.req.Query;
import com.uboxol.cloud.mermaid.api.req.SubOrder;
import com.uboxol.cloud.mermaid.api.req.SubOrderAll;
import com.uboxol.cloud.mermaid.api.req.User;
import com.uboxol.cloud.mermaid.api.req.UserInject;
import com.uboxol.cloud.mermaid.api.res.EntryResult;
import com.uboxol.cloud.mermaid.api.res.LoginBack;
import com.uboxol.cloud.mermaid.api.res.QueryBack;
import com.uboxol.cloud.mermaid.api.res.SaveComplaint;
import com.uboxol.cloud.mermaid.api.res.StatusResult;
import com.uboxol.cloud.mermaid.db.entity.kefu.CustomerComplaint;
import com.uboxol.cloud.mermaid.db.entity.kefu.WorkOrder;
import com.uboxol.cloud.mermaid.service.kefu.CustomerComplaintService;
import com.uboxol.cloud.mermaid.service.kefu.KesuDealService;
import com.uboxol.cloud.mermaid.service.kefu.KesuDetailService;
import com.uboxol.cloud.mermaid.service.kefu.KesuEntryService;
import com.uboxol.cloud.mermaid.service.kefu.WorkOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.uboxol.cloud.mermaid.cfg.NoOpenCloudConfig.SERVICE_NAME;

/**
 * model: mermaid
 *
 * @author liyunde
 * @sice 2019/10/18 15:04
 */
@Slf4j
@Api(tags = SERVICE_NAME)
@RestController
@RequestMapping("api/mermaid")
public class ApisController {

    private final CustomerComplaintService customerComplaintService;
    private final KesuDetailService kesuDetailService;
    private final KesuDealService kesuDealService;
    private final KesuEntryService kesuEntryService;
    private final WorkOrderService workOrderService;
    private final ObjectMapper mapper;

    public ApisController(final CustomerComplaintService customerComplaintService,
    		final KesuDetailService kesuDetailService,final KesuDealService kesuDealService,final KesuEntryService kesuEntryService,
    		final WorkOrderService workOrderService, final ObjectMapper mapper) {
        this.customerComplaintService = customerComplaintService;
        this.kesuDetailService = kesuDetailService;
        this.kesuDealService = kesuDealService;
        this.kesuEntryService = kesuEntryService;
        this.workOrderService = workOrderService;
        this.mapper = mapper;
    }

    private String toJson(Object o) {

        if (o == null) {
            return null;
        }

        if (o instanceof String) {
            return (String) o;
        }

        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    //0.客诉信息录入
    // 0.1 查询单子是否已经有客诉

    //1.客诉信息列表 查询(分页)
    // 1.1 单条客诉详情
    //      包含 a:客诉基本信息 b:错误信息(可能没有) c:子单数,投诉子单数 d:子单列表 e:开门前后的图片信息列表
    // 1.2 客诉订单列表

    //2.客诉分配
    //3.客诉处理
    // 4.客诉回访
    // == 5.条件式建议

    @Getter
    @Setter
    @ToString
    public static class TestRequest {

        String userName;

        String age;
    }

    @ApiOperation(value = "0.测试请求")
    @PostMapping(value = "/testReq")
    public String testReq(@UserParam UserInject user, @RequestBody TestRequest req) {
        logger.info("req:{},user:{}", req, user);

        Map<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("req", req);
        map.put("user", user);

        return toJson(map);
    }


    @ApiOperation(value = "1.客诉查询", notes = "根据交易号查询客诉信息")
    @PostMapping("/complaint/query")
    public QueryBack customerComplaintQuery(@RequestBody Query req) {
        QueryBack queryBack = null;
        try {
            queryBack = customerComplaintService.query(req);
        } catch (Exception e) {
            logger.error("推送客诉信息录入出错:{}", e.getMessage(), e);
        }

        return queryBack;
    }

    @ApiOperation(value = "2.客诉保存", notes = "客诉保存，返回当前客诉状态，客诉数")
    @PostMapping("/complaint/save")
    public SaveComplaint customerComplaintSave(@RequestBody SubOrder dto) {
        SaveComplaint saveComplaint = new SaveComplaint();
        try {
            saveComplaint = customerComplaintService.save(dto);
        } catch (Exception e) {
            logger.error("推送客诉信息保存出错:{}", e.getMessage(), e);
        }
        return saveComplaint;
    }
    
    @ApiOperation(value = "3.客诉提交", notes = "客诉提交保存,保存录入的客诉")
    @PostMapping("/complaint/commit")
    public QueryBack customerComplaintCommit(@RequestBody QueryBack queryBack) {
        try {
        	queryBack = customerComplaintService.commit(queryBack);
        } catch (Exception e) {
            logger.error("推送客诉信息录入提交出错:{}", e.getMessage(), e);
        }
        return queryBack;
    }
    
    @ApiOperation(value = "4.配置查询", notes = "分配日期时间段内查询是否有分配处理人")
    @PostMapping("/complaint/distributionQuery")
    public List<WorkOrder> distributionQuery(@RequestBody DistributionQuery query) {
    	List<WorkOrder> list = new ArrayList<WorkOrder>();
        try {
        	list = workOrderService.query(query);
        } catch (Exception e) {
            logger.error("推送客诉分配出错:{}", e.getMessage(), e);
            return null;
        }
        return list;
    }
    
    @ApiOperation(value = "5.配置新建", notes = "分配工单新建")
    @PostMapping("/complaint/build")
    public List<WorkOrder> newBuild(@RequestBody List<User> list) {
    	List<WorkOrder> wlist = new ArrayList<WorkOrder>();
        try {
        	wlist = workOrderService.build(list);
        } catch (Exception e) {
            logger.error("推送配置新建出错:{}", e.getMessage(), e);
            return null;
        }
        return wlist;
    }
    
    @ApiOperation(value = "6.登录账号验证", notes = "检查账号是否在指定的分配人里存在,成功返回待办数目")
    @PostMapping("/complaint/accountVerify")
    public LoginBack accountVerify(@RequestBody User user) {
    	LoginBack loginBack = new LoginBack();
        try {
        	loginBack = workOrderService.verify(user);
        } catch (Exception e) {
            logger.error("推送登录账号验证出错:{}", e.getMessage(), e);
            return null;
        }
        return loginBack;
    }

    @ApiOperation(value = "7.立即获取", notes = "工作台立即获取当前处理人所有的工单")
    @PostMapping("/complaint/get")
    public Page<CustomerComplaint> get(@RequestBody User user) {
        try {
            return customerComplaintService.get(user.getAccount());
        } catch (Exception e) {
            logger.error("推送立即获取出错:{}", e.getMessage(), e);
        }
        return Page.empty();
    }
    
//    @ApiOperation(value = "7.1我的待办/客诉列表", notes = "导出我的待办/客诉列表并发送邮件到当前账号")
//    @PostMapping("/complaint/get/export")
//    public void getExportXlsx(@RequestBody User user) {
//        try {
//            customerComplaintService.getExportXlsx(user.getAccount());
//        } catch (Exception e) {
//            logger.error("推送立即获取出错:{}", e.getMessage(), e);
//        }
//    }
    
    @ApiOperation(value = "8.刷新", notes = "获取待办主单列表并过滤掉已经闭环的主单")
    @PostMapping("/complaint/refresh")
    public Page<CustomerComplaint> refresh(@RequestBody User user) {
        try {
            return customerComplaintService.refresh(user.getAccount());
        } catch (Exception e) {
            logger.error("推送刷新出错:{}", e.getMessage(), e);
        }
        return Page.empty();
    }
    
    @ApiOperation(value = "9.单条客诉详情", notes = "单条客诉详情")
    @PostMapping("/complaint/detail")
    public QueryBack singleOrderDetail(@RequestBody CustomerComplaintQuery query) {
        QueryBack queryBack;
        try {
            queryBack = kesuDetailService.detail(query.getTransactionId());
        } catch (Exception e) {
            logger.error("推送客诉详情出错:{}", e.getMessage(), e);
            return null;
        }
        return queryBack;
    }
    
//    @ApiOperation(value = "9.1客诉详情页导出", notes = "导出客诉详情页并发送邮件到当前账号")
//    @PostMapping("/complaint/detail/export")
//    public QueryBack orderDetailExportXlsx(@RequestBody CustomerComplaintQuery query) {
//        QueryBack queryBack;
//        try {
//            queryBack = kesuDetailService.detail(query.getTransactionId());
//            customerComplaintService.orderDetailExportXlsx(query.getAccount(), queryBack.getSubOrderList());
//        } catch (Exception e) {
//            logger.error("推送客诉详情出错:{}", e.getMessage(), e);
//            return null;
//        }
//        return queryBack;
//    }
    
//    @ApiOperation(value = "9.2客诉详情页总表导出", notes = "导出客诉详情页总表并发送邮件到当前账号")
//    @PostMapping("/complaint/detail/exportAll")
//    public QueryBack orderDetailAllExportXlsx(@RequestBody CustomerComplaintQuery query) {
//        QueryBack queryBack;
//        try {
//            queryBack = kesuDetailService.detailAll(query.getTransactionId());
//            customerComplaintService.orderDetailAllExportXlsx(query.getAccount(), queryBack.getSubOrderAllList());
//        } catch (Exception e) {
//            logger.error("推送客诉详情出错:{}", e.getMessage(), e);
//            return null;
//        }
//        return queryBack;
//    }

    @ApiOperation(value = "10.客诉处理", notes = "详情对应的客诉处理")
    @PostMapping("/complaint/deal")
    public List<SaveComplaint> orderList(@RequestBody List<SubOrder> dtoList) {

    	List<SaveComplaint> list;
        try {
        	list = kesuDealService.deal(dtoList);
        } catch (Exception e) {
            logger.error("推送客诉处理出错:{}", e.getMessage(), e);
            return null;
        }

        return list;
    }
    
    @ApiOperation(value = "11.客诉列表查询", notes = "根据条件搜索查询客诉信息")
    @PostMapping("/complaint/search")
    public Page<CustomerComplaint> complaintQuery(@RequestBody CustomerComplaintQuery query) {
        try {
            return customerComplaintService.search(query);
        } catch (Exception e) {
            logger.error("客诉列表查询出错:{}", e.getMessage(), e);
        }
        return Page.empty();
    }
    
    @ApiOperation(value = "11.1客诉列表导出", notes = "导出客诉列表总表并发送邮件到当前账号")
    @PostMapping("/complaint/search/exportAll")
    public List<SubOrderAll> complaintQueryExportXlsx(@RequestBody CustomerComplaintQuery query) {
    	List<SubOrderAll>  borderList;
        try {
        	borderList = customerComplaintService.searchXlsx(query);
            customerComplaintService.orderDetailAllExportXlsx(query.getAccount(), borderList);
        } catch (Exception e) {
            logger.error("推送客诉详情出错:{}", e.getMessage(), e);
            return null;
        }
        return borderList;
    }
    
    
    @ApiOperation(value = "12.多平台录入客诉", notes = "根据order_id查询交易ID录入客诉")
    @PostMapping("/complaint/entry")
    public EntryResult customerComplaintEntry(@RequestBody EntryComplaint req) {
    	EntryResult entryResult = null;
        try {
        	entryResult = kesuEntryService.entry(req);
        } catch (Exception e) {
            logger.error("推送多平台录入客诉出错:{}", e.getMessage(), e);
        }

        return entryResult;
    }
    
    @ApiOperation(value = "12.查询子单状态", notes = "根据order_id查询当前单子的状态")
    @PostMapping("/complaint/orderIdStatus")
    public StatusResult customerComplaintStatus(@RequestBody EntryComplaint req) {
    	StatusResult statusResult = null;
        try {
        	statusResult = kesuEntryService.status(req);
        } catch (Exception e) {
            logger.error("推送多平台录入客诉出错:{}", e.getMessage(), e);
        }

        return statusResult;
    }
    

}
