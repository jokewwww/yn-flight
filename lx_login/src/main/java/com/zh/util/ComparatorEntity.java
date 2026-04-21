package com.zh.util;

import com.google.common.collect.Lists;
import com.zh.bean.login.MyStaffVehiTask;
import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/9/4 21:29
 * @Description:
 */
public class ComparatorEntity {

    public static void main(String[] args) {
        ArrayList<MyStaffVehiTask> objects = Lists.newArrayList();
        MyStaffVehiTask a = new MyStaffVehiTask();
        MyStaffVehiTask b = new MyStaffVehiTask();
        MyStaffVehiTask c = new MyStaffVehiTask();
        MyStaffVehiTask d = new MyStaffVehiTask();
        a.setStaffName("啊");
        b.setStaffName("吧");
        c.setStaffName("吃");
        d.setStaffName("的");
        objects.add(d);
        objects.add(c);
        objects.add(b);
        objects.add(a);
        objects.forEach(myStaffVehiTask -> {
            System.out.println(myStaffVehiTask.getStaffName());
        });
        System.out.println("-----------");
        listSort(objects);
        objects.forEach(myStaffVehiTask -> {
            System.out.println(myStaffVehiTask.getStaffName());
        });
    }

    private static String ToPinYinString(String str) {

        StringBuilder sb = new StringBuilder();
        String[] arr = null;

        for (int i = 0; i < str.length(); i++) {
            arr = PinyinHelper.toHanyuPinyinStringArray(str.charAt(i));
            if (arr != null && arr.length > 0) {
                for (String string : arr) {
                    sb.append(string);
                }
            }
        }
        return sb.toString();
    }

    public static void listSort(List<MyStaffVehiTask> list) {
        Collections.sort(list, new Comparator<MyStaffVehiTask>() {
            @Override
            public int compare(MyStaffVehiTask o1, MyStaffVehiTask o2) {
                try {
                    String staffName1 = o1.getStaffName();
                    String staffName2 = o2.getStaffName();
                    return ToPinYinString(staffName1).compareTo(ToPinYinString(staffName2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }


}
