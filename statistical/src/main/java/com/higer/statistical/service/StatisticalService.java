package com.higer.statistical.service;

import com.higer.statistical.entity.flight.TAirlinesCode;
import com.higer.statistical.entity.flight.TFuelRecpt;
import com.higer.statistical.entity.user.TStaff;
import com.higer.statistical.repository.flight.TAirlinesCodeRepository;
import com.higer.statistical.repository.flight.TFuelRecptRepository;
import com.higer.statistical.repository.user.TStaffRepository;
import com.higer.statistical.util.DateUtils;
import com.higer.statistical.util.ResponseObject;
import com.higer.statistical.util.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/24 09:40
 * @Description:
 */
@Service
public class StatisticalService {

    @Autowired
    private TAirlinesCodeRepository tAirlinesCodeRepository;
    @Autowired
    private TFuelRecptRepository tFuelRecptRepository;
    @Autowired
    private TStaffRepository tStaffRepository;

    @PersistenceContext
    private EntityManager entityManagerFlight;

    public ResponseObject<Object> getStatisticalUserMore(List<String> taskOpeStaffId, Integer type, String dateStr) {
        EntityManager entityManager = entityManagerFlight.getEntityManagerFactory().createEntityManager();
        try {
            if (type == null) {
                return ResponseObject.error("type不可为空");
            }
            if (type == 1) {  //按月份
                StringBuffer sql = new StringBuffer();
                sql.append("select \n" +
                        "a.task_ope_staff_id staffId\n" +
                        ",SUM(a.flrc_figuars) sumFiguars\n" +
                        ",COUNT(a.task_ope_staff_id) counts\n" +
                        "from \n" +
                        "(\n" +
                        "select a.*, b.* from  T_TASK a \n" +
                        "LEFT JOIN T_FUEL_RECPT b  on a.task_fuel_recpt_no = b.flrc_no where ");
                if (taskOpeStaffId.size() > 0) {
                    StringBuffer taskOpeStaffIds = new StringBuffer();
                    taskOpeStaffId.forEach(taskOpeStaff -> {
                        taskOpeStaffIds.append(taskOpeStaff + ",");
                    });
                    StringBuffer stringBuffer = taskOpeStaffIds.deleteCharAt(taskOpeStaffIds.length() - 1);
                    sql.append("a.task_ope_staff_id in( " + stringBuffer.toString() + ") and");
                }
                sql.append("\t a.task_status = 7 and  b.flrc_id is not null\n" +
                        ") a\n" +
                        " where a.task_done_time BETWEEN \n");
                if (!StringUtils.isEmpty(dateStr)) {
                    String preMonth = DateUtils.getPreMonth(dateStr);
                    sql.append("\t\tCONCAT(\"" + dateStr + "\",\"-01 00:00:00\")\n" +
                            " and \n" +
                            "DATE_ADD(CONCAT(\"" + preMonth + "\",\"-01 00:00:00\"),INTERVAL 1 MONTH)\n" +
                            "GROUP BY a.task_ope_staff_id ");
                } else {
                    sql.append("\t\tCONCAT(DATE_FORMAT(now(),'%Y-%m'),\"-01 00:00:00\")\n" +
                            " and \n" +
                            "DATE_ADD(CONCAT(DATE_FORMAT(now(),'%Y-%m'),\"-01 00:00:00\"),INTERVAL 1 MONTH)\n" +
                            "GROUP BY a.task_ope_staff_id ");
                }
                System.err.println(sql.toString());
                Query querys = entityManager.createNativeQuery(sql.toString());
                querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List rows = querys.getResultList();
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (Object obj : rows) {
                    Map row = (Map) obj;
                    Object staffId = row.get("staffId");
                    if (!StringUtils.isEmpty(staffId)) {
                        TStaff byStaffId = tStaffRepository.findByStaffId((String) staffId);
                        if (byStaffId != null) {
                            String staffName = byStaffId.getStaffName();
                            row.put("staffName", staffName);
                        }
                    }
                    list.add(row);
                }
                return ResponseObject.success(list, "success");
            } else {  // 按照年
                StringBuffer sql = new StringBuffer();
                sql.append("select \n" +
                        "a.task_ope_staff_id staffId\n" +
                        ",SUM(a.flrc_figuars) sumFiguars\n" +
                        ",COUNT(a.task_ope_staff_id) counts\n" +
                        "from \n" +
                        "(\n" +
                        "select a.*, b.* from  T_TASK a \n" +
                        "LEFT JOIN T_FUEL_RECPT b  on a.task_fuel_recpt_no = b.flrc_no where ");
                if (taskOpeStaffId.size() > 0) {
                    StringBuffer taskOpeStaffIds = new StringBuffer();
                    taskOpeStaffId.forEach(taskOpeStaff -> {
                        taskOpeStaffIds.append(taskOpeStaff + ",");
                    });
                    StringBuffer stringBuffer = taskOpeStaffIds.deleteCharAt(taskOpeStaffIds.length() - 1);
                    sql.append("a.task_ope_staff_id in( " + stringBuffer.toString() + ") and");
                }
                sql.append("\t a.task_status = 7 and  b.flrc_id is not null\n" +
                        ") a\n" +
                        "WHERE a.task_done_time BETWEEN \n");
                if (!StringUtils.isEmpty(dateStr)) {
                    sql.append("\tconcat( \"" + dateStr + "\" ,'-01-01 00:00:00')\n" +
                            "    AND concat( \"" + dateStr + "\" ,'-12-31 23:59:59') \n" +
                            "GROUP BY a.task_ope_staff_id ");
                } else {
                    sql.append("\tconcat( date_format(curdate(), '%Y') ,'-01-01 00:00:00')\n" +
                            "    AND concat( date_format(curdate(), '%Y') ,'-12-31 23:59:59') \n" +
                            "GROUP BY a.task_ope_staff_id ");
                }

                Query querys = entityManager.createNativeQuery(sql.toString());
                querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List rows = querys.getResultList();
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (Object obj : rows) {
                    Map row = (Map) obj;
                    Object staffId = row.get("staffId");
                    if (!StringUtils.isEmpty(staffId)) {
                        TStaff byStaffId = tStaffRepository.findByStaffId((String) staffId);
                        if (byStaffId != null) {
                            String staffName = byStaffId.getStaffName();
                            row.put("staffName", staffName);
                        }
                    }
                    list.add(row);
                }
                EntityManagerFactoryUtils.closeEntityManager(entityManager);
                return ResponseObject.success(list, "success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityManagerFactoryUtils.closeEntityManager(entityManager);
        return ResponseObject.error("error");
    }

    public ResponseObject<Object> test() {
        try {
            Map map = new HashMap();
            List<TFuelRecpt> all = tFuelRecptRepository.findAll();
            List<TStaff> all1 = tStaffRepository.findAll();
            map.put("1", all.size());
            map.put("2", all1.size());
            return ResponseObject.success(map, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseObject.error("error");
    }

    /**
     * @Description: 两组数据做比较 拼接格式 (使用航空公司)
     * @Param: [dateList, dataList]
     * @return: java.util.Map<java.lang.Object                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                               java.lang.Object>
     * @Author: XiuHongXin
     * @Date: 2019/1/25
     */
    private Map<Object, Object> fillCompanyData(List<Map<String, Object>> dateList, List<Map<String, Object>> dataList) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        //循环得到每一个日期
        try {
            dateList.forEach(date -> {
                List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
                Object d = date.get("d");
                dataList.forEach(data -> {
                    if (d.equals(data.get("datas"))) {
                        listData.add(data);
                    }
                });
                if (listData.size() > 0) {
                    map.put(date.get("d"), listData);
                } else {
                    map.put(date.get("d"), "-");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    public ResponseObject<Object> getStatisticalCompany(List<String> flrcAirlCode, Integer type, String dateStr) {
        EntityManager entityManager = entityManagerFlight.getEntityManagerFactory().createEntityManager();
        try {
            if (type == null) {
                return ResponseObject.error("type不可为空");
            }
            if (type == 1) {  //按月份
                StringBuffer sql = new StringBuffer();
                sql.append("select \n" +
                        "a.flrc_airl_code flrcArilCode\n" +
                        ",a.flrc_airl_name flrcArilName\n " +
                        ",SUM(a.flrc_figuars) sumFiguars\n " +
                        ",COUNT(a.flrc_airl_code) counts\n " +
                        "from \n" +
                        "(\n" +
                        "select a.*,b.* from  T_FUEL_RECPT b \n" +
                        "LEFT JOIN T_TASK a  on a.task_fuel_recpt_no = b.flrc_no where  a.task_status = 7 and  a.task_id is not null ");
                if (flrcAirlCode.size() > 0) {
                    StringBuffer flrcAirlCodes = new StringBuffer();
                    flrcAirlCode.forEach(flrcAirlCodeOne -> {
                        flrcAirlCodes.append("\"" + flrcAirlCodeOne + "\"" + ",");
                    });
                    StringBuffer stringBuffer = flrcAirlCodes.deleteCharAt(flrcAirlCodes.length() - 1);
                    sql.append("and b.flrc_airl_name in (" + stringBuffer + ")\n");
                }
                sql.append(") a\n" +
                        " where a.task_done_time BETWEEN \n");
                if (!StringUtils.isEmpty(dateStr)) {
                    String preMonth = DateUtils.getPreMonth(dateStr);
                    sql.append("\t\tCONCAT(" + dateStr + ",\"-01 00:00:00\")\n" +
                            " and \n" +
                            "\tCONCAT(" + preMonth + ",\"-01 00:00:00\")\n" +
                            "GROUP BY a.flrc_airl_code ");
                } else {
                    sql.append("\t\tCONCAT(DATE_FORMAT(now(),'%Y-%m'),\"-01 00:00:00\")\n" +
                            " and \n" +
                            "DATE_ADD(CONCAT(DATE_FORMAT(now(),'%Y-%m'),\"-01 00:00:00\"),INTERVAL 1 MONTH)\n" +
                            "GROUP BY a.flrc_airl_code ");
                }

                System.err.println(sql.toString());
                Query querys = entityManager.createNativeQuery(sql.toString());
                querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List rows = querys.getResultList();
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (Object obj : rows) {
                    Map row = (Map) obj;
                    list.add(row);
                }
                return ResponseObject.success(list, "success");
            } else {  // 按照年
                StringBuffer sql = new StringBuffer();
                sql.append("select \n" +
                        "a.flrc_airl_code flrcArilCode\n" +
                        ",a.flrc_airl_name flrcArilName\n " +
                        ",SUM(a.flrc_figuars) sumFiguars\n" +
                        ",COUNT(a.flrc_airl_code) counts\n" +
                        "from \n" +
                        "(\n" +
                        "select a.*,b.* from  T_FUEL_RECPT b \n" +
                        "LEFT JOIN T_TASK a  on a.task_fuel_recpt_no = b.flrc_no where  a.task_status = 7 and  a.task_id is not null ");

                if (flrcAirlCode.size() > 0) {
                    StringBuffer flrcAirlCodes = new StringBuffer();
                    flrcAirlCode.forEach(flrcAirlCodeOne -> {
                        flrcAirlCodes.append("\"" + flrcAirlCodeOne + "\"" + ",");
                    });
                    StringBuffer stringBuffer = flrcAirlCodes.deleteCharAt(flrcAirlCodes.length() - 1);
                    sql.append("and b.flrc_airl_name in (" + stringBuffer + ")\n");
                }
                sql.append(") a\n" +
                        "WHERE a.task_done_time BETWEEN \n");
                if (!StringUtils.isEmpty(dateStr)) {
                    sql.append("\tconcat( " + dateStr + " ,'-01-01 00:00:00')\n" +
                            "    AND concat( " + dateStr + " ,'-12-31 23:59:59') \n" +
                            "GROUP BY a.flrc_airl_code ");
                } else {
                    sql.append("\tconcat( date_format(curdate(), '%Y') ,'-01-01 00:00:00')\n" +
                            "    AND concat( date_format(curdate(), '%Y') ,'-12-31 23:59:59') \n" +
                            "GROUP BY a.flrc_airl_code ");
                }
                System.err.println(sql.toString());
                Query querys = entityManager.createNativeQuery(sql.toString());
                querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List rows = querys.getResultList();
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (Object obj : rows) {
                    Map row = (Map) obj;
                    list.add(row);
                }
                EntityManagerFactoryUtils.closeEntityManager(entityManager);
                return ResponseObject.success(list, "success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityManagerFactoryUtils.closeEntityManager(entityManager);
        return ResponseObject.error("error");
    }

    /**
     * @Description: 获取导出需要的 加油员数据
     * @Param: [taskOpeStaffId, type]
     * @return: java.util.List<java.util.Map                                                                                                                               <                                                                                                                               java.lang.String                                                                                                                               ,                                                                                                                               java.lang.Object>>
     * @Author: XiuHongXin
     * @Date: 2019/1/26
     */
    public List<Map<String, Object>> exportUserData(String taskOpeStaffId, Integer type) {
        EntityManager entityManager = entityManagerFlight.getEntityManagerFactory().createEntityManager();
        try {
            if (type == 1) {
                StringBuffer sql = new StringBuffer();
                sql.append("select \n" +
                        "a.*\n" +
                        "from \n" +
                        "(\n" +
                        "select  a.task_done_time,b.* from  T_TASK a \n" +
                        "LEFT JOIN T_FUEL_RECPT b  on a.task_fuel_recpt_no = b.flrc_no where ");
                if (!StringUtils.isEmpty(taskOpeStaffId)) {

                    sql.append("a.task_ope_staff_id = " + taskOpeStaffId + "  and  ");
                }
                sql.append(" a.task_status = 7 and  b.flrc_id is not null\n" +
                        ") a\n" +
                        " where a.task_done_time BETWEEN \n" +
                        "\t\tCONCAT(DATE_FORMAT(now(),'%Y-%m'),'-01 00:00:00')\n" +
                        " and \n" +
                        "DATE_ADD(CONCAT(DATE_FORMAT(now(),'%Y-%m'),'-01 00:00:00'),INTERVAL 1 MONTH)");
                System.err.println(sql.toString());
                Query querys = entityManager.createNativeQuery(sql.toString());
                querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List rows = querys.getResultList();
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (Object obj : rows) {
                    Map row = (Map) obj;
                    list.add(row);
                }
                return list;
            } else {
                StringBuffer sql = new StringBuffer();
                sql.append("select \n" +
                        "a.*\n" +
                        "from \n" +
                        "(\n" +
                        "select  a.task_done_time,b.* from  T_TASK a \n" +
                        "LEFT JOIN T_FUEL_RECPT b  on a.task_fuel_recpt_no = b.flrc_no where ");
                if (!StringUtils.isEmpty(taskOpeStaffId)) {

                    sql.append("a.task_ope_staff_id = " + taskOpeStaffId + " and");
                }
                sql.append(" a.task_status = 7 and  b.flrc_id is not null\n" +
                        ") a\n" +
                        "WHERE a.task_done_time BETWEEN \n" +
                        "\tconcat( date_format(curdate(), '%Y') ,'-01-01 00:00:00')\n" +
                        "    AND concat( date_format(curdate(), '%Y') ,'-12-31 23:59:59') \n");
                System.err.println(sql.toString());
                Query querys = entityManager.createNativeQuery(sql.toString());
                querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List rows = querys.getResultList();
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (Object obj : rows) {
                    Map row = (Map) obj;
                    list.add(row);
                }
                EntityManagerFactoryUtils.closeEntityManager(entityManager);
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityManagerFactoryUtils.closeEntityManager(entityManager);
        return null;
    }

    /**
     * @Description: 获取导出需要的航空公司数据`
     * @Param: [taskOpeStaffId, type]
     * @return: java.util.List<java.util.Map                                                                                                                               <                                                                                                                               java.lang.String                                                                                                                               ,                                                                                                                               java.lang.Object>>
     * @Author: XiuHongXin
     * @Date: 2019/1/26
     */
    public List<Map<String, Object>> exportCompanyData(String flrcAirlCode, Integer type) {
        EntityManager entityManager = entityManagerFlight.getEntityManagerFactory().createEntityManager();
        try {
            if (type == 1) {
                StringBuffer sql = new StringBuffer();
                sql.append("select \n" +
                        "a.*\n" +
                        " from \n" +
                        "(\n" +
                        "select a.task_done_time,b.* from  T_FUEL_RECPT b \n" +
                        "LEFT JOIN T_TASK a  on a.task_fuel_recpt_no = b.flrc_no where  a.task_status = 7 and  a.task_id is not null ");
                if (!StringUtils.isEmpty(flrcAirlCode)) {
                    sql.append("and b.flrc_airl_name = \"" + flrcAirlCode + "\"\n");
                }
                sql.append(") a\n" +
                        " where a.task_done_time BETWEEN \n" +
                        "\t\tCONCAT(DATE_FORMAT(now(),'%Y-%m'),\"-01 00:00:00\")\n" +
                        " and \n" +
                        "DATE_ADD(CONCAT(DATE_FORMAT(now(),'%Y-%m'),\"-01 00:00:00\"),INTERVAL 1 MONTH)");
                Query querys = entityManager.createNativeQuery(sql.toString());
                querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List rows = querys.getResultList();
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (Object obj : rows) {
                    Map row = (Map) obj;
                    list.add(row);
                }
                return list;
            } else {
                StringBuffer sql = new StringBuffer();
                sql.append("select \n" +
                        "a.*\n" +
                        " from \n" +
                        "(\n" +
                        "select a.task_done_time,b.* from  T_FUEL_RECPT b \n" +
                        "LEFT JOIN T_TASK a  on a.task_fuel_recpt_no = b.flrc_no where  a.task_status = 7 and  a.task_id is not null ");
                if (!StringUtils.isEmpty(flrcAirlCode)) {
                    sql.append("and b.flrc_airl_name = \"" + flrcAirlCode + "\"\n");
                }
                sql.append(") a\n" +
                        "WHERE a.task_done_time BETWEEN \n" +
                        "\tconcat( date_format(curdate(), '%Y') ,'-01-01 00:00:00')\n" +
                        "    AND concat( date_format(curdate(), '%Y') ,'-12-31 23:59:59') \n");
                Query querys = entityManager.createNativeQuery(sql.toString());
                querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List rows = querys.getResultList();
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (Object obj : rows) {
                    Map row = (Map) obj;
                    list.add(row);
                }
                EntityManagerFactoryUtils.closeEntityManager(entityManager);
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityManagerFactoryUtils.closeEntityManager(entityManager);
        return null;
    }

    public ResponseObject<Object> getStatisticalUserOne(String taskOpeStaffId, Integer type, String dateStr) {
        EntityManager entityManager = entityManagerFlight.getEntityManagerFactory().createEntityManager();
        try {
            if (type == null || StringUtils.isEmpty(taskOpeStaffId)) {
                return ResponseObject.error("type 或者 taskOpeStaffId 不可为空");
            }
            String sql = joiningTogetherUserSql(taskOpeStaffId, type, dateStr);
            System.err.println(sql);
            Query querys = entityManager.createNativeQuery(sql);
            querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List rows = querys.getResultList();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            String staffName = "";
            Map<String, Object> map = new HashMap<String, Object>();
            if (rows.size() > 0) {
                Map row = (Map) rows.get(0);
                Object staffId = row.get("staffId");
                if (!StringUtils.isEmpty(staffId)) {
                    map.put("staffId", staffId);
                    TStaff byStaffId = tStaffRepository.findByStaffId((String) staffId);
                    if (byStaffId != null) {
                        staffName = byStaffId.getStaffName();
                    }
                }
            }
            map.put("staffName", staffName);
            for (Object obj : rows) {
                Map row = (Map) obj;
                list.add(row);
            }
            map.put("datas", list);
            EntityManagerFactoryUtils.closeEntityManager(entityManager);
            return ResponseObject.success(map, "success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityManagerFactoryUtils.closeEntityManager(entityManager);
        return ResponseObject.error("error");

    }

    /**
     * @Description: 加油员 动态拼接sql
     * @Param: [id, type, dateStr]
     * @return: java.lang.String
     * @Author: XiuHongXin
     * @Date: 2019/1/26
     */
    private String joiningTogetherUserSql(String id, Integer type, String dateStr) {
        String preMonth = DateUtils.getPreMonth(dateStr);
        StringBuffer sql = new StringBuffer();
        sql.append("select\n" +
                " a.d date,\n" +
                "IFNULL(b.counts,\"-\") counts,\n" +
                "IFNULL(b.staffId,\"" + id + "\") staffId,\n" +
                "IFNULL(b.sumFiguars,\"-\") sumFiguars\n" +
                " from (");
        if (type == 1) { // 月
            sql.append("SELECT\n" +
                    "\t(\n" +
                    "\t\t`y`.`FIRST` + INTERVAL (`x`.`d` - 1) DAY\n" +
                    "\t) AS `d`\n" +
                    "FROM\n" +
                    "\t(\n" +
                    "\t\t(\n" +
                    "\t\t\t(SELECT 1 AS `d`) UNION ALL SELECT 2 AS `2` UNION ALL SELECT 3 AS `3` UNION ALL\n" +
                    "\t\t\t\t\t\tSELECT\t4 AS `4` UNION ALL\tSELECT\t5 AS `5`\tUNION ALL\tSELECT\t6 AS `6` UNION ALL SELECT\t7 AS `7` UNION ALL  SELECT\t8 AS `8` UNION ALL SELECT\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t9 AS `9`\tUNION ALL\tSELECT\t10 AS `10`\tUNION ALL\tSELECT 11 AS `11`\tUNION ALL\tSELECT 12 AS `12`\tUNION ALL\tSELECT\t13 AS `13` UNION ALL\tSELECT\t14 AS `14`\tUNION ALL\tSELECT\t15 AS `15`\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tUNION ALL\tSELECT\t16 AS `16` UNION ALL SELECT\t17 AS `17` UNION ALL SELECT\t18 AS `18`\tUNION ALL\tSELECT\t19 AS `19` UNION ALL\tSELECT\t20 AS `20`\tUNION ALL\tSELECT\t21 AS `21` UNION ALL SELECT\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t22 AS `22`\tUNION ALL\tSELECT\t23 AS `23` UNION ALL SELECT\t24 AS `24`\tUNION ALL\tSELECT\t25 AS `25` UNION ALL\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tSELECT 26 AS `26`\tUNION ALL\tSELECT 27 AS `27`\tUNION ALL\tSELECT\t28 AS `28`\tUNION ALL\tSELECT\t29 AS `29` UNION ALL SELECT 30 AS `30` UNION ALL\tSELECT 31 AS `31`\n" +
                    "\t\t) `x`\n" +
                    "\t\tJOIN (\n" +
                    "\t\t\tSELECT\n" +
                    "\t\t\t\tconcat(\"" + dateStr + "\",'-01') AS `FIRST`,\n" +
                    "\t\t\t\tdayofmonth(last_day(concat(\"" + dateStr + "\",'-01'))\n" +
                    "\t\t\t\t) AS `LAST`\n" +
                    "\t\t) `y`\n" +
                    "\t)\n" +
                    "WHERE\n" +
                    "\t(`x`.`d` <= `y`.`LAST`) ");
        } else { // 年
            sql.append("select concat(\"" + dateStr + "\",'-01') AS `d` union all select concat(\"" + dateStr + "\",'-02') AS `mon` union all select concat(\"" + dateStr + "\",'-03') AS `mon` union all select concat(\"" + dateStr + "\",'-04') AS `mon` union all select concat(\"" + dateStr + "\",'-05') AS `mon` union all select concat(\"" + dateStr + "\",'-06') AS `mon` union all select concat(\"" + dateStr + "\",'-07') AS `mon` union all select concat(\"" + dateStr + "\",'-08') AS `mon` union all select concat(\"" + dateStr + "\",'-09') AS `mon` union all select concat(\"" + dateStr + "\",'-10') AS `mon` union all select concat(\"" + dateStr + "\",'-11') AS `mon` union all select concat(\"" + dateStr + "\",'-12') AS `mon`");
        }
        sql.append(") a LEFT JOIN (\n" +
                "select \n");
        //拼接时间
        if (type == 1) {
            sql.append("DATE_FORMAT(a.task_done_time,'%Y-%m-%d') datas \n");
        } else {
            sql.append("DATE_FORMAT(a.task_done_time,'%Y-%m') datas \n");
        }
        sql.append(",a.task_ope_staff_id staffId\n" +
                ",SUM(a.flrc_figuars) sumFiguars\n" +
                ",COUNT(a.task_ope_staff_id) counts\n" +
                "from \n" +
                "(\n" +
                "select a.*, b.* from  T_TASK a \n" +
                "LEFT JOIN T_FUEL_RECPT b  on a.task_fuel_recpt_no = b.flrc_no where a.task_ope_staff_id = " + id + " and a.task_status = 7 and  b.flrc_id is not null\n" +
                ") a\n" +
                " where a.task_done_time BETWEEN \n");
        if (type == 1) {
            if (!StringUtils.isEmpty(dateStr)) {
                sql.append("\t\tCONCAT(\"" + dateStr + "\",\"-01 00:00:00\")\n" +
                        " and \n" +
                        "DATE_ADD(CONCAT(\"" + preMonth + "\",\"-01 00:00:00\"),INTERVAL 1 MONTH)\n" +
                        "GROUP BY DATE_FORMAT(a.task_done_time,'%Y-%m-%d'),a.task_ope_staff_id ");
            } else {
                sql.append("\t\tCONCAT(DATE_FORMAT(now(),'%Y-%m'),\"-01 00:00:00\")\n" +
                        " and \n" +
                        "DATE_ADD(CONCAT(DATE_FORMAT(now(),'%Y-%m'),\"-01 00:00:00\"),INTERVAL 1 MONTH)\n" +
                        "GROUP BY DATE_FORMAT(a.task_done_time,'%Y-%m-%d'),a.task_ope_staff_id ");
            }
        } else {
            if (!StringUtils.isEmpty(dateStr)) {
                sql.append("\tconcat( \"" + dateStr + "\" ,'-01-01 00:00:00')\n" +
                        "    AND concat( \"" + dateStr + "\" ,'-12-31 23:59:59') \n" +
                        "GROUP BY DATE_FORMAT(a.task_done_time,'%Y-%m'),a.task_ope_staff_id ");
            } else {
                sql.append("\tconcat( date_format(curdate(), '%Y') ,'-01-01 00:00:00')\n" +
                        "    AND concat( date_format(curdate(), '%Y') ,'-12-31 23:59:59') \n" +
                        "GROUP BY DATE_FORMAT(a.task_done_time,'%Y-%m'),a.task_ope_staff_id ");
            }
        }
        sql.append("  ) b on a.d = b.datas  ");
        return sql.toString();
    }

    /**
     * @Description: 航空公司 动态拼接sql
     * @Param: [id, type, dateStr]
     * @return: java.lang.String
     * @Author: XiuHongXin
     * @Date: 2019/1/26
     */
    private String joiningTogetherCompanySql(String name, Integer type, String dateStr) {
        TAirlinesCode byFlrcAirlCode = tAirlinesCodeRepository.findByAlcdArlnName(name);
        String id = "";
        if (byFlrcAirlCode != null && !StringUtils.isEmpty(byFlrcAirlCode.getAlcdIcaoCode())) {
            id = byFlrcAirlCode.getAlcdIcaoCode();
        } else {
            return "";
        }
        String preMonth = DateUtils.getPreMonth(dateStr);
        StringBuffer sql = new StringBuffer();
        sql.append("select\n" +
                " a.d date,\n" +
                "IFNULL(b.counts,\"-\") counts,\n" +
                "IFNULL(b.flrcAirlCode,\"" + id + "\") flrcAirlCode,\n" +
                "IFNULL(b.flrcAirlName,\"" + name + "\") flrcAirlName,\n" +
                "IFNULL(b.sumFiguars,\"-\") sumFiguars\n" +
                " from (");
        if (type == 1) { // 月
            sql.append("SELECT\n" +
                    "\t(\n" +
                    "\t\t`y`.`FIRST` + INTERVAL (`x`.`d` - 1) DAY\n" +
                    "\t) AS `d`\n" +
                    "FROM\n" +
                    "\t(\n" +
                    "\t\t(\n" +
                    "\t\t\t(SELECT 1 AS `d`) UNION ALL SELECT 2 AS `2` UNION ALL SELECT 3 AS `3` UNION ALL\n" +
                    "\t\t\t\t\t\tSELECT\t4 AS `4` UNION ALL\tSELECT\t5 AS `5`\tUNION ALL\tSELECT\t6 AS `6` UNION ALL SELECT\t7 AS `7` UNION ALL  SELECT\t8 AS `8` UNION ALL SELECT\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t9 AS `9`\tUNION ALL\tSELECT\t10 AS `10`\tUNION ALL\tSELECT 11 AS `11`\tUNION ALL\tSELECT 12 AS `12`\tUNION ALL\tSELECT\t13 AS `13` UNION ALL\tSELECT\t14 AS `14`\tUNION ALL\tSELECT\t15 AS `15`\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tUNION ALL\tSELECT\t16 AS `16` UNION ALL SELECT\t17 AS `17` UNION ALL SELECT\t18 AS `18`\tUNION ALL\tSELECT\t19 AS `19` UNION ALL\tSELECT\t20 AS `20`\tUNION ALL\tSELECT\t21 AS `21` UNION ALL SELECT\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t22 AS `22`\tUNION ALL\tSELECT\t23 AS `23` UNION ALL SELECT\t24 AS `24`\tUNION ALL\tSELECT\t25 AS `25` UNION ALL\n" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tSELECT 26 AS `26`\tUNION ALL\tSELECT 27 AS `27`\tUNION ALL\tSELECT\t28 AS `28`\tUNION ALL\tSELECT\t29 AS `29` UNION ALL SELECT 30 AS `30` UNION ALL\tSELECT 31 AS `31`\n" +
                    "\t\t) `x`\n" +
                    "\t\tJOIN (\n" +
                    "\t\t\tSELECT\n" +
                    "\t\t\t\tconcat(\"" + dateStr + "\",'-01') AS `FIRST`,\n" +
                    "\t\t\t\tdayofmonth(last_day(concat(\"" + dateStr + "\",'-01'))\n" +
                    "\t\t\t\t) AS `LAST`\n" +
                    "\t\t) `y`\n" +
                    "\t)\n" +
                    "WHERE\n" +
                    "\t(`x`.`d` <= `y`.`LAST`) ");
        } else { // 年
            sql.append("select concat(\"" + dateStr + "\",'-01') AS `d` union all select concat(\"" + dateStr + "\",'-02') AS `mon` union all select concat(\"" + dateStr + "\",'-03') AS `mon` union all select concat(\"" + dateStr + "\",'-04') AS `mon` union all select concat(\"" + dateStr + "\",'-05') AS `mon` union all select concat(\"" + dateStr + "\",'-06') AS `mon` union all select concat(\"" + dateStr + "\",'-07') AS `mon` union all select concat(\"" + dateStr + "\",'-08') AS `mon` union all select concat(\"" + dateStr + "\",'-09') AS `mon` union all select concat(\"" + dateStr + "\",'-10') AS `mon` union all select concat(\"" + dateStr + "\",'-11') AS `mon` union all select concat(\"" + dateStr + "\",'-12') AS `mon`");
        }
        sql.append(") a LEFT JOIN (\n" +
                "select \n");
        //拼接时间
        if (type == 1) {
            sql.append("DATE_FORMAT(a.task_done_time,'%Y-%m-%d') datas \n");
        } else {
            sql.append("DATE_FORMAT(a.task_done_time,'%Y-%m') datas \n");
        }
        sql.append(",a.flrc_airl_code flrcAirlCode,a.flrc_airl_name flrcAirlName\n" +
                ",SUM(a.flrc_figuars) sumFiguars\n" +
                ",COUNT(a.flrc_airl_code) counts\n" +
                "from \n" +
                "(\n" +
                "select a.*, b.* from  T_TASK a \n" +
                "LEFT JOIN T_FUEL_RECPT b  on a.task_fuel_recpt_no = b.flrc_no where b.flrc_airl_code = \"" + id + "\" and a.task_status = 7 and  b.flrc_id is not null\n" +
                ") a\n" +
                " where a.task_done_time BETWEEN \n");
        if (type == 1) {
            if (!StringUtils.isEmpty(dateStr)) {
                sql.append("\t\tCONCAT(\"" + dateStr + "\",\"-01 00:00:00\")\n" +
                        " and \n" +
                        "DATE_ADD(CONCAT(\"" + preMonth + "\",\"-01 00:00:00\"),INTERVAL 1 MONTH)\n" +
                        "GROUP BY DATE_FORMAT(a.task_done_time,'%Y-%m-%d'),a.flrc_airl_code ,a.flrc_airl_name");
            } else {
                sql.append("\t\tCONCAT(DATE_FORMAT(now(),'%Y-%m'),\"-01 00:00:00\")\n" +
                        " and \n" +
                        "DATE_ADD(CONCAT(DATE_FORMAT(now(),'%Y-%m'),\"-01 00:00:00\"),INTERVAL 1 MONTH)\n" +
                        "GROUP BY DATE_FORMAT(a.task_done_time,'%Y-%m-%d'),a.flrc_airl_code ,a.flrc_airl_name");
            }
        } else {
            if (!StringUtils.isEmpty(dateStr)) {
                sql.append("\tconcat( \"" + dateStr + "\" ,'-01-01 00:00:00')\n" +
                        "    AND concat( \"" + dateStr + "\" ,'-12-31 23:59:59') \n" +
                        "GROUP BY DATE_FORMAT(a.task_done_time,'%Y-%m'),a.flrc_airl_code ,a.flrc_airl_name");
            } else {
                sql.append("\tconcat( date_format(curdate(), '%Y') ,'-01-01 00:00:00')\n" +
                        "    AND concat( date_format(curdate(), '%Y') ,'-12-31 23:59:59') \n" +
                        "GROUP BY DATE_FORMAT(a.task_done_time,'%Y-%m'),a.flrc_airl_code ,a.flrc_airl_name");
            }
        }
        sql.append("  ) b on a.d = b.datas  ");
        return sql.toString();
    }

    public ResponseObject<Object> getStatisticalCompanyOne(String flrcAirlCode, Integer type, String dateStr) {
        EntityManager entityManager = entityManagerFlight.getEntityManagerFactory().createEntityManager();
        try {
            if (type == null || StringUtils.isEmpty(flrcAirlCode)) {
                return ResponseObject.error("type 或者 flrcAirlName 不可为空");
            }
            String sql = joiningTogetherCompanySql(flrcAirlCode, type, dateStr);
            System.err.println(sql);
            if (StringUtils.isEmpty(sql)) {
                return new ResponseObject<Object>().error("flrcAirlName 数据不完整");
            }
            Query querys = entityManager.createNativeQuery(sql);
            querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List rows = querys.getResultList();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = new HashMap<String, Object>();
            //map.put("flrcAirlCode", flrcAirlCode);
            if (rows.size() > 0) {
                Map row = (Map) rows.get(0);
                Object flrcAirlName = row.get("flrcAirlName");
                if (!StringUtils.isEmpty(flrcAirlName)) {
                    map.put("flrcAirlName", flrcAirlName);
                } else {
                    map.put("flrcAirlName", "");
                }
            }
            for (Object obj : rows) {
                Map row = (Map) obj;
                list.add(row);
            }
            map.put("datas", list);
            EntityManagerFactoryUtils.closeEntityManager(entityManager);
            return ResponseObject.success(map, "success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityManagerFactoryUtils.closeEntityManager(entityManager);
        return ResponseObject.error("error");
    }

    public ResponseObject<Object> getStatisticalCompanyMoreDay(String date) {
        EntityManager entityManager = entityManagerFlight.getEntityManagerFactory().createEntityManager();
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("select \n" +
                    "a.flrc_airl_code flrcArilCode\n" +
                    ",a.flrc_airl_name flrcArilName\n " +
                    ",SUM(a.flrc_figuars) sumFiguars\n " +
                    ",COUNT(a.flrc_airl_code) counts\n " +
                    "from \n" +
                    "(\n" +
                    "select a.*,b.* from  T_FUEL_RECPT b \n" +
                    "LEFT JOIN T_TASK a  on a.task_fuel_recpt_no = b.flrc_no where  a.task_status = 7 and  a.task_id is not null ");
            sql.append(") a\n" +
                    " where ");
            if (!StringUtils.isEmpty(date)) {
                sql.append("DATE_FORMAT(a.task_done_time,'%Y-%m-%d') = \"" + date + "\" ");
            } else {
                sql.append("DATE_FORMAT(a.task_done_time,'%Y-%m-%d') = curdate() ");
            }
            sql.append("GROUP BY a.flrc_airl_code ");
            System.err.println(sql.toString());
            Query querys = entityManagerFlight.createNativeQuery(sql.toString());
            querys.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List rows = querys.getResultList();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (Object obj : rows) {
                Map row = (Map) obj;
                list.add(row);
            }
            EntityManagerFactoryUtils.closeEntityManager(entityManager);
            return ResponseObject.success(list, "success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityManagerFactoryUtils.closeEntityManager(entityManager);
        return ResponseObject.error("error");
    }
}
