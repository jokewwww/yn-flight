package com.example.jobschedual.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auther: 修宏鑫
 * @Date: 2019/3/13 19:17
 * @Description:
 */
@EqualsAndHashCode
@Data
public class QueueEntity {
    private String taskId;
    private String taskName;
    private String type;

}
