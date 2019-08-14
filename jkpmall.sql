/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.7.20 : Database - jkpsale
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`jkpsale` /*!40100 DEFAULT CHARACTER SET gbk */;

USE `jkpsale`;

/*Table structure for table `t_admin_operator` */

DROP TABLE IF EXISTS `t_admin_operator`;

CREATE TABLE `t_admin_operator` (
  `USER_NAME` varchar(100) NOT NULL COMMENT '登录用户名',
  `PASS_WORD` varchar(512) DEFAULT NULL COMMENT '密码',
  `REAL_NAME` varchar(100) DEFAULT NULL COMMENT '真实用户名',
  `PHONE` varchar(30) DEFAULT NULL COMMENT '联系电话',
  `EMAIL` varchar(255) DEFAULT NULL COMMENT '邮件',
  `RETRY` decimal(2,0) DEFAULT NULL COMMENT '允许最大登录次数',
  `STATUS` decimal(6,0) DEFAULT NULL COMMENT '状态 0：无效 1：有效',
  `CREATE_DATE` decimal(16,0) DEFAULT NULL COMMENT '创建时间',
  `LAST_DATE` decimal(16,0) DEFAULT NULL COMMENT '最后一次登陆时间',
  `ROLE_CODE` varchar(100) DEFAULT NULL COMMENT '角色代码',
  `REMARK` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `CHANNEL_ID` varchar(20) DEFAULT NULL COMMENT '渠道编号',
  PRIMARY KEY (`USER_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='操作员';

/*Data for the table `t_admin_operator` */

insert  into `t_admin_operator`(`USER_NAME`,`PASS_WORD`,`REAL_NAME`,`PHONE`,`EMAIL`,`RETRY`,`STATUS`,`CREATE_DATE`,`LAST_DATE`,`ROLE_CODE`,`REMARK`,`CHANNEL_ID`) values ('123','1000:6220c49d95e59fda00dbcf99b99019107ed872efcb6058fa:f997ad927e301f79c20ef1aee5cf8814a8686b1af92cb4f6',NULL,'123',NULL,NULL,'1','1555508693204',NULL,'today',NULL,NULL),('12312312312313','1000:a5febda06634087e7bcb6f78f7b7bff96463980dc5715105:117b38218b39c5b82121b7479dbbe39f933f25b59539fc9c',NULL,'123123123123',NULL,NULL,'1','1554109770339',NULL,'company',NULL,NULL),('234','1000:0e58288622fae69e3845c82c12b129c468aff7990b8fb5c4:75bf07e8272c52d887e1e9b55ccf061e9d7a542fbf72cc83',NULL,'234',NULL,NULL,'1','1553081424725',NULL,'laohe',NULL,NULL),('99999','1000:de3e27b1f4f57ec2e52c865bed4c93cd514f2249e0e2027a:a4c4edc1156b6b994d3a9099aa6c77a5c2a75cb2f32a0cfb',NULL,'99999',NULL,NULL,'1','1557478838481',NULL,'today',NULL,NULL),('aaa','1000:8cc3e5d0e571b5aed623307c8704df7942cfbd54ab96d3a1:673e164c6e56cf291522722ce2e90287deaeb8f81a4323f3',NULL,'aaa',NULL,NULL,'1','1557477879375',NULL,'zgyc',NULL,NULL),('admin','1000:a63f5041491e11ad0eb0d680cc6e726574a9d004020cecaf:b846d1804ecb77a16d2b69823fb3b8bec32bb2870a9b9d24','管理员','13520062842',NULL,NULL,'1','1520500342395','1521016097248','admin',NULL,NULL),('center','1000:e12b6b860555d83bce4c6907035189338604b90f07071506:c0525326f9e1a573fb6601c51c4ef9529ad2acb30b70c313',NULL,'13520062842',NULL,NULL,'1','1529920377629',NULL,'center',NULL,NULL),('laohe','1000:15dc188ff491de56ba5131882e8207e8340f163eafe5593c:8ccb3b876bfa64e7e3cdb59b87021375ae5b49ef83bd7662',NULL,'18910966702',NULL,NULL,'1','1532080642247',NULL,'laohe',NULL,NULL),('test','1000:1eaef5d39dbea65da13e026f32657f85cc8eb2ad1d047641:9dfbe57445f98a00782cc196e824ddb923069e0e74e4ae99',NULL,'18910966702',NULL,NULL,'1','1532595527048',NULL,'channel',NULL,'10052'),('test123','1000:90e10c7ab52abb55a057a1cb4d37fb43b06dfca7276340ac:43c01d371980e0423e0a935bede907abb9007bcda206ceab',NULL,'18910966702',NULL,NULL,'1','1532607202167',NULL,'channel',NULL,'10051'),('yy','1000:3371f10646363eb3d2bec24252d7e5b22966e5ce5e939014:819f92acac8f2f084d95fb3bcd2c4c45f0eebc79bdced5ec',NULL,'110',NULL,NULL,'1','1553840806132',NULL,'laohe',NULL,NULL),('zgsy','1000:21d52967df90411b99954cd8bbe759c3a20c7e07bab68ce4:7e8c87d4f455ffc2a70adf10e08417e624fd7d2fc58d03a4',NULL,'13520062842',NULL,NULL,'1','1530087673318',NULL,'zgsy',NULL,'zgsy'),('zgyc','1000:50bddaec8da56b26552fcc41512b63c16f7828b4f3ea2bc9:cdcd11b4b2640156737bb734cc9876b8cdef8fa8ae40eb99',NULL,'13520062842',NULL,NULL,'1','1530157674572',NULL,'zgyc',NULL,'zgyc');

/*Table structure for table `t_admin_permission` */

DROP TABLE IF EXISTS `t_admin_permission`;

CREATE TABLE `t_admin_permission` (
  `ID` decimal(8,0) NOT NULL COMMENT '资源id',
  `PID` decimal(8,0) NOT NULL COMMENT '父级id',
  `NAME` varchar(100) DEFAULT NULL COMMENT '资源名称',
  `URL` varchar(200) DEFAULT NULL COMMENT 'URL地址',
  `LEAF` decimal(6,0) DEFAULT NULL COMMENT '是否为叶子节点',
  `STATUS` decimal(6,0) DEFAULT NULL COMMENT '状态',
  `ORDER_ID` decimal(6,0) DEFAULT NULL COMMENT '排序号',
  `ICON` varchar(100) DEFAULT NULL COMMENT '菜单显示图片信息',
  `BUTTON_TYPE` varchar(45) DEFAULT NULL COMMENT '设置本页面已存在的按钮id（根据T_ADMIN_POST_PERMISSION中的type字段设置此处按钮id）',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='权限列表';

/*Data for the table `t_admin_permission` */

insert  into `t_admin_permission`(`ID`,`PID`,`NAME`,`URL`,`LEAF`,`STATUS`,`ORDER_ID`,`ICON`,`BUTTON_TYPE`) values ('1','0','记录统计','..','0','1','1','pie-graph',NULL),('2','0','系统管理','./','0','1','2','',NULL),('8','0','系统设置','../page','0','1','8','gear-b','1'),('10','1','产品管理','/','1','1','1','',NULL),('11','1','兑奖记录','..','1','1','1','',NULL),('12','1','转账记录','..','1','1','2','',NULL),('13','1','物流记录','./','1','1','3','',NULL),('14','1','手动返奖','..','1','1','4','',NULL),('21','2','渠道管理','./','1','1','1','',NULL),('22','2','渠道信息管理','./','1','1','2','',NULL),('23','2','物流管理','./','1','1','3','',NULL),('31','1','出入库管理','..','1','1','1','/',NULL),('81','8','角色管理','/pages/admin/role.html','1','1','2','person',''),('82','8','用户管理','/pages/admin/usermanagement.html','1','1','1','person-stalker','1,2,3,4,8'),('83','8','权限管理','/pages/admin/authoritymanage.html','1','1','3','key','1,2,3,4'),('84','8','资源管理','../page','1','1','4','folder',NULL),('85','8','参数管理','../page','1','1','5','hammer',NULL),('86','8','参数配置','..','1','1','6','..',NULL),('99','1','订单管理','..','1','1','1','/',NULL);

/*Table structure for table `t_admin_role` */

DROP TABLE IF EXISTS `t_admin_role`;

CREATE TABLE `t_admin_role` (
  `ROLE_CODE` varchar(100) NOT NULL COMMENT '角色代码',
  `ROLE_NAME` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `PARENT_CODE` varchar(100) DEFAULT NULL COMMENT '父级角色',
  PRIMARY KEY (`ROLE_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='角色';

/*Data for the table `t_admin_role` */

insert  into `t_admin_role`(`ROLE_CODE`,`ROLE_NAME`,`PARENT_CODE`) values ('admin','管理员',NULL),('center','中心',NULL),('channel','渠道测试人员',NULL),('company','畅享',NULL),('laohe','测试人员',NULL),('today','今日测试',NULL),('zgsy','中国石油渠道',NULL),('zgyc','中国烟草渠道',NULL);

/*Table structure for table `t_admin_role_permission` */

DROP TABLE IF EXISTS `t_admin_role_permission`;

CREATE TABLE `t_admin_role_permission` (
  `ROLE_CODE` varchar(100) NOT NULL COMMENT '角色代码',
  `PERMISSION_ID` decimal(8,0) NOT NULL COMMENT '权限代码',
  `FIELD1` decimal(12,0) DEFAULT NULL COMMENT 'FIELD1',
  PRIMARY KEY (`ROLE_CODE`,`PERMISSION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='角色权限关系';

/*Data for the table `t_admin_role_permission` */

insert  into `t_admin_role_permission`(`ROLE_CODE`,`PERMISSION_ID`,`FIELD1`) values ('admin','1',NULL),('admin','8',NULL),('admin','10',NULL),('admin','11',NULL),('admin','12',NULL),('admin','13',NULL),('admin','14',NULL),('admin','31',NULL),('admin','81',NULL),('admin','82',NULL),('admin','83',NULL),('admin','84',NULL),('admin','85',NULL),('admin','86',NULL),('admin','99',NULL),('center','10',NULL),('center','11',NULL),('center','12',NULL),('channel','15',NULL),('channel','17',NULL),('channel','19',NULL),('channel','22',NULL),('company','15',NULL),('company','17',NULL),('company','19',NULL),('company','22',NULL),('finance','1',NULL),('finance','11',NULL),('finance','12',NULL),('finance','13',NULL),('laohe','8',NULL),('laohe','10',NULL),('laohe','11',NULL),('laohe','12',NULL),('laohe','81',NULL),('laohe','82',NULL),('laohe','83',NULL),('laohe','84',NULL),('laohe','85',NULL),('laohe','86',NULL),('test','21',NULL),('test','22',NULL),('zgsy','15',NULL),('zgsy','17',NULL),('zgsy','19',NULL),('zgsy','22',NULL),('zgyc','15',NULL),('zgyc','17',NULL),('zgyc','19',NULL),('zgyc','22',NULL);

/*Table structure for table `t_cash_record` */

DROP TABLE IF EXISTS `t_cash_record`;

CREATE TABLE `t_cash_record` (
  `ID` varchar(64) DEFAULT NULL COMMENT '主键',
  `OPEN_ID` varchar(32) DEFAULT NULL COMMENT '用户唯一标识（微信唯一标识）',
  `PRIZE_CHANNEL_ID` varchar(32) DEFAULT NULL COMMENT '兑奖渠道编号',
  `PRIZE_CHANNEL_NAME` varchar(32) DEFAULT NULL COMMENT '兑奖渠道名称',
  `USER_NAME` varchar(32) DEFAULT NULL COMMENT '用户名',
  `TELEPHONE` varchar(32) DEFAULT NULL COMMENT '电话',
  `CASH_TYPE` int(1) DEFAULT NULL COMMENT '兑奖类型(0 公众号兑奖 1 渠道兑奖)',
  `SALE_CHANNEL_ID` varchar(32) DEFAULT NULL COMMENT '销售渠道编号',
  `SALE_CHANNEL_NAME` varchar(32) DEFAULT NULL COMMENT '销售渠道名称',
  `GAME_CODE` varchar(10) DEFAULT NULL COMMENT '游戏代码',
  `GAME_NAME` varchar(10) DEFAULT NULL COMMENT '游戏名称',
  `PRODUCTION_BATCH` varchar(32) DEFAULT NULL COMMENT '生产批次',
  `BOOK_NUM` varchar(32) DEFAULT NULL COMMENT '本号',
  `TICKET_NUM` varchar(45) NOT NULL COMMENT '票号',
  `PROVINCE_ID` int(4) DEFAULT NULL COMMENT '省编码',
  `PAPER_NUM` varchar(45) DEFAULT NULL COMMENT '张号',
  `PRIZE_AMOUNT` int(10) DEFAULT NULL COMMENT '中奖金额(单位：分)',
  `TAX_POINT` int(2) DEFAULT NULL COMMENT '税点',
  `TAX` int(10) DEFAULT NULL COMMENT '税金',
  `AWARD_GRADES` int(1) DEFAULT NULL COMMENT '奖等 （0-小奖 1-大奖）',
  `CASH_STATUS` int(1) DEFAULT NULL COMMENT '兑奖状态(0 未中奖 1 已兑奖 2 兑奖失败）',
  `CASH_TIME` decimal(16,0) DEFAULT NULL COMMENT '兑奖时间',
  `REDEEM_NUMBER` varchar(64) DEFAULT NULL COMMENT '返奖流水号',
  `REDEEM_STATUS` int(1) DEFAULT NULL COMMENT '返奖状态(0 待返奖 1 已返奖 2 返奖失败)',
  `REDEEM_TIME` decimal(16,0) DEFAULT NULL COMMENT '返奖时间',
  `OPR_ID` varchar(45) DEFAULT NULL COMMENT '兑奖操作ID',
  `USER_ID` varchar(45) DEFAULT NULL COMMENT '中彩用户信息报备接口返回userId',
  PRIMARY KEY (`TICKET_NUM`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='兑奖记录表';

/*Data for the table `t_cash_record` */

insert  into `t_cash_record`(`ID`,`OPEN_ID`,`PRIZE_CHANNEL_ID`,`PRIZE_CHANNEL_NAME`,`USER_NAME`,`TELEPHONE`,`CASH_TYPE`,`SALE_CHANNEL_ID`,`SALE_CHANNEL_NAME`,`GAME_CODE`,`GAME_NAME`,`PRODUCTION_BATCH`,`BOOK_NUM`,`TICKET_NUM`,`PROVINCE_ID`,`PAPER_NUM`,`PRIZE_AMOUNT`,`TAX_POINT`,`TAX`,`AWARD_GRADES`,`CASH_STATUS`,`CASH_TIME`,`REDEEM_NUMBER`,`REDEEM_STATUS`,`REDEEM_TIME`,`OPR_ID`,`USER_ID`) values ('402897a969e388b70169e38af5920002','ouGOP4p__6m-ILv33BQotuYyQq-M',NULL,NULL,NULL,NULL,0,NULL,NULL,'J0238',NULL,'17063','0009160','J023817063000916011685302886747619820070',NULL,'116',30000,NULL,NULL,NULL,1,'1554300728722','80ec32f8d5d0479a9ebe2a68f95f082a',2,'1554300728721',NULL,NULL),('402897a969e388b70169e388b73f0001','ouGOP4p__6m-ILv33BQotuYyQq-M',NULL,NULL,NULL,NULL,0,NULL,NULL,'J0238',NULL,'17063','0009191','J023817063000919113278438198815148952150',NULL,'132',30000,NULL,NULL,NULL,1,'1554300581694','5306d39f344543e49dd11127bf8ec789',2,'1554300581683',NULL,NULL),('402897a969e388b70169e38ffe250004','ouGOP4p__6m-ILv33BQotuYyQq-M',NULL,NULL,NULL,NULL,0,NULL,NULL,'J0596',NULL,'17104','0051823','J059617104005182311731058679043149650870',NULL,'117',30000,NULL,NULL,NULL,1,'1554301058597','a4f325d783514703b291056e3f2ccb0f',2,'1554301058596',NULL,NULL),('402897a969e388b70169e38b91340003','ouGOP4n-R7a2QKR13nnexzVyqyCk',NULL,NULL,NULL,NULL,0,NULL,NULL,'J0596',NULL,'17104','0051823','J059617104005182313531650955333135956053',NULL,'135',30000,NULL,NULL,NULL,1,'1554300768564','f6c370c8a09648adac660359cd68dc6a',2,'1554300768563',NULL,NULL);

/*Table structure for table `t_channel_info` */

DROP TABLE IF EXISTS `t_channel_info`;

CREATE TABLE `t_channel_info` (
  `CHANNEL_NAME` varchar(30) DEFAULT NULL COMMENT '渠道名称',
  `CHANNEL_ID` varchar(30) NOT NULL COMMENT '渠道编号',
  `PROVINCE_ID` int(4) DEFAULT NULL COMMENT '省编码',
  `CONTACT` varchar(10) DEFAULT NULL COMMENT '联系人',
  `BROUGHT_ACCOUNT` varchar(30) DEFAULT NULL COMMENT '对公账户号',
  `TELEPHONE` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `CREATE_TIME` decimal(16,0) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`CHANNEL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='渠道信息表';

/*Data for the table `t_channel_info` */

insert  into `t_channel_info`(`CHANNEL_NAME`,`CHANNEL_ID`,`PROVINCE_ID`,`CONTACT`,`BROUGHT_ACCOUNT`,`TELEPHONE`,`CREATE_TIME`) values ('环球','000',11,'王先生','1212','13800138000','1532595764241'),('测试','10051',11,'贺炯','1111111111','18910966702','1532394194005'),('湖北','10052',13,'jjkjkjk','11111111111111','18910966702','1532595371655'),('中国石油','zgsy',12,'张先生','13123165468465','13520062842','1532052662514'),('中国烟草','zgyc',13,'赵鑫','123123123123123123','13520062842','1532070093717');

/*Table structure for table `t_core_product` */

DROP TABLE IF EXISTS `t_core_product`;

CREATE TABLE `t_core_product` (
  `GAME_CODE` varchar(10) NOT NULL COMMENT '游戏编码',
  `GAME_NAME` varchar(45) DEFAULT NULL COMMENT '游戏名称',
  `PAPER_NUM` int(11) DEFAULT NULL COMMENT '规格（每包张数）',
  `PRIZE_AMOUNT` float DEFAULT NULL COMMENT '单价（分）',
  `PICTURE_PATH` varchar(45) DEFAULT NULL COMMENT '图片地址',
  `STORE_NUM` int(11) DEFAULT NULL COMMENT '库存数量',
  `DETAIL` varchar(255) DEFAULT NULL COMMENT '商品规格',
  `PARAMETER` varchar(255) DEFAULT NULL COMMENT '商品参数',
  `SERVICE` varchar(255) DEFAULT NULL COMMENT '附加信息',
  `FACEVALUE` varchar(32) DEFAULT NULL COMMENT '面值',
  `STATUS` int(1) NOT NULL COMMENT '库存状态（1：上架 2：下架）',
  PRIMARY KEY (`GAME_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='产品信息表';

/*Data for the table `t_core_product` */

insert  into `t_core_product`(`GAME_CODE`,`GAME_NAME`,`PAPER_NUM`,`PRIZE_AMOUNT`,`PICTURE_PATH`,`STORE_NUM`,`DETAIL`,`PARAMETER`,`SERVICE`,`FACEVALUE`,`STATUS`) values ('1','大吉大利(即开票套装)',1,0.01,'http://192.168.23.41:9999/image/img3.png',100,'即开型彩票包装','10(十元),20(5元)/个','不可以退换','10元',1),('2','大吉大利(即开票套装)',2,5,'http://192.168.23.41:9999/image/img4.png',9,'即开型彩票包装','10(十元),20(5元)/个','不可以退换','10元',1),('3','大吉大利(即开票套装)',3,300,'http://192.168.23.41:9999/image/img4.png',3,'即开型彩票','10(十元),20(5元)/个','不可以退换','20元',1),('4','大吉大利(即开票套装)',4,150,'http://192.168.23.41:9999/image/img3.png',4,'即开型彩票','10(十元),20(5元)/个','不可以退换','5元',1),('5','大吉大利(即开票套装)',5,100,'http://192.168.23.41:9999/image/img4.png',5,'即开型彩票','10(十元),20(5元)/个','不可以退换','2元',1),('6','大吉大利(即开票套装)',6,50,'http://192.168.23.41:9999/image/img3.png',6,'即开型彩票','10(十元),20(5元)/个','不可以退换','5元',1);

/*Table structure for table `t_core_record` */

DROP TABLE IF EXISTS `t_core_record`;

CREATE TABLE `t_core_record` (
  `UUID` varchar(64) NOT NULL COMMENT '订单编号',
  `GAME_CODE` varchar(10) DEFAULT NULL COMMENT '游戏编号',
  `SALE_NUM` varchar(255) DEFAULT NULL COMMENT '购买数量',
  `TELEPHONE` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `USER_NAME` varchar(45) DEFAULT NULL COMMENT '收货人',
  `ADDRESS` varchar(45) DEFAULT NULL COMMENT '收货地址',
  `LOGISTICS` varchar(45) DEFAULT NULL COMMENT '物流公司',
  `LOGISTICS_NUM` varchar(45) DEFAULT NULL COMMENT '快递编号',
  `OPENID` varchar(50) NOT NULL COMMENT '用户唯一标识',
  `AMOUNT` int(255) DEFAULT NULL COMMENT '订单总金额',
  `STATUS` varchar(1) DEFAULT NULL COMMENT '订单状态(0:未支付,1:未发货，2:已发货)',
  `DATETIME` varchar(50) NOT NULL COMMENT '下单日期',
  PRIMARY KEY (`UUID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='订单信息表';

/*Data for the table `t_core_record` */

insert  into `t_core_record`(`UUID`,`GAME_CODE`,`SALE_NUM`,`TELEPHONE`,`USER_NAME`,`ADDRESS`,`LOGISTICS`,`LOGISTICS_NUM`,`OPENID`,`AMOUNT`,`STATUS`,`DATETIME`) values ('','111','222','222','222','',NULL,NULL,'J20190716155939559',11,'1','2019-07-16 15:59:39'),('1','1','1','1','1','1','倒萨','132131','1',1,'2','2019-05-31 16:28:46'),('111','1111','111','111','111','111','1','1','J20190710114333410',111,'2','2019-07-10 11:43:33'),('1111','1111','111','111','11111','1111111',NULL,NULL,'J20190710114733420',1111,'1','2019-07-10 11:47:33'),('123','123','123','123','123','123',NULL,NULL,'J20190710113927898',123,'1','2019-07-10 11:39:27'),('8950838660172467','1','1','17794224603','樊世强','朝阳西坝河路临2号院9号楼','顺丰物流','315643513','opX8Kvy-kM2O9ddCvyGXdib9QSbE',0,'2','2019-05-31 16:28:46'),('J20190627143218897','1','1','123123123','123123123','东城1231231231',NULL,NULL,'opX8Kv6WN5hOnXH1IEzHZnN6QESs',0,'0','2019-06-27 14:32:18'),('J20190627145848627','1','1','123123123','123123123','东城1231231231',NULL,NULL,'opX8Kv6WN5hOnXH1IEzHZnN6QESs',0,'0','2019-06-27 14:58:48'),('J20190627154210915','1','1','123123123','123123123','东城1231231231',NULL,NULL,'opX8Kv6WN5hOnXH1IEzHZnN6QESs',0,'0','2019-06-27 15:42:10'),('J20190627154236479','1','1','123123123','123123123','东城1231231231',NULL,NULL,'opX8Kv6WN5hOnXH1IEzHZnN6QESs',0,'0','2019-06-27 15:42:36'),('J20190627155408539','1','1','123123123','123123123','东城1231231231',NULL,NULL,'opX8Kv6WN5hOnXH1IEzHZnN6QESs',0,'0','2019-06-27 15:54:08'),('J20190627160636428','1','1','12312312323','123','东城123123123',NULL,NULL,'J20190627160636428',200,'0','2019-06-27 16:06:36'),('J20190627161645531','1','1','12312312323','123','东城123123123',NULL,NULL,'J20190627161645531',200,'0','2019-06-27 16:16:45'),('J20190627161727857','1','1','12312312323','123','东城1231232222',NULL,NULL,'J20190627161727857',200,'0','2019-06-27 16:17:27'),('J20190627163429840','1','1','12312312323','123','东城123123',NULL,NULL,'J20190627163429840',200,'0','2019-06-27 16:34:29'),('J20190710182734431','1,2','1,5','123456789','sfasd','afafddsf',NULL,NULL,'ouGOP4vvHcKERx5SZ25quaGaNMl8',15200,'0','2019-07-10 18:27:34'),('J20190710185536405','5,7','1,1','18600921552','姚洋','昌平-兴寿',NULL,NULL,'ouGOP4vvHcKERx5SZ25quaGaNMl8',1,'0','2019-07-10 18:55:36'),('J20190710190123139','5,7','1,1','18600921552','姚洋','昌平-兴寿',NULL,NULL,'ouGOP4vvHcKERx5SZ25quaGaNMl8',1,'0','2019-07-10 19:01:23'),('J20190710190154845','5,7','1,1','18600921552','姚洋','昌平-兴寿',NULL,NULL,'ouGOP4vvHcKERx5SZ25quaGaNMl8',1,'0','2019-07-10 19:01:54'),('J20190710190254429','5,7','1,1','18600921552','姚洋','昌平-兴寿',NULL,NULL,'ouGOP4vvHcKERx5SZ25quaGaNMl8',1,'0','2019-07-10 19:02:54'),('J20190710190440554','5,7','1,1','18600921552','姚洋','昌平-兴寿',NULL,NULL,'ouGOP4vvHcKERx5SZ25quaGaNMl8',1,'0','2019-07-10 19:04:40'),('J20190710190628364','5','3','18600921552','姚洋','昌平-兴寿',NULL,NULL,'ouGOP4vvHcKERx5SZ25quaGaNMl8',1,'0','2019-07-10 19:06:28'),('J20190716164802896','3','1','1','1','朝阳区',NULL,NULL,'J20190716164802896',1,'1','2019-07-16 16:48:02'),('J20190716170220157','3','1','1','1','朝阳区',NULL,NULL,'J20190716170220157',1,'1','2019-07-16 17:02:20'),('J20190716182422641','2','1','1','1','阿达',NULL,NULL,'J20190716182422641',1,'1','2019-07-16 18:24:22'),('J20190716192237965','2','1','1','1','防守打法',NULL,NULL,'J20190716192237965',1,'1','2019-07-16 19:22:37'),('J20190716192711937','1','1','1','1','昌平区111',NULL,NULL,'J20190716192711937',1,'1','2019-07-16 19:27:11'),('J20190716193118723','2','1','1','1','朝阳区1',NULL,NULL,'J20190716193118723',1,'1','2019-07-16 19:31:18'),('J20190716195215169','6','1','1','1','丰台区fdsfd',NULL,NULL,'J20190716195215169',1,'1','2019-07-16 19:52:15'),('J20190716195828157','1','1','1','1','昌平区-111111',NULL,NULL,'J20190716195828157',100,'1','2019-07-16 19:58:28'),('J20190717141453241','1,2','1,5','123456789','sfasd','afafddsf',NULL,NULL,'ouGOP4vvHcKERx5SZ25quaGaNMl8',15200,'0','2019-07-17 14:14:53'),('J20190717141655861','1,2','1,5','123456789','sfasd','afafddsf',NULL,NULL,'ouGOP4vvHcKERx5SZ25quaGaNMl8',15200,'0','2019-07-17 14:16:55'),('J20190717142913704','1,2','1,5','123456789','sfasd','afafddsf',NULL,NULL,'ouGOP4vvHcKERx5SZ25quaGaNMl8',15200,'0','2019-07-17 14:29:13'),('J20190717143152509','1,2','1,5','123456789','sfasd','afafddsf',NULL,NULL,'ouGOP4vvHcKERx5SZ25quaGaNMl8',15200,'0','2019-07-17 14:31:52'),('J20190717151601492','1,2','1,5','123456789','sfasd','afafddsf',NULL,NULL,'ouGOP4vvHcKERx5SZ25quaGaNMl8',15200,'0','2019-07-17 15:16:01'),('J20190717151745156','1,2','1,5','123456789','sfasd','afafddsf',NULL,NULL,'ouGOP4vvHcKERx5SZ25quaGaNMl8',15200,'0','2019-07-17 15:17:45'),('J20190717151956931','1,2','1,5','123456789','sfasd','afafddsf',NULL,NULL,'ouGOP4vvHcKERx5SZ25quaGaNMl8',15200,'0','2019-07-17 15:19:56'),('J20190717152015168','1,2','1,5','123456789','sfasd','afafddsf',NULL,NULL,'ouGOP4vvHcKERx5SZ25quaGaNMl8',15200,'0','2019-07-17 15:20:15'),('J20190717152031118','1,2','1,5','123456789','sfasd','afafddsf',NULL,NULL,'ouGOP4vvHcKERx5SZ25quaGaNMl8',15200,'0','2019-07-17 15:20:31');

/*Table structure for table `t_iobound_info` */

DROP TABLE IF EXISTS `t_iobound_info`;

CREATE TABLE `t_iobound_info` (
  `GAME_CODE` varchar(10) NOT NULL COMMENT '游戏代码',
  `GAME_NAME` varchar(10) DEFAULT NULL COMMENT '游戏名称',
  `SALE_NUM` varchar(255) DEFAULT NULL COMMENT '购买数量',
  `STORE_NUM` mediumtext COMMENT '库存',
  `OPERATOR` varchar(4) DEFAULT '' COMMENT '操作人员',
  `STORAGE_TIME` decimal(16,0) DEFAULT NULL COMMENT '入库时间',
  `DELIVERY_TIME` decimal(16,0) DEFAULT NULL COMMENT '出库时间',
  `BOUND_STATE` varchar(1) NOT NULL COMMENT '出入库状态',
  PRIMARY KEY (`GAME_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='入库信息表\n';

/*Data for the table `t_iobound_info` */

insert  into `t_iobound_info`(`GAME_CODE`,`GAME_NAME`,`SALE_NUM`,`STORE_NUM`,`OPERATOR`,`STORAGE_TIME`,`DELIVERY_TIME`,`BOUND_STATE`) values ('1','1','1','1','ssss','1557484660391',NULL,'1');

/*Table structure for table `t_logistics_info` */

DROP TABLE IF EXISTS `t_logistics_info`;

CREATE TABLE `t_logistics_info` (
  `GAME_NAME` varchar(30) CHARACTER SET gbk DEFAULT NULL COMMENT '游戏名',
  `GAME_CODE` varchar(10) CHARACTER SET gbk NOT NULL COMMENT '游戏',
  `PRODUCTION_BATCH` varchar(5) CHARACTER SET gbk NOT NULL COMMENT '批次',
  `BOOK_NUM` varchar(255) CHARACTER SET gbk NOT NULL COMMENT '本号',
  `CHANNEL_NUMBER` varchar(32) CHARACTER SET gbk DEFAULT NULL COMMENT '渠道编号',
  `CONFIRM_TIME` decimal(16,0) DEFAULT NULL COMMENT '确认时间',
  `STATUS` int(1) DEFAULT NULL COMMENT '状态(0：物流中，1：已开售)',
  `SITE_LOCATION` int(10) DEFAULT NULL COMMENT '站点位置',
  `USER_ID` varchar(45) CHARACTER SET gbk DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`GAME_CODE`,`PRODUCTION_BATCH`,`BOOK_NUM`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='物流记录表';

/*Data for the table `t_logistics_info` */

insert  into `t_logistics_info`(`GAME_NAME`,`GAME_CODE`,`PRODUCTION_BATCH`,`BOOK_NUM`,`CHANNEL_NUMBER`,`CONFIRM_TIME`,`STATUS`,`SITE_LOCATION`,`USER_ID`) values ('KING','J0666','00002','0000364','laohe','1533281011431',1,11,'dui'),('KING','J0666','00002','0000515','laohe','1533281021436',1,11,'dui'),('KING','J0666','00002','0000670','laohe','1533286580303',1,11,'dui'),('KING','J0666','00002','0000671','laohe','1533286488882',1,11,'dui'),('KING','J0666','00002','0001948','laohe','1533280998235',1,11,'dui'),('KING','J0666','00002','0009638','laohe','1533265536107',1,11,'dui'),('KING','J0777','00003','0002808','laohe','1533265662739',1,11,'dui'),('KING','J0777','00003','0007337','laohe','1533266094689',1,11,'dui'),('KING','J0777','00003','0007547','laohe','1533288149547',1,11,'dui'),('KING','J0777','00003','0007550','laohe','1533288008211',1,11,'dui'),('KING','J0888','00001','0000001','laohe','1532398219225',1,11,'dui'),('KING','J0888','00001','0000002','laohe','1532398304265',1,11,'dui'),('KING','J0888','00001','0000003','laohe','1532398328694',1,11,'dui'),('KING','J0888','00001','0000004','laohe','1532398658874',1,11,'dui'),('KING','J0888','00001','0000005','laohe','1532398677851',1,11,'dui'),('KING','J0888','00001','0000006','laohe','1532398692292',1,11,'dui'),('KING','J0888','00001','0000007','laohe','1532398709740',1,11,'dui'),('KING','J0888','00001','0000008','laohe','1532398724199',1,11,'dui'),('KING','J0888','00001','0000009','laohe','1532398736669',1,11,'dui'),('KING','J0888','00001','0000010','laohe','1532398751129',1,11,'dui'),('KING','J0888','00001','0000011','laohe','1532398765599',1,11,'dui'),('KING','J0888','00001','0000012','laohe','1532398787539',1,11,'dui'),('KING','J0888','00001','0000013','laohe','1532398800515',1,11,'dui'),('KING','J0888','00001','0000014','laohe','1532398812979',1,11,'dui'),('KING','J0888','00001','0000015','laohe','1532398825443',1,11,'dui'),('KING','J0888','00001','0000016','laohe','1532398839909',1,11,'dui'),('KING','J0888','00001','0000017','laohe','1532398855369',1,11,'dui'),('KING','J0888','00001','0000018','laohe','1532398869842',1,11,'dui'),('KING','J0888','00001','0000019','laohe','1532398985053',1,11,'dui'),('KING','J0888','00001','0000020','laohe','1532398999516',1,11,'dui'),('KING','J0888','00001','0000033','laohe','1532413145688',1,11,'dui'),('KING','J0888','00001','0000182','laohe','1532569066999',1,11,'dui'),('KING','J0888','00001','0000282','laohe','1532569166229',1,11,'dui'),('KING','J0888','00001','0000323','laohe','1532569305882',1,11,'dui'),('KING','J0888','00001','0000341','laohe','1532569356758',1,11,'dui'),('KING','J0888','00001','0000472','laohe','1532569096899',1,11,'dui'),('KING','J0888','00001','0000498','laohe','1532569212610',1,11,'dui'),('KING','J0888','00001','0000663','laohe','1532569293410',1,11,'dui'),('KING','J0888','00001','0000670','laohe','1532397651454',1,11,'dui'),('KING','J0888','00001','0000676','laohe','1532569346273',1,11,'dui'),('KING','J0888','00001','0000707','laohe','1532569228558',1,11,'dui'),('KING','J0888','00001','0000712','laohe','1532569252509',1,11,'dui'),('KING','J0888','00001','0000737','laohe','1532569136779',1,11,'dui'),('KING','J0888','00001','0000757','laohe','1532488639795',1,11,'dui'),('KING','J0888','00001','0000758','laohe','1532569125818',1,11,'dui'),('KING','J0888','00001','0000759','laohe','1532569243553',1,11,'dui'),('KING','J0888','00001','0000762','laohe','1532569266499',1,11,'dui'),('KING','J0888','00001','0000785','laohe','1532569326830',1,11,'dui'),('KING','J0888','00001','0000787','laohe','1532569316357',1,11,'dui'),('KING','J0888','00001','0002999','laohe','1532489864141',1,11,'dui'),('KING','J0888','00001','0003000','laohe','1532489822877',1,11,'dui'),('KING','J8888','18012','0000001','laohe','1532137020946',1,11,'兑奖'),('KING','J8888','18012','0000002','laohe','1532158814486',1,11,'dui'),('KING','J8888','18012','0000003','laohe','1532158827911',1,11,'dui'),('KING','J8888','18012','0000004','laohe','1532158835890',1,11,'dui'),('KING','J8888','18012','0000005','laohe','1532158845372',1,11,'dui'),('KING','J8888','18012','0000006','laohe','1532158841381',1,11,'dui'),('KING','J8888','18012','0000007','laohe','1533080053065',1,11,'dui'),('KING','J8888','18012','0000008','laohe','1533080054911',1,11,'dui'),('KING','J8888','18012','0000009','laohe','1533080075900',1,11,'dui'),('KING','J8888','18012','0000010','laohe','1533080084330',1,11,'dui'),('KING','J8888','18012','0000011','laohe','1533080089830',1,11,'dui'),('KING','J8888','18012','0000012','laohe','1533080094310',1,11,'dui'),('KING','J8888','18012','0000013','laohe','1533080098846',1,11,'dui'),('KING','J8888','18012','0000014','laohe','1533080101291',1,11,'dui'),('KING','J8888','18012','0000015','laohe','1533080103790',1,11,'dui'),('KING','J8888','18012','0000016','laohe','1533080107791',1,11,'dui'),('KING','J8888','18012','0000017','laohe','1533080110770',1,11,'dui'),('KING','J8888','18012','0000018','laohe','1533080113770',1,11,'dui'),('KING','J8888','18012','0000019','laohe','1533080116760',1,11,'dui'),('KING','J8888','18012','0000020','laohe','1533080121232',1,11,'dui'),('KING','J8888','18012','0000021','laohe','1533080123739',1,11,'dui'),('KING','J8888','18012','0000022','laohe','1533079799610',1,11,'dui'),('KING','J8888','18012','0000023','laohe','1533079805533',1,11,'dui'),('KING','J8888','18012','0000024','laohe','1533079901786',1,11,'dui'),('KING','J8888','18012','0000025','laohe','1534472198685',1,11,'dui');

/*Table structure for table `t_para_control` */

DROP TABLE IF EXISTS `t_para_control`;

CREATE TABLE `t_para_control` (
  `CONTROL_CODE` varchar(64) NOT NULL COMMENT '参数key',
  `CONTROL_NAME` varchar(128) DEFAULT NULL COMMENT '参数名称',
  `CONTROL_STATE` int(1) DEFAULT NULL COMMENT '状态 0:关，1:开',
  `CONTROL_MONEY` int(20) DEFAULT NULL COMMENT '控制金额',
  `CONTROL_TIME` int(3) DEFAULT NULL COMMENT '控制有效时间。单位:分钟',
  `CONTROL_NUMBER` int(2) DEFAULT NULL COMMENT '控制次数',
  `PHONE_NUMBER` varchar(256) DEFAULT NULL COMMENT '电话号码，多个电话号码用逗号隔开',
  `RESERVED` varchar(45) DEFAULT NULL COMMENT '预留字段',
  PRIMARY KEY (`CONTROL_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

/*Data for the table `t_para_control` */

insert  into `t_para_control`(`CONTROL_CODE`,`CONTROL_NAME`,`CONTROL_STATE`,`CONTROL_MONEY`,`CONTROL_TIME`,`CONTROL_NUMBER`,`PHONE_NUMBER`,`RESERVED`) values ('channel.advances.max.money','子渠道单票垫付最高金额',NULL,4000,NULL,NULL,'',''),('channel.continuous.lock','渠道连续兑未中奖票锁定',1,NULL,1,3,'',''),('grand.prize.min.money','大奖最低奖金',NULL,2000,NULL,NULL,'',''),('small.prize.max.money','微信单票支付最高金额',NULL,1000000,NULL,NULL,'',''),('spuer.prize.min.money','超大奖最低奖金',NULL,5000,NULL,NULL,'',''),('wechat.account.amount','对公微信账户余额',1,0,NULL,NULL,'13220119572,18600921552',''),('wechat.continuous.lock','彩民连续兑未中奖票锁定',1,NULL,1,3,'','');

/*Table structure for table `t_para_sysparame` */

DROP TABLE IF EXISTS `t_para_sysparame`;

CREATE TABLE `t_para_sysparame` (
  `THKEY` varchar(255) NOT NULL COMMENT '键',
  `VALUE` varchar(255) DEFAULT NULL COMMENT '值',
  `MODULE` varchar(255) DEFAULT NULL COMMENT '所属模块',
  `DES` varchar(255) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`THKEY`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

/*Data for the table `t_para_sysparame` */

insert  into `t_para_sysparame`(`THKEY`,`VALUE`,`MODULE`,`DES`) values ('anti.fraud.server.ip','111.85.43.52',NULL,'反欺诈服务ip'),('bank.interface.address','http://123.58.253.115:28013/','award','银行接口地址'),('cash.server.status','1','系统参数','兑奖的系统服务状态'),('channel.md5.password','%@xuKfX&@KeoDMZ5',NULL,'渠道部接口md5密码'),('channel.plat.id','98','系统参数','渠道所需平台id'),('channel.server.url','http://192.168.23.204:8099/todo/','http://218.249.41.156:8882/cmop','中彩渠道部接口地址'),('channel.user.id','99',NULL,NULL),('channl.id','100052','系统参数','渠道编号'),('grand.prize.max.money','1000','12','大奖最高奖金(单位:分)'),('grand.prize.tax.point','20',NULL,'大奖奖金税点(百分比 20表示百分之20)'),('index.ip.port','http://192.168.21.22:8089/login','front','网站首页跳转后台登录页面ip地址'),('ips.b2b.address','http://192.168.23.110:8080/IPS_Simulator/action1009','系统参数','b2b地址'),('ips.b2b.booknum','0051823','系统参数','booknum 本号'),('ips.b2b.bookswitch','true','系统参数','本号验证开关(开：true  关：false)'),('ips.b2b.phonenumber','17603666297,+19298887132,18600921552','系统参数','白名单手机号'),('ips.b2b.userswitch','true','系统参数','用户验证开关'),('ips.mobile.booknum','\"\"','系统参数','中国移动 bookNum本号'),('ips.terminal.id','19005','系统参数','终端编号'),('login.faild.max.times','5','front','登录失败最大次数'),('mobile.smscode.account.appid','8aaf070860fcad290160fdb53263017c',NULL,'ACCOUNT APPID'),('mobile.smscode.account.sid','8a48b55153eae511015404d02422296f','','ACCOUNT SID'),('mobile.smscode.account.token','20d5e68bc8ac40c68b12fc8bf2459af9','','ACCOUNT TOKEN'),('mobile.smscode.template.captcha','238045','','短信验证码模板'),('temp.ip','127.0.0.1','award','临时ip'),('terminal.password','12345678','系统参数','终端密码'),('terminal.public.key.path','/home/javadev/eyibcfile','系统参数','终端公钥路径'),('transfer.retry.times','3','award','异常重试次数'),('webchat.waring.amount','100000','award','微信预警金额(单位:分)'),('wechat.image.path','www','ncps','图片上传路径');

/*Table structure for table `t_top_price_audit` */

DROP TABLE IF EXISTS `t_top_price_audit`;

CREATE TABLE `t_top_price_audit` (
  `ID` varchar(64) DEFAULT NULL COMMENT '主键',
  `CASH_ID` varchar(64) DEFAULT NULL COMMENT '兑奖记录ID',
  `OPEN_ID` varchar(32) DEFAULT NULL COMMENT '用户唯一标识（微信唯一标识）',
  `TICKET_NUM` varchar(45) NOT NULL COMMENT '票号',
  `GAME_CODE` varchar(10) DEFAULT NULL COMMENT '游戏代码',
  `GAME_NAME` varchar(10) DEFAULT NULL COMMENT '游戏名称',
  `PRIZE_AMOUNT` int(10) DEFAULT NULL COMMENT '中奖金额(单位：分)',
  `CASH_TIME` decimal(16,0) DEFAULT NULL COMMENT '兑奖时间',
  `AUDIT_STATUS` int(1) DEFAULT NULL COMMENT '审核状态 0-待完善信息：大奖记录默认为待完善信息状态 1-审核中：用户中奖金额为50-100万之间并完善信息之后为审核中状态，此状态时需用户携带相关资料到省中心进行兑奖 2-待支付：用户中奖金额为1-50万并且完善信息之后通过审核或用户中奖金额为50-100万之间且审核通过为待支付状态 3-兑奖成功：由百信银行转账至用户银行卡时以及转账操作完成之后为兑奖成功状态 4-审核失败：审核失败',
  `AUDIT_TIME` decimal(16,0) DEFAULT NULL COMMENT '审核时间',
  `AUDIT_REMARK` varchar(500) DEFAULT NULL COMMENT '审核备注',
  `NAME` varchar(20) DEFAULT NULL COMMENT '姓名',
  `TELEPHONE` varchar(11) DEFAULT NULL COMMENT '手机号',
  `ID_CARD` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `BANK_CARD_NUMBER` varchar(30) DEFAULT NULL COMMENT '银行卡号',
  `BANK_CARD_PROVINCE` varchar(20) DEFAULT NULL COMMENT '开户省份',
  `ACCOUNT_OPENING_BANK` varchar(20) DEFAULT NULL COMMENT '开户银行',
  `FACADE_OF_ID_CARD` varchar(256) DEFAULT NULL COMMENT '身份证正面照片',
  `OPPOSITE_OF_ID_CARD` varchar(256) DEFAULT NULL COMMENT '身份证反面照片',
  `WINNING_LOTTERY_TICKET` varchar(256) DEFAULT NULL COMMENT '中奖彩票照片',
  `OPR_ID` varchar(100) DEFAULT NULL COMMENT '中彩渠道返奖通知 ID',
  PRIMARY KEY (`TICKET_NUM`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='大奖申报审核表';

/*Data for the table `t_top_price_audit` */

/*Table structure for table `t_transfer_account_record` */

DROP TABLE IF EXISTS `t_transfer_account_record`;

CREATE TABLE `t_transfer_account_record` (
  `REDEEM_NUMBER` varchar(64) DEFAULT NULL COMMENT '转账流水号',
  `CASH_TYPE` int(1) DEFAULT NULL COMMENT '兑奖类型(0 公众号兑奖 1 渠道兑奖)',
  `OPEN_ID` varchar(32) DEFAULT NULL COMMENT '用户唯一标识(微信)',
  `REDEEM_TIME` decimal(16,0) DEFAULT NULL COMMENT '返奖日期(格式 yyyy-MM-dd HH:mm:ss)',
  `PRIZE_CHANNEL_ID` varchar(32) DEFAULT NULL COMMENT '兑奖渠道编号',
  `PRIZE_CHANNEL_NAME` varchar(32) DEFAULT NULL COMMENT '兑奖渠道名称',
  `PROVINCE_ID` int(4) DEFAULT NULL COMMENT '省码',
  `PRIZE_COUNT` int(4) DEFAULT NULL COMMENT '兑奖笔数',
  `PRIZE_AMOUNT` int(10) DEFAULT NULL COMMENT '兑奖金额(单位：分)',
  `REDEEM_STATUS` int(1) DEFAULT '0' COMMENT '转账状态(0 待返奖 1 已返奖 2 返奖失败 )',
  `UPDATE_TIME` decimal(16,0) DEFAULT NULL COMMENT '更新时间',
  `QUOTA` int(5) DEFAULT NULL COMMENT '限额',
  `TICKET_NUM` varchar(60) NOT NULL COMMENT '票号',
  `ORDER_NUM` varchar(100) DEFAULT NULL COMMENT '微信转账订单号',
  `ISSUING_NUM` varchar(100) DEFAULT NULL COMMENT '代发流水号',
  `WITHDRAW_DEPOSIT_NUM` varchar(100) DEFAULT NULL COMMENT '提现流水号',
  `WECHAT_RANDOM_NUM` varchar(100) DEFAULT NULL COMMENT '微信随机字符串',
  `NOTICE_STATUS` int(1) DEFAULT NULL COMMENT '通知状态:0:通知失败，1:通知成功',
  `BANK_TRANSACTION_DATE` varchar(8) DEFAULT NULL COMMENT '银行代发交易日期，格式YYYYMMDD',
  PRIMARY KEY (`TICKET_NUM`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk COMMENT='转账明细表';

/*Data for the table `t_transfer_account_record` */

insert  into `t_transfer_account_record`(`REDEEM_NUMBER`,`CASH_TYPE`,`OPEN_ID`,`REDEEM_TIME`,`PRIZE_CHANNEL_ID`,`PRIZE_CHANNEL_NAME`,`PROVINCE_ID`,`PRIZE_COUNT`,`PRIZE_AMOUNT`,`REDEEM_STATUS`,`UPDATE_TIME`,`QUOTA`,`TICKET_NUM`,`ORDER_NUM`,`ISSUING_NUM`,`WITHDRAW_DEPOSIT_NUM`,`WECHAT_RANDOM_NUM`,`NOTICE_STATUS`,`BANK_TRANSACTION_DATE`) values ('5306d39f344543e49dd11127bf8ec789',0,'ouGOP4p__6m-ILv33BQotuYyQq-M','1554300583809',NULL,NULL,NULL,NULL,30000,2,'1554300583809',NULL,'J023817063000919113278438198815148952150','8cd7e252bbb6462d8f1da3312c6f3949',NULL,NULL,'d74f1023a50c4f53942fceee79e931bc',NULL,NULL),('80ec32f8d5d0479a9ebe2a68f95f082a',0,'ouGOP4p__6m-ILv33BQotuYyQq-M','1554300730756',NULL,NULL,NULL,NULL,30000,2,'1554300730756',NULL,'J023817063000916011685302886747619820070','2fa76daabf5941f5873a55c190356be8',NULL,NULL,'0c54b4e8e2bd46eda57734029ee160ab',NULL,NULL),('f6c370c8a09648adac660359cd68dc6a',0,'ouGOP4n-R7a2QKR13nnexzVyqyCk','1554300769760',NULL,NULL,NULL,NULL,30000,2,'1554300769760',NULL,'J059617104005182313531650955333135956053','0630c58ce99c4ced9846c4db31ec2211',NULL,NULL,'4abf53ca5fba4c3389cd802112bfce29',NULL,NULL),('a4f325d783514703b291056e3f2ccb0f',0,'ouGOP4p__6m-ILv33BQotuYyQq-M','1554301059760',NULL,NULL,NULL,NULL,30000,2,'1554301059760',NULL,'J059617104005182311731058679043149650870','dd356e2da8b544d29fe8e1fe3e340d81',NULL,NULL,'e7284173554b4850854833c137c6251c',NULL,NULL);

/*Table structure for table `t_transfer_account_turnover` */

DROP TABLE IF EXISTS `t_transfer_account_turnover`;

CREATE TABLE `t_transfer_account_turnover` (
  `UUID` varchar(64) NOT NULL COMMENT 'UUID',
  `REDEEM_NUMBER` varchar(64) DEFAULT NULL COMMENT '转账流水号',
  `CASH_TYPE` int(1) DEFAULT NULL COMMENT '兑奖类型(0 公众号兑奖 1 渠道兑奖)',
  `OPEN_ID` varchar(32) DEFAULT NULL COMMENT '用户唯一标识(微信)',
  `REDEEM_TIME` decimal(16,0) DEFAULT NULL COMMENT '返奖日期(格式 yyyy-MM-dd HH:mm:ss)',
  `PRIZE_CHANNEL_ID` varchar(32) DEFAULT NULL COMMENT '兑奖渠道编号',
  `PRIZE_CHANNEL_NAME` varchar(32) DEFAULT NULL COMMENT '兑奖渠道名称',
  `PROVINCE_ID` int(4) DEFAULT NULL COMMENT '省码',
  `PRIZE_COUNT` int(4) DEFAULT NULL COMMENT '兑奖笔数',
  `PRIZE_AMOUNT` int(10) DEFAULT NULL COMMENT '兑奖金额(单位：分)',
  `REDEEM_STATUS` int(1) DEFAULT '0' COMMENT '转账状态(0 待返奖 1 已返奖 2 返奖失败 )',
  `UPDATE_TIME` decimal(16,0) DEFAULT NULL COMMENT '更新时间',
  `QUOTA` int(5) DEFAULT NULL COMMENT '限额',
  `TICKET_NUM` varchar(60) NOT NULL COMMENT '票号',
  `ORDER_NUM` varchar(100) DEFAULT NULL COMMENT '微信转账订单号',
  `ISSUING_NUM` varchar(100) DEFAULT NULL COMMENT '代发流水号',
  `WITHDRAW_DEPOSIT_NUM` varchar(100) DEFAULT NULL COMMENT '提现流水号',
  `WECHAT_RANDOM_NUM` varchar(100) DEFAULT NULL COMMENT '微信随机字符串',
  PRIMARY KEY (`UUID`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk COMMENT='转账流水表记录表';

/*Data for the table `t_transfer_account_turnover` */

/*Table structure for table `t_user_address` */

DROP TABLE IF EXISTS `t_user_address`;

CREATE TABLE `t_user_address` (
  `OPEN_ID` varchar(255) NOT NULL COMMENT '用户ID，可以获取微信openid',
  `USER_NAME` varchar(45) DEFAULT '' COMMENT '用户名称',
  `TELEPHONE` varchar(12) DEFAULT NULL COMMENT '手机号码',
  `ADDRESS` varchar(200) DEFAULT NULL COMMENT '详细地址',
  PRIMARY KEY (`OPEN_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='用户地址表';

/*Data for the table `t_user_address` */

insert  into `t_user_address`(`OPEN_ID`,`USER_NAME`,`TELEPHONE`,`ADDRESS`) values ('hhhh','45646','456464','46546'),('J20190627145848627','123123123','123123123','东城1231231231'),('J20190627154210915','123123123','123123123','东城1231231231'),('J20190627154236479','123123123','123123123','东城1231231231'),('J20190627155408539','123123123','123123123','东城1231231231'),('J20190627155457150','123123','12312312323','东城123123'),('J20190627155608793','123','12312312323','东城123123123'),('J20190627160636428','123','12312312323','东城123123123'),('J20190627161645531','123','12312312323','东城123123123'),('J20190627161727857','123','12312312323','东城1231232222'),('J20190627163429840','123','12312312323','东城123123'),('jhjjhj','1212','13220119572','北京市朝阳区'),('opVlp5Nr-PJHyoBdBIxgCVAdG-Nk','asdasddas','17794224603','朝阳-西坝河路临二号院9号'),('opX8Kv6WN5hOnXH1IEzHZnN6QESs','123123123','123123123','东城1231231231'),('opX8Kvy-kM2O9ddCvyGXdib9QSbE','aasas','13220119572','朝阳-西坝河路临二号院9号'),('ouGOP4kJiT9YD93hPDfwBjD5SaKM','物流','17794224603','北京市朝阳区'),('ouGOP4uLKM7EaBgqvp-A3FH0iJHg','物流人员','17603666297','北京市朝阳区'),('ouGOP4vvHcKERx5SZ25quaGaNMl8','sfasd','123456789','afafddsf'),('wu','姚洋','18600921552','昌平xxxxxxxxxxxxxxxxx'),('wxbb6b8383a755c755','yaorrrrrrrrr','18600921552','xxx'),('wxbba2080c75bf62c0','qq','13220119572','北京市朝阳区'),('yrb','兑奖人员','13220119572','北京市朝阳区');

/*Table structure for table `t_user_info` */

DROP TABLE IF EXISTS `t_user_info`;

CREATE TABLE `t_user_info` (
  `TICKET_NUM` varchar(64) CHARACTER SET latin1 NOT NULL COMMENT '票号',
  `ID_CARD` varchar(64) CHARACTER SET latin1 DEFAULT NULL COMMENT '身份证号',
  `REAL_NAME` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户真实姓名',
  `PHOE_NUM` varchar(64) CHARACTER SET latin1 DEFAULT NULL COMMENT '手机号',
  `BANK_CARD` varchar(64) CHARACTER SET latin1 DEFAULT NULL COMMENT '银行卡号',
  `OPEN_ID` varchar(64) DEFAULT NULL COMMENT '微信唯一标识号',
  `ID_CARD_PHOTO_FRONT` varchar(100) CHARACTER SET latin1 DEFAULT NULL COMMENT '身份证号正面照片',
  `ID_CARD_PHOTO_BACK` varchar(100) CHARACTER SET latin1 DEFAULT NULL COMMENT '身份证号背面照片',
  `TICKET_PHOTO_FRONT` varchar(100) CHARACTER SET latin1 DEFAULT NULL COMMENT '彩票正面照片',
  `ACCOUNT_OPENING_BANK` varchar(64) CHARACTER SET latin1 DEFAULT NULL COMMENT '开户银行',
  `CUSTOMER_ID` varchar(128) CHARACTER SET latin1 DEFAULT NULL COMMENT '百信银行客户编号',
  `ACCT_NO` varchar(128) CHARACTER SET latin1 DEFAULT NULL COMMENT '百信银行账户号',
  `LINKED_PHONE` varchar(45) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`TICKET_NUM`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

/*Data for the table `t_user_info` */

insert  into `t_user_info`(`TICKET_NUM`,`ID_CARD`,`REAL_NAME`,`PHOE_NUM`,`BANK_CARD`,`OPEN_ID`,`ID_CARD_PHOTO_FRONT`,`ID_CARD_PHOTO_BACK`,`TICKET_PHOTO_FRONT`,`ACCOUNT_OPENING_BANK`,`CUSTOMER_ID`,`ACCT_NO`,`LINKED_PHONE`) values ('J888818012000000111419710443591455378959','510922198603031097','贺炯','18910966702','6217001210025391230','ouGOP4iLsfD2JvDSIX74MJuhl1_g',NULL,NULL,NULL,'ICBC','100000019405','1000000194052161000036',NULL),('J888818012000000111611762093821633672211','510922198603031097','贺炯','18910966702','6217001210025391230','ouGOP4iLsfD2JvDSIX74MJuhl1_g',NULL,NULL,NULL,'ICBC','100000019405','1000000194052161000037',NULL),('J888818012000000111719037541541254863204','510922198603031097','贺炯','18910966702','6217001210025391230','ouGOP4iLsfD2JvDSIX74MJuhl1_g',NULL,NULL,NULL,'ICBC','100000019405','1000000194052161000038',NULL),('J888818012000000111812421689011059317584','510922198603031097','贺炯','18910966702','6217001210025391230','ouGOP4iLsfD2JvDSIX74MJuhl1_g',NULL,NULL,NULL,'ICBC','100000019405','1000000194052161000039',NULL),('J888818012000000112014355772861490203868','510922198603031097','贺炯','18910996702','6217001210025391230','ouGOP4iLsfD2JvDSIX74MJuhl1_g',NULL,NULL,NULL,'ICBC','100000019405','1000000194052161000044',NULL),('J888818012000000112110310223171260017989','510922198603031097','贺炯','18918966702','6217001210025391230','ouGOP4iLsfD2JvDSIX74MJuhl1_g',NULL,NULL,NULL,'ICBC','100000019405','1000000194052161000042',NULL),('J888818012000000112210152380821075035459','510922198603031097','贺炯','18910996702','6217001210025391230','ouGOP4iLsfD2JvDSIX74MJuhl1_g',NULL,NULL,NULL,'ICBC','100000019405','1000000194052161000045',NULL),('J888818012000000112311403659091585069568','510922198603031097','贺炯','18910966702','6217001210025391230','ouGOP4iLsfD2JvDSIX74MJuhl1_g',NULL,NULL,NULL,'ICBC','100000019405','1000000194052161000051',NULL),('J888818012000000112418574566951315230821','510922198603031097','贺炯','18910966702','6217001210025391230','ouGOP4iLsfD2JvDSIX74MJuhl1_g',NULL,NULL,NULL,'ICBC','100000019405','1000000194052161000056',NULL),('J888818012000000112512425808931295549652','110221199202054410','姚洋','18600921552','6217001210025391230','ouGOP4iLsfD2JvDSIX74MJuhl1_g',NULL,NULL,NULL,'ICBC','100000017260','1000000172602131000005',NULL),('J888818012000000112613312961441655454187','110221199202054410','姚洋','18600921552','6217001210025391230','ouGOP4iLsfD2JvDSIX74MJuhl1_g',NULL,NULL,NULL,'ICBC','100000017260','1000000172602131000003',NULL),('J888818012000000112819356227181317848045','510922198603031097','贺炯','18910966702','6217001210025391230','ouGOP4iLsfD2JvDSIX74MJuhl1_g',NULL,NULL,NULL,'ICBC','100000019405','1000000194052161000057',NULL),('J888818012000000112918438125511660936983','370920196001124984','边翠勤','15718820854','6215590888771200000','ouGOP4iLsfD2JvDSIX74MJuhl1_g',NULL,NULL,NULL,'ICBC','100000021932','1000000219322131000002',NULL),('J888818012000000113510271097521068658281','230704199406083887','梅从筠','18516875250','6217001210025391230','1',NULL,NULL,NULL,'ABC','100000003578','1000000035782151000216',NULL);

/*Table structure for table `t_user_relation` */

DROP TABLE IF EXISTS `t_user_relation`;

CREATE TABLE `t_user_relation` (
  `ID` varchar(64) NOT NULL COMMENT '兑奖平台用户ID',
  `USER_ID` varchar(45) DEFAULT NULL COMMENT '中彩返回userId（用户信息报备）\n渠道管理系统的用户 id，用户兑奖时对用户进行验证',
  `CHANN_USER_ID` varchar(45) DEFAULT NULL COMMENT '中彩返回，渠道用户 ID',
  `LOCK_STATUS` int(1) DEFAULT NULL COMMENT '用户状态 0:正常兑奖1:锁定状态',
  `NO_WINS` int(2) DEFAULT NULL COMMENT '兑未中奖票次数',
  `LOCK_TIME` bigint(20) DEFAULT NULL COMMENT '锁定时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `t_user_relation` */

insert  into `t_user_relation`(`ID`,`USER_ID`,`CHANN_USER_ID`,`LOCK_STATUS`,`NO_WINS`,`LOCK_TIME`) values ('10052',NULL,NULL,1,NULL,1534503750772),('ouGOP4g9Xn4O8EvS7C-H1Xc_qYO0','bOQzeB','99',NULL,NULL,NULL),('ouGOP4iLsfD2JvDSIX74MJuhl1_g',NULL,NULL,1,NULL,1534745395248),('ouGOP4otsxNhF4nO_yNQLfhFW5Tk','G73cCz','99',NULL,NULL,NULL),('ouGOP4tLpwfmbGDpriA0wQSMYA14','vzuMJh','99',NULL,NULL,NULL),('ouGOP4vvHcKERx5SZ25quaGaNMl8',NULL,NULL,0,NULL,1552641407591);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
