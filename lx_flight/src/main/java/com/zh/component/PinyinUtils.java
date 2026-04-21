package com.zh.component;

import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PinyinUtils {

    public static char getPinyinFirstWord(String chineseWord) {
        try {
            if (StringUtils.isNotBlank(chineseWord)) {
                char[] chars = chineseWord.toCharArray();
                String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(chars[0]);
                if (pinyin == null) {
                    return chars[0];
                }
                return pinyin[0].charAt(0);
            }
        } catch (Exception e) {
            System.out.println("出错字符：" + chineseWord);
            e.printStackTrace();
        }
        return '\0';
    }

    public static void main(String[] args) {
        /**
         * @param args
         */
// TODO Auto-generated method stub
        String url = "jdbc:mysql://123.56.1.170:3306/zhoil_flight?useUnicode=true&characterEncoding=utf-8&useSSL=false";
        String username = "root";
        String password = "Abcd-1234";
        String strsql = "SELECT t.* FROM zhoil_flight.T_FLIGHT_CODE t LIMIT 10";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement sta = conn.createStatement();
            ResultSet rs = sta.executeQuery(strsql);
            while (rs.next()) {
                System.out.println(rs.getInt(1));
                System.out.println(rs.getInt(2));
            }
            rs.close();
            sta.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
