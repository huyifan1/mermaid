package com.uboxol.cloud.mermaid.api.res;

import java.util.List;

import com.uboxol.cloud.mermaid.api.req.SubOrder;
import com.uboxol.cloud.mermaid.api.req.SubOrderAll;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * model: mermaid
 *
 * @author huyifan
 * @sice 2019/10/18 15:04
 */
@Getter
@Setter
@ToString
public class QueryBack {

    @ApiModelProperty(
        value = "基础交易信息",
        required = true,
        example = "object"
    )
    BasicInformation trade;
    @ApiModelProperty(
        value = "错误码参考"
    )
    List<ArrorMsg> errorMsg;
    @ApiModelProperty(
        value = "子单列表信息"
    )
    List<SubOrder> subOrderList;
    @ApiModelProperty(
            value = "子单列表信息"
        )
    List<SubOrderAll> subOrderAllList;
    @ApiModelProperty(
        value = "订单数",
        example = "3"
    )
    int orderNum;

    @ApiModelProperty(
        value = "客诉数",
        example = "1"
    )
    int kesuNum;
    
    @ApiModelProperty(
            value = "客诉编号",
            example = "19101099"
        )
    String ksNo;

    @ApiModelProperty(
        value = "开柜前图片",
        example = "[\"http://xxxx\"]"
    )
    String[] imageBefore;

    @ApiModelProperty(
        value = "开柜后图片",
        example = "[\"http://xxxx\"]"
    )
    String[] imageAfter;
}
