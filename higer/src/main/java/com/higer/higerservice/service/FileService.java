package com.higer.higerservice.service;

import com.higer.higerservice.entity.oilpro.AApkVersion;
import com.higer.higerservice.entity.oilpro.Apk;
import com.higer.higerservice.repository.oilpro.AApkVersionRepository;
import com.higer.higerservice.util.PcResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * @Auther: 修宏鑫
 * @Date: 2018/11/29 10:42
 * @Description:
 */
@Service
public class FileService {


    @Value("${apkPath}")
    private String apkPath;

    @Autowired
    private AApkVersionRepository aApkVersionRepository;

    //  ver 版本号 版本号一样  返回 null  不一样 返回数组(先放一条)  备注
    public Apk getBytes(String ver, Integer type) {
        Apk apk = new Apk();
        byte[] buffer = null;
        AApkVersion byVer = aApkVersionRepository.findByVer_Type(ver, type);
        if (byVer != null) {
            return apk;
        } else {
            try {
                //根据递增ID取最新的数据
                AApkVersion maxIdData = aApkVersionRepository.getMaxId(type);
                //2、数据库中没有文件版本，返回null
                if (maxIdData == null) {
                    return apk;
                }
                //3、数据库中有不同文件版本，返回该文件的byte[]

                //判断如果最新版本没有上传 APK 则返回null
                if (StringUtils.isEmpty(maxIdData.getApkUrl())) {
                    return apk;
                }
                apk.setVer(maxIdData.getVer());
                File file = new File(maxIdData.getApkUrl());
                InputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
                byte[] b = new byte[1000];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
                fis.close();
                bos.close();
                buffer = bos.toByteArray();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        apk.setApk(buffer);

        return apk;
    }

    public void uploadFile(MultipartFile file, Integer infoid) {
        String fileName = file.getOriginalFilename();
        String sname = fileName.substring(fileName.lastIndexOf("."));//后缀
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String newFileName = uuid + sname;
        String filePath = apkPath + newFileName;
        File desFile = new File(filePath);
        if (!desFile.getParentFile().exists()) {
            desFile.getParentFile().mkdirs();
        }
        try {
            file.transferTo(desFile);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        AApkVersion one = aApkVersionRepository.findOne(infoid);
        if (one != null) {
            if (!StringUtils.isEmpty(one.getApkUrl())) {
                File oldFile = new File(one.getApkUrl());
                oldFile.delete();
            }
            one.setApkUrl(filePath);
            aApkVersionRepository.saveAndFlush(one);
        }
    }

    public PcResponseObject<AApkVersion> findAll(int page) {
        AApkVersion maxIdData = aApkVersionRepository.getMaxId(1);
        Page<AApkVersion> all = aApkVersionRepository.findByTypeNot(6, new PageRequest(page - 1, 10));
//        int r=-1;
//        for (int i=0 ;i<  all.getContent().size();i++){
//            if( all.getContent().get(i).getType()==6){
//               r=i;
//               break;
//            }
//        }
//        if(r>0){
//            all.getContent().remove(r);
//        }

        return new PcResponseObject<AApkVersion>(all);
    }

    public String save(AApkVersion aApkVersion) {
        try {
            //根据type判断先吧数据库的都删掉，然后在重新添加此条数据
            aApkVersionRepository.deleteByType(aApkVersion.getType());
            aApkVersionRepository.save(aApkVersion);

            //aApkVersion.setType(1);
            /*AApkVersion byVer = aApkVersionRepository.findByVer(aApkVersion.getVer());

            if(byVer != null){
                return "false";
            }
            if (aApkVersion.getId() == null) {
                aApkVersionRepository.save(aApkVersion);
            } else {
                AApkVersion one = aApkVersionRepository.findOne(aApkVersion.getId());
                one.setVer(aApkVersion.getVer());
                one.setRemark(aApkVersion.getRemark());
                aApkVersionRepository.save(one);
            }*/
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    public AApkVersion getPrintNum(int type) {
        List<AApkVersion> alsit = aApkVersionRepository.findByType(type);
        if (alsit != null && alsit.size() > 0) {
            return alsit.get(0);
        } else {
            AApkVersion a = new AApkVersion();
            a.setType(type);
            if (type == 6) {
                a.setRemark("140");
            } else if (type == 7) {
                a.setRemark("0");
            }
            aApkVersionRepository.save(a);
            return a;
        }
    }

    public String updPrintNum(String num) {
        try {

            List<AApkVersion> alsit = aApkVersionRepository.findByType(6);
            if (alsit != null && alsit.size() > 0) {
                alsit.get(0).setRemark(num);
                aApkVersionRepository.save(alsit.get(0));
                return "success";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    public String updFlowThreshold(String flowThreshold) {
        try {

            List<AApkVersion> alsit = aApkVersionRepository.findByType(7);
            if (alsit != null && alsit.size() > 0) {
                alsit.get(0).setRemark(flowThreshold);
                aApkVersionRepository.save(alsit.get(0));
                return "success";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
}
