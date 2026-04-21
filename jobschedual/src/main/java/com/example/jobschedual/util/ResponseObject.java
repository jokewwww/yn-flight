package com.example.jobschedual.util;

import lombok.*;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ResponseObject {

	private int code;//服务状态代码 0,1,2
	private String msg;//普通服务返回消息
	private Object data;//实体返回


	public static ResponseObject error(String msg){
		return new ResponseObject (0,msg,null);
	}
	public static ResponseObject error(String msg,Exception e){
		return new ResponseObject (0,msg,e.getMessage());
	}

	public static  ResponseObject success(Object obj, String msg){
		return new ResponseObject (1,msg,obj);
	}
}
