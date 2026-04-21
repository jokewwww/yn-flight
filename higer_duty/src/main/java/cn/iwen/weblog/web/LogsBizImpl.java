package cn.iwen.weblog.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.iwen.frame.BaseUtils;
import cn.iwen.frame.dao.BaseDao;

@Service
public class LogsBizImpl extends BaseDao<LogsBean> implements ILogsBiz {

	protected static  Log log = LogFactory.getLog(LogsBizImpl.class);
	
	@Resource(name = "anroidLogFilterImpl")
	private ILogFilter appFilter;
	
	@Override
	public void importLog(String filePath) {
		try {
			super.setLogFlag(false);
			String encoder = "UTF-8";
			File filename = new File(filePath);
			InputStreamReader reader;
			reader = new InputStreamReader(new FileInputStream(filename), encoder);
			BufferedReader bufReader = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能
			parseLog(bufReader);
			bufReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 日志内容分析
	 * */
	private void doFilter(LogsBean logsBean) {
		String msg = logsBean.getMsg();
		if(msg == null) return;
		appFilter.doFilter(logsBean);
		super.add(logsBean);
	}
	
	/*
	 * 处理步骤：
	 * 1 读取一行文本
	 * 2 判断是否为记录头，若不时跳到步骤4
	 * 3 关闭上一个日志记录，并进行日志分析，提取关键字，新建日志记录，并解析参数，然后回到步骤1
	 * 4 添加文本到当前记录里
	 * */
	public void parseLog(BufferedReader bufReader) throws IOException {
		int count = 0;
		LogsBean currLogBean = new LogsBean();//当前
		do {
			String line = bufReader.readLine();
			if(line == null) break;//结束
			if(line.length() == 0) continue;//空行
			if(appFilter.isHeader(line)) {
				//上一个日志完整了
				if(currLogBean != null) {
					doFilter(currLogBean);
				}
				currLogBean = new LogsBean();
			}
			currLogBean.addMsg(line);
		}while(true && count++ < 13500);
		doFilter(currLogBean);
	}
	
	public static void main(String[] args) {
		String msg = "\"flrcNo\":\"2901360001720\",";
		String[] strs = BaseUtils.regMatches(msg, "\"(\\d{13})\"");
		System.out.println(JSON.toJSON(strs));
	}
}
