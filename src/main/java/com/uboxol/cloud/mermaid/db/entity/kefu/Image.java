package com.uboxol.cloud.mermaid.db.entity.kefu;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="image")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Image {
	
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
     * 图片ID
     */
    String imageId;
    /**
     * 每排的顺序
     */
    int num;
    /**
     * 开门前 1  开门后2
     */
    String flag;
}
