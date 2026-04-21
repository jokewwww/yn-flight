package com.zh.controller;

import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyAirlinesCode;
import com.zh.constant.Constant;
import com.zh.dao.mapper.my.FlightMapper;
import com.zh.service.AirlinesCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 航空公司代码表
 */
@RestController
@RequestMapping(value = "/airlines")
public class AirlinesCodeController extends BaseController {

    @Autowired
    private AirlinesCodeService airlinesCodeService;
    @Autowired
    private FlightMapper flightMapper;

    /*private void setty() throws IOException, ParseException {
        //List<A> al =getA();
        int num=0;
        String text = getText("D:\\b.txt");
        String[] split = text.split("\n");
        List<MyFlightCode> l = new ArrayList<MyFlightCode>();
        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
            String string = split[i];
            String[] split2 = string.split("\t");
            MyFlightCode f = new MyFlightCode();





            f.setArcrRegn(split2[0]);//飞机号
            f.setArcrCustomNum(split2[1]);	//购货方编号
            f.setArcrStartDate(format(split2[2]));
            f.setArcrEndDate(format(split2[4]));
            for (int j = 0; j < al.size(); j++) {
                if(al.get(j).getArcrRegn().equals(split2[0])) {
                    f.setArcrAcname(al.get(j).getType());
                    num++;
                    break;
                }
            }
            flightMapper.addFLIGHTCODE(f);
            l.add(f);
        }
        System.out.println("共计："+l.size()+"条没有飞机类型的有:"+(l.size()-num)+"条");

    }

    private static List<A> getA() throws IOException {
        List<A> al= new ArrayList<A>();
        String text = getText("D:\\p.txt");
        String[] split = text.split("\n");
        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
            String string = split[i];
            String[] split2 = string.split("\t");
            if(split2.length==2) {
                A a= new A();
                a.setArcrRegn(split2[0]);
                a.setType(split2[1]);
                al.add(a);
            }
        }
        return al;
    }*/
    public static String getText(String path) throws IOException {
        String text = "";
        try {
            File file = new File(path);
            String lineTxt = "";

            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read;
                read = new InputStreamReader(new FileInputStream(file));
                BufferedReader bufferedReader = new BufferedReader(read);
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    // System.out.println(lineTxt);
                    text += lineTxt + "\n";

                }
                read.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return text;
    }

    public static void main(String[] args) throws ParseException {
        String a = "2019.08.08";
        System.out.println(format(a));
    }

    public static Date format(String date) throws ParseException, NullPointerException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date result = null;
        try {
            String s = date.replaceAll("\\.", "-");
            result = sdf.parse(s);
        } catch (ParseException e) {
            throw new ParseException("日期格式错误!", 0);
        } catch (NullPointerException e) {
            throw new NullPointerException("格式化日期为空!");
        }
        return result;
    }

    /**
     * 添加航空公司
     *
     * @param airlinesCode
     * @return
     */
    @PostMapping(value = "/insertAirlinesCode")
    public ReturnMsg<MyAirlinesCode> insertAirlinesCode(@RequestBody MyAirlinesCode airlinesCode) {
        if (airlinesCode.getAlcdIcaoCode().length() != 2) {
            return new ReturnMsg(Constant.CODE_ERR, "机场二字码只能是2位长度的字符!", null);
        }
        Integer insertCode = airlinesCodeService.insertAirlinesCode(airlinesCode);
        if (insertCode == 1) {
            return new ReturnMsg(Constant.CODE_ERR, "已存在!", null);
        } else {
            return new ReturnMsg(Constant.CODE_OK, null, null);
        }
    }

    /**
     * 修改航空公司
     */
    @PutMapping(value = "/updateAirlinesCode")
    public ReturnMsg<MyAirlinesCode> updateAirlinesCode(@RequestBody MyAirlinesCode airlinesCode) {
        airlinesCodeService.updateAirlinesCode(airlinesCode);
        return new ReturnMsg<MyAirlinesCode>(Constant.CODE_OK, null, null);
    }

    /**
     * 删除航空公司
     *
     * @param airlinesCode
     * @return
     */
    @DeleteMapping(value = "/deleteAirlinesCode")
    public ReturnMsg<MyAirlinesCode> deleteAirlinesCode(@RequestBody MyAirlinesCode airlinesCode) {
        airlinesCodeService.deleteAirlinesCode(airlinesCode);
        return new ReturnMsg<MyAirlinesCode>(Constant.CODE_OK, null, null);
    }

    /**
     * 查询航空公司
     *
     * @param airlinesCode
     * @return
     */
    @PostMapping(value = "/selectAirlinesCode")
    public ReturnMsg<List<MyAirlinesCode>> selectAirlinesCode() throws IOException, ParseException {
		/*
		于总
		String text = getText("D:\\c.txt");
		String[] split = text.split("\n");
		List<MyCustom> l = new ArrayList<MyCustom>();
		for (int i = 0; i < split.length; i++) {
			System.out.println(split[i]);
			String string = split[i];
			String[] split2 = string.split("\t");
			MyCustom c = new MyCustom();
			c.setCstmNum(split2[0]);//编号
			c.setCstmRegion(split2[1]);//国家
			if(split2.length==3 && split2[2]!=null &&!"".contentEquals(split2[2]))
				c.setCstmName(split2[2]);//购货方名称
			l.add(c);
			//插库
			flightMapper.addCUSTOM(c);

			for (int j = 0; j < split2.length; j++) {
				System.out.println(split2[j]);
			}
		}
		System.out.println("共计："+l.size()+"条");*/


        //setty();


        List<MyAirlinesCode> airlinescode = airlinesCodeService.selectAirlinesCode();
        return new ReturnMsg<List<MyAirlinesCode>>(Constant.CODE_OK, null, airlinescode);

    }

    /**
     * 航空公司详情
     */
    @PostMapping(value = "/selectAirlinesCodeFind")
    public ReturnMsg<MyAirlinesCode> selectAirlinesCodeFind(@RequestBody MyAirlinesCode airlinesCode) {
        MyAirlinesCode airlinescode = airlinesCodeService.selectAirlinesCodeFind(airlinesCode);
        return new ReturnMsg<MyAirlinesCode>(Constant.CODE_OK, null, airlinescode);
    }
}
