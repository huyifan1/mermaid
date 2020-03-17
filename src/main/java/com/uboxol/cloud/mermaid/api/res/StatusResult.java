package com.uboxol.cloud.mermaid.api.res;

import com.uboxol.cloud.mermaid.db.entity.kefu.BOrder;

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
public class StatusResult {
	//只有code="200" mas="成功" bOrder里才会有详细的子单信息
	
	@ApiModelProperty(value = "请求结果",example = "200成功")
	String code;
	
	@ApiModelProperty(value = "查询子单结果描述",example = "表里没有查到当前子单/失败")
	String msg;

	@ApiModelProperty(value = "子单查询结果",example = "null代表没查到该单")
	BOrder bOrder;
	
}
