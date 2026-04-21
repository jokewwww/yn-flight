package cn.iwen.weblog.web;

public interface ILogFilter {

	boolean isHeader(String line);
	
	boolean doFilter(LogsBean logsBean);
}
