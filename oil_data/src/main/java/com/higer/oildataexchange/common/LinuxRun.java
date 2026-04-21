package com.higer.oildataexchange.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
@Slf4j
public class LinuxRun implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("----- 执行 ApplicationRunner 的方法----");

        try {
            String[] cmd = new String[]{"/bin/sh", "-c", "echo  192.168.1.69 sinopo-web.incnaf.com  >> /etc/hosts\n" +
                    "echo 192.168.1.69 zuul-sinopo.incnaf.com  >> /etc/hosts\n" +
                    "echo 192.168.1.69 zuul-sinopo-test.incnaf.com >> /etc/hosts"};
            Process ps = Runtime.getRuntime().exec(cmd);

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();

            log.info("linux命令执行成功。 cmd:{}, result:{} ", "路由执行--------------", result);
        } catch (Exception e) {
            log.error("linux命令执行失败。cmd：{}", "路由执行--------------", e);
        }
    }
}

