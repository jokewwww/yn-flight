alter table t_task
    add flrc_type int null comment '油单类型：1-外航加油 2-内航离境加油 3-内航国内加油 4-外航抽油 5-内航离境抽油 6-内航国内抽油' after flrc_bwtar;

-- 飞机号码重构

ALTER TABLE `t_flight_code`
DROP INDEX `regn`;

ALTER TABLE `t_flight_code`
    ADD UNIQUE INDEX `udx_regn_flno`(`arcr_regn`, `flno`) USING BTREE;

ALTER TABLE `t_flight_code` DROP INDEX `udx_regn_flno`;

ALTER TABLE `t_flight_code`
    ADD UNIQUE INDEX `udx_regn_flno`(`arcr_regn` ASC, `flno` ASC, `arcr_custom_num` ASC) USING BTREE;

ALTER TABLE `t_flight_code` DROP INDEX `udx_regn_flno`;

ALTER TABLE `t_flight_code`
    ADD COLUMN `cid` int NULL COMMENT '中航油ID' AFTER `id`;

ALTER TABLE `t_flight_code`
    ADD UNIQUE INDEX `udx_cid`(`cid` ASC) USING BTREE;

ALTER TABLE `t_flight_code_history`
    ADD COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '0-未删除 1-删除' AFTER `flno`;

ALTER TABLE `t_flight_code`
    ADD COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '0-未删除 1-删除' AFTER `flno`;

ALTER TABLE `t_flight_code`
    ADD COLUMN `cnaf_create_time` datetime NULL COMMENT '记录创建时间-中航油创建时间' AFTER `del_flag`,
    ADD COLUMN `cnaf_update_time` datetime NULL COMMENT '记录更新时间-中航油更新时间' AFTER `cnaf_create_time`,
    ADD COLUMN `create_time` datetime NULL COMMENT '记录创建时间-系统' AFTER `cnaf_update_time`,
    ADD COLUMN `update_time` datetime NULL COMMENT '记录更新时间-系统' AFTER `create_time`;

-- 以上部分已更新

ALTER TABLE t_foreignairport_code
    MODIFY COLUMN `id` int (11) NOT NULL AUTO_INCREMENT COMMENT '序号' FIRST;


ALTER TABLE `t_fuel_recpt`
    ADD COLUMN `flrc_standard_status` int NULL COMMENT '标准化状态 0未标准化 1 标准化成功 2标准化失败' AFTER `flrc_pay_err_msg`,
ADD COLUMN `flrc_standard_msg` varchar(255) NULL COMMENT '标准化失败原因' AFTER `flrc_standard_status`,
ADD COLUMN `flrc_send_status` int NULL COMMENT '发送航司状态 0未发送 1发送成功 2发送失败' AFTER `flrc_standard_msg`,
ADD COLUMN `flrc_send_msg` varchar(255) NULL COMMENT '发送航司失败原因' AFTER `flrc_send_status`;

ALTER TABLE `t_fuel_recpt`
    MODIFY COLUMN `flrc_standard_status` int NULL DEFAULT 0 COMMENT '标准化状态 0未标准化 1 标准化成功 2标准化失败' AFTER `flrc_pay_err_msg`,
    MODIFY COLUMN `flrc_send_status` int NULL DEFAULT 0 COMMENT '发送航司状态 0未发送 1已发送 2发送失败' AFTER `flrc_standard_msg`;

CREATE TABLE `t_airlines_config`
(
    `id`                     bigint                                                       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `airlines_table`         varchar(32) COLLATE utf8mb4_general_ci                       NOT NULL COMMENT '航空公司表(t_airport_code或者t_foreignairport_code)',
    `airlines_id`            varchar(16) COLLATE utf8mb4_general_ci                       NOT NULL COMMENT '航空公司ID',
    `alcd_icao_code`         varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '航空公司二字码',
    `apcd_iata_code`         varchar(8) COLLATE utf8mb4_general_ci                        NOT NULL COMMENT '机场三字码',
    `apcd_cnaf_airport_code` varchar(4) COLLATE utf8mb4_general_ci                                 DEFAULT NULL COMMENT '中航油内部机场编号',
    `cstm_num`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '加油客户编号',
    `cstm_name`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         DEFAULT NULL COMMENT '加油客户名称',
    `cfg_code`               varchar(16) COLLATE utf8mb4_general_ci                       NOT NULL COMMENT '配置编码',
    `cfg_value`              varchar(128) COLLATE utf8mb4_general_ci                      NOT NULL COMMENT '配置值',
    `cfg_order`              bigint                                                       NOT NULL DEFAULT '0' COMMENT '配置排序',
    `create_time`            datetime                                                              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`              varchar(128) COLLATE utf8mb4_general_ci                               DEFAULT NULL COMMENT '创建人',
    `update_time`            datetime                                                              DEFAULT NULL COMMENT '更新时间',
    `update_by`              varchar(128) COLLATE utf8mb4_general_ci                               DEFAULT NULL COMMENT '更新人',
    `remark`                 varchar(255) COLLATE utf8mb4_general_ci                               DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `udx_item` (`alcd_icao_code`,`apcd_iata_code`,`cstm_num`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='航司配置表';


ALTER TABLE `t_fuel_recpt`
    ADD COLUMN `flrc_ctr_status` int NULL DEFAULT 1 COMMENT '上传云平台受控状态 1-自动 2-受控 3-通过' AFTER `flrc_is_export`;
ALTER TABLE `t_fuel_recpt`
    ADD COLUMN `flrc_valid_status` int NULL DEFAULT 1 COMMENT '油单校验状态 1-通过 2-未通过' AFTER `flrc_ctr_status`,
    ADD COLUMN `flrc_valid_msg` varchar(512) NULL COMMENT '油单校验未通过原因' AFTER `flrc_valid_status`;

ALTER TABLE `t_fuel_recpt`
    ADD COLUMN `flrc_ctr_msg` varchar(255) NULL COMMENT '受控信息描述' AFTER `flrc_ctr_status`;

ALTER TABLE `t_fuel_recpt`
    ADD COLUMN `task_id` varchar(40) NULL COMMENT '任务ID' AFTER `flgt_id`;

ALTER TABLE `t_fuel_recpt`
    ADD COLUMN `signature` int NULL DEFAULT 2 COMMENT 'pad签名方式1-纸质签单（默认配置，打印纸质油单并签字） 2-PAD签单（在航油PAD签字确认） 3-EFB签单（在机组电子飞行包签字确认））' AFTER `flrc_sign`;

ALTER TABLE `t_fuel_recpt`
    ADD COLUMN `flrc_ctr_operator` varchar(255) NULL COMMENT '受控通过操作人员' AFTER `flrc_ctr_msg`,
    ADD COLUMN `flrc_ctr_opertime` datetime NULL COMMENT '受控通过操作时间' AFTER `flrc_ctr_operator`,
    MODIFY COLUMN `flrc_status` int NULL DEFAULT NULL COMMENT '-1 失效  0未上传  1 上传失败 2 上传成功 3 新增 4 修改 ' AFTER `flrc_valid_msg`;


ALTER TABLE `t_fuel_recpt`
    ADD COLUMN `flrc_valid_operator` varchar(255) NULL COMMENT '油单校验失败强制上传操作人' AFTER `flrc_valid_msg`,
    ADD COLUMN `flrc_valid_time` datetime NULL COMMENT '油单校验失败强制上传时间' AFTER `flrc_valid_operator`;

ALTER TABLE `t_fuel_recpt`
    MODIFY COLUMN `flrc_valid_operator` varchar (255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '油单校验操作人' AFTER `flrc_valid_msg`,
    MODIFY COLUMN `flrc_valid_time` datetime NULL DEFAULT NULL COMMENT '油单校验时间' AFTER `flrc_valid_operator`,
    ADD COLUMN `flrc_valid_forceup_operator` varchar (255) NULL COMMENT '油单校验失败强制上传操作人' AFTER `flrc_valid_time`,
    ADD COLUMN `flrc_valid_forceup_time` datetime NULL COMMENT '油单校验失败强制上传时间' AFTER `flrc_valid_forceup_operator`;

ALTER TABLE `t_fuel_recpt`
    ADD COLUMN `fuel_id` int NULL COMMENT '油料ID' AFTER `flrc_dest`;

ALTER TABLE `t_fuel_recpt`
    ADD COLUMN `flrc_upload_time` datetime NULL COMMENT '油单上传云平台时间' AFTER `flrc_valid_forceup_time`;

INSERT INTO `t_param` (`ID`, `p_name`, `value1`, `value2`, `remark`, `p_type`, `flgt_airport_code`)
VALUES (34, 'densityErrorRange', '0', '',
        '密度偏差范围（录入油料密度时与历史油料参数中上一次发布的温度相同下的密度进行比对，当密度差设定偏差范围（例如：万分之五），进行数值异常提醒）',
        2, '1');

INSERT INTO `t_param` (`ID`, `p_name`, `value1`, `value2`, `remark`, `p_type`, `flgt_airport_code`)
VALUES (38, 'enableAirlineSignCfg', '1', NULL, '是否开启航司签名配置', 1, '2901');
INSERT INTO `t_param` (`ID`, `p_name`, `value1`, `value2`, `remark`, `p_type`, `flgt_airport_code`)
VALUES (40, 'enableAirlineSignCfg', '0', NULL, '是否开启航司签名配置', 1, '1');
INSERT INTO `t_param` (`ID`, `p_name`, `value1`, `value2`, `remark`, `p_type`, `flgt_airport_code`)
VALUES (41, 'taskTimeoutMinute', '45', NULL, '任务接受时间距离当前时间超过多少分钟', 1, '1');
INSERT INTO `t_param` (`ID`, `p_name`, `value1`, `value2`, `remark`, `p_type`, `flgt_airport_code`)
VALUES (42, 'fuelRecptFailureMinute', '1', NULL, '油单上传失败到此刻的间隔时间', 1, '1');

INSERT INTO `t_airlines_code` (`alcd_icao_code`, `alcd_arln_name`, `alcd_arln_name_s`, `alcd_arln_nw`, `cstm_num`)
VALUES ('00', '代表所有航司', '代表所有航司', 0, '0');


ALTER TABLE `t_task`
    ADD COLUMN `signature` int NULL DEFAULT 2 COMMENT 'pad签名方式1-纸质签单（默认配置，打印纸质油单并签字） 2-PAD签单（在航油PAD签字确认） 3-EFB签单（在机组电子飞行包签字确认））' AFTER `task_crew_sign`;

ALTER TABLE `t_flight`
    ADD COLUMN `custom_num` varchar(30) NULL COMMENT '加油客户编号' AFTER `flgt_alcname`,
    ADD COLUMN `custom_name` varchar(255) NULL COMMENT '加油客户名称' AFTER `custom_num`;

ALTER TABLE `t_task`
    ADD COLUMN `task_manual` int NULL DEFAULT 0 COMMENT '手工油单任务 0-否 1-是' AFTER `task_crew_sign`;


-- 油单打印

ALTER TABLE `t_staff`
    ADD COLUMN `print_server_url` varchar(255) NULL COMMENT '油单打印服务地址' AFTER `staff_level`;