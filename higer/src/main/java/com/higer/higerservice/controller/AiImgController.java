package com.higer.higerservice.controller;

import com.alibaba.fastjson.JSON;
import com.higer.higerservice.entity.oilpro.*;
import com.higer.higerservice.service.AAiImgService;
import com.higer.higerservice.util.BaseUtil;
import com.higer.higerservice.util.FaceComparisonUtil;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.apache.commons.codec.binary.Base64;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/5 09:46
 * @Description:
 */
@Controller
public class AiImgController {

    @Autowired
    private AAiImgService aAiImgService;

    @GetMapping("/aiImg")
    public String version() {
        return "listzp";
    }

    @RequestMapping("/basefileimg")
    public String basefile(
            @RequestParam(value = "id") Integer id,
            Model model
    ) {
        AAiImg aAiImg = aAiImgService.findOne(id);
        if (aAiImg != null) {
            model.addAttribute("imgbase", aAiImg.getImgDataBase());
        }
        model.addAttribute("id", id);
        return "img";
    }

    @PostMapping("/aiImg/getAll")
    @ResponseBody
    public List<AAiImg> findAll() {
        return aAiImgService.findAll(1);
    }

    public void saveDate() {
        AAiImg img = aAiImgService.getByCode("ygh");
        if (img != null) {
            img.setImgData(BaseUtil.formatSec(new Date()));
            aAiImgService.save(img);
        }


    }

    @PostMapping(value = "/aiImg/save")
    public String save(@RequestParam("file1") MultipartFile file1, @RequestParam("userName") String userName, @RequestParam("userPassWord") String userPassWord, @RequestParam("id") Integer id, Model model) {
        try {
            User u = new User();
            u.setName(userName);
            u.setPass(userPassWord);
            String json = JSON.toJSONString(u);
            System.out.println(json);
            String s = "";
            if (!StringUtils.isEmpty(file1.getOriginalFilename())) {
                InputStream i = file1.getInputStream();
                ByteOutputStream b = new ByteOutputStream();

                FileUtil.copyStream(i, b);
                byte[] bytes = b.getBytes();
                Base64 b64 = new Base64();
                s = b64.encodeToString(bytes);
            }
            AAiImg aAiImg = new AAiImg();
            aAiImg.setLogoCode(json);
            aAiImg.setImgDataBase(s);
            aAiImg.setImgData(BaseUtil.formatSec(new Date()));
            String res = aAiImgService.save(aAiImg);

            model.addAttribute("mes", res);
            if (res.equals("success")) {
                saveDate();
            }
        } catch (IOException e) {

        }
        return "listzp";

    }

    @PostMapping(value = "/aiImg/update")
    public String update(@RequestParam("file1") MultipartFile file1, @RequestParam("userName") String userName, @RequestParam("userPassWord") String userPassWord, @RequestParam("id") Integer id, Model model) {

        try {

            User u = new User();
            u.setName(userName);
            u.setPass(userPassWord);
            String json = JSON.toJSONString(u);
            System.out.println(json);
            String s = "";
            if (!StringUtils.isEmpty(file1.getOriginalFilename())) {
                InputStream i = file1.getInputStream();
                ByteOutputStream b = new ByteOutputStream();

                FileUtil.copyStream(i, b);
                byte[] bytes = b.getBytes();
                Base64 b64 = new Base64();
                s = b64.encodeToString(bytes);
            }


            saveDate();
            String res = aAiImgService.update(id, s, json);
            if (res.equals("success")) {
                saveDate();
            }

            model.addAttribute("mes", res);

        } catch (IOException e) {

        }
        return "listzp";


    }

    @PostMapping(value = "/aiImg/delete")
    @ResponseBody
    public String delete(@RequestParam("id") Integer id) {
        return aAiImgService.delete(id);
    }

    @PostMapping("/faceCompare")
    @ResponseBody
    public String faceCompare(HttpServletRequest request) {
        System.out.println("进入-----------------------:" + BaseUtil.formatSec(new Date()));
        byte[] byRcvData = null;
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            System.out.println("获取完成inputStream-----------------------:" + BaseUtil.formatSec(new Date()));
            byRcvData = FaceComparisonUtil.readInputStream(inputStream);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (byRcvData != null) {
            try {
                System.out.println("调用比对-----------------------:" + BaseUtil.formatSec(new Date()));
                String code = aAiImgService.facecomparesome(byRcvData);
                System.out.println("获取的标识码为:" + code + "---" + BaseUtil.formatSec(new Date()));

                /*response.setContentType("application/json");
                // response.setCharacterEncoding("GBK");
                response.setHeader("Access-Control-Allow-Origin", "*");
                byte[]   bySndData = code.getBytes("utf-8");
                response.setContentLength(bySndData.length);
                OutputStream outs = response.getOutputStream();
                outs.write(bySndData);
                outs.close();
                System.out.println("发送完成...................:"+ BaseUtil.formatSec(new Date()));*/
                return code;
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("byRcvData为空");
        }


        return "{}";
    }

    public String getParm(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String line = null;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sb.toString();
    }

    @PostMapping("/checkNeedUpdate")
    @ResponseBody
    public String checkNeedUpdate(HttpServletRequest request) throws UnsupportedEncodingException {
        String data = getParm(request);
        System.out.println("data-----------------------:" + data + "---" + BaseUtil.formatSec(new Date()));
        AAiImg img = aAiImgService.getByCode("ygh");
        UpdateRequest ud = JSON.parseObject(data, UpdateRequest.class);
        System.out.println(ud.getDate());
        UpdateResponse r = new UpdateResponse();
        r.setDate(img.getImgData());
        if (!img.getImgData().equals(ud.getDate())) {
            r.setStatus(1);
            List<AAiImg> jsyLists = aAiImgService.findAll(1);
            List<String> strs = new ArrayList<String>();
            for (int i = 0; i < jsyLists.size(); i++) {
                if (jsyLists.get(i).getLogoCode().equals("ygh")) {
                    continue;
                }
                strs.add(jsyLists.get(i).getId() + "");
            }
            r.setListAllData(strs);
        } else {

            r.setStatus(0);
        }
        return JSON.toJSONString(r);

    }

    @PostMapping("/checkOneNeedUpdate")
    @ResponseBody
    public String checkOneNeedUpdate(HttpServletRequest request) throws UnsupportedEncodingException {
        String data = getParm(request);
        System.out.println("data-----------------------:" + data + "---" + BaseUtil.formatSec(new Date()));

        UpdateOneRequest uo = JSON.parseObject(data, UpdateOneRequest.class);
        UpdateOneResponse r = new UpdateOneResponse();
        if (uo != null) {
            AAiImg img = aAiImgService.findOne(Integer.parseInt(uo.getId()));

            if (img != null) {
                if (!img.getImgData().equals(uo.getDate())) {
                    r.setStatus(1);
                    r.setPicData(img.getImgDataBase());
                    Base64 base64 = new Base64();
                    r.setUserInfo(base64.encodeToString(img.getLogoCode().getBytes()));
                    r.setDate(img.getImgData());

                } else {
                    r.setStatus(0);
                }
            } else {

            }
        }

        return JSON.toJSONString(r);

    }

}
