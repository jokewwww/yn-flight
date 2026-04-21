package com.example.jobschedual.service;

import com.example.jobschedual.dao.TSettingTestDao;
import com.example.jobschedual.dao.TStaffRepository;
import com.example.jobschedual.entity.TSetting;
import com.example.jobschedual.entity.TStaff;
import com.example.jobschedual.factory.QueueInterface;
import com.example.jobschedual.redis.RedisService;
import com.example.jobschedual.util.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/13 13:56
 * @Description:
 */
@Service
@Slf4j
public class WorkStaffService {

    private LocalDateTime cDay=LocalDateTime.of(2019, Month.APRIL,1,9,30,0);
    private LocalDateTime bDay=LocalDateTime.of(2019,Month.APRIL,1,19,0,0);
    private LocalDateTime dDay=LocalDateTime.of(2019,Month.APRIL,1,13,0,0);
    private LocalDateTime jDay=LocalDateTime.of(2019,Month.MAY,1,12,30,0);

    @Autowired
    private TStaffRepository tStaffRepository;

    @Autowired
    private TSettingTestDao tSettingTestDao;

    @Autowired
    private RedisService redisService;

    /**
     * 初始化C7C8队列
     * 队列内容为人员ID
     */
    public void initC7C8() {
        try {
            //当前队列起始元素：
            log.info("初始化C7C8");
            List<TStaff> tStaffsC7C8 = tStaffRepository.findByStaffGroupIds("C7C8");
            if (!tStaffsC7C8.isEmpty()){
                Queue<TStaff> queueC7 = queueRelocation(tStaffsC7C8,cDay);
                Queue<TStaff> queueC8 = queueRelocation(tStaffsC7C8,LocalDateTime.of(2019,Month.APRIL,1,14,0,0));
                recycleQueue(queueC8);
                QueueInterface.maps.put("C7",queueC7);
                QueueInterface.maps.put("C8",queueC8);
                //设置当天值班人
                setStaff("C7",recycleQueue(queueC7));
                setStaff("C8",recycleQueue(queueC8));
            }else{
                log.error("C7C8查询数据失败");
            }
        }catch (Exception e){
            log.error("队列初始化失败：C7C8",e);
        }
    }


    /**
     * 初始化C2C20
     */
    public void initC2C20() {
        try {
            //当前队列起始元素：
            log.info("初始化C2C20");
            List<TStaff> tStaffsC2C20 = tStaffRepository.findByStaffGroupIds("C2C20");
            if(!tStaffsC2C20.isEmpty()){
                Queue<TStaff> queueC2 =  queueRelocation( tStaffsC2C20,cDay);
                Queue<TStaff> queueC20 =  queueRelocation( tStaffsC2C20,cDay);
                recycleQueue(queueC20);
                recycleQueue(queueC20);
                recycleQueue(queueC20);
                QueueInterface.maps.put("C2",queueC2);
                QueueInterface.maps.put("C20",queueC20);
                //设置redis当天值班人
                setStaff("C2",recycleQueue(queueC2));
                setStaff("C20",recycleQueue(queueC20));
            }else{
                log.error("C2C20查询数据失败");
            }
        } catch (Exception e) {
            log.error("队列初始化失败：C2C20", e);
        }
    }


    /**
     * 初始化C1
     */
    public void initC1() {
        try {
            log.info("初始化C1");
            List<TStaff> tStaffsC1 = tStaffRepository.findByStaffGroupIds("C1");
            if (!tStaffsC1.isEmpty()){
                initQueue("C1", tStaffsC1);
            }else{
                log.error("C1查询数据失败");
            }
        } catch (Exception e) {
            log.error("队列初始化失败：C1", e);
        }
    }

    /**
     * 初始化CJ
     */
    public void initCJ(){
        try {
            log.info("初始化CJ");
            List<TStaff> tStaffs = tStaffRepository.findByStaffGroupIds("CJ");
            List<TStaff> tStaffsCJ = Lists.newArrayList();
            tStaffs.forEach(tStaff -> {
                for (int i = 0; i < 2; i++) {
                    tStaffsCJ.add(tStaff);
                }
            });
            if (!tStaffsCJ.isEmpty()){
                initQueue("CJ", tStaffsCJ);
            }else{
                log.error("CJ查询数据失败");
            }
        } catch (Exception e) {
            log.error("队列初始化失败：CJ", e);
        }
    }



    /**
     * 队列重定位
     */
    private Queue<TStaff> queueRelocation(List<TStaff> list,LocalDateTime start) {
        /* //测试日期
        instance.set(2019,Calendar.APRIL,14,16,0,0);//假设日期
        long end=instance.getTimeInMillis();
        */
        long totalDays=getTotalDays(start)%list.size();
        Queue<TStaff> tempQueue = Queues.newLinkedBlockingQueue(new ArrayList<>(list));//创建一个临时队列
        for (int i = 0; i < totalDays; i++) {//循环队列
            recycleQueue(tempQueue);
        }
        return tempQueue;
    }

    /**
     * 循环队列
     *
     * @param queue
     */
    public static TStaff recycleQueue(Queue<TStaff> queue) {
        TStaff t = queue.poll();
        queue.offer(t);
        return t;
    }


    public void initLeader() {
        try {
            log.info("初始化Leader");
            List<TStaff> tStaffs11 = tStaffRepository.findByStaffGroupIdAndStaffType("铁拳", "4");
            List<TStaff> tStaffs21 = tStaffRepository.findByStaffGroupIdAndStaffType("铁汉", "4");
            List<TStaff> tStaffs31 = tStaffRepository.findByStaffGroupIdAndStaffType("铁锤", "4");
            List<TStaff> tStaffs41 = tStaffRepository.findByStaffGroupIdAndStaffType("铁人", "4");
            List<TStaff> tStaffs12 = tStaffRepository.findByStaffGroupIdAndStaffType("铁拳", "5");
            List<TStaff> tStaffs22 = tStaffRepository.findByStaffGroupIdAndStaffType("铁汉", "5");
            List<TStaff> tStaffs32 = tStaffRepository.findByStaffGroupIdAndStaffType("铁锤", "5");
            List<TStaff> tStaffs42 = tStaffRepository.findByStaffGroupIdAndStaffType("铁人", "5");
            List<TStaff> bzAll = unionAllList(tStaffs21, tStaffs11, tStaffs41, tStaffs31);
            List<TStaff> bz1All = unionAllList(tStaffs22, tStaffs12, tStaffs42, tStaffs32);
            if(!bzAll.isEmpty()){
                initQueue("BZ", bzAll);
            }else{
                log.error("BZ查询失败");
            }
           if(!bz1All.isEmpty()){
               initQueue("BZ1", bz1All);
           }else{
               log.error("BZ1查询失败");
           }
            List<TStaff> dtAll = unionAllList(tStaffs42,tStaffs31, tStaffs21, tStaffs11, tStaffs41, tStaffs32, tStaffs22, tStaffs11);
            List<TStaff> dzAll = unionAllList(tStaffs41,tStaffs32, tStaffs22, tStaffs12, tStaffs42, tStaffs31, tStaffs21, tStaffs12);
            if(!dtAll.isEmpty()){
                initQueue("DT", dtAll);
            }else{
                log.error("DT查询失败");
            }
            if(!dzAll.isEmpty()){
                System.out.println(dzAll.stream().map(TStaff::getStaffName).collect(Collectors.joining(",")));
                initQueue("DZ", dzAll);
            }else{
                log.error("DZ查询失败");
            }

        } catch (Exception e) {
            log.error("初始化失败：leader", e);
        }
    }


    public List<Map<String, Object>> pingBn(Integer num) {
        try {
            //TODO 暂定逻辑
            String b14 = redisService.getStr("B14");
            if(StringUtils.isEmpty(b14)){
                redisService.setStr("B14","-");
            }
            String b15 = redisService.getStr("B15");
            if(StringUtils.isEmpty(b15)){
                redisService.setStr("B15","-");
            }

            // TODO 逻辑结束

            String format = "HH:mm:ss";

            Date nowTime = new SimpleDateFormat(format).parse(new SimpleDateFormat(format).format(new Date()));
            Date startTime = new SimpleDateFormat(format).parse("19:00:00");
            Date endTime = new SimpleDateFormat(format).parse("23:59:59");
            boolean effectiveDate = DateUtil.isEffectiveDate(nowTime, startTime, endTime);
            if(effectiveDate){
              if(num == 4){
                  num = 1;
              }else{
                  num += 1;
              }
            }
            System.out.println("init B班 时 使用的队伍 是 : " + num );
            String bx1 = redisService.getStr("Bx1");
            String bx2 = redisService.getStr("Bx2");
            String bx3 = redisService.getStr("Bx3");
            String bx4 = redisService.getStr("Bx4");
            if (StringUtils.isEmpty(bx1)) {
                redisService.setStr("Bx1", "0");
            }
            if (StringUtils.isEmpty(bx2)) {
                redisService.setStr("Bx2", "0");
            }
            if (StringUtils.isEmpty(bx3)) {
                redisService.setStr("Bx3", "0");
            }
            if (StringUtils.isEmpty(bx4)) {
                redisService.setStr("Bx4", "0");
            }
            List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
            //首先查询 7个 组的数据   初始化查询数据
            LinkedBlockingQueue<Object>  ts = null;
            List<TStaff> tStaffs1 = tStaffRepository.findByStaffGroupIds("铁拳");
            List<TStaff> tStaffs2 = tStaffRepository.findByStaffGroupIds("铁汉");
            List<TStaff> tStaffs3 = tStaffRepository.findByStaffGroupIds("铁锤");
            List<TStaff> tStaffs4 = tStaffRepository.findByStaffGroupIds("铁人");

            LinkedBlockingQueue<Object> ts1 = QueueInterface.getQueue("BtStaffs1");
            insertQueue(tStaffs2, ts1, bx1);
            LinkedBlockingQueue<Object> ts2 = QueueInterface.getQueue("BtStaffs2");
            insertQueue(tStaffs1, ts2, bx2);
            LinkedBlockingQueue<Object> ts3 = QueueInterface.getQueue("BtStaffs3");
            insertQueue(tStaffs4, ts3, bx3);
            LinkedBlockingQueue<Object> ts4 = QueueInterface.getQueue("BtStaffs4");
            insertQueue(tStaffs3, ts4, bx4);
            if (num == 1) {
                ts = ts1;
            } else if (num == 2) {
                ts = ts2;
            } else if (num == 3) {
                ts = ts3;
            } else {
                ts = ts4;
            }
            //B N           2  1  4  3
            Integer[] bN = {0, 0, 0, 0};
            Integer[] news = {tStaffs1.size(), tStaffs2.size(), tStaffs3.size(), tStaffs4.size()};
            Arrays.sort(news);
            //得到人员最多的组的 大小
            Integer maxTstaff = news[news.length - 1];
            if (null != maxTstaff && maxTstaff != 0) {
                for (int i = 0; i < maxTstaff; i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    if (i >= ts.toArray().length) {
                        bN[0] = i - 1;
                        break;
                    } else {
                        String key =""
;                        if(i == 4 || i == 5 || i== 8){
                             key = "C" + (i + 1);
                        }else{
                             key = "B" + (i + 1);
                        }
                        Object staffId = ts.toArray()[i];
                        redisService.setStr(key, staffId.toString());
                        map.put(key, staffId);
                        listMap.add(map);
                    }
                }
            }
            return listMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Map<String, Object>> pingDn(Integer num) {
        try {
            //TODO 暂定逻辑
            String d14 = redisService.getStr("D14");
            if(StringUtils.isEmpty(d14)){
                redisService.setStr("D14","-");
            }
            String d15 = redisService.getStr("D15");
            if(StringUtils.isEmpty(d15)){
                redisService.setStr("D15","-");
            }
            // TODO 逻辑结束

            String format = "HH:mm:ss";
            Date nowTime = new SimpleDateFormat(format).parse(new SimpleDateFormat(format).format(new Date()));
            Date startTime = new SimpleDateFormat(format).parse("00:00:00");
            Date endTime = new SimpleDateFormat(format).parse("13:00:00");
            boolean effectiveDate = DateUtil.isEffectiveDate(nowTime, startTime, endTime);
            if(effectiveDate){
                if(num == 1){
                    num = 4;
                }else{
                    num -= 1;
                }
            }
            System.out.println("init D班 时 使用的队伍 是 : " + num );
            String bx1 = redisService.getStr("Dx1");
            String bx2 = redisService.getStr("Dx2");
            String bx3 = redisService.getStr("Dx3");
            String bx4 = redisService.getStr("Dx4");
            if (StringUtils.isEmpty(bx1)) {
                redisService.setStr("Dx1", "0");
            }
            if (StringUtils.isEmpty(bx2)) {
                redisService.setStr("Dx2", "0");
            }
            if (StringUtils.isEmpty(bx3)) {
                redisService.setStr("Dx3", "0");
            }
            if (StringUtils.isEmpty(bx4)) {
                redisService.setStr("Dx4", "0");
            }
            List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
            //首先查询 7个 组的数据   初始化查询数据
            LinkedBlockingQueue<Object> ts = null;
            List<TStaff> tStaffs1 = tStaffRepository.findByStaffGroupIds("铁拳");
            List<TStaff> tStaffs2 = tStaffRepository.findByStaffGroupIds("铁汉");
            List<TStaff> tStaffs3 = tStaffRepository.findByStaffGroupIds("铁锤");
            List<TStaff> tStaffs4 = tStaffRepository.findByStaffGroupIds("铁人");

            LinkedBlockingQueue<Object> ts1 = QueueInterface.getQueue("DtStaffs1");
            insertQueue(tStaffs3, ts1, bx1);
            LinkedBlockingQueue<Object> ts2 = QueueInterface.getQueue("DtStaffs2");
            insertQueue(tStaffs2, ts2, bx2);
            LinkedBlockingQueue<Object> ts3 = QueueInterface.getQueue("DtStaffs3");
            insertQueue(tStaffs1, ts3, bx3);
            LinkedBlockingQueue<Object> ts4 = QueueInterface.getQueue("DtStaffs4");
            insertQueue(tStaffs4, ts4, bx4);
            if (num == 1) {
                ts = ts1;
            } else if (num == 2) {
                ts = ts2;
            } else if (num == 3) {
                ts = ts3;
            } else {
                ts = ts4;
            }
            //B N           3  2  1  4
            Integer[] bN = {0, 0, 0, 0};
            Integer[] news = {tStaffs1.size(), tStaffs2.size(), tStaffs3.size(), tStaffs4.size()};
            Arrays.sort(news);
            //得到人员最多的组的 大小
            Integer maxTstaff = news[news.length - 1];
            if (null != maxTstaff && maxTstaff != 0) {
                for (int i = 0; i < maxTstaff; i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    if (i >= ts.toArray().length) {
                        bN[0] = i - 1;
                        break;
                    } else {
                        String key = "";
                        if(i == 0 ){
                            key = "CE";
                        }else{
                            key = "D" + (i + 1);
                        }
                        Object staffId = ts.toArray()[i];
                        redisService.setStr(key, staffId.toString());
                        map.put(key, staffId);
                        listMap.add(map);
                    }
                }
            }
            return listMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Boolean insertQueue(List<TStaff> tStaffs, LinkedBlockingQueue<Object> ts1, String bx) {
        try {
            if (tStaffs.size() < 1 || null == ts1) {
                return false;
            }
            for (int i = 0; i < tStaffs.size(); i++) {
                ts1.offer(tStaffs.get(i).getStaffId());
            }
            if(!StringUtils.isEmpty(bx)){
                Integer sumBx = Integer.valueOf(bx);
                for (int i = 0; i < ts1.toArray().length -sumBx; i++) {
                    Object take = ts1.take();
                    ts1.offer(take);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    //初始化所有Queue
    public void init() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String queueintit = redisService.getStr("queueintit");
        System.out.println("queueintit= " + queueintit);
        Date now=new Date();
        if (StringUtils.isEmpty(queueintit)) {
            //初始化的时间暂时放入这里
            redisService.setStr("queueintit", DateUtil.getCurrentDateStr());
            pingDn(0);
            pingBn(0);
            initLeader();
            initC7C8();
            initC2C20();
            initC1();
        } else {
            try {
                Date parse = format.parse(queueintit);
                Date parse1 = format.parse(DateUtil.getCurrentDateStr());
                Integer num = DateUtil.differentDays(parse, parse1);
                Integer team = 0;     // 第几队
                if (num < 4) {
                    team = num;
                } else {
                    team = num % 4;
                }
           /*     if (team == 0) {
                    team = 1;
                }*/
                team = team + 1;
                System.out.println("team="+team);
                pingDn(team);
                pingBn(team);
                initLeader();
                initC7C8();
                initC2C20();
                initC1();
                initCJ();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 合并所有集合
     *
     * @param lists
     * @param <T>
     * @return
     */
    public static <T> List<T> unionAllList(List<T>... lists) {
        List<T> list = Lists.newArrayList();
        for (List<T> ts : lists) {
            list.addAll(ts);
        }
        return list;
    }

    private void initQueue(String type, List<TStaff> list) {
        Queue<TStaff> queue;
        if(type.startsWith("B")){
            queue= queueRelocation(list,bDay);
        }else if(type.startsWith("D")){
            queue=queueRelocation(list,dDay);
        }else if(type.startsWith("CJ")) {
            queue=queueRelocation(list,jDay);
        }else{
            queue=queueRelocation(list,cDay);
        }
        QueueInterface.maps.put(type, queue);
        setStaff(type,recycleQueue(queue));
    }


    public void test(){
        try {
            for (int s = 1; s <31 ; s++) {
                System.out.println("2019-04-"+s);
                int i = 4;
                if(i > 4){
                    i = i%4 ;
                }
                System.out.println("i==========="+i);
//                B  ------------------------------
                LinkedBlockingQueue mapQueue = null;
                if(i == 1){
                    System.out.println("B 铁汉上班");
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("BtStaffs1");
                }else if(i == 2){
                    System.out.println("B 铁拳上班");
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("BtStaffs2");
                }else if(i == 3){
                    System.out.println("B 铁人上班");
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("BtStaffs3");
                }else if(i == 4){
                    System.out.println("B 铁锤上班");
                    mapQueue = (LinkedBlockingQueue) QueueInterface.getMapQueue("BtStaffs4");
                }
                //Bn更新之前 , 队列中所有值
                if(null != mapQueue){
                    Object take = mapQueue.take();
                    mapQueue.offer(take);
                }
                Object[] objects = mapQueue.toArray();
                //更新 B5  B6 的人
                for (int i1 = 0; i1 <objects.length ; i1++) {
                    System.out.println("新---B"+(i1+1)+"---"+objects[i1]);
                }
                //B----------------------------
                //D----------------------------
                LinkedBlockingQueue mapQueued = null;
                //  1=3  2= 2  3= 1  4= 4
                if(i == 1){
                    System.out.println("D 铁锤上班");
                    mapQueued = (LinkedBlockingQueue) QueueInterface.getMapQueue("DtStaffs1");
                }else if(i == 2){
                    System.out.println("D 铁汉上班");
                    mapQueued = (LinkedBlockingQueue) QueueInterface.getMapQueue("DtStaffs2");
                }else if(i == 3){
                    System.out.println("D 铁拳上班");
                    mapQueued = (LinkedBlockingQueue) QueueInterface.getMapQueue("DtStaffs3");
                }else if(i == 4){
                    System.out.println("D 铁人上班");
                    mapQueued = (LinkedBlockingQueue) QueueInterface.getMapQueue("DtStaffs4");
                }
                if(null != mapQueued){
                    Object take = mapQueued.take();
                    mapQueued.offer(take);
                }
                Object[] objectsd = mapQueue.toArray();
                for (int i2 = 1; i2 < objectsd.length; i2++) {
                    System.out.println("新---D"+(i2+1)+"---"+objects[i2]);
                }
                //D----------------------------

                //CE
                LinkedBlockingQueue mapQueuece = null;
                //  1=3  2= 2  3= 1  4= 4
                if(i == 1){
                    mapQueuece = (LinkedBlockingQueue) QueueInterface.getMapQueue("DtStaffs1");
                }else if(i == 2){
                    mapQueuece = (LinkedBlockingQueue) QueueInterface.getMapQueue("DtStaffs2");
                }else if(i == 3){
                    mapQueuece = (LinkedBlockingQueue) QueueInterface.getMapQueue("DtStaffs3");
                }else if(i == 4){
                    mapQueuece = (LinkedBlockingQueue) QueueInterface.getMapQueue("DtStaffs4");
                }
                if(null != mapQueuece){
                    Object[] objectsce = mapQueue.toArray();
                    if(objectsce.length > 0){
                        System.out.println("新---D1(CE)---"+objectsce[0]);
                    }
                }

                //C7
                Queue<TStaff> queue = (Queue<TStaff>) QueueInterface.getMapQueue("C7");
                String newC7 = WorkStaffService.recycleQueue(queue).getStaffName();
                System.out.println("新 C7 的值 : " + newC7);

                //c8
                Queue<TStaff> queuec8 = (Queue<TStaff>) QueueInterface.getMapQueue("C8");
                String newC8 = WorkStaffService.recycleQueue(queuec8).getStaffName();
                System.out.println("新 C8 的值 : " + newC8);


                //c2
                Queue<TStaff> queuec2 = (Queue<TStaff>) QueueInterface.getMapQueue("C2");
                String newC2 = WorkStaffService.recycleQueue(queuec2).getStaffName();
                System.out.println("新 C2 的值 : " + newC2);

                //c20
                Queue<TStaff> queuec20 = (Queue<TStaff>) QueueInterface.getMapQueue("C20");
                String newC20 = WorkStaffService.recycleQueue(queuec20).getStaffName();
                System.out.println("新 C20 的值 : " + newC20);

                //c1
                Queue<TStaff> queuec1= (Queue<TStaff>) QueueInterface.getMapQueue("C1");
                String newC1 = WorkStaffService.recycleQueue(queuec1).getStaffName();
                System.out.println("新 C1 的值 : " + newC1);

                //bz
                Queue<TStaff> queuebz= (Queue<TStaff>) QueueInterface.getMapQueue("BZ");
                String newBZ = WorkStaffService.recycleQueue(queuebz).getStaffName();
                System.out.println("新 BZ 的值 : " + newBZ);

                //bz1
                Queue<TStaff> queuebz1= (Queue<TStaff>) QueueInterface.getMapQueue("BZ1");
                String newBZ1 = WorkStaffService.recycleQueue(queuebz1).getStaffName();
                System.out.println("新 BZ1 的值 : " + newBZ1);


                //Dt
                Queue<TStaff> queuedt= (Queue<TStaff>) QueueInterface.getMapQueue("DT");
                String newDT = WorkStaffService.recycleQueue(queuedt).getStaffName();
                System.out.println("新 DT 的值 : " + newDT);


                //dz
                Queue<TStaff> queuedz = (Queue<TStaff>) QueueInterface.getMapQueue("DZ");
                String newDZ = WorkStaffService.recycleQueue(queuedz).getStaffName();
                System.out.println("新 DZ 的值 : " + newDZ);

                i++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 计算两个日期差多少天
     * @param localDate
     * @return
     */
    private long  getTotalDays(LocalDateTime localDate){
        LocalDateTime now=LocalDateTime.now();
        return localDate.until(now, ChronoUnit.DAYS);
    }

    private void setStaff(String type,TStaff tStaff){
        redisService.setStr(type,tStaff.getStaffId());
        log.info(type+"当前值班人员为："+tStaff.getStaffName());
    }


//    public static void main(String[] args) {
//        try {
//            LinkedBlockingQueue<Object> ts1 = QueueInterface.getQueue("test");
//            ts1.offer("1");
//            ts1.offer("2");
//            ts1.offer("3");
//            ts1.offer("4");
//            System.out.println(ts1.toArray()[0]+"-"+ts1.toArray()[1]+"-"+ts1.toArray()[2]+"-"+ts1.toArray()[3]);
//
//            for (int i = 0; i < ts1.toArray().length -0; i++) {
//                Object take = ts1.take();
//                ts1.offer(take);
//            }
//            System.out.println("------"+ts1.toArray()[0]+"-"+ts1.toArray()[1]+"-"+ts1.toArray()[2]+"-"+ts1.toArray()[3]);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    public String tests() {
        try {
            List<TStaff> all = tStaffRepository.findAll();
            String s = "{\"title\":[{\"taskStarmark\":\"任务星标\"},{\"flgtAlarm\":\"闹钟\"},{\"flgtNum\":\"序号\"},{\"flgtLinkFlno\":\"进港航班\"},{\"flgtFlno\":\"离港航班\"},{\"flgtAcname\":\"机型\"},{\"flgtRegn\":\"机号\"},{\"flgtPlacecode\":\"机位\"},{\"flgtVialc\":\"航线\"},{\"flgtAStot\":\"计达\"},{\"flgtAEtot\":\"预达\"},{\"flgtAAtot\":\"实达\"},{\"flgtDStot\":\"计飞\"},{\"flgtDEtot\":\"预飞\"},{\"flgtFtyp\":\"航班状态\"},{\"flgtDAtot\":\"实飞\"},{\"flgtMissionProp\":\"航班任务\"},{\"flgtAl2c\":\"航空公司二字码\"},{\"flgtAlcname\":\"航空公司\"},{\"flgtId\":\"航班ID\"},{\"flgtOrg3c\":\"出发地机场三字码\"},{\"flgtOrgnm\":\"出发地机场\"},{\"flgtTrs3c\":\"经停备降机场三字码\"},{\"flgtTrsnm\":\"经停备降机场\"},{\"flgtDes3c\":\"目的地机场三字码\"},{\"flgtDesnm\":\"目的地机场\"},{\"flgtAdid\":\"进离港\"},{\"flgtFlti\":\"航班国内/国际\"},{\"flgtProxy\":\"航空服务代理\"},{\"flgtFnflag\":\"远近机位\"},{\"flgtGame\":\"本场\"},{\"flgtChocksIn\":\"上轮档时间\"},{\"flgtChocksOut\":\"撤轮挡时间\"},{\"flgtVip\":\"要客\"},{\"taskId\":\"任务ID\"},{\"taskOpeStaffId\":\"加油员员工ID\"},{\"taskContent\":\"任务内容\"},{\"taskStatus\":\"任务状态\"},{\"taskAsgTime\":\"任务派发时间\"},{\"taskAccTime\":\"任务接受时间\"},{\"taskChagStaTime\":\"加油开始时间\"},{\"taskChagEndTime\":\"加油完成时间\"},{\"taskDoneTime\":\"任务完成时间\"},{\"taskFuelRecptNo\":\"加油单编号\"},{\"taskVehiNo\":\"加油车编号\"},{\"taskCreStaffId\":\"创建人员工ID\"},{\"taskRecCreTime\":\"记录创建时间\"}]}";
            String s1= "{\"col\":[{\"fieldName\":\"机位\",\"fieldKey\":\"flgtPlacecode\",\"children\":[{\"childrenFieldName\":\"改变\",\"fieldColor\":\"\"}]},{\"fieldName\":\"航空公司\",\"fieldKey\":\"flgtAlcname\",\"children\":[{\"childrenFieldName\":\"改变\",\"fieldColor\":\"\"}]},{\"fieldName\":\"机号\",\"fieldKey\":\"flgtRegn\",\"children\":[{\"childrenFieldName\":\"改变\",\"fieldColor\":\"\"}]},{\"fieldName\":\"航班号\",\"fieldKey\":\"flgtFlno\",\"children\":[{\"childrenFieldName\":\"改变\",\"fieldColor\":\"\"}]},{\"fieldName\":\"航线\",\"fieldKey\":\"flgtVialc\",\"children\":[{\"childrenFieldName\":\"改变\",\"fieldColor\":\"\"}]},{\"fieldName\":\"计划到达时间\",\"fieldKey\":\"flgtAStot\",\"children\":[{\"childrenFieldName\":\"改变\",\"fieldColor\":\"#ffff80\"}]},{\"fieldName\":\"预计到达时间\",\"fieldKey\":\"flgtAEtot\",\"children\":[{\"childrenFieldName\":\"改变\",\"fieldColor\":\"\"}]},{\"fieldName\":\"实际到达时间\",\"fieldKey\":\"flgtAAtot\",\"children\":[{\"childrenFieldName\":\"改变\",\"fieldColor\":\"\"}]},{\"fieldName\":\"预计起飞时间\",\"fieldKey\":\"flgtDEtot\",\"children\":[{\"childrenFieldName\":\"改变\",\"fieldColor\":\"\"}]},{\"fieldName\":\"实际起飞时间\",\"fieldKey\":\"flgtDAtot\",\"children\":[{\"childrenFieldName\":\"改变\",\"fieldColor\":\"\"}]},{\"fieldName\":\"计划起飞时间\",\"fieldKey\":\"flgtDStot\",\"children\":[{\"childrenFieldName\":\"改变\",\"fieldColor\":\"\"}]},{\"fieldName\":\"是否离境\",\"fieldKey\":\"\",\"children\":[{\"childrenFieldName\":\"改变\",\"fieldColor\":\"\"}]},{\"fieldName\":\"航班状态\",\"fieldKey\":\"flgtFtyp\",\"children\":[{\"childrenFieldName\":\"改变\",\"fieldColor\":\"#ff80ff\"}]},{\"fieldName\":\"是否长航线\",\"fieldKey\":\"\",\"children\":[{\"childrenFieldName\":\"改变\",\"fieldColor\":\"\"}]}],\"row\":{}}";
            all.forEach(tStaff -> {
                TSetting tSetting = new TSetting();
                    tSetting.setSettOptionId(UUID.randomUUID().toString());
                    tSetting.setSettStaffId(tStaff.getStaffId());
                    tSetting.setSettAirportCode(tStaff.getStaffAirportCode());
                    tSetting.setSettType("1");
                    tSetting.setSettStatus(0);
                    tSetting.setSettRoworcol(1);
                    tSetting.setSettPriority(0);
                     tSetting.setSettInfo(s);
                    tSettingTestDao.saveAndFlush(tSetting);
                TSetting tSetting1 = new TSetting();
                    tSetting1.setSettOptionId(UUID.randomUUID().toString());
                    tSetting1.setSettStaffId(tStaff.getStaffId());
                    tSetting1.setSettAirportCode(tStaff.getStaffAirportCode());
                    tSetting1.setSettType("2");
                    tSetting1.setSettStatus(0);
                    tSetting1.setSettRoworcol(2);
                    tSetting1.setSettPriority(0);
                tSetting1.setSettInfo(s1);
                tSettingTestDao.saveAndFlush(tSetting1);
            });
            return "succ";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
}
