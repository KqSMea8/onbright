package com.bright.apollo.service;

import java.util.concurrent.Future;

/**
 * @param <E>
 * @param <T>
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月10日
 * @Version:1.1.0
 */
public interface AsyncService {
	public void asyncInvoke();

	public <T> void asyncInvoke(T t);

	public <E, T> Future<E> asyncInvokeReturnFuture(T t);
}
