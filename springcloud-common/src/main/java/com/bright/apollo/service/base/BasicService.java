package com.bright.apollo.service.base;

import java.util.List;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月2日
 * @Version:1.1.0
 */
public interface BasicService {
	@SuppressWarnings("rawtypes")
	public <T ,  E> T handlerExample(E e);
	
	@SuppressWarnings("rawtypes")
	public <T , E> List<T> handlerExampleToList(E e);
	//select by primarykey
	<T> T queryTById(T t);

	 
}