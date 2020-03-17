package com.uboxol.cloud.mermaid.db.entity.kefu;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="work_order")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})

public class WorkOrder {
	private static final String DDFormat = "yyyy-MM-dd HH:mm:ss";
	private static final String TIME_ZONE = "GMT+8";
	private static final String DATE = "yyyy-MM-dd";
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

	@ApiModelProperty(
    value = "统一账号",
    example = "zhangsan"
    )
    String account;

    @ApiModelProperty(
     value = "姓名",
     example = "张三"
	 )
	 String name;
    
    @ApiModelProperty(
	 value = "订单客诉数",
	 example = "30"
     )
    int subNum;
    
    @ApiModelProperty(
	 value = "客诉工单数(父单)",
	 example = "30"
     )
    int parentNum;
    
    @ApiModelProperty(
	 value = "操作人",
	 example = "张三"
     )
     String operator;
    
    @ApiModelProperty(
	        value = "分配日期起始时间",
	        example = "2019-10-30"
	    )
    @JsonFormat(pattern=DATE)
    String  beginTime;
	
	@ApiModelProperty(
	        value = "分配日期结束时间",
	        example = "2019-11-30"
	    )
	@JsonFormat(pattern=DATE)
	String  endTime;
	
    @ApiModelProperty(
        value = "分配时间",
        example = "2019-10-30 10:10:00"
    )
    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
	private Timestamp time;
}
