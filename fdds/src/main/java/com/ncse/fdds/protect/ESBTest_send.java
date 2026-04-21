package com.ncse.fdds.protect;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author dy
 */
@Slf4j
public class ESBTest_send {
    private static MQQueueManager qMgr;

    private static String QMSend = "QMIN1";

    private static String QSend = "HY_IN";

    public static void RequestFlight(String msg) {
        log.info("XML-------->" + msg);
        /*-------将消息写入队列------------*/
        MQEnvironment.hostname = "10.0.252.140";
        MQEnvironment.channel = "SVRCONN.HY";
        MQEnvironment.CCSID = 1208;
        MQEnvironment.port = 1414;
        MQEnvironment.userID = "ICPProUser";
        MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES_CLIENT);
        try {
            int openOptions = MQC.MQOO_OUTPUT | MQC.MQOO_FAIL_IF_QUIESCING;
            /* 关闭了就重新打开 */
            if (qMgr == null || !qMgr.isConnected()) {
                qMgr = new MQQueueManager(QMSend);
            }
            MQQueue queue = qMgr.accessQueue(QSend, openOptions); //打开接收消息的目标队列
            MQMessage putMessage = new MQMessage(); // 定义一个简单的消息
            putMessage.writeString(msg);  // 将数据放入消息缓冲区
            /*设置写入消息的属性（默认属性）*/
            MQPutMessageOptions pmo = new MQPutMessageOptions();
            // 将消息写入队列
            queue.put(putMessage, pmo);
            queue.close();
            qMgr.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                qMgr.disconnect();
            } catch (MQException e) {
            }
        }
    }


}