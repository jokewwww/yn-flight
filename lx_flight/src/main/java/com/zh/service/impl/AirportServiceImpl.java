package com.zh.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zh.bean.ReturnMsg;
import com.zh.bean.flight.MyAirportCode;
import com.zh.constant.StatusConstant;
import com.zh.dao.mapper.my.AirportCodeMapper;
import com.zh.exception.CustomException;
import com.zh.service.AirportCodeService;
import com.zh.util.PinyinUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class AirportServiceImpl implements AirportCodeService {

    private static final String WORDS = "abcdefghijklmnopqrstuvwxyz";

    @Autowired
    private AirportCodeMapper airportCodeMapper;

    /**
     * 自定义流去重
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    /**
     * 增加机场
     *
     * @param airportCode
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public Integer insertAirportCode(MyAirportCode airportCode) {
        //添加前先根据机场三字码查询是否存在此信息，如果存在就不添加，不存在才添加
        MyAirportCode airportInfo = airportCodeMapper.selectAirportCodeFind(airportCode.getApcdIataCode());
        if (airportInfo == null) {
            if (airportCodeMapper.insertAirportCode(airportCode) != 1) {
                throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.INSERT_FALSE, null));
            }
            //如果已添加返回0
            return 0;
        } else {
            //如果已存在返回1
            return 1;
        }
    }

    /**
     * 查询机场
     */
    @Override
    public List<MyAirportCode> selectAirportCode() {
        return airportCodeMapper.selectAirportCode();
    }

    /**
     * 级联查询机场
     *
     * @return
     */
    @Override
    public Map<String, Map<String, Map<String, Object>>> selectAirportCodeCascade() {
        List<MyAirportCode> airportCode = airportCodeMapper.selectAirportCode();
        Map result = new HashMap<>();
        Map<String, Map<Character, Map<String, Map<String, Map<String, Object>>>>> collect = airportCode.stream()
                .collect(
                        Collectors.groupingBy(//按照国内外机场分组
                                MyAirportCode::getApcdAirportProp,
                                Collectors.collectingAndThen(
                                        Collectors.groupingBy(//按照拼音首字母分组
                                                airport -> PinyinUtils.getPinyinFirstWord(airport.getApcdAirportNameS()),
                                                Collectors.groupingBy(//按照简称分组
                                                        MyAirportCode::getApcdAirportNameS,
                                                        Collectors.toMap(//List->Map
                                                                MyAirportCode::getApcdIataCode,
                                                                this::myAirportCode2Map
                                                        )
                                                )
                                        ),
                                        map -> {//补充缺少的拼音首字母
                                            for (int i = 0; i < WORDS.length(); i++) {
                                                boolean b = map.containsKey(WORDS.charAt(i));
                                                if (!b) {
                                                    map.put(WORDS.charAt(i), Maps.newHashMap());
                                                }
                                            }
                                            return map;
                                        }
                                )
                        )
                );


//		System.out.println(JSONObject.toJSONString(collect));

        collect.forEach((key1, value1) -> {
            List<Map<String, Map<String, Map<String, Object>>>> list = Lists.newArrayList(value1.values());
            value1.forEach((k, v) -> {
                int num = StringUtils.equals(key1, "D") ? 100000 : StringUtils.equals(key1, "I") ? 400000 : 800000;
                HashMap<Object, Object> map = Maps.newHashMap();
                List<Map<String, Map<String, Object>>> _list = Lists.newArrayList(v.values());
                int xh = num + (list.indexOf(v) + 1) * 10000;
                v.forEach((_k, _v) -> {
                    map.put(xh + (_list.indexOf(_v) + 1) * 100 + "", getMap(_k, PinyinUtils.getPinyinFirstWord(_k) + ""));
                    result.put(xh + (_list.indexOf(_v) + 1) * 100 + "", _v);
                });
                result.put(xh + "", map);
            });
        });

        return result;
    }

    @Override
    public List<Map<String, Object>> selectAirportCodeCascadeAndroid() {
        List<MyAirportCode> airportCode = airportCodeMapper.selectAirportCode();
        return airportCode.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.groupingBy(
                                MyAirportCode::getApcdAirportProp,
                                Collectors.groupingBy(
                                        airport -> PinyinUtils.getPinyinToUpperCase(airport.getApcdAirportName()),
                                        Collectors.groupingBy(MyAirportCode::getApcdAirportNameS)
                                )
                        ),
                        result -> {
                            List<Map<String, Object>> res = Lists.newArrayList();
                            result.forEach((k, v) -> {
                                v.forEach((_k, _v) -> {
                                    String name = StringUtils.equals("D", k) ? "国内" : StringUtils.equals("I", k) ? "国际" : "混合";
                                    List<Object> list = Lists.newArrayList();
                                    _v.forEach((__k, __v) -> {
                                        list.add(getMap(__k, __v));
                                    });
                                    Map<String, Object> map = getMap(name + _k, list);
                                    res.add(map);
                                });
                            });
                            return res;
                        }
                )
        );
    }

    private String concatKey(MyAirportCode airportCode) {
        String airportPort = airportCode.getApcdAirportProp();
        String key = StringUtils.equals("D", airportPort) ? "国内" : StringUtils.equals("I", airportPort) ? "国外" : "混合";
        key += PinyinUtils.getPinyinFirstWord(airportCode.getApcdAirportNameS());
        return key;
    }

    private Map<String, Object> getMap(String name, Object value) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("name", name);
        map.put("value", value);
        return map;
    }


    private Map<String, Object> myAirportCode2Map(MyAirportCode myAirportCode) {
        Map<String, Object> _map = new HashMap<>();
        _map.put("name", myAirportCode.getApcdAirportName());
        _map.put("alpha", PinyinUtils.getPinyinFirstWord(myAirportCode.getApcdAirportNameS()));
        return _map;
    }


    /**
     * 修改机场
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void updateAirportCode(MyAirportCode airportCode) {
        if (airportCodeMapper.updateAirportCode(airportCode) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 删除机场
     */
    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void deleteAirportCode(MyAirportCode airportCode) {
        if (airportCodeMapper.deleteAirportCode(airportCode.getApcdIataCode(), airportCode.getApcdIcaoCode()) != 1) {
            throw new CustomException(ReturnMsg.getInstanceNGz(StatusConstant.UPDATE_FALSE, null));
        }
    }

    /**
     * 查询机场详情
     */
    @Override
    public MyAirportCode selectAirportCodeFind(MyAirportCode airportCode) {
        return airportCodeMapper.selectAirportCodeFind(airportCode.getApcdIataCode());
    }

    /**
     * 查询机场
     */
    @Override
    public List<MyAirportCode> selectAirportCodePropD() {
        return airportCodeMapper.selectAirportCodePropD();
    }

}
