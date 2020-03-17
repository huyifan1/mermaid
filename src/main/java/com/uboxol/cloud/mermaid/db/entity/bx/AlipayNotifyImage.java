package com.uboxol.cloud.mermaid.db.entity.bx;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString(callSuper = true)
@DynamicInsert
@DynamicUpdate
@Table(name="alipay_notify_image")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class AlipayNotifyImage {
	@Id
    Long id;
	/**
     * transactionID
     */
	String transactionId;
	/**
     * 图片Id
     */
	String imageId;
	/**
     * 摄像头编号
     */
	String camId;
	/**
     * 拍照时间，1-开门前，2-开门后
     */
	String shootMoment;
}
