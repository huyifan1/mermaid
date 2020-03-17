package com.uboxol.cloud.mermaid.api.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DistributionQuery {
	private static final String DATE = "yyyy-MM-dd";
	
	@ApiModelProperty(
	        value = "分配日期起始时间",
	        example = "2019-10-30"
	    )
	@JsonFormat(pattern=DATE)
	String beginTime;
	
	@ApiModelProperty(
	        value = "分配日期结束时间",
	        example = "2019-11-30"
	    )
	@JsonFormat(pattern=DATE)
	String endTime;
	
	@ApiModelProperty(
	        value = "统一账号",
	        example = "zhangsan"
	    )
	String account;
}
