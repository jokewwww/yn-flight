package com.zh.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.zh.bean.FlightImportDto;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.*;
import com.zh.bean.login.MyStaff;
import com.zh.component.RedissonDistributedLocker;
import com.zh.constant.Constant;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.*;
import com.zh.exception.CustomException;
import com.zh.prop.Prop;
import com.zh.service.FlightService;
import com.zh.service.IExcelService;
import com.zh.service.TaskService;
import com.zh.util.UUIDUitl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class ExcelServiceImpl implements IExcelService {

    private final static Logger log = LoggerFactory.getLogger(ExcelServiceImpl.class);
    @Autowired
    Prop prop;
    @Autowired
    private FlightMapper flightMapper;
    @Autowired
    private AirportCodeMapper airportCodeMapper;
    @Autowired
    private AirlinesCodeMapper airlinesCodeMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MyTFlightCodeMapper myTFlightCodeMapper;

    @Autowired
    private RedissonDistributedLocker redissonDistributedLocker;

    @Autowired
    private FlightService flightService;

    @Autowired
    private TaskService taskService;

    public static void main(String[] args) {
        String flgtReg = "B1120";
        String olds = flgtReg;

        if (flgtReg.startsWith("B") && flgtReg.indexOf("-") == -1) {
            StringBuffer stringBuilder1 = new StringBuffer(flgtReg);
            stringBuilder1.insert(1, "-");
            flgtReg = stringBuilder1.toString();
            System.out.println("飞机号:" + flgtReg);
        } else {
            //TODO
            String a = "^([A-Z]+)(\\d*.*)$";
            Pattern compile = Pattern.compile(a);
            Matcher matcher = compile.matcher(flgtReg);
            if (matcher.matches()) {
                if (StringUtils.isNotEmpty(matcher.group(2)) && StringUtils.isNotEmpty(matcher.group(1))) {
                    flgtReg = matcher.group(1) + "-" + matcher.group(2);
                } else if (StringUtils.isNotEmpty(matcher.group(1))) {
                    flgtReg = matcher.group(1);
                }
            }
        }
        System.out.println(flgtReg);
        System.out.println(olds);


    }

    @Override
    public ReturnMsg<String> importTFlight(byte[] file, String airportCode) {
        try {
            Assert.hasLength(airportCode, "机场代码不能为空");
            MyAirportCode tAirportCode = airportCodeMapper.selectAirportCodes(airportCode);
            Assert.notNull(tAirportCode, "查询不到机场代码：" + airportCode);
            List<Row> rowList = workToSheet(new ByteArrayInputStream(file)).skip(1).collect(Collectors.toList());
            List<String> errorFlnoList = Lists.newArrayList();

            AtomicReference<Integer> count = new AtomicReference<>(0);
            rowList.stream().forEach(row -> {
                FlightImportDto flightImportDto = this.verifyFlightData(row);
                if (flightImportDto.isOk()) {
                    Pair<MyFlight, MyFlight> myFlightMyFlightPair = this.rowToTFlight(flightImportDto, tAirportCode);
                    addFlight(myFlightMyFlightPair);
                    count.getAndSet(count.get() + 1);
                } else {
                    errorFlnoList.add(flightImportDto.getFlgtFlno());
                }
            });
            flightService.cacheFlightAndTaskForPad(airportCode);
            StringBuffer resultInfo = new StringBuffer("导入航班")
                    .append(count.get() + "条");
            if (CollectionUtils.isNotEmpty(errorFlnoList)) {
                resultInfo.append(",航班号" + errorFlnoList.toString() + "导入失败。");
            }
            return ReturnMsg.getInstanceOKz(resultInfo.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnMsg.getInstanceNGz(e.getMessage());
        }
    }

    private FlightImportDto verifyFlightData(Row row) {
        FlightImportDto flightImportDto = new FlightImportDto();
        int rowNum = row.getRowNum();
        flightImportDto.setRowNum(rowNum);

        boolean isOk = Boolean.TRUE;

        Cell cell = row.getCell(0, Row.RETURN_NULL_AND_BLANK);
        String flgtFlno = getCellValue(cell);
        flightImportDto.setFlgtFlno(flgtFlno);
        isOk = isOk && StringUtils.isNotBlank(flgtFlno);

        Cell cell1 = row.getCell(1, Row.RETURN_NULL_AND_BLANK);
        String flgtReg = getCellValue(cell1);
        flightImportDto.setFlgtReg(flgtReg);
        isOk = isOk && StringUtils.isNotBlank(flgtReg);

        Cell cell2 = row.getCell(2, Row.RETURN_NULL_AND_BLANK);
        String flgtFlti = getCellValue(cell2);
        flightImportDto.setFlgtFlti(flgtFlti);
        isOk = isOk && StringUtils.isNotBlank(flgtFlti);

        Cell cell3 = row.getCell(3, Row.RETURN_NULL_AND_BLANK);
        String airportNames = getCellValue(cell3);
        flightImportDto.setAirportNames(airportNames);
        isOk = isOk && StringUtils.isNotBlank(airportNames);

        Cell cell4 = row.getCell(4, Row.RETURN_NULL_AND_BLANK);
        String dstot = getCellValue(cell4);
        flightImportDto.setDstot(dstot);
        isOk = isOk && StringUtils.isNotBlank(dstot);

        Cell cell5 = row.getCell(5, Row.RETURN_NULL_AND_BLANK);
        String detot = getCellValue(cell5);
        flightImportDto.setDetot(detot);
        isOk = isOk && StringUtils.isNotBlank(detot);

        Cell cell6 = row.getCell(6, Row.RETURN_NULL_AND_BLANK);
        String placeCode = getCellValue(cell6);
        flightImportDto.setPlaceCode(placeCode);
        isOk = isOk && StringUtils.isNotBlank(placeCode);

        Cell cell7 = row.getCell(7, Row.RETURN_NULL_AND_BLANK);
        String flgtNature = getCellValue(cell7);
        flightImportDto.setFlgtNature(flgtNature);

        flightImportDto.setOk(isOk);
        return flightImportDto;
    }

    private Pair<MyFlight, MyFlight> rowToTFlight(FlightImportDto flightImportDto, MyAirportCode airport) {

        System.out.println("rowNum------" + flightImportDto.getRowNum());

        String flgtNo = flightImportDto.getFlgtFlno();

        String flgtReg = flightImportDto.getFlgtReg();
        String oldFlgtReg = flgtReg;

        String flgtFlti = parseFlti(flightImportDto.getFlgtFlti());
        String airportNames = flightImportDto.getAirportNames();


        String dstot = flightImportDto.getDstot();
        String detot = flightImportDto.getDetot();
        String placeCode = flightImportDto.getPlaceCode();

        String flgtNature = StatusConstant.FlgtNatureEnum.PAX.getFtypType();//默认是客机
        if (StringUtils.equals("货机", flightImportDto.getFlgtNature())) {
            flgtNature = StatusConstant.FlgtNatureEnum.CGO.getFtypType();
        }

        // flgtReg  selectByArcrRegn
        MyFlight out = new MyFlight();
        MyFlightCode flightCodeInfoByRegnAndFlno = taskService.getFlightCodeInfoByRegnAndFlno(flgtReg, flgtNo);
        if (Objects.nonNull(flightCodeInfoByRegnAndFlno)) {
            out.setFlgtAcname(flightCodeInfoByRegnAndFlno.getArcrAcname());
        }
        out.setFlgtFlno(flgtNo);
        out.setFlgtRegn(oldFlgtReg);
        out.setFlgtFlti(flgtFlti);
        String[] airportName = airportNames.split("/");
        if (airportName.length > 1) {
            out.setFlgtTrs3c1(getItatLikeAirport(airportName[0]));
            out.setFlgtDes3c(getItatLikeAirport(airportName[1]));
        } else {
            out.setFlgtDes3c(getItatLikeAirport(airportName[0]));
        }
        if (dstot.length() == 5) {
            //如果给的是时间，长度为5，则默认为当天的这个时间
            out.setFlgtDStot(parseDate(LocalDate.now().toString() + " " + dstot, "yyyy-MM-dd HH:mm"));
        } else {
            //如果给是不是时间，则按照日期处理
            out.setFlgtDStot(DateUtil.parse(dstot, "yyyy-MM-dd HH:mm"));
        }

        if (dstot.length() == 5) {
            //如果给的是时间，长度为5，则默认为当天的这个时间
            out.setFlgtDEtot(parseDate(LocalDate.now().toString() + " " + detot, "yyyy-MM-dd HH:mm"));
        } else {
            //如果给是不是时间，则按照日期处理
            out.setFlgtDEtot(DateUtil.parse(detot, "yyyy-MM-dd HH:mm"));
        }
        out.setFlgtPlacecode(placeCode);
        out.setFlgtAdid("D");
        out.setFlgtFlop(new Date());
        out.setFlgtFtyp("OT");
        out.setFlgtMissionProp("W/Z");
        out.setFlgtAirportCode(airport.getApcdCnafAirportCode());
        out.setFlgtAl2c(StringUtils.substring(flgtNo, 0, 2));
        out.setFlgtOrg3c(airport.getApcdIataCode());
        out.setFlgtOrgnm(airport.getApcdAirportNameS());
        return Pair.of(null, out);
    }

    private Pair<MyFlight, MyFlight> rowToTFlight(Row row, MyAirportCode airport) {
        MyFlight in = null;
        MyFlight out = null;
        int rowNum = row.getLastCellNum();
        System.out.println("rowNum------" + rowNum);
        out = new MyFlight();
        Cell cell = row.getCell(0, Row.RETURN_NULL_AND_BLANK);
        if (null == cell) {
            return null;
        }
        String flgtNo = cell.getStringCellValue();
        if (StringUtils.isEmpty(flgtNo)) {
            return null;
        }
        Assert.hasText(flgtNo, "航班号不能为空");
        cell = row.getCell(1, Row.RETURN_NULL_AND_BLANK);
        String flgtReg = getCellValue(cell);
        Assert.hasText(flgtReg, "飞机号码不能为空");
        String oldFlgtReg = flgtReg;
        cell = row.getCell(2, Row.RETURN_NULL_AND_BLANK);
        String flgtFlti = parseFlti(getCellValue(cell));
        Assert.hasText(flgtFlti, "航班区域属性不能为空");
        cell = row.getCell(3, Row.RETURN_NULL_AND_BLANK);
        String airportNames = getCellValue(cell);
        Assert.hasText(airportNames, "到达机场不能为空");
        String[] airportName = airportNames.split("/");
        cell = row.getCell(4, Row.RETURN_NULL_AND_BLANK);
        String dstot = getCellValue(cell);
        Assert.hasText(dstot, "计划起飞时间不能为空");
        cell = row.getCell(5, Row.RETURN_NULL_AND_BLANK);
        String detot = getCellValue(cell);
        cell = row.getCell(6, Row.RETURN_NULL_AND_BLANK);
        String placeCode = getCellValue(cell);
        // flgtReg  selectByArcrRegn

        log.info("Excel导入航班获取飞机信息：飞机号-{}，航班号-{}", flgtReg, flgtNo);
        //获取飞机号归属信息
        MyFlightCode myFlightCode = taskService.getFlightCodeInfoByRegnAndFlno(flgtReg, flgtNo);
        if (null != myFlightCode) {
            out.setFlgtAcname(myFlightCode.getArcrAcname());
        }
        out.setFlgtFlno(flgtNo);
        out.setFlgtRegn(oldFlgtReg);
        out.setFlgtFlti(flgtFlti);
        if (airportName.length > 1) {
            out.setFlgtTrs3c1(getItatLikeAirport(airportName[0]));
            out.setFlgtDes3c(getItatLikeAirport(airportName[1]));
        } else {
            out.setFlgtDes3c(getItatLikeAirport(airportName[0]));
        }

        if (StrUtil.isNotBlank(dstot)) {
            if (dstot.length() == 5) {
                //如果给的是时间，长度为5，则默认为当天的这个时间
                out.setFlgtDStot(parseDate(LocalDate.now().toString() + " " + dstot, "yyyy-MM-dd HH:mm"));
            } else {
                //如果给是不是时间，则按照日期处理
                out.setFlgtDStot(DateUtil.parse(dstot, "yyyy-MM-dd HH:mm"));
            }
        }

        if (StringUtils.isNotEmpty(detot)) {
            if (dstot.length() == 5) {
                //如果给的是时间，长度为5，则默认为当天的这个时间
                out.setFlgtDEtot(parseDate(LocalDate.now().toString() + " " + detot, "yyyy-MM-dd HH:mm"));
            } else {
                //如果给是不是时间，则按照日期处理
                out.setFlgtDEtot(DateUtil.parse(detot, "yyyy-MM-dd HH:mm"));
            }
        }
        out.setFlgtPlacecode(placeCode + "");
        out.setFlgtAdid("D");
        out.setFlgtFlop(new Date());
        out.setFlgtFtyp("OT");
        out.setFlgtMissionProp("W/Z");
        out.setFlgtAirportCode(airport.getApcdCnafAirportCode());
        out.setFlgtAl2c(StringUtils.substring(flgtNo, 0, 2));
        out.setFlgtOrg3c(airport.getApcdIataCode());
        out.setFlgtOrgnm(airport.getApcdAirportNameS());
        return Pair.of(in, out);
    }

    private String parseFlti(String flti) {
        if (StringUtils.isEmpty(flti)) {
            return null;
        }
        switch (flti) {
            case "国内":
                return "D";
            case "国际":
                return "I";
            case "地区":
                return "R";
            case "混合":
                return "M";
            default:
                return "U";
        }
    }

    private String getItatLikeAirport(String name) {
        MyAirportCode myAirportCode = airportCodeMapper.selectAirportLikeName(name);
        return myAirportCode != null ? myAirportCode.getApcdIataCode() : null;
    }

    private Stream<Row> workToSheet(InputStream in) {
        try {
            POIFSFileSystem poi = new POIFSFileSystem(in);
            HSSFWorkbook sheets = new HSSFWorkbook(poi);
            HSSFSheet sheet = sheets.getSheetAt(0);
            return StreamSupport.stream(sheet.spliterator(), false);
        } catch (IOException e) {
            throw new RuntimeException("解析excel失败", e);
        }
    }

    private String getCellValue(Cell cell) {
        if (null == cell) {
            return "";
        }
        Object obj;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                obj = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    obj = DateFormatUtils.format(date, "HH:mm");
                } else {
                    double num = cell.getNumericCellValue();
                    DecimalFormat df = new DecimalFormat("0");
                    obj = df.format(num);
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                obj = "";
                break;
            default:
                obj = null;
                break;
        }
        return obj != null ? obj.toString() : "";
    }

    public void addFlight(Pair<MyFlight, MyFlight> pair) {
        try {
            MyFlight aFlight = null;
            MyFlight dFlight = null;
            String linkIdA = null;
            String uuidD = null;
            if (pair.getLeft() != null) {
                linkIdA = UUIDUitl.getUUID();
                pair.getLeft().setFlgtId(linkIdA);
                aFlight = pair.getLeft();
            }
            if (pair.getRight() != null) {
                uuidD = UUIDUitl.getUUID();
                pair.getRight().setFlgtId(uuidD);
                pair.getRight().setFlgtFfid(uuidD);
                if (StringUtils.isNotEmpty(linkIdA)) {
                    pair.getRight().setFlgtLinkFfid(linkIdA);
                    pair.getLeft().setFlgtLinkFfid(uuidD);
                    aFlight = pair.getLeft();
                }
                dFlight = pair.getRight();
            }
            addFlightAndTask(aFlight, dFlight);
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* try {
            MyFlight aFlight = null;
            MyFlight dFlight = null;
            String linkId = null;
            if (pair.getLeft() != null) {
                linkId = UUIDUitl.getUUID();
                pair.getLeft().setFlgtId(linkId);
                pair.getLeft().setFlgtFfid(linkId);
                aFlight = pair.getLeft();
            }
            if (pair.getRight() != null) {
                String uuid = UUIDUitl.getUUID();
                pair.getRight().setFlgtId(UUIDUitl.getUUID());
                pair.getRight().setFlgtId(UUIDUitl.getUUID());
                pair.getRight().setFlgtLinkFfid(linkId);
                dFlight = pair.getRight();
            }
            addFlightAndTask(aFlight, dFlight);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void setAirline(MyFlight myFlight, String[] airline) {
        myFlight.setFlgtOrg3c(airline[0]);
        List<String> list = Lists.newArrayList(airline);
        Collections.reverse(list);
        airline = list.toArray(new String[0]);
        myFlight.setFlgtDes3c(airline[0]);
    }

    private Date parseDate(String date, String... patterns) {
        try {
            if (StringUtils.isEmpty(date)) return null;
            return DateUtils.parseDate(date, patterns);
        } catch (ParseException e) {
            throw new RuntimeException(String.format("解析日期失败：%s,格式：%s", date, String.join("或", patterns)));
        }
    }

    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public MyFlightTask addFlightAndTask(MyFlight aFlight, MyFlight dFlight) {
        Integer taskContent = -1;
        MyFlightTask taskAndFlightById = null;
        Integer maxFlgtRepeat = 0;
        //+ 1
        Integer maxNum = flightService.getFlightMaxNum();
        try {
            maxFlgtRepeat = flightMapper.findMaxFlgtRepeat();
            if (maxFlgtRepeat == null) {
                maxFlgtRepeat = 0;
            }
            if (maxFlgtRepeat == 0) {
                maxFlgtRepeat = 1;
            } else {
                maxFlgtRepeat = maxFlgtRepeat + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //创建Map集合用来推送消息
        Map<String, Object> webMap = new HashMap<String, Object>();
        //创建人员对象用来存放机场所属代码
        MyStaff staffInfo = new MyStaff();
        if (dFlight != null) {
            staffInfo.setStaffAirportCode(dFlight.getFlgtAirportCode());
            staffInfo.setStaffAptareaCode(dFlight.getFlgtAptareaCode());
        }

        // 先创建进港
        if (aFlight != null) {
            String lockKey = new StringBuffer("flightAddLock:").append(DateUtil.today()).append(":")
                    .append(StrUtil.blankToDefault(aFlight.getFlgtRegn(), StrUtil.EMPTY)).append("-")
                    .append(StrUtil.blankToDefault(aFlight.getFlgtFlno(), StrUtil.EMPTY)).append("-")
                    .append(StrUtil.blankToDefault(aFlight.getFlgtAdid(), StrUtil.EMPTY)).append("-")
                    .append(StrUtil.blankToDefault(aFlight.getFlgtAirportCode(), StrUtil.EMPTY)).append("-")
                    .append(StrUtil.blankToDefault(aFlight.getFlgtAptareaCode(), StrUtil.EMPTY)).toString();
            // 尝试获取锁，获取不到，立即返回结果
            if (!redissonDistributedLocker.tryLock(lockKey)) {
                return taskAndFlightById;
            }
            try {
                //先查询 进港航班存不存在
                List<MyFlight> flightInfos = flightMapper.getFlightInfos(
                        aFlight, aFlight.getFlgtAirportCode(), aFlight.getFlgtAptareaCode());
                MyFlight flightInfo = CollUtil.isNotEmpty(flightInfos) ? flightInfos.get(0) : null;
                if (flightInfo != null) {
                    //航空公司二字码
                    MyAirlinesCode myAirlinesCode = airlinesCodeMapper.selectAirlinesCodeFind(aFlight.getFlgtAl2c());
                    if (myAirlinesCode != null) {
                        aFlight.setFlgtAlcname(myAirlinesCode.getAlcdArlnName());
                    }
                    aFlight.setFlgtId(aFlight.getFlgtFfid());
                    //判断 flgt_org3c 经停机场三字码  			//判断 flgt_org3c 出发地机场三字码
                    if (StringUtils.isBlank(aFlight.getFlgtTrs3c1())) {
                        //把拼接好的航线赋值进航班的航线字段中
                        aFlight.setFlgtVialc(aFlight.getFlgtOrg3c() + "-" + aFlight.getFlgtDes3c());
                    } else {
                        //把拼接好的航线赋值进航班的航线字段中
                        aFlight.setFlgtVialc(aFlight.getFlgtOrg3c() + "-" + aFlight.getFlgtTrs3c1() + "-" + aFlight.getFlgtDes3c());
                    }
                    aFlight.setFlgtOrgnm(flightMapper.getAirportName(aFlight.getFlgtOrg3c()));
                    aFlight.setFlgtOrg3c(aFlight.getFlgtOrg3c());
                    // 根据目的地机场三字码去DB中查出对应的目的地机场名称，赋值到flight对象中
                    aFlight.setFlgtDesnm(flightMapper.getAirportName(aFlight.getFlgtDes3c()));
                    // 创建一个空字符串用来装航线机场全名称
                    String flgtVialcName = null;
                    // 创建一个空字符串用来装航线机场简称
                    String flgtVialcNames = null;
                    // 把航线按照-分割成每一个机场三字码
                    String[] split = aFlight.getFlgtVialc().split("-");
                    // for循环航线数组
                    for (int i = 0; i < split.length; i++) {
                        // 循环航线，根据航线中的机场三字码查询每个机场
                        String airportName = flightMapper.getAirportName(split[i]);
                        // 循环航线，根据航线中的机场三字码查询每个机场简称
                        String airportNames = flightMapper.getAirportNamess(split[i]);
                        // 如果i等于0说明是第一次进来直接赋值就行
                        if (i == 0 && !StringUtils.isBlank(airportName)) {
                            flgtVialcName = airportName;
                        }
                        if (i == 0 && StringUtils.isBlank(airportName)) {
                            flgtVialcName = split[i];
                        }
                        // 如果i等于0说明是第一次进来直接赋值就行
                        if (i == 0 && !StringUtils.isBlank(airportNames)) {
                            flgtVialcNames = airportNames;
                        }
                        if (i == 0 && StringUtils.isBlank(airportNames)) {
                            flgtVialcNames = split[i];
                        }
                        if (i > 0 && i < split.length && StringUtils.isBlank(airportName)) {
                            flgtVialcName = flgtVialcName + "-" + split[i];
                        }
                        if (i > 0 && i < split.length && !StringUtils.isBlank(airportName)) {
                            flgtVialcName = flgtVialcName + "-" + airportName;
                        }
                        // 如果i>0并且小于数组的长度的话说明不是第一次进来就开始拼接
                        if (i > 0 && i < split.length && !StringUtils.isBlank(airportNames)) {
                            // 拼接简称
                            flgtVialcNames = flgtVialcNames + "-" + airportNames;
                        }
                        if (i > 0 && i < split.length && StringUtils.isBlank(airportNames)) {
                            // 拼接简称
                            flgtVialcNames = flgtVialcNames + "-" + split[i];
                        }
                    }
                    // 把拼接好的航线全称放进航班对象中
                    aFlight.setFlgtTrsnm3(flgtVialcName);
                    // 把拼接好的航线全称放进航班对象中
                    aFlight.setFlgtTrsnm5(flgtVialcName);
                    // 把拼接好的航线三字码放进航班对象中
                    aFlight.setFlgtTrs3c5(aFlight.getFlgtVialc());
                    // 把拼接完成的航线简称赋值到将要添加的航班中
                    aFlight.setFlgtTrsnm4(flgtVialcNames);
                    aFlight.setFlgtGame(Constant.FLGT_DGAME);
                    //任务下发标识如果等于空就赋默认值0
                    if (aFlight.getFlgtTaskAsign() == null) {
                        aFlight.setFlgtTaskAsign(0);
                    }
                    //航班星标如果等于空就赋默认值0
                    if (aFlight.getFlgtStarmark() == null) {
                        aFlight.setFlgtStarmark(0);
                    }
                    //手动修改航班如果等于空就赋默认值0
                    if (aFlight.getFlgtManualFlg() == null) {
                        aFlight.setFlgtManualFlg(0);
                    }
                    if (flightMapper.addFlight(aFlight) != 1) {
                        throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                    }
                }
            } finally {
                // 无条件释放锁
                redissonDistributedLocker.unlock(lockKey);
            }

        }
        // 处理出港航班
        //if (dFlight != null) {
        //    //先查询 进港航班存不存在
        //    MyFlight flightInfo = flightMapper.getFlightInfo(
        //            dFlight, dFlight.getFlgtAirportCode(), dFlight.getFlgtAirportCode());
        //
        //    MyAirlinesCode myAirlinesCode = airlinesCodeMapper.selectAirlinesCodeFind(dFlight.getFlgtAl2c());
        //    if (myAirlinesCode != null) {
        //        dFlight.setFlgtAlcname(myAirlinesCode.getAlcdArlnName());
        //    }
        //    if (flightInfo != null) {
        //        dFlight.setFlgtId(dFlight.getFlgtFfid());
        //        //判断 flgt_org3c 经停机场三字码  			//判断 flgt_org3c 出发地机场三字码
        //        if (StringUtils.isBlank(dFlight.getFlgtTrs3c1())) {
        //            //把拼接好的航线赋值进航班的航线字段中
        //            dFlight.setFlgtVialc(dFlight.getFlgtOrg3c() + "-" + dFlight.getFlgtDes3c());
        //        } else {
        //            //把拼接好的航线赋值进航班的航线字段中
        //            dFlight.setFlgtVialc(dFlight.getFlgtOrg3c() + "-" + dFlight.getFlgtTrs3c1() + "-" + dFlight.getFlgtDes3c());
        //        }
        //        dFlight.setFlgtOrgnm(flightMapper.getAirportName(dFlight.getFlgtOrg3c()));
        //        dFlight.setFlgtOrg3c(dFlight.getFlgtOrg3c());
        //        // 根据目的地机场三字码去DB中查出对应的目的地机场名称，赋值到flight对象中
        //        dFlight.setFlgtDesnm(flightMapper.getAirportName(dFlight.getFlgtDes3c()));
        //        // 创建一个空字符串用来装航线机场全名称
        //        String flgtVialcName = null;
        //        // 创建一个空字符串用来装航线机场简称
        //        String flgtVialcNames = null;
        //        // 把航线按照-分割成每一个机场三字码
        //        String[] split = aFlight.getFlgtVialc().split("-");
        //        // for循环航线数组
        //        for (int i = 0; i < split.length; i++) {
        //            // 循环航线，根据航线中的机场三字码查询每个机场
        //            String airportName = flightMapper.getAirportName(split[i]);
        //            // 循环航线，根据航线中的机场三字码查询每个机场简称
        //            String airportNames = flightMapper.getAirportNamess(split[i]);
        //            // 如果i等于0说明是第一次进来直接赋值就行
        //            if (i == 0 && !StringUtils.isBlank(airportName)) {
        //                flgtVialcName = airportName;
        //            }
        //            if (i == 0 && StringUtils.isBlank(airportName)) {
        //                flgtVialcName = split[i];
        //            }
        //            // 如果i等于0说明是第一次进来直接赋值就行
        //            if (i == 0 && !StringUtils.isBlank(airportNames)) {
        //                flgtVialcNames = airportNames;
        //            }
        //            if (i == 0 && StringUtils.isBlank(airportNames)) {
        //                flgtVialcNames = split[i];
        //            }
        //            if (i > 0 && i < split.length && StringUtils.isBlank(airportName)) {
        //                flgtVialcName = flgtVialcName + "-" + split[i];
        //            }
        //            if (i > 0 && i < split.length && !StringUtils.isBlank(airportName)) {
        //                flgtVialcName = flgtVialcName + "-" + airportName;
        //            }
        //            // 如果i>0并且小于数组的长度的话说明不是第一次进来就开始拼接
        //            if (i > 0 && i < split.length && !StringUtils.isBlank(airportNames)) {
        //                // 拼接简称
        //                flgtVialcNames = flgtVialcNames + "-" + airportNames;
        //            }
        //            if (i > 0 && i < split.length && StringUtils.isBlank(airportNames)) {
        //                // 拼接简称
        //                flgtVialcNames = flgtVialcNames + "-" + split[i];
        //            }
        //        }
        //        // 把拼接好的航线全称放进航班对象中
        //        aFlight.setFlgtTrsnm3(flgtVialcName);
        //        // 把拼接好的航线全称放进航班对象中
        //        aFlight.setFlgtTrsnm5(flgtVialcName);
        //        // 把拼接好的航线三字码放进航班对象中
        //        aFlight.setFlgtTrs3c5(aFlight.getFlgtVialc());
        //        // 把拼接完成的航线简称赋值到将要添加的航班中
        //        aFlight.setFlgtTrsnm4(flgtVialcNames);
        //        aFlight.setFlgtGame(Constant.FLGT_DGAME);
        //        //任务下发标识如果等于空就赋默认值0
        //        if (aFlight.getFlgtTaskAsign() == null) {
        //            aFlight.setFlgtTaskAsign(0);
        //        }
        //        //航班星标如果等于空就赋默认值0
        //        if (aFlight.getFlgtStarmark() == null) {
        //            aFlight.setFlgtStarmark(0);
        //        }
        //        //手动修改航班如果等于空就赋默认值0
        //        if (aFlight.getFlgtManualFlg() == null) {
        //            aFlight.setFlgtManualFlg(0);
        //        }
        //        if (flightMapper.addFlight(aFlight) != 1) {
        //            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
        //        }
        //    }
        //}
        //判断如果查出来的航班对象如果等于NUll说明没有航班，就新建一个航班
        if (dFlight != null) {
            String lockKey = new StringBuffer("flightAddLock:").append(DateUtil.today()).append(":")
                    .append(StrUtil.blankToDefault(dFlight.getFlgtRegn(), StrUtil.EMPTY)).append("-")
                    .append(StrUtil.blankToDefault(dFlight.getFlgtFlno(), StrUtil.EMPTY)).append("-")
                    .append(StrUtil.blankToDefault(dFlight.getFlgtAdid(), StrUtil.EMPTY)).append("-")
                    .append(StrUtil.blankToDefault(dFlight.getFlgtAirportCode(), StrUtil.EMPTY)).append("-")
                    .append(StrUtil.blankToDefault(dFlight.getFlgtAptareaCode(), StrUtil.EMPTY)).toString();
            // 尝试获取锁，获取不到，立即返回结果
            if (!redissonDistributedLocker.tryLock(lockKey)) {
                return taskAndFlightById;
            }
            try {
                //先查询 出港航班存不存在
                List<MyFlight> flightInfos = flightMapper.getFlightInfos(
                        dFlight, dFlight.getFlgtAirportCode(), dFlight.getFlgtAptareaCode());
                MyFlight flightInfo = CollUtil.isNotEmpty(flightInfos) ? flightInfos.get(0) : null;
                if (flightInfo == null) {
                    MyAirlinesCode myAirlinesCode = airlinesCodeMapper.selectAirlinesCodeFind(dFlight.getFlgtAl2c());
                    if (myAirlinesCode != null) {
                        dFlight.setFlgtAlcname(myAirlinesCode.getAlcdArlnName());
                    }
                    if (!StringUtils.isBlank(dFlight.getFlgtAdid()) && "D".equals(dFlight.getFlgtAdid())) {
                        dFlight.setFlgtNum(maxNum);
                    }
                    //判断航班性质是否为CGO
           /* if (!StringUtils.isEmpty(dFlight.getFlgtNature()) && "CGO".equals(dFlight.getFlgtNature())) {
                //如果是CGO的话就往flight对象中的是否为货机字段赋值Y
                dFlight.setFlgtIfsr("Y");
            } else {
                //如果不是CGO的话就往flight对象中的是否为货机字段赋值N
                dFlight.setFlgtIfsr("N");
            }*/

                    //判断 flgt_org3c 经停机场三字码  			//判断 flgt_org3c 出发地机场三字码
                    if (StringUtils.isBlank(dFlight.getFlgtTrs3c1())) {
                        //把拼接好的航线赋值进航班的航线字段中
                        dFlight.setFlgtVialc(dFlight.getFlgtOrg3c() + "-" + dFlight.getFlgtDes3c());
                    } else {
                        //把拼接好的航线赋值进航班的航线字段中
                        dFlight.setFlgtVialc(dFlight.getFlgtOrg3c() + "-" + dFlight.getFlgtTrs3c1() + "-" + dFlight.getFlgtDes3c());
                    }
                    dFlight.setFlgtOrgnm(flightMapper.getAirportName(dFlight.getFlgtOrg3c()));
                    dFlight.setFlgtOrg3c(dFlight.getFlgtOrg3c());
                    // 根据目的地机场三字码去DB中查出对应的目的地机场名称，赋值到flight对象中
                    dFlight.setFlgtDesnm(flightMapper.getAirportName(dFlight.getFlgtDes3c()));
                    // 创建一个空字符串用来装航线机场全名称
                    String flgtVialcName = null;
                    // 创建一个空字符串用来装航线机场简称
                    String flgtVialcNames = null;
                    // 把航线按照-分割成每一个机场三字码
                    String[] split = dFlight.getFlgtVialc().split("-");
                    // for循环航线数组
                    for (int i = 0; i < split.length; i++) {
                        // 循环航线，根据航线中的机场三字码查询每个机场
                        String airportName = flightMapper.getAirportName(split[i]);
                        // 循环航线，根据航线中的机场三字码查询每个机场简称
                        String airportNames = flightMapper.getAirportNamess(split[i]);
                        // 如果i等于0说明是第一次进来直接赋值就行
                        if (i == 0 && !StringUtils.isBlank(airportName)) {
                            flgtVialcName = airportName;
                        }
                        if (i == 0 && StringUtils.isBlank(airportName)) {
                            flgtVialcName = split[i];
                        }
                        // 如果i等于0说明是第一次进来直接赋值就行
                        if (i == 0 && !StringUtils.isBlank(airportNames)) {
                            flgtVialcNames = airportNames;
                        }
                        if (i == 0 && StringUtils.isBlank(airportNames)) {
                            flgtVialcNames = split[i];
                        }
                        if (i > 0 && i < split.length && StringUtils.isBlank(airportName)) {
                            flgtVialcName = flgtVialcName + "-" + split[i];
                        }
                        if (i > 0 && i < split.length && !StringUtils.isBlank(airportName)) {
                            flgtVialcName = flgtVialcName + "-" + airportName;
                        }
                        // 如果i>0并且小于数组的长度的话说明不是第一次进来就开始拼接
                        if (i > 0 && i < split.length && !StringUtils.isBlank(airportNames)) {
                            // 拼接简称
                            flgtVialcNames = flgtVialcNames + "-" + airportNames;
                        }
                        if (i > 0 && i < split.length && StringUtils.isBlank(airportNames)) {
                            // 拼接简称
                            flgtVialcNames = flgtVialcNames + "-" + split[i];
                        }
                    }
                    // 把拼接好的航线全称放进航班对象中
                    dFlight.setFlgtTrsnm3(flgtVialcName);
                    // 把拼接好的航线全称放进航班对象中
                    dFlight.setFlgtTrsnm5(flgtVialcName);
                    // 把拼接好的航线三字码放进航班对象中
                    dFlight.setFlgtTrs3c5(dFlight.getFlgtVialc());
                    // 把拼接完成的航线简称赋值到将要添加的航班中
                    dFlight.setFlgtTrsnm4(flgtVialcNames);
                    dFlight.setFlgtGame(Constant.FLGT_DGAME);
                    //任务下发标识如果等于空就赋默认值0
                    if (dFlight.getFlgtTaskAsign() == null) {
                        dFlight.setFlgtTaskAsign(0);
                    }
                    //航班星标如果等于空就赋默认值0
                    if (dFlight.getFlgtStarmark() == null) {
                        dFlight.setFlgtStarmark(0);
                    }
                    //手动修改航班如果等于空就赋默认值0
                    if (dFlight.getFlgtManualFlg() == null) {
                        dFlight.setFlgtManualFlg(0);
                    }
                    // 在处理 关联航班
                    if (aFlight != null) {
                        //TODO 处理关联航班
                        // 连接航班日期
                        if (!org.springframework.util.StringUtils.isEmpty(aFlight.getFlgtFlop()) && !org.springframework.util.StringUtils.isEmpty(dFlight.getFlgtFlop())) {
                            //对调航班日期
                            dFlight.setFlgtLinkFlop(aFlight.getFlgtFlop());
                            aFlight.setFlgtLinkFlop(dFlight.getFlgtFlop());
                        }

                        //对调 航班号存入 关联航班号
                        dFlight.setFlgtLinkFlno(aFlight.getFlgtFlno());
                        aFlight.setFlgtLinkFlno(dFlight.getFlgtFlno());

                        dFlight.setFlgtLinkFfid(aFlight.getFlgtFfid());
                        aFlight.setFlgtLinkFfid(dFlight.getFlgtFfid());

                        //放统一的数字
                        // 连接航班连接次数
                        dFlight.setFlgtLinkRepeat(maxFlgtRepeat);
                        aFlight.setFlgtLinkRepeat((maxFlgtRepeat + 1));
                        // 航班连接次数
                        dFlight.setFlgtRepeat((maxFlgtRepeat + 1));
                        aFlight.setFlgtRepeat(maxFlgtRepeat);

                        // 获取查出来的航班航线
                        String flgtVialcs = aFlight.getFlgtVialc();
                        // 把航线按照-分割成数组
                        String[] split2 = dFlight.getFlgtVialc().split("-");
                        // for循环航线数组
                        for (int i = 1; i < split2.length; i++) {
                            // 把查出来的航线和传过来的航线进行拼接
                            if (null != flgtVialcs) {
                                flgtVialcs = flgtVialcs + "-" + split2[i];
                            }
                        }
                        if (null != flgtVialcs) {
                            // 把拼接完成的航线三字码赋值到将要添加的航班中
                            dFlight.setFlgtTrs3c5(flgtVialcs);
                            // 航班拼接
                            dFlight = getFlightLine(dFlight, dFlight.getFlgtTrs3c5());
                        }
                        if (flightMapper.addFlight(dFlight) != 1) {
                            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                        }
                        if (flightMapper.addFlight(aFlight) != 1) {
                            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                        }

                    } else {
                        // 把拼接完成的航线三字码赋值到将要添加的航班中
                        dFlight.setFlgtTrs3c5(dFlight.getFlgtVialc());
                        // 航班拼接
                        dFlight = getFlightLine(dFlight, dFlight.getFlgtVialc());
                        if (flightMapper.addFlight(dFlight) != 1) {
                            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                        }
                    }
                } else {
                    dFlight.setFlgtId(flightInfo.getFlgtId());
                    dFlight.setFlgtFfid(flightInfo.getFlgtFfid());
                }
                List<MyFlightTask> tasks = taskMapper.getTaskAndFlightByFlightId(dFlight.getFlgtId());
                if (CollUtil.isEmpty(tasks)) {
                    //创建任务对象为添加任务做准备
                    MyTask task = new MyTask();
                    //生成UUID为任务ID
                    String taskId = UUID.randomUUID().toString();
                    //把生成的UUID赋值到任务对象中的任务ID里
                    task.setTaskId(taskId);
                    //把航班对象中的航班ID赋值到任务对象中的航班ID里
                    task.setTaskFlightId(dFlight.getFlgtId());
                    //把航班对象中的航班号赋值到任务对象中的航班号里
                    task.setTaskFlightNo(dFlight.getFlgtFlno());
                    //把航班对象中的所属机场代码赋值到任务对象中的所属机场代码里
                    task.setTaskAirportCode(dFlight.getFlgtAirportCode());
                    //把航班对象中的所属机场区域代码赋值到任务对象中的所属机场区域代码里
                    task.setTaskAptareaCode(dFlight.getFlgtAptareaCode());
                    //创建人ID赋值到任务对象中的创建人ID里
                    task.setTaskCreStaffId("9999");
                    //如果任务内容等于空的话赋默认值0
                    task.setTaskContent(-1);
                    //如果任务状态等于空的话赋默认值0
                    if (task.getTaskStatus() == null) {
                        task.setTaskStatus(0);
                    }
                    //如果任务星标等于空的话赋默认值0
                    if (task.getTaskStarmark() == null) {
                        task.setTaskStarmark(0);
                    }
                    //获取当前系统时间
                    Date date = new Date();
                    //创建SimpleDateFormat日期格式化对象
                    SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //接收格式化以后的时间
                    String forMatTime = sim.format(date);
                    try {
                        //因为任务对象中的记录创建时间是Date型数据所以要把刚刚格式化的时间转换成Date型数据再赋值进任务对象中的记录创建时间中去
                        task.setTaskRecCreTime(sim.parse(forMatTime));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //添加任务信息,判断如果返回等于1说明添加成功，否则添加失败
                    if (flightMapper.addTask(task) != 1) {
                        throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
                    }
                    //根据任务ID查出单条任务航班信息
                    taskAndFlightById = taskMapper.getTaskAndFlightByIds(task.getTaskId());
                    //把要推送的航班任务对象放进Map集合中
                    webMap.put("flight", taskAndFlightById);
                    //判断如果航班是本场的话再推送一条本场航班消息
                    if (Constant.FLGT_DGAME.equals(taskAndFlightById.getFlgtGame())) {
                        //把要推送的航班任务对象放进Map集合中
                        webMap.put("selfFlight", taskAndFlightById);
                    }
                }
            } finally {
                // 无条件释放锁
                redissonDistributedLocker.unlock(lockKey);
            }

        }
       /* //垮库查询获取调度员ID
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<MyStaff> formEntity = new HttpEntity<MyStaff>(staffInfo, headers);
        //跨库查询后使用人员List接受
        ResponseEntity<List<MyStaff>> rateResponse = restTemplate.exchange(Constant.HTTP + prop.getLoginIp() + ":" + prop.getLoginPort() + "/base/staffController/getStaffLists", HttpMethod.POST, formEntity, new ParameterizedTypeReference<List<MyStaff>>() {
        });
        List<MyStaff> staffList = rateResponse.getBody();
        // for循环遍历查出来的调度员信息集合，因为下面推送消息需要用到调度员ID
        String userId = "";
        int i = 0;
        for (MyStaff myStaff : staffList) {
            if (i == 0) {
                userId = myStaff.getStaffId();
            }
            if (i > 0) {
                userId = userId + "," + myStaff.getStaffId();
            }
            i++;
        }
        SendMsg2Redis.testDingYue(stringRedisTemplate, userId, Constant.TASKFLIGHT, Constant.PC_FLIGHT_ADD, webMap);
*/
        return taskAndFlightById;
    }

    /**
     * 航线拼接
     *
     * @param flight
     * @param flightLine
     * @return
     */
    private MyFlight getFlightLine(MyFlight flight, String flightLine) {

        // 再把拼接好的航线三字码分割成数组
        String[] split3 = flightLine.split("-");
        // 用来存放全称
        String flgtVialcNames = null;
        // 用来存放简称
        String flgtVialcNamess = null;
        // for循环航线数组
        for (int i = 0; i < split3.length; i++) {
            // 循环航线，根据航线中的机场三字码查询每个机场全称
            String airportName = flightMapper.getAirportName(split3[i]);
            // 循环航线，根据航线中的机场三字码查询每个机场简称
            String airportNames = flightMapper.getAirportNamess(split3[i]);
            // 如果i等于0说明是第一次进来直接赋值就行
            if (i == 0 && !StringUtils.isBlank(airportName)) {
                // 赋值全称
                flgtVialcNames = airportName;
            }
            if (i == 0 && StringUtils.isBlank(airportName)) {
                // 赋值全称
                flgtVialcNames = split3[i];
            }
            // 如果i等于0说明是第一次进来直接赋值就行
            if (i == 0 && !StringUtils.isBlank(airportNames)) {
                // 赋值简称
                flgtVialcNamess = airportNames;
            }
            if (i == 0 && StringUtils.isBlank(airportNames)) {
                // 赋值简称
                flgtVialcNamess = split3[i];
            }
            // 如果i>0并且小于数组的长度的话说明不是第一次进来就开始拼接
            if (i > 0 && i < split3.length && !StringUtils.isBlank(airportName)) {
                // 拼接全称
                flgtVialcNames = flgtVialcNames + "-" + airportName;
            }
            if (i > 0 && i < split3.length && StringUtils.isBlank(airportName)) {
                // 拼接全称
                flgtVialcNames = flgtVialcNames + "-" + split3[i];
            }
            // 如果i>0并且小于数组的长度的话说明不是第一次进来就开始拼接
            if (i > 0 && i < split3.length && !StringUtils.isBlank(airportNames)) {
                // 拼接简称
                flgtVialcNamess = flgtVialcNamess + "-" + airportNames;
            }
            if (i > 0 && i < split3.length && StringUtils.isBlank(airportNames)) {
                // 拼接简称
                flgtVialcNamess = flgtVialcNamess + "-" + split3[i];
            }
        }
        // 把拼接完成的航线全称赋值到将要添加的航班中
        flight.setFlgtTrsnm5(flgtVialcNames);
        // 把拼接完成的航线简称赋值到将要添加的航班中
        flight.setFlgtTrsnm4(flgtVialcNamess);

        return flight;

    }


}
