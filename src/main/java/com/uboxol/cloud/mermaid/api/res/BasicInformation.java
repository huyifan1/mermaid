package com.uboxol.cloud.mermaid.api.res;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
/**
 * model: mermaid
 *
 * @author huyifan
 * @sice 2019/10/18 15:04
 */
@Getter
@Setter
public class BasicInformation {
	private static final String DDFormat = "yyyy-MM-dd HH:mm:ss";
	private static final String TIME_ZONE = "GMT+8";
	
	@ApiModelProperty(
			value = "交易ID"
		)
	String transactionId;
	
	@ApiModelProperty(
		    value = "扣款时间",
		    example = "yyyy-MM-dd HH:mm:ss"
		    )
	@JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	private Timestamp deductionTime;
	
	@ApiModelProperty(
		    value = "用户UID",
		    example = "112345"
		    )
	List<String> userUID;
	
	@ApiModelProperty(
			value = "支付宝交易单号"
		)
	String tradeNo;
	
	@ApiModelProperty(
		    value = "扣款金额",
		    example = "112"
		    )
	BigDecimal amount;
	
	@ApiModelProperty(
		    value = "机器编号",
		    example = "112"
		    )
	String vmId;
	
	@ApiModelProperty(
		    value = "发生时间",
		    example = "yyyy-MM-dd HH:mm:ss"
		    )
	@JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	private Timestamp createTime;
	
	@ApiModelProperty(
		    value = "错误码",
		    example = "1000"
		    )
	String errcode;
	
	@ApiModelProperty(
		    value = "分公司",
		    example = "深圳分公司"
		    )
	String brachCompany;

}
