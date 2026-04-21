package com.higer.statistical.util;

import org.springframework.data.domain.Page;

public class ResponseObject<T> {
	public ResponseObject() {
	}

	public ResponseObject(Page<T> page) {
		PageInfo<T> pi=new PageInfo<T>(page);
		this.data=page.getContent();
		this.pageInfo=pi;
	}

	public ResponseObject(int code,T t,String msg){
		this.msg=msg;
		if(t instanceof Page){
			Page<T> pageT = (Page<T>) t;
			PageInfo<T> pi=new PageInfo<T>(pageT);
			this.pageInfo=pi;
			this.data=pageT.getContent();
		}else{
			this.data=t;
		}
		this.code=code;
	}

	public ResponseObject(int code,Page<T> page){
		PageInfo<T> pi=new PageInfo<T>(page);
		this.data=page.getContent();
		this.pageInfo=pi;
		this.code=code;
		this.data=page.getContent();
	}


	private int code;//服务状态代码 0,1,2
	private String msg;//普通服务返回消息
	private Object data;//实体返回
	private PageInfo pageInfo;//分页信息


	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

	@Override
	public String toString() {
		return "ResponseObject{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", data=" + data +
				", pageInfo=" + pageInfo +
				'}';
	}

	public static ResponseObject error(String msg){
		return new ResponseObject (0,null,msg);
	}

	public static <T> ResponseObject<T> success(T obj, String msg){
		return new ResponseObject (1,obj,msg);
	}

	public static <T> ResponseObject<T> page(Page<T> page){
		return new ResponseObject<>(1,page);
	}
}
