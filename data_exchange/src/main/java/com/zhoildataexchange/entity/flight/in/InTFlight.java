package com.zhoildataexchange.entity.flight.in;

import com.zhoildataexchange.entity.flight.TFlight;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/1/28 10:41
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InTFlight extends TFlight {
    //时间
    private String timestamp = null;
    //操作类型
    private String opration = null;
    //关联航班的id   用 , 分割的 航班id
    private String linkFfid;
    //共享航班的ffids    用 / 分割的  航班ID  已经拼接好
    private String sharedFfidStr;

    //测试字段 用来查看发送时间
    private String createTime;

    //沈阳异常状态码
    private String errorCode;
}
