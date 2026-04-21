package com.higer.higerservice.controller;

import com.higer.higerservice.entity.oilpro.AApkVersion;
import com.higer.higerservice.entity.oilpro.Apk;
import com.higer.higerservice.service.FileService;
import com.higer.higerservice.util.DateUtil;
import com.higer.higerservice.util.PcResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/29 10:41
 * @Description:
 */
@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @RequestMapping("/file")
    public String version() {
        System.out.println("in file.............");
        return "version";
    }

    @RequestMapping("/basefile")
    public String basefile(
            @RequestParam(value = "id") int id,
            Model model
    ) {
        model.addAttribute("id", id);
        return "basefile";
    }

    @PostMapping(value = "/uploadfiles")
    public String Upload(@RequestParam("file1") MultipartFile file1, @RequestParam("upgradeid") Integer infoid, MultipartHttpServletRequest request, Model model)
            throws Exception {
        String restr = "";
        MultipartFile file = file1;
        try {
            fileService.uploadFile(file, infoid);
            restr = "上传成功";
        } catch (Exception e) {
            e.printStackTrace();
            restr = "上传失败";
        }
        model.addAttribute("id", infoid);
        model.addAttribute("mes", restr);
        return "basefile";
    }

    @GetMapping("/version/getAll")
    @ResponseBody
    public PcResponseObject<AApkVersion> findAll(@RequestParam(value = "pages", required = false) int page) {


        return fileService.findAll(page);
    }

    @GetMapping("/version/getPrintNum")
    @ResponseBody
    public String getPrintNum() {


        return fileService.getPrintNum(6).getRemark();
    }

    @GetMapping("/version/getFlowThreshold")
    @ResponseBody
    public String getFlowThreshold() {


        return fileService.getPrintNum(7).getRemark();
    }


    @PostMapping(value = "/version/updFlowThreshold")
    @ResponseBody
    public String updFlowThreshold(@RequestParam("flowThreshold") String flowThreshold) {
        return fileService.updFlowThreshold(flowThreshold);
    }

    @PostMapping(value = "/version/updPrintNum")
    @ResponseBody
    public String updPrintNum(@RequestParam("num") String num) {
        return fileService.updPrintNum(num);
    }

    @PostMapping(value = "/version/save")
    @ResponseBody
    public String save(AApkVersion aApkVersion) {
        return fileService.save(aApkVersion);
    }

    @PostMapping(value = "/getVersion")
    @ResponseBody
    public byte[] getVersion(@RequestParam(value = "version") String version,
                             @RequestParam(value = "pos") int pos,
                             @RequestParam(value = "crc") String crc,
                             @RequestParam(value = "type", required = false, defaultValue = "1") Integer type,
                             HttpServletResponse r) {

        System.out.println("我进来了------------------");
        System.out.println("我进来了----------version--------" + version);
        System.out.println("我进来了---------pos---------" + pos);
        System.out.println("我进来了-------crc-----------" + crc);

        if (!StringUtils.isEmpty(version)) {
            Apk apk = fileService.getBytes(version, type);
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String padTime = df.format(date);
            if (apk.getApk() != null && !StringUtils.isEmpty(apk.getVer())) {
                //如果回传回来的有版本号
                System.out.println("crc-------------------------" + crc);
                if (!StringUtils.isEmpty(crc)) {
                    System.out.println("(!StringUtils.isEmpty(crc)){");
                    //版本号和查询的是一个则断点续传
                    if (crc.equals(apk.getVer()) && pos > 0 && apk.getApk().length > pos) {
                        System.out.println(" if(crc.equals(apk.getVer()) && pos >0 && apk.getApk().length>pos ){");
                        byte[] apkByte = new byte[apk.getApk().length - pos];
                        System.arraycopy(apk.getApk(), pos, apkByte, 0, apk.getApk().length - pos);
                        r.setHeader("len", apk.getApk().length + "");
                        r.setHeader("crc", apk.getVer());
                        r.setHeader("curtime", DateUtil.getCurrentDateTimeStr());
                        r.setHeader("pos", String.valueOf(pos));
                        r.setHeader("padTime", padTime);
                        System.out.println("----pos" + pos);

                        System.out.println("----len" + r.getHeader("len"));
                        System.out.println("----crc" + r.getHeader("crc"));
                        System.out.println("apkByte---" + apkByte.length);
                        return apkByte;

                    } else {
                        System.out.println(" if(crc.equals(apk.getVer()) && pos >0 && apk.getApk().length>pos ){     else");
                        r.setHeader("len", apk.getApk().length + "");
                        r.setHeader("crc", apk.getVer());
                        r.setHeader("curtime", DateUtil.getCurrentDateTimeStr());
                        r.setHeader("pos", String.valueOf(pos));
                        r.setHeader("padTime", padTime);
                        System.out.println("----pos" + pos);
                        System.out.println("----len" + r.getHeader("len"));
                        System.out.println("----crc" + r.getHeader("crc"));
                        System.out.println("apk.getApk()---" + apk.getApk().length);
                        return apk.getApk();
                    }


                    // pos表示偏移 int
                    // crc表示文件校验码 使用文件版本 string
                    //  返回的是：
                    //  len表示apk byte数组长度
                    //  pos表示提交的时候的偏移，服务器可能需要纠正
                    //  crc表示文件校验码 使用文件版本

                } else {
                    System.out.println("else");
                    r.setHeader("len", apk.getApk().length + "");
                    r.setHeader("crc", apk.getVer());
                    r.setHeader("curtime", DateUtil.getCurrentDateTimeStr());
                    r.setHeader("pos", String.valueOf(pos));
                    r.setHeader("padTime", padTime);
                    System.out.println("----pos" + pos);
                    System.out.println("----" + r.getHeader("len"));
                    System.out.println("----" + r.getHeader("crc"));
                    System.out.println("apk.getApk()---" + apk.getApk().length);
                    return apk.getApk();
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

    }
}
