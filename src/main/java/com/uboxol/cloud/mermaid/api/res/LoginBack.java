package com.uboxol.cloud.mermaid.api.res;

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
public class LoginBack {
	@ApiModelProperty(
		    value = "验证结果",
		    example = "验证成功"
		    )
	String msg;
	
	@ApiModelProperty(
		    value = "统一账号",
		    example = "zhangsan"
		    )
	String user;
	
//	@ApiModelProperty(
//			 value = "订单客诉数",
//			 example = "30"
//		     )
//	int subNum;
	
	@ApiModelProperty(
			 value = "我的统计--今日待办已经完成的",
			 example = "1"
		     )
	int one;
	
	@ApiModelProperty(
			 value = "我的统计--今日待办总数",
			 example = "60"
		     )
	int two;
	
	@ApiModelProperty(
			 value = "我的统计--本月已经完成的",
			 example = "1"
		     )
	int three;
	
	@ApiModelProperty(
			 value = "我的统计--本月总数",
			 example = "60"
		     )
	int four;
	
	@ApiModelProperty(
			 value = "部门统计--今日已经完成的",
			 example = "30"
		     )
	int five;
	
	@ApiModelProperty(
			 value = "部门统计--今日待办总数",
			 example = "30"
		     )
	int six;
	
	@ApiModelProperty(
			 value = "部门统计--本月已经完成的",
			 example = "1"
		     )
	int seven;
	
	@ApiModelProperty(
			 value = "部门统计--本月总数",
			 example = "60"
		     )
	int eight;
	
}
