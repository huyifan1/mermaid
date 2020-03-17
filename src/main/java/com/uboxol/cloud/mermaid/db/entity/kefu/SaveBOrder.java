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
@Table(name="save_b_order")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class SaveBOrder {
	
	private static final String DDFormat = "yyyy-MM-dd HH:mm:ss";
	private static final String TIME_ZONE = "GMT+8";
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
	/**
     * transactionID
     */
	@ApiModelProperty(
			value = "交易ID"
		)
	String transactionId;
	/**
     * 客诉子编号
     */
    String ksSubNo;
    
    //这三个字段页面默认显示
    /**
     * 客诉状态
     */
    Integer ccStatus;
    /**
     * 客诉时间
     */
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	private Timestamp ccTime;
    /**
     * 客诉渠道
     */
    String ccChannel;
    
    /**
     * 友宝订单号
     */
    Long orderId;
    /**
     * 订单时间
     */
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	private Timestamp orderTime;
    /**
     * 商品价格
     */
    BigDecimal productPrice;
    /**
     * 商品名称
     */
    String productName;
    
    /**
     * 客诉现象
     */
    String ccPhenomenon;
    /**
     * 客诉诉求
     */
    String ccAppeal;
    /**
     * 联系方式
     */
    String contact;
    /**
     * 回访人
     */
    String visitor;
    /**
     * 回访状态
     */
    Integer visitStatus;
    /**
     * 回访时间
     */
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	private Timestamp visitTime;
    /**
     * 回访建议
     */
    String visitAdvice;
    /**
     * 回访备注
     */
    String visitRemark;
    /**
     * 处理人
     */
    String handle;
    /**
     * 处理状态
     */
    Integer handleStatus;
    /**
     * 处理时间
     */
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	private Timestamp handleTime;
    /**
     * 处理方式
     */
    String handleWay;
    /**
     * 问题归类
     */
    String questionClassify ;
    /**
     *处理备注
     */
    String handleRemark;
}
