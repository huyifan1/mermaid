package com.uboxol.cloud.mermaid.db.entity.kefu;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.liaochong.myexcel.core.annotation.ExcelColumn;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author huyifan
 * @version 创建时间：2019年4月8日 下午6:06:43
 * <p>
 * 类说明    客诉信息录入表
 */
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="customer_complaint")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class CustomerComplaint{
	private static final String DDFormat = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE = "yyyy-MM-dd";
	private static final String TIME_ZONE = "GMT+8";
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
	
	@ApiModelProperty(value = "统一账号",example = "zhangsan")
	String account;
	
	@ApiModelProperty(value = "客诉主编号",example = "20191010")
	@ExcelColumn(title = "客诉编号(主)")
	String ksNo;
    /**
     * 客诉状态
     * 
     * 1	进行中   含：进行中 时  
     * 		子单的处理状态为：待处理 或 处理中 时，那么子单的客诉状态为：进行中
     * 9	已闭环   均：已闭环 时  
     * 		子单的处理状态为：已处理 时，那么子单的客诉状态为：已闭环
     * 
     */
	@ApiModelProperty(value = "客诉状态",example = "1	进行中,9	已闭环")
	@ExcelColumn(title = "客诉状态(主)")
	Integer ccStatus;
    
	@ApiModelProperty(value = "客诉时间",example = "2019-11-07 09:15:55")
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	@ExcelColumn(title = "客诉时间(最新)")
	private Timestamp ccTime;
   
	@ApiModelProperty(value = "客诉开始日期",example = "2019-11-07")
    @JsonFormat(pattern=DATE)
    String ccbeginTime;
   
	@ApiModelProperty(value = "客诉结束日期",example = "2019-11-07")
    @JsonFormat(pattern=DATE)
    String ccendTime;
    
	@ApiModelProperty(value = "transactionID",example = "")
	@ExcelColumn(title = "交易ID")
	String transactionId;
    
	@ApiModelProperty(value = "第三方流水号",example = "")
	@ExcelColumn(title = "第三方流水号")
     String threeSerialNo;
    
	@ApiModelProperty(value = "扣款时间",example = "2019-11-07 09:15:55")
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	@ExcelColumn(title = "扣款时间")
	private Timestamp eductionTime;
	
    @ApiModelProperty(value = "订单数量",example = "3")
    @ExcelColumn(title = "订单数量")
    Integer orderNum;
    
    @ApiModelProperty(value = "扣款金额",example = "55")
    @ExcelColumn(title = "订单金额")
    BigDecimal eductionMoney;
   
    @ApiModelProperty(value = "机器编号",example = "55")
    @ExcelColumn(title = "机器编号")
    String machineNo;
   
    @ApiModelProperty(value = "分公司",example = "分公司")
    @ExcelColumn(title = "分公司")
    String branchCompany;
    
    @ApiModelProperty(value = "用户UID",example = "55")
    @ExcelColumn(title = "用户UID")
    String uids;
    
    @ApiModelProperty(value = "用户UID",example = "2088..")
    @ExcelColumn(title = "用户UID")
    String uId1;
    
    @ApiModelProperty(value = "用户UID",example = "17..")
    @ExcelColumn(title = "用户UID")
    String uId2;
    
    @ApiModelProperty(value = "服务提示",example = "服务提示")
    @ExcelColumn(title = "服务提示") 
    String serviceReminder;
     /**
      * 回访状态
      * 
      * 1	需回访  仅：需回访 时	子单的回访状态为：需回访
      * 2	回访中  含：回访中 时	子单的回访状态为：回访中
      * 3	已回访  均：已回访 时	子单的回访状态为：已回访
      */
    @ApiModelProperty(value = "回访状态",example = "1")
    @ExcelColumn(title = "回访状态")
     Integer visitStatus;
     
    @ApiModelProperty(value = "回访时间",example = "2019-11-07 09:15:55")
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
    @ExcelColumn(title = "回访时间")
    private Timestamp visitTime;
     /**
      * 处理状态
      * 
      * 1	待处理  仅：待处理 时	子单的处理状态为：待处理
      * 2	处理中  含：处理中 时	 子单的处理状态为：处理中（注意，处理中，是包含全部的回访状态哦）
      * 3	已处理  均：已处理 时	子单的处理状态为：已处理
      * 
      */
    @ApiModelProperty(value = "处理状态",example = "1")
    @ExcelColumn(title = "处理状态")
     Integer handleStatus;
     
    @ApiModelProperty(value = "处理时间",example = "2019-11-07 09:15:55")
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
    @ExcelColumn(title = "处理时间")
 	private Timestamp handleTime;
   
    @ApiModelProperty(value = "处理开始日期",example = "2019-11-07")
    @JsonFormat(pattern=DATE)
    String  handlebeginTime;
    
    @ApiModelProperty(value = "处理结束日期",example = "2019-11-07")
    @JsonFormat(pattern=DATE)
     String  handleendTime;
     
     @ApiModelProperty(value = "处理方式",example = "1")
     @ExcelColumn(title = "处理方式")
     String handleWay;
     
     @ApiModelProperty(value = "支付宝交易号",example = "20191107")
     String tradeNo;
//    /**
//     * 发生时间
//     */
//    LocalDateTime happenTime;
//    /**
//     * 错误码
//     */
//    String errorCode;
      
}
