package com.uboxol.cloud.mermaid.api.res;

import com.uboxol.cloud.mermaid.db.entity.kefu.CustomerComplaint;

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
public class EntryResult {
	@ApiModelProperty(value = "请求结果",example = "200成功")
	String code;
	
	@ApiModelProperty(value = "请求结果描述",example = "成功/失败")
	String msg;
	
	@ApiModelProperty(value = "客诉主表信息")
	CustomerComplaint customerComplaint;
	
}
