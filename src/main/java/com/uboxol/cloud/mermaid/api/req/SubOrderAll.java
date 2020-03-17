package com.uboxol.cloud.mermaid.api.req;

import java.math.BigDecimal;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.liaochong.myexcel.core.annotation.ExcelColumn;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubOrderAll {
	private static final String DDFormat = "yyyy-MM-dd HH:mm:ss";
	private static final String TIME_ZONE = "GMT+8";
	
	
	@ApiModelProperty(value = "客诉子编号",example = "59733314")
	@ExcelColumn(title = "客诉编号(子)")
    String ksSubNo;
    
    @ApiModelProperty(value = "客诉状态",example = "1进行中,9已闭环")
    //@ExcelColumn(title = "客诉状态(子)")
    Integer ccStatus;
    
    @ApiModelProperty(value = "客诉状态",example = "1进行中,9已闭环")
    @ExcelColumn(title = "客诉状态(子)")
    String status;
   
    @ApiModelProperty(value = "客诉时间",example = "2019-11-07 09:15:55")
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
    //@ExcelColumn(title = "客诉时间")
	private Timestamp ccTime;
    
    @ApiModelProperty(value = "客诉时间",example = "2019-11-07 09:15:55")
    @ExcelColumn(title = "客诉时间")
	String time;
    
    @ApiModelProperty(value = "交易ID")
	@ExcelColumn(title = "交易ID")
	String transactionId;
    
  //以下字段是导出总表时候加的主单的几个字段
    @ApiModelProperty(value = "第三方流水号",example = "")
	@ExcelColumn(title = "第三方流水号")
     String threeSerialNo;
    
    @ApiModelProperty(value = "扣款时间",example = "2019-11-07 09:15:55")
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	//@ExcelColumn(title = "扣款时间")
	private Timestamp eductionTime;
    
    @ApiModelProperty(value = "扣款时间",example = "2019-11-07 09:15:55")
	@ExcelColumn(title = "扣款时间")
    String kkTime;
    
    @ApiModelProperty(value = "机器编号",example = "55")
    @ExcelColumn(title = "机器编号")
    String machineNo;
    
    @ApiModelProperty(value = "分公司",example = "分公司")
    @ExcelColumn(title = "分公司")
    String branchCompany;
    
    @ApiModelProperty(value = "用户UID",example = "55")
    @ExcelColumn(title = "用户UID")
    String uids;
    
    @ApiModelProperty(value = "服务提示",example = "服务提示")
    @ExcelColumn(title = "服务提示") 
    String serviceReminder;
    
    
    @ApiModelProperty(value = "客诉渠道",example = "1")
    @ExcelColumn(title = "客诉渠道")
    String ccChannel;
    
    @ApiModelProperty(value = "友宝订单号",example = "123456")
    @ExcelColumn(title = "友宝订单号")
    Long orderId;
    
    @ApiModelProperty(value = "订单时间",example = "2019-11-07 09:15:55")
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
    //@ExcelColumn(title = "订单时间")
	private Timestamp orderTime;
    
    @ApiModelProperty(value = "订单时间",example = "2019-11-07 09:15:55")
    @ExcelColumn(title = "订单时间")
    String orTime;
    
    @ApiModelProperty(value = "商品价格",example = "55")
    @ExcelColumn(title = "商品价格")
    BigDecimal productPrice;
    
    @ApiModelProperty(value = "商品名称",example = "商品名称")
    @ExcelColumn(title = "商品名称")
    String productName;
    
    @ApiModelProperty(value = "客诉现象",example = "1")
    @ExcelColumn(title = "客诉现象")
    String ccPhenomenon;
    
    @ApiModelProperty(value = "客诉诉求",example = "1")
    @ExcelColumn(title = "客诉诉求")
    String ccAppeal;
    
    @ApiModelProperty(value = "联系方式",example = "1234")
    @ExcelColumn(title = "联系方式")
    String contact;
    
    @ApiModelProperty(value = "回访人",example = "回访人")
    @ExcelColumn(title = "回访人")
    String visitor;
    
    @ApiModelProperty(value = "回访状态",example = "1")
    //@ExcelColumn(title = "回访状态")
    Integer visitStatus;
    
    @ApiModelProperty(value = "回访状态",example = "1需回访 2回访中 3已回访")
    @ExcelColumn(title = "回访状态")
    String vStatus;
    
    @ApiModelProperty(value = "回访时间",example = "2019-11-07 09:15:55")
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
    //@ExcelColumn(title = "回访时间")
	private Timestamp visitTime;
    
    @ApiModelProperty(value = "回访时间",example = "2019-11-07 09:15:55")
    @ExcelColumn(title = "回访时间")
    String vTime;
    
    @ApiModelProperty(value = "回访建议",example = "1")
    @ExcelColumn(title = "回访建议")
    String visitAdvice;
    
    @ApiModelProperty(value = "回访备注",example = "回访备注")
    @ExcelColumn(title = "回访备注")
    String visitRemark;
   
    @ApiModelProperty(value = "处理人",example = "zhangsan")
    @ExcelColumn(title = "处理人")
    String handle;
    
    @ApiModelProperty(value = "处理状态",example = "1")
    //@ExcelColumn(title = "处理状态")
    Integer handleStatus;
    
    @ApiModelProperty(value = "处理状态",example = "1待处理2处理中3已处理")
    @ExcelColumn(title = "处理状态")
    String hStatus;
    
    @ApiModelProperty(value = "处理时间",example = "2019-11-07 09:15:55")
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
    //@ExcelColumn(title = "处理时间")
	private Timestamp handleTime;
    
    @ApiModelProperty(value = "处理时间",example = "2019-11-07 09:15:55")
    @ExcelColumn(title = "处理时间")
    String hTime;
    
    @ApiModelProperty(value = "处理方式",example = "1")
    @ExcelColumn(title = "处理方式")
    String handleWay;
    
    @ApiModelProperty(value = "问题归类",example = "1")
    @ExcelColumn(title = "问题归类")
    String questionClassify ;
    
    @ApiModelProperty(value = "处理备注",example = "处理备注")
    @ExcelColumn(title = "处理备注")
    String handleRemark;
    
    
}
