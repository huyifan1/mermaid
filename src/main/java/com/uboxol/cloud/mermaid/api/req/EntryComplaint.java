package com.uboxol.cloud.mermaid.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EntryComplaint {
	
	@ApiModelProperty(value = "渠道")
	String channelId;
	
	@ApiModelProperty(value = "类型")
	String payTypeId;
	
	@ApiModelProperty(value = "订单号",example = "123456")
     Long orderId;
	
	@ApiModelProperty(value = "用户id",example = "288")
	String userId;
	
	@ApiModelProperty(value = "thirdPartId",example = "123")
	String thirdPartId;
	
	@ApiModelProperty(value = "原因",example = "多扣款")
	String reason;
	
	@ApiModelProperty(value = "电话",example = "13761809876")
	String phone;
	
	@ApiModelProperty(value = "描述",example = "已访问")
	String remark;
	
	@ApiModelProperty(value = "isRedPacketActivity")
	String isRedPacketActivity;
	
	@ApiModelProperty(value = "offset",example = "offset")
	String offset;
	
	@ApiModelProperty(value = "limit",example = "limit")
	String limit;

}
