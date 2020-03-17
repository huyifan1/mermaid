package com.uboxol.cloud.mermaid.api.res;

import java.time.LocalDateTime;
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
public class ArrorMsg {
	/**
     * 时间
     */
	LocalDateTime createTime;
	/**
     * errorCode
     */
	String errorCode;
	/**
     * errorMsg
     */
	String errorMsg;
}
