package com.uboxol.cloud.mermaid.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * 录入客诉
 * 交易查询，订单查询
 *
 * @author huyifan
 * @since 2019/10/28 15:34
 */
@Getter
@Setter
@ToString
public class Query {
	@ApiModelProperty(
        value = "transactionId",
        example = "59733314"
    )
    String transactionId;
	
	@ApiModelProperty(
        value = "友宝订单号",
        example = "59733314"
    )
    Long orderId;
	    
    @ApiModelProperty(
        value = "支付宝交易号",
        example = "59733314"
    )
    String tradeNo;
}
