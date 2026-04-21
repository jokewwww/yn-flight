package com.higer.statistical.util;



/**
 * 分页信息
 *
 */
public class Pager {
	int pageSize = 10;// 默认每页的行数
	int page = 1; // 默认当前页
	int totalCount = 0;// 总记录数
	int totalPage = 0;// 总页数
	private String sort; // 排序字段名字
	private String order; // 排序规则

	public Pager() {
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPage() {
		return page;
	}

	public void checkPages() {
		if (page > totalPage) {
			page = 1;
		}
		if (page < 1) {
			page = 1;
		}
	}

	public int getTotalPage() {
		return totalPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		this.totalPage = (int) Math.ceil(totalCount * 1.0 / pageSize);
	}

	public void setTotalCount(int totalCount, int pageSize) {
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.totalPage = (int) Math.ceil(totalCount * 1.0 / pageSize);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrderByClause() {
		if (sort == null || sort.length() == 0) {
			return null;
		}
		if (order == null) {
			order = "";
		}
		return " ".concat(StringUtils.camelVunderline(sort)).concat(" ").concat(order);
	}
}
