package com.bright.apollo.dao.mapper.base;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @Title: BaseMapper.java 
 * @Package com.bright.apollo.base  
 * @Description: TODO 
 * @author Liujj
 * @date 2017年10月12日 下午5:22:53
 * @version V1.0   
 * @param <T>
 */
@Deprecated
@Component
public interface BaseMapper<T, E, PK extends Serializable> {

	long countByExample(E example);

	int deleteByExample(E example);

	int deleteByPrimaryKey(PK pk);

	int insert(T record);

	int insertSelective(T record);

	List<T> selectByExample(E example);

	T selectByPrimaryKey(PK pk);

	int updateByExampleSelective(@Param("record") T record,
			@Param("example") E example);

	int updateByExample(@Param("record") T record, @Param("example") E example);

	int updateByPrimaryKeySelective(T record);

	int updateByPrimaryKey(T record);
}