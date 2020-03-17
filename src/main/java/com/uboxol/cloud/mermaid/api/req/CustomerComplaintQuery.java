package com.uboxol.cloud.mermaid.api.req;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/10/28 15:34
 */
@Getter
@Setter
@ToString
public class CustomerComplaintQuery {
	private static final String DATE = "yyyy-MM-dd";
	
	@ApiModelProperty(
		    value = "统一账号",
		    example = "zhangsan"
		    )
	String account;

    @ApiModelProperty(
        value = "每页条数",
        example = "30"
    )
    int pageSize = 30;

    @ApiModelProperty(
        value = "页码",
        example = "0"
    )
    int page;
    
    @ApiModelProperty(
            value = "uids",
            example = "59733314"
        )
    String uids;
    
    @ApiModelProperty(
        value = "transactionId",
        example = "59733314"
    )
    String transactionId;

    @ApiModelProperty(
        value = "支付宝交易号",
        example = "59733314"
    )
    String tradeNo;

    @ApiModelProperty(
        value = "机器号",
        example = "59733314"
    )
    String vm;

    @ApiModelProperty(
            value = "客诉状态",
            example = "1"
        )
    Integer ccStatus;

    @ApiModelProperty(
            value = "回访状态",
            example = "2"
        )
    Integer visitStatus;

    @ApiModelProperty(
            value = "处理状态",
            example = "1"
        )
    Integer handleStatus;

    @ApiModelProperty(
        value = "处理开始时间",
        example = "2019-10-30 10:10:00~2019-10-31 10:10:00"
    )
    @JsonFormat(pattern=DATE)
    String handlebeginTime;
    @ApiModelProperty(
            value = "处理结束时间",
            example = "2019-10-30 10:10:00~2019-10-31 10:10:00"
        )
    @JsonFormat(pattern=DATE)
    String  handleendTime;

    @ApiModelProperty(
        value = "客诉开始时间",
        example = "2019-10-30 10:10:00~2019-10-31 10:10:00"
    )
    @JsonFormat(pattern=DATE)
    String  ccbeginTime;
    @ApiModelProperty(
            value = "客诉结束时间",
            example = "2019-10-30 10:10:00~2019-10-31 10:10:00"
        )
    @JsonFormat(pattern=DATE)
    String  ccendTime;

    @ApiModelProperty(
        value = "分公司",
        example = "深圳分公司"
    )
    String company;
//Integer companyId;
    
    @ApiModelProperty(
        value = "订单笔数",
        example = ">=1"
    )
    Integer orderNum;
    
    @ApiModelProperty(
            value = "子单数",
            example = ">=1"
        )
        String subOrderCnt;

    @ApiModelProperty(
        value = "总金额",
        example = ">=10"
    )
    BigDecimal totalAmount;
}
