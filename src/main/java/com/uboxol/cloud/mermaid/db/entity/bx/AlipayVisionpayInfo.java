package com.uboxol.cloud.mermaid.db.entity.bx;

import javax.persistence.Entity;
import javax.persistence.Id;
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
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class AlipayVisionpayInfo {
	@Id
    Long id;
    /**
     * 支付宝用户id
     */
    String userId;
    /**
     * transactionID
     */
    String transactionId;

}
