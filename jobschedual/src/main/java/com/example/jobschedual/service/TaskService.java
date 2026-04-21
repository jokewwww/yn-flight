package com.example.jobschedual.service;

import com.example.jobschedual.entity.TStaff;
import com.example.jobschedual.factory.QueueInterface;
import com.example.jobschedual.redis.RedisService;
import com.example.jobschedual.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/14 11:10
 * @Description:
 */
@Component
@EnableScheduling
public class TaskService {

    private static Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private RedisService redisService;


    //  Bn 定时任务处理下   8.30 上线 B组下线 5点
    @Scheduled(cron = "0 0 19 * * ?")
    private void change() {
        // TODO 清空B14  B15 的值
        redisService.setStr("B14","-");
        redisService.setStr("B15","-");

        String queueintit = redisService.getStr("queueintit");
        Integer bx = 0;
        String bx1 = redisService.getStr("Bx1");
        String bx2 = redisService.getStr("Bx2");
        String bx3 = redisService.getStr("Bx3");
        String bx4 = redisService.getStr("Bx4");
        //String queueintit ="2019-03-31";
        System.out.println("Bn---queueintit == " + queueintit);
        if (!StringUtils.isEmpty(queueintit)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date parse = format.parse(queueintit);
                Date parse1 = format.parse(DateUtil.getCurrentDateStr());
                Integer num = DateUtil.differentDays(parse, parse1);
                num = num + 1;
                Integer team = 0;     // 第几队
                if (num < 5) {
                    team = num;
                } else {
                    team = num % 4;
                }
                // 取下一天的数据
                if (team == 4) {
                    team = 1;
                } else {
                    team += 1;
                }
                switch (team) {
                    case 1:
                        System.out.println("B 铁汉队 下班 ");
                        System.out.println("B 铁拳队 上班");
                        break;
                    case 2:
                        System.out.println("B 铁拳队 下班 ");
                        System.out.println("B 铁人队 上班");
                        break;
                    case 3:
                        System.out.println("B 铁人队 下班 ");
                        System.out.println("B 铁锤队 上班");
                        break;
                    case 4:
                        System.out.println("B 铁锤队 下班 ");
                        System.out.println("B 铁汉队 上班");
                        break;
                    default:
                        break;
                }
                System.err.println("Bz   定时任务  下个应该上班的 队伍(个人算法) 是----------" + team);
                LinkedBlockingQueue mapQueue = null;
                //  1=3  2= 2  3= 1  4= 4
                if (team == 1) {
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("BtStaffs1");
                    //更新队列位置
                    if (!StringUtils.isEmpty(bx1)) {
                        Integer bxInt = Integer.valueOf(bx1);
                        bx = bxInt;
                        if (mapQueue.toArray().length == (bxInt + 1)) {
                            redisService.setStr("Bx1", "0");
                        } else {
                            redisService.setStr("Bx1", String.valueOf(bxInt + 1));
                        }
                    }
                } else if (team == 2) {
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("BtStaffs2");
                    //更新队列位置
                    if (!StringUtils.isEmpty(bx2)) {
                        Integer bxInt = Integer.valueOf(bx2);
                        bx = bxInt;
                        if (mapQueue.toArray().length == (bxInt + 1)) {
                            redisService.setStr("Bx2", "0");
                        } else {
                            redisService.setStr("Bx2", String.valueOf(bxInt + 1));
                        }
                    }
                } else if (team == 3) {
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("BtStaffs3");
                    //更新队列位置
                    if (!StringUtils.isEmpty(bx3)) {
                        Integer bxInt = Integer.valueOf(bx3);
                        bx = bxInt;
                        if (mapQueue.toArray().length == (bxInt + 1)) {
                            redisService.setStr("Bx3", "0");
                        } else {
                            redisService.setStr("Bx3", String.valueOf(bxInt + 1));
                        }
                    }
                } else if (team == 4) {
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("BtStaffs4");
                    //更新队列位置
                    if (!StringUtils.isEmpty(bx4)) {
                        Integer bxInt = Integer.valueOf(bx4);
                        bx = bxInt;
                        if (mapQueue.toArray().length == (bxInt + 1)) {
                            redisService.setStr("Bx4", "0");
                        } else {
                            redisService.setStr("Bx4", String.valueOf(bxInt + 1));
                        }
                    }
                }
                //Bn更新之前 , 队列中所有值

                if (null != mapQueue) {
                    for (int i = 0; i < mapQueue.toArray().length - bx; i++) {
                        Object take = mapQueue.take();
                        mapQueue.offer(take);
                    }
                }
                Object[] objects = mapQueue.toArray();
                //获取redis   中 之前 的 数据
                for (int i = 0; i < objects.length; i++) {
                    String str = redisService.getStr(("B" + String.valueOf(i + 1)));
                    logger.info(DateUtil.getCurrentDateTimeStr() + "旧---B" + (i + 1) + "---" + str);
                }
                //更新 B5  B6 的人
                for (int i = 0; i < objects.length; i++) {
                    //                              1               0
                    if(i == 4 || i == 5 || i== 8){
                        redisService.setStr(("C" + String.valueOf(i + 1)), objects[i].toString());
                        logger.info(DateUtil.getCurrentDateTimeStr() + "新---C" + (i + 1) + "---" + objects[i]);
                    }else{
                        redisService.setStr(("B" + String.valueOf(i + 1)), objects[i].toString());
                        logger.info(DateUtil.getCurrentDateTimeStr() + "新---B" + (i + 1) + "---" + objects[i]);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

   /* //  c5c6 定时任务处理下
    @Scheduled(cron = "0 0 14 * * ?" )
    private void changeC5C6() {
        String queueintit = redisService.getStr("queueintit");
        if(!StringUtils.isEmpty(queueintit)){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date parse = format.parse(queueintit);
                Date parse1 = format.parse(DateUtil.getCurrentDateStr());
                Integer num = DateUtil.differentDays(parse, parse1);
                Integer team = 0;     // 第几队
                if(num < 5){
                    team = num;
                }else{
                    team = num % 4 ;
                }
                // 取下一天的数据
                if(team == 4){
                    team = 1;
                }else{
                    team += 1;
                }

                LinkedBlockingQueue mapQueue = null;
                //  1=3  2= 2  3= 1  4= 4
                if(team == 1){
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("BtStaffs1");
                }else if(team == 2){
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("BtStaffs2");
                }else if(team == 3){
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("BtStaffs3");
                }else if(team == 4){
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("BtStaffs4");
                }
                if(null != mapQueue){
                    Object[] objects = mapQueue.toArray();
                    logger.info(DateUtil.getCurrentDateTimeStr() + "原C5  ---- C6 的值 " + objects[4].toString() +"------"+ objects[5].toString());
                    Object take = mapQueue.take();
                    mapQueue.offer(take);
                }
                Object[] objects = mapQueue.toArray();
                //更新 B5  B6 的人
                logger.info(DateUtil.getCurrentDateTimeStr() + "新C5  ---- C6 的值 " + objects[4].toString() +"------"+ objects[5].toString());
                redisService.setStr("B5",objects[4].toString());
                redisService.setStr("B6",objects[5].toString());
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }*/

    //  Dn 定时任务处理下
   @Scheduled(cron = "0 30 15 * * ?")
    private void changeDn() {
       // TODO 清空D14  D15 的值
       redisService.setStr("D14","-");
       redisService.setStr("D15","-");


       String queueintit = redisService.getStr("queueintit");
        Integer bx = 0;
        String bx1 = redisService.getStr("Dx1");
        String bx2 = redisService.getStr("Dx2");
        String bx3 = redisService.getStr("Dx3");
        String bx4 = redisService.getStr("Dx4");
        System.out.println("Dn ----- queueintit ==" + queueintit);
        if (!StringUtils.isEmpty(queueintit)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date parse = format.parse(queueintit);
                Date parse1 = format.parse(DateUtil.getCurrentDateStr());
                Integer num = DateUtil.differentDays(parse, parse1);
                Integer team = 0;     // 第几队
                //num = num + 1;
                if (num < 5) {
                    team = num;
                } else {
                    team = num % 4;
                }
                // 取下一天的数据
                if (team == 4) {
                    team = 1;
                } else {
                    team += 1;
                }
                switch (team) {
                    case 1:
                        System.out.println("D 铁锤队 下班 ");
                        System.out.println("D 铁汉队 上班");
                        break;
                    case 2:
                        System.out.println("D 铁汉队 下班 ");
                        System.out.println("D 铁拳队 上班");
                        break;
                    case 3:
                        System.out.println("D 铁拳队 下班 ");
                        System.out.println("D 铁人队 上班");
                        break;
                    case 4:
                        System.out.println("D 铁人队 下班 ");
                        System.out.println("D 铁锤队 上班");
                        break;
                    default:
                        break;
                }

                System.err.println("Dz   定时任务  下个应该上班的 队伍(个人算法) 是-----" + team);
                LinkedBlockingQueue mapQueue = null;
                //  1=3  2= 2  3= 1  4= 4
                if (team == 1) {
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("DtStaffs1");
                    //更新队列位置
                    if (!StringUtils.isEmpty(bx1)) {
                        Integer bxInt = Integer.valueOf(bx1);
                        bx = bxInt;
                        if (mapQueue.toArray().length == (bxInt + 1)) {
                            redisService.setStr("Dx1", "0");
                        } else {
                            redisService.setStr("Dx1", String.valueOf(bxInt + 1));
                        }
                    }
                } else if (team == 2) {
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("DtStaffs2");
                    //更新队列位置
                    if (!StringUtils.isEmpty(bx2)) {
                        Integer bxInt = Integer.valueOf(bx2);
                        bx = bxInt;
                        if (mapQueue.toArray().length == (bxInt + 1)) {
                            redisService.setStr("Dx2", "0");
                        } else {
                            redisService.setStr("Dx2", String.valueOf(bxInt + 1));
                        }
                    }
                } else if (team == 3) {
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("DtStaffs3");
                    //更新队列位置
                    if (!StringUtils.isEmpty(bx3)) {
                        Integer bxInt = Integer.valueOf(bx3);
                        bx = bxInt;
                        if (mapQueue.toArray().length == (bxInt + 1)) {
                            redisService.setStr("Dx3", "0");
                        } else {
                            redisService.setStr("Dx3", String.valueOf(bxInt + 1));
                        }
                    }
                } else if (team == 4) {
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("DtStaffs4");
                    //更新队列位置
                    if (!StringUtils.isEmpty(bx4)) {
                        Integer bxInt = Integer.valueOf(bx4);
                        bx = bxInt;
                        if (mapQueue.toArray().length == (bxInt + 1)) {
                            redisService.setStr("Dx4", "0");
                        } else {
                            redisService.setStr("Dx4", String.valueOf(bxInt + 1));
                        }
                    }
                }
                if (null != mapQueue) {
                    for (int i = 0; i < mapQueue.toArray().length - bx; i++) {
                        Object take = mapQueue.take();
                        mapQueue.offer(take);
                    }
                }
                Object[] objects = mapQueue.toArray();
                for (int i = 1; i < objects.length; i++) {
                    //更新除 所有数据
                    logger.info(DateUtil.getCurrentDateTimeStr() + "新---D" + (i + 1) + "---" + objects[i]);
                    redisService.setStr(("D" + String.valueOf(i + 1)), objects[i].toString());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //  CE 定时任务处理下
    @Scheduled(cron = "0 30 19 * * ?")
    private void changeCe() {
        String queueintit = redisService.getStr("queueintit");
        if (!StringUtils.isEmpty(queueintit)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date parse = format.parse(queueintit);
                Date parse1 = format.parse(DateUtil.getCurrentDateStr());
                Integer num = DateUtil.differentDays(parse, parse1);
                Integer team = 0;     // 第几队
                if (num < 5) {
                    team = num;
                } else {
                    team = num % 4;
                }
                // 取下一天的数据
                if (team == 4) {
                    team = 1;
                } else {
                    team += 1;
                }
                System.err.println("------------------------------" + team);
                LinkedBlockingQueue mapQueue = null;
                //  1=3  2= 2  3= 1  4= 4
                if (team == 1) {
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("DtStaffs1");
                } else if (team == 2) {
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("DtStaffs2");
                } else if (team == 3) {
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("DtStaffs3");
                } else if (team == 4) {
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("DtStaffs4");
                }
                if (null != mapQueue) {
                    Object[] objects = mapQueue.toArray();
                    if (objects.length > 0) {
                        logger.info(DateUtil.getCurrentDateTimeStr() + "原---D1(CE)---" + objects[objects.length - 1]);
                        logger.info(DateUtil.getCurrentDateTimeStr() + "新---D1(CE)---" + objects[0]);
                        redisService.setStr("D1", objects[0].toString());
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * C7C8的定时任务
     */
    @Scheduled(cron = "0 30 9 * * ?")
    public void changeC7() {
        Queue<TStaff> queue = (Queue<TStaff>) QueueInterface.getMapQueue("C7");
        String C7 = redisService.getStr("C7");
        logger.info(DateUtil.getCurrentDateTimeStr() + "原 C7 的值 : " + C7);
        String newC7 = WorkStaffService.recycleQueue(queue).getStaffId();
        logger.info(DateUtil.getCurrentDateTimeStr() + "新 C7 的值 : " + newC7);
        redisService.setStr("C7", newC7);
    }

    @Scheduled(cron = "0 0 14 * * ?")
    public void changeC8() {
        Queue<TStaff> queue = (Queue<TStaff>) QueueInterface.getMapQueue("C8");
        String c8 = redisService.getStr("C8");
        logger.info(DateUtil.getCurrentDateTimeStr() + "原 C8 的值 : " + c8);
        String newC8 = WorkStaffService.recycleQueue(queue).getStaffId();
        logger.info(DateUtil.getCurrentDateTimeStr() + "新 C8 的值 : " + newC8);
        redisService.setStr("C8", newC8);
    }

    /**
     * C2的定时任务
     */
    @Scheduled(cron = "0 30 9 * * ?")
    public void changeC2() {
        Queue<TStaff> queue = (Queue<TStaff>) QueueInterface.getMapQueue("C2");
        String C2 = redisService.getStr("C2");
        logger.info(DateUtil.getCurrentDateTimeStr() + "原 C2 的值 : " + C2);
        String newC2 = WorkStaffService.recycleQueue(queue).getStaffId();
        logger.info(DateUtil.getCurrentDateTimeStr() + "新 C2 的值 : " + newC2);
        redisService.setStr("C2", newC2);
    }

    /**
     * C20的定时任务
     */
    @Scheduled(cron = "0 30 9 * * ?")
    public void changeC20() {
        Queue<TStaff> queue = (Queue<TStaff>) QueueInterface.getMapQueue("C20");
        String C20 = redisService.getStr("C20");
        logger.info(DateUtil.getCurrentDateTimeStr() + "原 C20 的值 : " + C20);
        String newC20 = WorkStaffService.recycleQueue(queue).getStaffId();
        logger.info(DateUtil.getCurrentDateTimeStr() + "新 C20 的值 : " + newC20);
        redisService.setStr("C20", newC20);
    }

    /**
     * C1的定时任务
     */
    @Scheduled(cron = "0 30 9 * * ?")
    public void changeC1() {
        Queue<TStaff> queue = (Queue<TStaff>) QueueInterface.getMapQueue("C1");
        String C1 = redisService.getStr("C1");
        logger.info(DateUtil.getCurrentDateTimeStr() + "原 C1 的值 : " + C1);
        String newC1 = WorkStaffService.recycleQueue(queue).getStaffId();
        logger.info(DateUtil.getCurrentDateTimeStr() + "新 C1 的值 : " + newC1);
        redisService.setStr("C1", newC1);
    }

    /**
     * BZ的定时任务
     */
    @Scheduled(cron = "0 0 19 * * ?")
    public void changeBZ() {
        Queue<TStaff> queue = (Queue<TStaff>) QueueInterface.getMapQueue("BZ");
        String BZ = redisService.getStr("BZ");
        logger.info(DateUtil.getCurrentDateTimeStr() + "原 BZ 的值 : " + BZ);
        String newBZ = WorkStaffService.recycleQueue(queue).getStaffId();
        logger.info(DateUtil.getCurrentDateTimeStr() + "新 BZ 的值 : " + newBZ);
        redisService.setStr("BZ", newBZ);
    }

    /**
     * BZ1的定时任务
     */
    @Scheduled(cron = "0 0 19 * * ?")//延时1秒启动，保证在队长后面获取
    public void changeBZ1() {
        Queue<TStaff> queue = (Queue<TStaff>) QueueInterface.getMapQueue("BZ1");
        String BZ1 = redisService.getStr("BZ1");
        logger.info(DateUtil.getCurrentDateTimeStr() + "原 BZ1 的值 : " + BZ1);
        String newBZ1 = WorkStaffService.recycleQueue(queue).getStaffId();
        logger.info(DateUtil.getCurrentDateTimeStr() + "新 BZ1 的值 : " + newBZ1);
        redisService.setStr("BZ1", newBZ1);
    }

    /**
     * DT的定时任务
     */
    @Scheduled(cron = "0 0 13 * * ?")
    public void changeDT() {
        Queue<TStaff> queue = (Queue<TStaff>) QueueInterface.getMapQueue("DT");
        String DT = redisService.getStr("DT");
        logger.info(DateUtil.getCurrentDateTimeStr() + "原 DT 的值 : " + DT);
        String newDT = WorkStaffService.recycleQueue(queue).getStaffId();
        logger.info(DateUtil.getCurrentDateTimeStr() + "新 DT 的值 : " + newDT);
        redisService.setStr("DT", newDT);
    }

    /**
     * DZ的定时任务
     */
    @Scheduled(cron = "0 0 13 * * ?")
    public void changeDZ() {
        Queue<TStaff> queue = (Queue<TStaff>) QueueInterface.getMapQueue("DZ");
        String DZ = redisService.getStr("DZ");
        logger.info(DateUtil.getCurrentDateTimeStr() + "原 DZ 的值 : " + DZ);
        String newDZ = WorkStaffService.recycleQueue(queue).getStaffId();
        logger.info(DateUtil.getCurrentDateTimeStr() + "新 DZ 的值 : " + newDZ);
        redisService.setStr("DZ", newDZ);
    }

    @Scheduled(cron="0 30 23 * * ?")
    public void changeCJ(){
        Queue<TStaff> queue = (Queue<TStaff>) QueueInterface.getMapQueue("CJ");
        String CJ = redisService.getStr("CJ");
        logger.info(DateUtil.getCurrentDateTimeStr() + "原 CJ 的值 : " + CJ);
        String newCJ = WorkStaffService.recycleQueue(queue).getStaffId();
        logger.info(DateUtil.getCurrentDateTimeStr() + "新 CJ 的值 : " + newCJ);
        redisService.setStr("CJ", newCJ);
    }


    public static void main(String[] args) {
        try {
            long a = new Date().getTime();
            Thread.sleep(2000);
            long b = new Date().getTime();
            int c = (int)((b - a) / 1000);
            System.out.println(c);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
