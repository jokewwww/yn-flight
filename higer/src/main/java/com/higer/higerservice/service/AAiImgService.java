package com.higer.higerservice.service;

import com.higer.higerservice.entity.oilpro.AAiImg;
import com.higer.higerservice.repository.oilpro.AAiImgRepository;
import com.higer.higerservice.util.BaseUtil;
import com.higer.higerservice.util.FaceComparisonUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/5 09:47
 * @Description:
 */
@Service
public class AAiImgService {
    @Autowired
    private AAiImgRepository aAiImgRepository;
    @Value("${faceUrl}")
    private String url;

    public AAiImg findOne(Integer id) {
        return aAiImgRepository.findOne(id);
    }

    public List<AAiImg> findAll(int page) {
        List<AAiImg> jsyLists = aAiImgRepository.findAll();
        for (int i = 0; i < jsyLists.size(); i++) {

            if (jsyLists.get(i).getImgDataBase() != null && !"".equals(jsyLists.get(i).getImgDataBase())) {
                jsyLists.get(i).setImgData("有照片");
            } else {
                jsyLists.get(i).setImgData("无照片");
            }
        }
        for (int i = 0; i < jsyLists.size(); i++) {
            if (jsyLists.get(i).getLogoCode().equals("ygh")) {
                jsyLists.remove(jsyLists.get(i));
                break;
            }

        }
        return jsyLists;

    }

    public String save(AAiImg aAiImg) {
        try {
            aAiImgRepository.save(aAiImg);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    public String update(Integer id, String imgdata, String logoCode) {
        try {

            AAiImg one = aAiImgRepository.findOne(id);
            if (one != null) {

                one.setLogoCode(logoCode);
                one.setImgData(BaseUtil.formatSec(new Date()));
                if (!StringUtils.isEmpty(imgdata)) {
                    one.setImgDataBase(imgdata);
                }

            }
            aAiImgRepository.save(one);
            return "保存成功";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    public String delete(Integer id) {
        try {

            AAiImg one = aAiImgRepository.findOne(id);
            if (one != null) {
                aAiImgRepository.delete(one);
            }

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    public String facecomparesome(byte[] byRcvData) throws IOException {
        FaceComparisonUtil.setUrlPath(url);

        String resData = "{}";
        Base64 base64 = new Base64();
        String yszp = base64.encodeToString(byRcvData);
        List<String> list = new ArrayList<>();

        list.add(yszp);
        System.out.println("转换原始特征码-----------------------:" + BaseUtil.formatSec(new Date()));
        List<String> facetrans = FaceComparisonUtil.facetrans(list);
        if (facetrans != null && facetrans.size() > 0) {
            System.out.println("获取数据库所有驾驶员-----------------------:" + BaseUtil.formatSec(new Date()));
            List<AAiImg> jsyLists = aAiImgRepository.findAll();
            if (jsyLists != null && jsyLists.size() > 0) {

                List<String> tzms = new ArrayList<>();
                for (int i = 0; i < jsyLists.size(); i++) {
                    if (jsyLists.get(i).getImgData() != null) {
                        System.out.println("标识码为:" + jsyLists.get(i).getLogoCode());
                        tzms.add(jsyLists.get(i).getImgData());
                    }
                }
                System.out.println("调用人脸比对-----------------------:" + BaseUtil.formatSec(new Date()));
                List<String> listSimple = FaceComparisonUtil.facecomparesome(facetrans.get(0), tzms);
                System.out.println("调用人脸比对结束-----------------------:" + BaseUtil.formatSec(new Date()));
                String s = "";
                double xsdSet = 0.6;
                for (int i = 0; i < listSimple.size(); i++) {
                    s += "第" + i + "张相似度为:" + listSimple.get(i) + " ";
                    double xsd = Double.parseDouble(listSimple.get(i));
                    if (xsd >= xsdSet) {
                        if (i < jsyLists.size()) {
                            resData = jsyLists.get(i).getLogoCode();
                        }
                        xsdSet = xsd;
                    }

                }
                System.out.println(s);
                return resData;
            }


        } else {
            System.out.println("特征码转换失败");
        }
        return "{}";
    }

    public AAiImg getByCode(String code) {
        List<AAiImg> one = aAiImgRepository.findByLogoCode(code);
        if (one != null && one.size() > 0) {
            return one.get(0);
        }
        return null;
    }
}
