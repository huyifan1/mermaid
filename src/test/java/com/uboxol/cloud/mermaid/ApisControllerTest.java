package com.uboxol.cloud.mermaid;

import com.alibaba.fastjson.JSON;
import com.uboxol.cloud.mermaid.api.ApisController;
import com.uboxol.cloud.mermaid.api.req.CustomerComplaintQuery;
import com.uboxol.cloud.mermaid.api.req.DistributionQuery;
import com.uboxol.cloud.mermaid.api.req.EntryComplaint;
import com.uboxol.cloud.mermaid.api.req.Query;
import com.uboxol.cloud.mermaid.api.req.SubOrder;
import com.uboxol.cloud.mermaid.api.req.User;
import com.uboxol.cloud.mermaid.api.req.UserInject;
import com.uboxol.cloud.mermaid.api.res.BasicInformation;
import com.uboxol.cloud.mermaid.api.res.QueryBack;
import com.uboxol.cloud.mermaid.db.entity.kefu.BOrder;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
public class ApisControllerTest {

    private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("yyyyMMdd");

    private WebClient client;

    @Before
    public void before() {
        String HOST;

        HOST = "http://localhost:10080";
//        HOST = "http://192.168.19.41:10080";

        client = WebClient.create(HOST);
    }

    @Test
    public void testReq() {

        String uri = "/api/mermaid/testReq";

        ApisController.TestRequest request = new ApisController.TestRequest();

        request.setAge("123");
        request.setUserName("一个测试地用户");

//        Map describe = BeanUtils.describe(request);

        logger.debug(JSON.toJSONString(request));

        BodyInserter<?, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(JSON.toJSONString(request));

        BasicNameValuePair pair = new BasicNameValuePair("X-User", "liyunde");

        String post = Post(uri, body, Collections.singletonList(pair), 60);

        logger.info("返回结果:{}", post);
    }

    @Test
    public void balanceQueryOfFundSupervisionAccountJson() {

        String uri = "/api/account/balanceQueryOfFundSupervisionAccount";

        String s = Post(uri, 30);

        logger.info("返回结果:{}", s);
    }


    private String Post(String uri, BodyInserter<?, ReactiveHttpOutputMessage> json, List<NameValuePair> headers, int timeOut) {

        WebClient.RequestBodySpec spec = client.post().uri(uri)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
//            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
            ;

        if (headers != null && !headers.isEmpty()) {
            headers.forEach(nameValuePair -> spec.header(nameValuePair.getName(), nameValuePair.getValue()));
        }

        return spec.body(json)
            .exchange()
            .flatMap(res -> res.bodyToMono(String.class))
            .block(Duration.ofSeconds(timeOut));
    }

    private String Post(String uri, int timeOut) {
        return client.post().uri(uri)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .flatMap(res -> res.bodyToMono(String.class))
            .block(Duration.ofSeconds(timeOut));
    }
    
    //客诉查询
    @Test
    public void testCustomerComplaintQuery() {

        String uri = "/api/mermaid/complaint/query";
        
        Query request = new Query();

        request.setTransactionId("visionpay8BC1D65D671807EAE4840706");

        logger.debug(JSON.toJSONString(request));

        BodyInserter<?, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(JSON.toJSONString(request));

        BasicNameValuePair pair = new BasicNameValuePair("X-User", "liyunde");

        String post = Post(uri, body, Collections.singletonList(pair), 60);

        logger.info("返回结果:{}", post);
        
//        {"transactionId":"visionpay8BC1D65D671807EAE4840706"}
//        返回结果:{"trade":{"transactionId":"visionpay8BC1D65D671807EAE4840706","deductionTime":[2020,5,20,20,5,20],"userUID":["9384954"],"tradeNo":"2019031822001404320584752498","amount":500,"vmId":null,"createTime":[2019,4,19,0,0],"errcode":null,"brachCompany":"深圳分公司"},"errorMsg":null,"orderNum":1,"kesuNum":0,"ksNo":null,"imageBefore":["https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*FiJDSJ72tuoAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*TmeBQa2rbDYAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*08LYTYsRyskAAAAAAAAAAABjAUInAQ"],"imageAfter":["https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*ZRJ_TJkRj-kAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*LpF_TqDkcZIAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*Kf5rRJ63YBIAAAAAAAAAAABjAUInAQ"],
        //"borderList":[{"id":null,"transactionId":null,"ksSubNo":"20191106-1","ccStatus":null,"ccTime":null,"ccChannel":null,"orderId":1555002151,"orderTime":[2020,5,20,20,5,20],"productPrice":0,"productName":"","ccPhenomenon":null,"ccAppeal":null,"contact":null,"visitor":null,"visitStatus":null,"visitTime":null,"visitAdvice":null,"visitRemark":null,"handle":null,"handleStatus":null,"handleTime":null,"handleWay":null,"questionClassify":null,"handleRemark":null}]}
        
    }
    
  //客诉保存
    @Test
    public void testCustomerComplaintSave() {

        String uri = "/api/mermaid/complaint/save";
        
        BOrder bOrder = new BOrder();
        bOrder.setTransactionId("visionpay8BC1D65D671807EAE4840706");
        bOrder.setOrderId(1555002151L);
        bOrder.setOrderTime(new Timestamp(new Date().getTime()));
        bOrder.setProductPrice(BigDecimal.ONE);
        bOrder.setProductName("可口可乐");
        bOrder.setContact("133715073248");
        bOrder.setCcPhenomenon("1");
        bOrder.setCcAppeal("想原路退回");
        bOrder.setHandleStatus(1);
        bOrder.setHandleWay("1");
        bOrder.setQuestionClassify("1");
        bOrder.setHandleRemark("已核实");
        bOrder.setVisitStatus(1);
        bOrder.setVisitor("zhangsan");
        bOrder.setVisitAdvice("1");
        bOrder.setVisitRemark("需回访");
        bOrder.setVisitTime(new Timestamp(new Date().getTime()));

        logger.debug(JSON.toJSONString(bOrder));

        BodyInserter<?, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(JSON.toJSONString(bOrder));

        BasicNameValuePair pair = new BasicNameValuePair("X-User", "liyunde");

        String post = Post(uri, body, Collections.singletonList(pair), 60);

        logger.info("返回结果:{}", post);
        
        //{"ccAppeal":"想原路退回","ccPhenomenon":"1","contact":"133715073248","handleRemark":"已核实","handleStatus":1,"handleWay":"1","orderId":1555002151,"orderTime":"2019-11-07T10:30:03.956","productName":"可口可乐","productPrice":1,"questionClassify":"1","transactionId":"visionpay8BC1D65D671807EAE4840706","visitAdvice":"1","visitRemark":"需回访","visitStatus":1,"visitTime":"2019-11-07T10:30:03.956","visitor":"zhangsan"}
        //{"subOrder":{"transactionId":"visionpay8BC1D65D671807EAE4840706","ksSubNo":"20191107-1555002151","ccStatus":1,
        //"ccTime":[2019,11,7,10,30,5,318000000],"ccChannel":"客服后台","orderId":1555002151,"orderTime":[2019,11,7,10,30,3,956000000],
        //"productPrice":1,"productName":"可口可乐","ccPhenomenon":"1","ccAppeal":"想原路退回","contact":"133715073248","visitor":"zhangsan","visitStatus":1,"visitTime":[2019,11,7,10,30,5,318000000],"visitAdvice":"1","visitRemark":"需回访","handle":null,"handleStatus":1,"handleTime":[2019,11,7,10,30,5,318000000],"handleWay":"1","questionClassify":"1","handleRemark":"已核实"},"kesuNum":1}

    }
    
  //客诉提交
    @Test
    public void testCustomerComplaintCommit() {

        String uri = "/api/mermaid/complaint/commit";
        
        QueryBack queryBack = new QueryBack();
        
        BasicInformation trade = new BasicInformation();
        trade.setTransactionId("visionpay8BC1D65D671807EAE4840706");
        trade.setTradeNo("2019031822001404320584752498");
        trade.setDeductionTime(new Timestamp(new Date().getTime()));
        trade.setAmount(new BigDecimal("3"));
        List<String> ulist = new ArrayList(); ulist.add("9384954");
        trade.setUserUID(ulist);
        trade.setVmId("000000");
        trade.setBrachCompany("深圳分公司");
        queryBack.setTrade(trade);
        
        List<SubOrder> subOrderList = new ArrayList<SubOrder>();
        SubOrder bOrder = new SubOrder();
        bOrder.setTransactionId("visionpay8BC1D65D671807EAE4840706");
        bOrder.setKsSubNo("20191107-1555002151");
        bOrder.setCcStatus(1);
        bOrder.setCcTime(new Timestamp(new Date().getTime()));
        bOrder.setCcChannel("客服后台");
        bOrder.setOrderId(1555002151L);
        bOrder.setOrderTime(new Timestamp(new Date().getTime()));
        bOrder.setProductPrice(BigDecimal.ONE);
        bOrder.setProductName("可口可乐");
        bOrder.setCcPhenomenon("1");
        bOrder.setCcAppeal("想原路退回");
        bOrder.setContact("133715073248");
        bOrder.setHandleStatus(1);
        bOrder.setHandleWay("1");
        bOrder.setHandleTime(new Timestamp(new Date().getTime()));
        bOrder.setQuestionClassify("1");
        bOrder.setHandleRemark("已核实");
        bOrder.setVisitStatus(1);
        bOrder.setVisitor("zhangsan");
        bOrder.setVisitAdvice("1");
        bOrder.setVisitRemark("需回访");
        bOrder.setVisitTime(new Timestamp(new Date().getTime()));
        subOrderList.add(bOrder);
        
//        SubOrder bOrder2 = new SubOrder();
//        bOrder2.setOrderId(1555002152L);
//        bOrder2.setOrderTime(LocalDateTime.now());
//        bOrder2.setProductPrice(BigDecimal.ONE);
//        bOrder2.setProductName("可口可乐");
//        subOrderList.add(bOrder2);
//        
//        SubOrder bOrder3 = new SubOrder();
//        bOrder3.setOrderId(1555002153L);
//        bOrder3.setOrderTime(LocalDateTime.now());
//        bOrder3.setProductPrice(BigDecimal.ONE);
//        bOrder3.setProductName("可口可乐");
//        subOrderList.add(bOrder3);
       
        queryBack.setSubOrderList(subOrderList);
        queryBack.setOrderNum(3);
        queryBack.setKesuNum(1);
        
        String[] imageBefore = new String[3];
        imageBefore[0]="https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*FiJDSJ72tuoAAAAAAAAAAABjAUInAQ";
        imageBefore[1]="https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*TmeBQa2rbDYAAAAAAAAAAABjAUInAQ";
        imageBefore[2]="https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*08LYTYsRyskAAAAAAAAAAABjAUInAQ";
        
        String[] imageAfter = new String[3];
        imageAfter[0]="https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*ZRJ_TJkRj-kAAAAAAAAAAABjAUInAQ";
        imageAfter[1]="https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*LpF_TqDkcZIAAAAAAAAAAABjAUInAQ";
        imageAfter[2]="https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*Kf5rRJ63YBIAAAAAAAAAAABjAUInAQ";
        
        queryBack.setImageBefore(imageBefore);
        queryBack.setImageAfter(imageAfter);
        

        logger.debug(JSON.toJSONString(queryBack));

        BodyInserter<?, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(JSON.toJSONString(queryBack));

        BasicNameValuePair pair = new BasicNameValuePair("X-User", "liyunde");

        String post = Post(uri, body, Collections.singletonList(pair), 60);

        logger.info("返回结果:{}", post);
        
        //{"imageAfter":["https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*ZRJ_TJkRj-kAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*LpF_TqDkcZIAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*Kf5rRJ63YBIAAAAAAAAAAABjAUInAQ"],"imageBefore":["https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*FiJDSJ72tuoAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*TmeBQa2rbDYAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*08LYTYsRyskAAAAAAAAAAABjAUInAQ"],"kesuNum":1,"orderNum":3,"subOrderList":[{"ccAppeal":"想原路退回","ccChannel":"客服后台","ccPhenomenon":"1","ccStatus":1,"ccTime":"2019-11-07T10:55:03.076","contact":"133715073248","handleRemark":"已核实","handleStatus":1,"handleWay":"1","ksSubNo":"20191107-1555002151","orderId":1555002151,"orderTime":"2019-11-07T10:55:03.076","productName":"可口可乐","productPrice":1,"questionClassify":"1","transactionId":"visionpay8BC1D65D671807EAE4840706","visitAdvice":"1","visitRemark":"需回访","visitStatus":1,"visitTime":"2019-11-07T10:55:03.076","visitor":"zhangsan"}],"trade":{"amount":3,"brachCompany":"深圳分公司","deductionTime":"2019-11-07T10:55:03.074","tradeNo":"2019031822001404320584752498","transactionId":"visionpay8BC1D65D671807EAE4840706","userUID":["9384954"],"vmId":"000000"}}
        //返回结果:true

    }


  //根据条件客诉列表查询
    @Test
    public void testCustomerComplaintSearch() {

        String uri = "/api/mermaid/complaint/search";
        
        CustomerComplaintQuery request = new CustomerComplaintQuery();

        //request.setTransactionId("visionpay8BC1D65D671807EAE4840706");
        request.setUids("9384954");

        logger.debug(JSON.toJSONString(request));

        BodyInserter<?, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(JSON.toJSONString(request));

        BasicNameValuePair pair = new BasicNameValuePair("X-User", "liyunde");

        String post = Post(uri, body, Collections.singletonList(pair), 60);

        logger.info("返回结果:{}", post);
        
        //pageSize=30, page=0, transactionId=visionpay8BC1D65D671807EAE4840706, tradeNo=null, vm=null, ccStatus=null, visitStatus=null, handleStatus=null, handleDate=null, ccDate=null, companyId=null, subOrderCnt=null, totalAmount=null)] {"content":[{"id":1,"ksNo":"20191107","ccStatus":1,"ccTime":"2019-11-07T11:15:55","transactionId":"visionpay8BC1D65D671807EAE4840706","threeSerialNo":"20191107111558","eductionTime":"2019-11-07T11:15:55","num":3,"eductionMoney":3.00,"machineNo":"000000","branchCompany":"深圳分公司","uids":"9384954","serviceReminder":null,"visitStatus":2,"visitTime":"2019-11-07T11:15:55","handleStatus":2,"handleTime":"2019-11-07T11:15:55","handleWay":"1","tradeNo":"2019031822001404320584752498"}],"pageable":"INSTANCE","totalPages":1,"totalElements":1,"last":true,"number":0,"size":0,"sort":{"unsorted":true,"sorted":false,"empty":true},"first":true,"numberOfElements":1,"empty":false}
//{"handlebeginTime":"2019-11-07","handleendTime":"2019-11-14","account":"lisi"}
        
    }
    
    //单条客诉详情
    @Test
    public void singleOrderDetail() {

        String uri = "/api/mermaid/complaint/detail";
        
        CustomerComplaintQuery request = new CustomerComplaintQuery();

        request.setTransactionId("visionpay8BC1D65D671807EAE4840706");

        logger.debug(JSON.toJSONString(request));

        BodyInserter<?, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(JSON.toJSONString(request));

        BasicNameValuePair pair = new BasicNameValuePair("X-User", "liyunde");

        String post = Post(uri, body, Collections.singletonList(pair), 60);

        logger.info("返回结果:{}", post);
        
        //"transactionId":"visionpay8BC1D65D671807EAE4840706"}
        //返回结果:{"trade":{"transactionId":"visionpay8BC1D65D671807EAE4840706","deductionTime":[2019,11,7,11,15,55],"userUID":["9384954"],"tradeNo":"2019031822001404320584752498","amount":3.00,"vmId":"000000","createTime":[2019,11,7,11,15,55],"errcode":null,"brachCompany":"深圳分公司"},"errorMsg":null,"subOrderList":[{"transactionId":"visionpay8BC1D65D671807EAE4840706","ksSubNo":"20191107-1555002151","ccStatus":1,"ccTime":[2019,11,7,11,15,55],"ccChannel":"客服后台","orderId":1555002151,"orderTime":[2019,11,7,11,15,55],"productPrice":1.00,"productName":"可口可乐","ccPhenomenon":"1","ccAppeal":"想原路退回","contact":"133715073248","visitor":"zhangsan","visitStatus":1,"visitTime":[2019,11,7,11,15,55],"visitAdvice":"1","visitRemark":"需回访","handle":null,"handleStatus":1,"handleTime":[2019,11,7,11,15,55],"handleWay":"1","questionClassify":"1","handleRemark":"已核实"}],"orderNum":3,"kesuNum":1,"ksNo":"20191107","imageBefore":["https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*FiJDSJ72tuoAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*TmeBQa2rbDYAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*08LYTYsRyskAAAAAAAAAAABjAUInAQ"],"imageAfter":["https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*ZRJ_TJkRj-kAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*LpF_TqDkcZIAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*Kf5rRJ63YBIAAAAAAAAAAABjAUInAQ"]}
        
    }
    
  //导出客诉详情
    @Test
    public void singleOrderDetailExportXlsx() {

        String uri = "/api/mermaid/complaint/detail/export";
        
        CustomerComplaintQuery request = new CustomerComplaintQuery();
        request.setAccount("huyifan");
        request.setTransactionId("visionpay8BC1D65D671807EAE4840706");

        logger.debug(JSON.toJSONString(request));

        BodyInserter<?, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(JSON.toJSONString(request));

        BasicNameValuePair pair = new BasicNameValuePair("X-User", "liyunde");

        String post = Post(uri, body, Collections.singletonList(pair), 60);

        logger.info("返回结果:{}", post);
        
        //"transactionId":"visionpay8BC1D65D671807EAE4840706"}
        //返回结果:{"trade":{"transactionId":"visionpay8BC1D65D671807EAE4840706","deductionTime":[2019,11,7,11,15,55],"userUID":["9384954"],"tradeNo":"2019031822001404320584752498","amount":3.00,"vmId":"000000","createTime":[2019,11,7,11,15,55],"errcode":null,"brachCompany":"深圳分公司"},"errorMsg":null,"subOrderList":[{"transactionId":"visionpay8BC1D65D671807EAE4840706","ksSubNo":"20191107-1555002151","ccStatus":1,"ccTime":[2019,11,7,11,15,55],"ccChannel":"客服后台","orderId":1555002151,"orderTime":[2019,11,7,11,15,55],"productPrice":1.00,"productName":"可口可乐","ccPhenomenon":"1","ccAppeal":"想原路退回","contact":"133715073248","visitor":"zhangsan","visitStatus":1,"visitTime":[2019,11,7,11,15,55],"visitAdvice":"1","visitRemark":"需回访","handle":null,"handleStatus":1,"handleTime":[2019,11,7,11,15,55],"handleWay":"1","questionClassify":"1","handleRemark":"已核实"}],"orderNum":3,"kesuNum":1,"ksNo":"20191107","imageBefore":["https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*FiJDSJ72tuoAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*TmeBQa2rbDYAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*08LYTYsRyskAAAAAAAAAAABjAUInAQ"],"imageAfter":["https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*ZRJ_TJkRj-kAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*LpF_TqDkcZIAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*Kf5rRJ63YBIAAAAAAAAAAABjAUInAQ"]}
        
    }
    
  //导出总表
    @Test
    public void singleOrderDetailAllExportXlsx() {

        String uri = "/api/mermaid/complaint/detail/exportAll";
        
        CustomerComplaintQuery request = new CustomerComplaintQuery();
        request.setAccount("huyifan");
        request.setTransactionId("visionpay8BC1D65D671807EAE4840706");

        logger.debug(JSON.toJSONString(request));

        BodyInserter<?, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(JSON.toJSONString(request));

        BasicNameValuePair pair = new BasicNameValuePair("X-User", "liyunde");

        String post = Post(uri, body, Collections.singletonList(pair), 60);

        logger.info("返回结果:{}", post);
        
        //"transactionId":"visionpay8BC1D65D671807EAE4840706"}
        //返回结果:{"trade":{"transactionId":"visionpay8BC1D65D671807EAE4840706","deductionTime":[2019,11,7,11,15,55],"userUID":["9384954"],"tradeNo":"2019031822001404320584752498","amount":3.00,"vmId":"000000","createTime":[2019,11,7,11,15,55],"errcode":null,"brachCompany":"深圳分公司"},"errorMsg":null,"subOrderList":[{"transactionId":"visionpay8BC1D65D671807EAE4840706","ksSubNo":"20191107-1555002151","ccStatus":1,"ccTime":[2019,11,7,11,15,55],"ccChannel":"客服后台","orderId":1555002151,"orderTime":[2019,11,7,11,15,55],"productPrice":1.00,"productName":"可口可乐","ccPhenomenon":"1","ccAppeal":"想原路退回","contact":"133715073248","visitor":"zhangsan","visitStatus":1,"visitTime":[2019,11,7,11,15,55],"visitAdvice":"1","visitRemark":"需回访","handle":null,"handleStatus":1,"handleTime":[2019,11,7,11,15,55],"handleWay":"1","questionClassify":"1","handleRemark":"已核实"}],"orderNum":3,"kesuNum":1,"ksNo":"20191107","imageBefore":["https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*FiJDSJ72tuoAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*TmeBQa2rbDYAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*08LYTYsRyskAAAAAAAAAAABjAUInAQ"],"imageAfter":["https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*ZRJ_TJkRj-kAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*LpF_TqDkcZIAAAAAAAAAAABjAUInAQ","https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=https://mdgw.alipay.com/wsdk/img?zoom=.gif&fileid=A*Kf5rRJ63YBIAAAAAAAAAAABjAUInAQ"]}
        
    }
    
  //单条客诉处理
    @Test
    public void testOrderList() {

        String uri = "/api/mermaid/complaint/deal";
        
        SubOrder bOrder = new SubOrder();
        bOrder.setTransactionId("visionpay8BC1D65D671807EAE4840706");
        bOrder.setKsSubNo("20191107-1555002151");
        bOrder.setCcStatus(1);
        bOrder.setCcTime(new Timestamp(new Date().getTime()));
        bOrder.setCcChannel("客服后台");
        bOrder.setOrderId(1555002151L);
        bOrder.setOrderTime(new Timestamp(new Date().getTime()));
        bOrder.setProductPrice(BigDecimal.ONE);
        bOrder.setProductName("可口可乐");
        bOrder.setCcPhenomenon("1");
        bOrder.setCcAppeal("想原路退回");
        bOrder.setContact("133715073248");
        bOrder.setHandleStatus(3);
        bOrder.setHandleWay("1");
        bOrder.setHandleTime(new Timestamp(new Date().getTime()));
        bOrder.setQuestionClassify("1");
        bOrder.setHandleRemark("已核实");
        bOrder.setVisitStatus(3);
        bOrder.setVisitor("zhangsan");
        bOrder.setVisitAdvice("1");
        bOrder.setVisitRemark("需回访");
        bOrder.setVisitTime(new Timestamp(new Date().getTime()));

        logger.debug(JSON.toJSONString(bOrder));

        BodyInserter<?, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(JSON.toJSONString(bOrder));

        BasicNameValuePair pair = new BasicNameValuePair("X-User", "liyunde");

        String post = Post(uri, body, Collections.singletonList(pair), 60);

        logger.info("返回结果:{}", post);
        
        //{"ccAppeal":"想原路退回","ccChannel":"客服后台","ccPhenomenon":"1","ccStatus":1,"ccTime":"2019-11-07T14:40:57.111","contact":"133715073248","handleRemark":"已核实","handleStatus":3,"handleTime":"2019-11-07T14:40:57.111","handleWay":"1","ksSubNo":"20191107-1555002151","orderId":1555002151,"orderTime":"2019-11-07T14:40:57.111","productName":"可口可乐","productPrice":1,"questionClassify":"1","transactionId":"visionpay8BC1D65D671807EAE4840706","visitAdvice":"1","visitRemark":"需回访","visitStatus":3,"visitTime":"2019-11-07T14:40:57.111","visitor":"zhangsan"}
        //返回结果:{"subOrder":{"transactionId":"visionpay8BC1D65D671807EAE4840706","ksSubNo":"20191107-1555002151","ccStatus":9,"ccTime":[2019,11,7,14,40,57,111000000],"ccChannel":"客服后台","orderId":1555002151,"orderTime":[2019,11,7,14,40,57,111000000],"productPrice":1,"productName":"可口可乐","ccPhenomenon":"1","ccAppeal":"想原路退回","contact":"133715073248","visitor":"zhangsan","visitStatus":3,"visitTime":[2019,11,7,14,40,57,111000000],"visitAdvice":"1","visitRemark":"需回访","handle":null,"handleStatus":3,"handleTime":[2019,11,7,14,40,57,111000000],"handleWay":"1","questionClassify":"1","handleRemark":"已核实"},"kesuNum":1}
        
    }
    
    //刷新
    @Test
    public void testRefresh() {

        String uri = "/api/mermaid/complaint/refresh";
        
        User u = new User();
        u.setAccount("zhangsan");

        logger.debug(JSON.toJSONString(u));

        BodyInserter<?, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(JSON.toJSONString(u));

        BasicNameValuePair pair = new BasicNameValuePair("X-User", "liyunde");

        String post = Post(uri, body, Collections.singletonList(pair), 60);

        logger.info("返回结果:{}", post);
       
        //{"account":"zhangsan","subNum":0}
        //返回结果:{"content":[{"id":1,"ksNo":"20191107","ccStatus":1,"ccTime":[2019,11,7,11,15,55],"transactionId":"visionpay8BC1D65D671807EAE4840706","threeSerialNo":"20191107111558","eductionTime":[2019,11,7,11,15,55],"num":3,"eductionMoney":3.00,"machineNo":"000000","branchCompany":"深圳分公司","uids":"9384954","serviceReminder":null,"visitStatus":2,"visitTime":[2019,11,7,11,15,55],"handleStatus":2,"handleTime":[2019,11,7,11,15,55],"handleWay":"1","tradeNo":"2019031822001404320584752498"}],"pageable":"INSTANCE","totalElements":1,"last":true,"totalPages":1,"number":0,"size":0,"sort":{"unsorted":true,"sorted":false,"empty":true},"numberOfElements":1,"first":true,"empty":false}
        
    }
    
    //立即获取
    @Test
    public void testGet() {

        String uri = "/api/mermaid/complaint/get";
        
        User u = new User();
        u.setAccount("zhangsan");

        logger.debug(JSON.toJSONString(u));

        BodyInserter<?, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(JSON.toJSONString(u));

        BasicNameValuePair pair = new BasicNameValuePair("X-User", "liyunde");

        String post = Post(uri, body, Collections.singletonList(pair), 60);

        logger.info("返回结果:{}", post);
        
        //{"account":"zhangsan","subNum":0}
        //返回结果:{"content":[{"id":1,"ksNo":"20191107","ccStatus":1,"ccTime":[2019,11,7,11,15,55],"transactionId":"visionpay8BC1D65D671807EAE4840706","threeSerialNo":"20191107111558","eductionTime":[2019,11,7,11,15,55],"num":3,"eductionMoney":3.00,"machineNo":"000000","branchCompany":"深圳分公司","uids":"9384954","serviceReminder":null,"visitStatus":2,"visitTime":[2019,11,7,11,15,55],"handleStatus":2,"handleTime":[2019,11,7,11,15,55],"handleWay":"1","tradeNo":"2019031822001404320584752498"}],"pageable":"INSTANCE","totalElements":1,"last":true,"totalPages":1,"number":0,"size":0,"sort":{"unsorted":true,"sorted":false,"empty":true},"numberOfElements":1,"first":true,"empty":false}
    }
    
  //导出我的待办列表
    @Test
    public void testGetexport() {

        String uri = "/api/mermaid/complaint/get/export";
        
        User u = new User();
        u.setAccount("huyifan");

        logger.debug(JSON.toJSONString(u));

        BodyInserter<?, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(JSON.toJSONString(u));

        BasicNameValuePair pair = new BasicNameValuePair("X-User", "liyunde");

        String post = Post(uri, body, Collections.singletonList(pair), 60);

        logger.info("返回结果:{}", post);
        
        //{"account":"zhangsan","subNum":0}
        //返回结果:{"content":[{"id":1,"ksNo":"20191107","ccStatus":1,"ccTime":[2019,11,7,11,15,55],"transactionId":"visionpay8BC1D65D671807EAE4840706","threeSerialNo":"20191107111558","eductionTime":[2019,11,7,11,15,55],"num":3,"eductionMoney":3.00,"machineNo":"000000","branchCompany":"深圳分公司","uids":"9384954","serviceReminder":null,"visitStatus":2,"visitTime":[2019,11,7,11,15,55],"handleStatus":2,"handleTime":[2019,11,7,11,15,55],"handleWay":"1","tradeNo":"2019031822001404320584752498"}],"pageable":"INSTANCE","totalElements":1,"last":true,"totalPages":1,"number":0,"size":0,"sort":{"unsorted":true,"sorted":false,"empty":true},"numberOfElements":1,"first":true,"empty":false}
    }
    
    //配置查询
    @Test
    public void testDistributionQuery() {

        String uri = "/api/mermaid/complaint/distributionQuery";
        
        DistributionQuery d = new DistributionQuery();
        
        d.setBeginTime("2019-11-09");
        d.setEndTime("2019-11-09");
       // d.setAccount("zhangsan");

        logger.debug(JSON.toJSONString(d));

        BodyInserter<?, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(JSON.toJSONString(d));

        BasicNameValuePair pair = new BasicNameValuePair("X-User", "liyunde");

        String post = Post(uri, body, Collections.singletonList(pair), 60);

        logger.info("返回结果:{}", post);
        
      //  {"account":"lisi","beginTime":"2019-11-07","endTime":"2019-11-09"}
     //[{"id":8,"account":"lisi","name":"李四","subNum":1,"parentNum":1,"beginTime":"2019-11-07","endTime":"2019-11-09","time":"2019-11-19 11:39:22"}]

        
    }
    
    //配置新建
    @Test
    public void testNewBuild() throws ParseException {

        String uri = "/api/mermaid/complaint/build";
        
        List<User> list = new ArrayList<User>();
        User u = new User();
        u.setAccount("zhangsan");
        u.setName("张三");
        u.setSubNum(1);
        
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
     
        java.util.Date begin=SDF.parse("2019-11-07");
        java.util.Date end=SDF.parse("2019-11-09");
        java.sql.Date beginDate=new java.sql.Date(begin.getTime());
        java.sql.Date endDate=new java.sql.Date(end.getTime());
        
        u.setBeginTime("2019-11-07");
        u.setEndTime("2019-11-07");
        list.add(u);
        
        User u2 = new User();
        u2.setAccount("lisi");
        u2.setName("李四");
        u2.setSubNum(1);
        
       
        u2.setBeginTime("2019-11-07");
        u2.setEndTime("2019-11-07");
        list.add(u2);

        logger.debug(JSON.toJSONString(list));

        BodyInserter<?, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(JSON.toJSONString(list));

        BasicNameValuePair pair = new BasicNameValuePair("X-User", "liyunde");

        String post = Post(uri, body, Collections.singletonList(pair), 60);

        logger.info("返回结果:{}", post);
        
        //{"account":"zhangsan","beginTime":"2019-11-07","endTime":"2019-11-09","name":"张三","subNum":1},{"account":"lisi","beginTime":"2019-11-07","endTime":"2019-11-09","name":"李四","subNum":1}]
        //返回结果:[{"id":3,"account":"zhangsan","name":"张三","subNum":1,"parentNum":1,"beginTime":"2019-11-07","endTime":"2019-11-09","time":"2019-11-19 10:52:11"},{"id":4,"account":"lisi","name":"李四","subNum":1,"parentNum":1,"beginTime":"2019-11-07","endTime":"2019-11-09","time":"2019-11-19 10:52:11"}]

    }
    
  //登录账号验证
    @Test
    public void testAccountVerify() {

        String uri = "/api/mermaid/complaint/accountVerify";
        
       // 账号登录查出所有属于该人的所有时间段的子单
        User u = new User();
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        u.setBeginTime(LocalDateTime.parse("2019-11-07 09:15:55",df));
//        u.setEndTime(LocalDateTime.parse("2019-11-07 18:15:55",df));
        u.setAccount("zhangsan");
        logger.debug(JSON.toJSONString(u));

        BodyInserter<?, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(JSON.toJSONString(u));

        BasicNameValuePair pair = new BasicNameValuePair("X-User", "liyunde");

        String post = Post(uri, body, Collections.singletonList(pair), 60);

        logger.info("返回结果:{}", post);
        
        //{"account":"zhangsan","subNum":0}
        //返回结果:{"msg":"验证成功!","user":"zhangsan","subNum":1}

    }
    
    
    @Test
    public void customerComplaintEntry() {

        String uri = "/api/mermaid/complaint/entry";
        
        EntryComplaint u = new EntryComplaint();
        u.setOrderId(1555002151L);
        logger.debug(JSON.toJSONString(u));

        BodyInserter<?, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(JSON.toJSONString(u));

        BasicNameValuePair pair = new BasicNameValuePair("X-User", "liyunde");

        String post = Post(uri, body, Collections.singletonList(pair), 60);

        logger.info("返回结果:{}", post);
        

    }
    
    
    @Test
    public void customerComplaintStatus() {

        String uri = "/api/mermaid/complaint/orderIdStatus";
        
        EntryComplaint u = new EntryComplaint();
        u.setOrderId(1555002151L);
        logger.debug(JSON.toJSONString(u));

        BodyInserter<?, ReactiveHttpOutputMessage> body = BodyInserters.fromObject(JSON.toJSONString(u));

        BasicNameValuePair pair = new BasicNameValuePair("X-User", "liyunde");

        String post = Post(uri, body, Collections.singletonList(pair), 60);

        logger.info("返回结果:{}", post);
        
        //ApisController customerComplaintStatus 68413 
        //[EntryComplaint(channelId=null, payTypeId=null, orderId=1555002151, userId=null, thirdPartId=null, reason=null, phone=null, remark=null, isRedPacketActivity=null, offset=null, limit=null)] 
        //{"id":1,"transactionId":"visionpay8BC1D65D671807EAE4840706","ksSubNo":"20191217-1555002151","ccStatus":1,"ccTime":"2019-12-17 17:05:43","ccChannel":"客服后台","orderId":1555002151,"orderTime":"2019-11-21 17:10:50","productPrice":0.00,"productName":"","ccPhenomenon":"1","ccAppeal":"方法都是","contact":"13715073248","visitor":"测试人","visitStatus":1,"visitTime":"2019-12-17 17:05:43","visitAdvice":"1","visitRemark":"回访测试","handle":"zhangsan","handleStatus":2,"handleTime":"2019-12-17 17:05:43","handleWay":"1","questionClassify":"1","handleRemark":"风格豆腐干","time":"2019-11-25 17:33:25","uid":null}


    }
    
    
    
    
    
    
    
    
    
    
    
}
