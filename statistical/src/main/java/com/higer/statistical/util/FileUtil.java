package com.higer.statistical.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

public class FileUtil {

    public static void downloadFiles(String prefix,String suffix,File file, HttpServletRequest request, HttpServletResponse response) throws IOException {
        IOUtils.copy(new FileInputStream(file),response.getOutputStream());
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition","attachment; filename=" +  URLEncoder.encode(prefix, "UTF-8")+ suffix);//导出中文名称
        FileUtils.deleteQuietly(file);
    }
}
