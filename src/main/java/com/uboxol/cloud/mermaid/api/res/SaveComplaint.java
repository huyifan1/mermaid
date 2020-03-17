package com.uboxol.cloud.mermaid.api.res;

import com.uboxol.cloud.mermaid.api.req.SubOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * 子单保存后  返回子单里面的客诉状态以及时间 和 客诉数目
 *
 * @author huyifan
 * @sice 2019/11/01 15:04
 */
@Getter
@Setter
@ToString
public class SaveComplaint {
	SubOrder subOrder;
	
	@ApiModelProperty(value = "客诉数",example = "1")
	int kesuNum;
	
	@ApiModelProperty(value = "处理为退款的时候有值",example = "1-成功 0-失败")
	int Flag;
	
	@ApiModelProperty(value = "处理为退款的时候描述",example = "...")
	String message;
	
}
