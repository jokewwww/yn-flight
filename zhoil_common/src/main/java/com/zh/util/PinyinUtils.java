package com.zh.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Comparator;

public class PinyinUtils {

    public static char getPinyinFirstWord(String chineseWord){
        try {
            if(StringUtils.isNotBlank(chineseWord)){
                char[] chars = chineseWord.toCharArray();
                String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(chars[0]);
                if(pinyin==null){
                    return chars[0];
                }
                return pinyin[0].charAt(0);
            }
        } catch (Exception e) {
            System.out.println("出错字符："+chineseWord);
            e.printStackTrace();
        }
        return '\0';
    }

    public static String getPinyinToUpperCase(String chineseWord){
        try {
            if(StringUtils.isNotBlank(chineseWord)){
                char[] chars = chineseWord.toCharArray();
                String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(chars[0]);
                if(pinyin==null){
                    return String.valueOf(chars[0]).toUpperCase();
                }
                return String.valueOf(pinyin[0].charAt(0)).toUpperCase();
            }
        } catch (Exception e) {
            System.out.println("出错字符："+chineseWord);
            e.printStackTrace();
        }
        return "0";
    }
}
