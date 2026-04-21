package cn.iwen.frame.redis;

public class OutMessage<T> {

	// 通知的人员ID
	private String to;

	// 0：人员状态通知，1:任务状态通知;2:航班状态通知
	private String flg;

	// 0 人员登录
	// 1 人员登出
	// 102 心跳检测  【前的pc端和pad都请无视】
	// 103 断网     【前台的pc端需要处理，pad端请无视】
	// 104 加油员或调度员自己下线（告知过期，WS需要断掉） 【pc和pad都需要处理】
	// 2 人车绑定
	// 3 任务下发-已下发任务列表
	// 4 任务状态变更-任务列表
	// 5 任务状态变更-已下发任务列表
	// 6 任务取消-已下发任务列表
	// 7 航班新建--本场起飞
	// 8 航班新建--任务新加
	// 9 航班新建--重要消息
	// 10 航班修改--本场起飞
	// 11 航班修改--任务变更
	// 12 航班修改--重要消息
	private String type;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	public OutMessage(String to, String flg, String type, T content) {
		super();
		this.to = to;
		this.flg = flg;
		this.type = type;
		this.content = content;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	private T content;

	public OutMessage() {
	}

	public OutMessage(T content) {
		this.content = content;

	}
	private String curDate;

	public String getCurDate() {
		return curDate;
	}

	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}
		public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFlg() {
		return flg;
	}

	public void setFlg(String flg) {
		this.flg = flg;
	}

}
