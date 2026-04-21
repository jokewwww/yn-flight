package cn.iwen.frame;

import java.util.ArrayList;
import java.util.List;

/*
 * 前台后台分页参数类
 */
public final class PageBean<T> extends MsgBean {

	public PageBean(){}
	
	public PageBean(int pageSize){
		this.pageSize = pageSize;
	}
	
	private List<T> rows = new ArrayList<>();
	//记录总数
	private Integer total;
	//页数
	private Integer pageNum = 1;
	//行数
	private Integer pageSize = 20;
	//是否查询总记录数
	private boolean bcount = true;
	//获取偏移量
    public Integer getOffset() {
    	if(pageNum == null || pageSize == null)
    		return 0;
		if(pageNum < 1) pageNum = 1;
		return (pageNum - 1)* pageSize;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}


	public Integer getPageNum() {
		return pageNum;
	}


	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}


	public Integer getPageSize() {
		return pageSize;
	}


	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getRows() {
		return rows;
	}


	public void setRows(List<T> rows) {
		this.rows = rows;
	}


	public boolean isBcount() {
		return bcount;
	}


	public void setBcount(boolean bcount) {
		this.bcount = bcount;
	}
	
}
