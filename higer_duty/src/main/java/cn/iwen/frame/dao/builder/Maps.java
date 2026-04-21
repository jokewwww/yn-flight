package cn.iwen.frame.dao.builder;

import java.util.HashMap;

public class Maps extends HashMap<String,Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Maps make() {
		return new Maps();
	}
	
	public Maps put(String key,Object value) {
		super.put(key, value);
		return this;
	}

}
