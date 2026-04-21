package com.higer.oildataexchange.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.higer.oildataexchange.common.Constant;
import com.higer.oildataexchange.common.ModelAssistant;
import com.higer.oildataexchange.common.XmlUtils;
import com.higer.oildataexchange.entity.R;
import com.higer.oildataexchange.entity.acdm.FligtVo;
import com.higer.oildataexchange.entity.oil.TFuelRecpt;
import com.higer.oildataexchange.entity.progressNodes.TaskNodes_R;
import com.higer.oildataexchange.entity.progressNodes.TaskReq;
import com.higer.oildataexchange.repository.TFuelRecptRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 推送进度节点
 */
@Service
@Slf4j
public class PushProgressNodesConsumer {

    @Autowired
    private SendHttpService httpService;

    @Value(value = "${pushProgressNodesUrl}")
    private String pushProgressNodesUrl;

    @Autowired
    private TFuelRecptRepository tFuelRecptRepository;

    //    @KafkaListener(topics = {"${push.progress.nodes.topic}"})
    // @KafkaListener(topics =  {"${task_change_log}"})
    public void listener(ConsumerRecord<String, String> record, Acknowledgment ack) {

        try {
            log.info("任务节点推送接收kafka数据;" + record.value());

//            TaskReq taskReq = JSONObject.parseObject(record.value(), TaskReq.class);
            FligtVo fligtVo = JSONObject.parseObject(record.value(), FligtVo.class);
            // 拿到油单信息     TFuelRecpt fuelRecpt = tFuelRecptRepository.findByTaskId(fligtVo.getTaskId());
            TFuelRecpt fuelRecpt = null;

            TaskReq taskReq = new TaskReq();
            taskReq.setTNB(fligtVo.getTaskId());
            taskReq.setPCID(fligtVo.getTaskId());
            taskReq.setFLOP(DateUtil.format(fligtVo.getFlgtFlop(), "YYYYMMDD"));
            taskReq.setADID(fligtVo.getFlgtAdid());
            // todo 有两个航班唯一号
            taskReq.setFKEY(fligtVo.getFlgtFlno());
            taskReq.setFNKEY(fligtVo.getFlgtFlno());

            taskReq.setREGN(fligtVo.getFlgtRegn());

            taskReq.setAC3C(fuelRecpt.getFlrcAircrftType());
            // todo 机型名称
            taskReq.setACNAME("");

            // todo 必须机场3码
            taskReq.setAP3C("");
            taskReq.setAL2C(fligtVo.getFlgtAl2c());
            // todo 航司中文名
            taskReq.setALCNAME("");
            taskReq.setPLACECODE(fuelRecpt.getFlgtPlacecode());
            taskReq.setBILLNUMBER(fuelRecpt.getFlrcNo());
            // todo 派发时间
            taskReq.setTDWN(new Date());
            // todo 收到
            taskReq.setTGET(new Date());
            // todo 接受
            taskReq.setTACP(new Date());
            // todo 到位
            taskReq.setTARV(new Date());
            // todo 开始
            taskReq.setTBEG(new Date());
            // todo 打印
            taskReq.setTPRT(new Date());
            // todo 结束
            taskReq.setTEND(new Date());
            // todo 取消
            taskReq.setTCAL(new Date());
            // todo 拒绝
            taskReq.setTDEC(new Date());
            // todo 暂停
            taskReq.setTSTO(new Date());

            // todo 服务类型
            taskReq.setRTYP("");

            // todo 服务类型名称
            taskReq.setRTNM("");

            // todo
            /**
             * 必须，任务状态/任务节点类型：
             * TACP     接受（加油员接受任务）
             * TARV     到位（加油车到达机位）
             * TBEG     开始（加油开始）
             * TPRT     打印（加油完毕，打印油单）
             * TEND    结束（任务结束，加油员撤离）
             * TCAL     取消（任务被取消）
             * TDEC     拒绝（加油员拒绝任务）
             */
            taskReq.setTTST("");

            // todo 计划时间
            taskReq.setSTOT(new Date());
            // todo 变更（预计）时间
            taskReq.setETOT(new Date());
            // todo 实际时间
            taskReq.setATOT(new Date());
            taskReq.setVNB(fuelRecpt.getFlrcVehiNo());


            TaskReq logTaskReq = new TaskReq();
            //copy 用于输出日志 忽略efs太长了
            ModelAssistant.copyProperties(taskReq, logTaskReq);

            R<TaskReq> taskReqr = R.newInstanceR("CNAF", "CZ", taskReq);
            R<TaskReq> logTaskReqr = R.newInstanceR("CNAF", "CZ", logTaskReq);

            log.info("任务节点推送数据参数：" + JSON.toJSONString(logTaskReqr));

            String xml = XmlUtils.convertToXml(taskReqr, Constant.CHARSET, true);

            String s = httpService.sendHttpPost(pushProgressNodesUrl, xml);
            log.info("任务节点推送数据，返回信息:{}", s);
            Document document = DocumentHelper.parseText(s);
            Element rootElement = document.getRootElement();
            Element bd = rootElement.element("BD");
            TaskNodes_R taskNodesR = XmlUtils.convertToObj(bd.asXML(), TaskNodes_R.class);
            //0未上传  1 上传失败 2 上传成功
            if (taskNodesR != null) {
                if (StringUtils.equals(taskNodesR.getPTST(), "S")) {//成功
                    log.info("任务节点推送数据成功：{}", taskNodesR.getTNB());
                } else {
                    log.error("任务节点推送数据失败：{}", taskNodesR.getTNB());
                }
            } else {
                log.error("任务节点推送数据失败：{}", taskReq.getTNB());
            }

            ack.acknowledge();

        } catch (Exception e) {
            log.error("任务节点推送数据异常错误：{}", e.getMessage());
            e.printStackTrace();
            log.error("XML解析错误", e);
            ack.acknowledge();
        }
    }

}
