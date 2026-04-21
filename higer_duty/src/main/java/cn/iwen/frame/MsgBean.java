package cn.iwen.frame;

public class MsgBean{

    private int id;

    private String info;

    private Object data;

    public MsgBean setRetInfo(int i, String retmsg) {
		this.id = i;
		this.info = retmsg;
		return this;
	}
    
	public String getInfo() {
		return info;
	}

	public String getErrInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getId() {
		return id;
	}
	
	public int getCode() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}