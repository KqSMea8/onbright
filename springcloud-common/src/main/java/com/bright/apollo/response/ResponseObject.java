package com.bright.apollo.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @param <T>
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月9日
 * @Version:1.1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseObject<T> implements Serializable {
	/**  
	 *   
	 */
	private static final long serialVersionUID = -3946200024233048495L;

	private String message;

	private int status;

	private T data;

	private Integer pageIndex;

	private Integer pageSize;

	private Integer pageCount;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

}
