package com.uboxol.cloud.mermaid.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.uboxol.cloud.mermaid.db.entity.kefu.CustomerComplaint;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/10/28 14:53
 */
@Mapper
public interface CustomerComplaintMapper {

	 List<CustomerComplaint> findAll(CustomerComplaint customerComplaint);
	 
	 List<CustomerComplaint> findAllLikeTime(CustomerComplaint customerComplaint);
	
}
