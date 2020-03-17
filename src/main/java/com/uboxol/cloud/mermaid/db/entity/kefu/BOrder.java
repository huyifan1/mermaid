package com.uboxol.cloud.mermaid.db.entity.kefu;

import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="b_order")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class BOrder {
	private static final String DDFormat = "yyyy-MM-dd HH:mm:ss";
	private static final String TIME_ZONE = "GMT+8";
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
	
	@ApiModelProperty(value = "交易ID")
	String transactionId;
	
	@ApiModelProperty(value = "客诉子编号",example = "59733314")
    String ksSubNo;
    
    @ApiModelProperty(value = "客诉状态",example = "1进行中,9已闭环")
    Integer ccStatus;
   
    @ApiModelProperty(value = "客诉时间",example = "2019-11-07 09:15:55")
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	private Timestamp  ccTime;
    
    @ApiModelProperty(value = "客诉渠道",example = "1")
    String ccChannel;
    
    @ApiModelProperty(value = "友宝订单号",example = "123456")
    Long orderId;
    
    @ApiModelProperty(value = "订单时间",example = "2019-11-07 09:15:55")
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	private Timestamp  orderTime;

    @ApiModelProperty(value = "商品价格",example = "55")
    BigDecimal productPrice;
   
    @ApiModelProperty(value = "商品名称",example = "商品名称")
    String productName;
    
    @ApiModelProperty(value = "客诉现象",example = "1")
    String ccPhenomenon;
    
    @ApiModelProperty(value = "客诉诉求",example = "1")
    String ccAppeal;
    
    @ApiModelProperty(value = "联系方式",example = "1234")
    String contact;
    
    @ApiModelProperty(value = "回访人",example = "回访人")
    String visitor;
    
    @ApiModelProperty(value = "回访状态",example = "1")
    Integer visitStatus;
    
    @ApiModelProperty(value = "回访时间",example = "2019-11-07 09:15:55")
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	private Timestamp  visitTime;
    
    @ApiModelProperty(value = "回访建议",example = "1")
    String visitAdvice;
    
    @ApiModelProperty(value = "回访备注",example = "回访备注")
    String visitRemark;
   
    @ApiModelProperty(value = "处理人",example = "zhangsan")
    String handle;
    
    @ApiModelProperty(value = "处理状态",example = "1")
    Integer handleStatus;
    
    @ApiModelProperty(value = "处理时间",example = "2019-11-07 09:15:55")
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	private Timestamp  handleTime;
    
    @ApiModelProperty(value = "处理方式",example = "1")
    String handleWay;
    
    @ApiModelProperty(value = "问题归类",example = "1")
    String questionClassify ;
   
    @ApiModelProperty(value = "处理备注",example = "处理备注")
    String handleRemark;
    
    @ApiModelProperty(
            value = "子单被处理人分配的时间",
            example = "2019-10-30 10:10:00"
        )
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	private Timestamp time;
    
    @ApiModelProperty(value = "uid",example = "2778...")
    Integer uid ;
    
    
}
