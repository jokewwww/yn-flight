package cn.iwen.frame.dao;

@SuppressWarnings("serial")
public class OrmException extends RuntimeException {

	public OrmException(String key) {
		super(String.format("Error: %s!", key));
	}

}
