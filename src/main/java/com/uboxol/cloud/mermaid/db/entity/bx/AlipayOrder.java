package com.uboxol.cloud.mermaid.db.entity.bx;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@ToString(callSuper = true)
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class AlipayOrder {
	private static final String DDFormat = "yyyy-MM-dd HH:mm:ss";
	private static final String TIME_ZONE = "GMT+8";
	
    @Id
    Long id;
    /**
     * 支付宝交易单号
     */
    String tradeNo;
    /**
     * 友宝订单号
     */
    Long orderId;
    /**
     * transactionID
     */
    String transactionId;
    /**
     * 订单金额
     */
    BigDecimal cost;
    /**
     * 创建时间
     */
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	private Timestamp createTime;
    /**
     * 退款时间
     */
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	private Timestamp refundTime;
    /**
     * 退款标识
     */
    String outRequestNo;
    /**
     * 商品id
     */
    String goodId;
    /**
     * 订单状态，0待支付，1已完成
     */
    Integer orderStatus;
    /**
     * 机器编号
     */
    String vmId;
    /**
     * 标识收款系统，退款使用，空为冰箱服务后台
     */
    Integer payeeSys;


}
