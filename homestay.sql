/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 8.0.17 : Database - homestay
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`homestay` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `homestay`;

/*Table structure for table `comment` */

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT,
  `comments` text COMMENT '评论',
  `reply` text COMMENT '回复',
  `score` int(11) DEFAULT '5' COMMENT '评分',
  `co_id` int(11) DEFAULT NULL COMMENT '评论订单id',
  `ch_id` int(11) DEFAULT NULL COMMENT '评论房源id',
  PRIMARY KEY (`comment_id`),
  KEY `fkc` (`co_id`),
  KEY `fkch` (`ch_id`),
  CONSTRAINT `fkc` FOREIGN KEY (`co_id`) REFERENCES `order` (`order_id`),
  CONSTRAINT `fkch` FOREIGN KEY (`ch_id`) REFERENCES `house` (`house_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/*Data for the table `comment` */

insert  into `comment`(`comment_id`,`comments`,`reply`,`score`,`co_id`,`ch_id`) values (1,'房间特别舒适，环境也非常好','尊敬的客户您好，非常感谢您的厚爱，也感激您对我们民宿及服务的真实评价，您的关注与支持是我们不断前行的源泉！',5,7,14),(2,'房子价格虽然偏高，但是非常舒服，环境安静','尊敬的客户您好，非常感谢您的厚爱，也感激您对我们民宿及服务的真实评价，您的关注与支持是我们不断前行的源泉，我们会努力做的更好！',5,12,25),(3,'房子很舒服，环境很好','尊敬的客户您好，非常感谢您的厚爱，也感激您对我们民宿及服务的真实评价，您的关注与支持是我们不断前行的源泉，我们会努力做的更好！',5,11,20),(4,'房间非常舒适','尊敬的客户您好，非常感谢您的厚爱，也感激您对我们民宿及服务的真实评价，您的关注与支持是我们不断前行的源泉，我们会努力做的更好！',5,14,27),(5,'房子非常舒适','尊敬的客户您好，非常感谢您的厚爱，也感激您对我们民宿及服务的真实评价，您的关注与支持是我们不断前行的源泉，我们会努力做的更好！',5,15,4),(6,'房子很好','尊敬的客户您好，非常感谢您的厚爱，也感激您对我们民宿及服务的真实评价，您的关注与支持是我们不断前行的源泉，我们会努力做的更好！',5,17,29),(7,'这个房子周围环境特别好，非常的舒服。','尊敬的客户您好，非常感谢您的厚爱，也感激您对我们民宿及服务的真实评价，您的关注与支持是我们不断前行的源泉，我们会努力做的更好！',5,20,33),(8,'环境非常好，非常安静','尊敬的客户您好，非常感谢您的厚爱，也感激您对我们民宿及服务的真实评价，您的关注与支持是我们不断前行的源泉，我们会努力做的更好！',5,23,35),(9,'房间非常安静','尊敬的客户您好，非常感谢您的厚爱，也感激您对我们民宿及服务的真实评价，您的关注与支持是我们不断前行的源泉，我们会努力做的更好！',5,24,38);

/*Table structure for table `house` */

DROP TABLE IF EXISTS `house`;

CREATE TABLE `house` (
  `house_id` int(11) NOT NULL AUTO_INCREMENT,
  `picture` text,
  `price` double NOT NULL COMMENT '价格',
  `number` int(11) NOT NULL COMMENT '总数量',
  `numbered` int(11) DEFAULT '0' COMMENT '已用数量',
  `information` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '具体信息',
  `housename` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '房源名字',
  `address` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '地址',
  `label` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标签',
  `hu_id` int(11) DEFAULT NULL,
  `score` double DEFAULT NULL COMMENT '评分',
  PRIMARY KEY (`house_id`),
  KEY `fk` (`hu_id`),
  CONSTRAINT `fk` FOREIGN KEY (`hu_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

/*Data for the table `house` */

insert  into `house`(`house_id`,`picture`,`price`,`number`,`numbered`,`information`,`housename`,`address`,`label`,`hu_id`,`score`) values (4,'http://localhost:8083/uploads/20220421/1650541399100.jpg',500,1,1,'【雅奢 SR晶玺】外滩180度全景观|【半熟恋人取景地】|人广东方明珠|超大落地窗|商拍请咨询报价','生活民宿','北京市/市辖区/西城区','超赞房东,可开发票,自助入住,近地铁站,可以做饭',3,5),(5,'http://localhost:8083/uploads/20220421/1650541409100.jpg',1290,1,0,'5号复兴路上的绝美艺术公寓(冬日暖气)','快乐游民宿','北京市/市辖区/西城区','抢手房源,\r\n超赞房东,\r\n行李寄存,\r\n自助入住,\r\n近地铁站,\r\n可以做饭',3,4.7),(6,'http://localhost:8083/uploads/20220421/1650541419100.jpg',1000,1,0,'蔓琳花园～宽敞四层别墅，欢唱麻将轻奢风，近奥特莱斯，购物休闲聚会好选择 +上海青浦赵巷','花园民宿','北京市/市辖区/西城区','超赞房东,\r\n可开发票,\r\n自助入住,\r\n免费停车,\r\n可以做饭',3,4.8),(7,'http://localhost:8083/uploads/20220421/1650541429100.jpg',2000,1,0,'【岭墅】外滩边顶层楼王超值特惠,繁华之上俯望尘嚣,外滩江景,巨幅投影,交通便利,近地铁l10/12','邻避民宿','北京市/市辖区/西城区','抢手房源,\r\n超赞房东,\r\n爱彼迎专享,\r\n行李寄存,\r\n自助入住,\r\n近地铁站',3,4.7),(8,'http://localhost:8083/uploads/20220421/1650541439100.jpg',1009,1,0,'「奈屋」近市中心静安 轨交1/3/4号 全沪直达','亲屋民宿','北京市/市辖区/东城区','提供接送机,\r\n自助入住,\r\n近地铁站,\r\n可以做饭,\r\n可以停车',3,4.7),(9,'http://localhost:8083/uploads/20220421/1650541449100.jpg\r\n',300,1,0,'大客厅/聚会/桌游/生日聚会/百寸投影/配钢琴/市中心轻奢3房 可接亲 网红聚会馆','聚会民宿','北京市/市辖区/东城区','可开发票,\r\n自助入住,\r\n近地铁站,\r\n可以做饭,\r\n桌游',3,4.9),(10,'http://localhost:8083/uploads/20220421/1650541459100.jpg',500,1,0,'Nice 人民广场 外滩 南京路步行街 全新两室一厅套房','外滩广场民宿','北京市/市辖区/东城区','超赞房东,\r\n灵活取消,\r\n近地铁站,\r\n可以做饭',3,4.8),(11,'http://localhost:8083/uploads/20220421/1650541469100.jpg\r\n',900,1,0,'【小太阳】270度落地窗，外滩景观房，东方明珠陆家嘴黄浦江、城隍庙豫园170平三居两卫，地铁10号线','小太阳民宿','北京市/市辖区/东城区','超赞房东,\r\n行李寄存,\r\n自助入住,\r\n近地铁站,\r\n可以做饭',3,4.9),(14,'http://localhost:8083/uploads/20220421/1650541479100.jpg',500,1,1,'【既白 SR晶玺】外滩180度全景观|全屋智能|人广商圈|两室一厅|商拍请联系客服报价','180度民宿','北京市/市辖区/东城区','超赞房东,\r\n灵活取消,\r\n行李寄存,\r\n自助入住,\r\n免费停车',4,4.8),(15,'http://localhost:8083/uploads/20220421/1650541489100.jpg\r\n',200,1,1,'黄埔”2号复兴路上的绝美艺术公寓','黄埔江边民宿','北京市/市辖区/东城区','抢手房源,\r\n超赞房东,\r\n行李寄存,\r\n自助入住,\r\n近地铁站,\r\n可以停车',4,4.9),(17,'http://localhost:8083/uploads/20220421/1650541509100.jpg\r\n',479,1,0,'【摩登 SR晶玺】外滩23层/一线江景/全屋智能/人广商圈/尊享两室一厅/商拍请咨询报价','一线民宿','北京市/市辖区/东城区','可开发票,\r\n灵活取消,\r\n行李寄存,\r\n自助入住,\r\n可以做饭',4,4.8),(18,'http://localhost:8083/uploads/20220421/1650541519100.jpg',900,1,0,'<Garden villa>『安心360消毒』百年法式老洋房，近武康大楼，安福路话剧大厦，巴金故居','法式民宿','北京市/市辖区/东城区','超赞房东,\r\n可开发票,\r\n自助入住,\r\n近地铁站,\r\n可以做饭',4,4.7),(19,'http://localhost:8083/uploads/20220421/1650541529100.jpg',700,1,0,'【Airbnb精选别墅】松江庄园式别墅，占地1800平独栋花园，网红综艺拍摄，公司家庭聚会，娱乐超全','松江民宿','北京市/市辖区/东城区','超赞房东,\r\n自助入住,\r\n免费停车,\r\n可以做饭,\r\n桌游',4,4.9),(20,'http://localhost:8083/uploads/20220421/1650541539100.jpg',600,1,1,'漫咖 日式整套复式+地铁站+商业区+地铁直达机场，高铁站，城隍庙，东方明珠，外滩～','曼加民宿','北京市/市辖区/东城区','抢手房源,\r\n超赞房东,\r\n可开发票,\r\n行李寄存,\r\n自助入住,\r\n近地铁站',4,5),(21,'http://localhost:8083/uploads/20220421/1650541549100.jpg',908,1,0,'长租特惠 • 玻璃花园洋房丨 独门独院 iapm陕西南路地铁站丨淮海中路丨新天地','玻璃民宿','北京市/市辖区/西城区','抢手房源，\r\n超赞房东，\r\n爱彼迎专享，\r\n灵活取消，\r\n近地铁站，\r\n可以做饭',4,4.8),(24,'http://localhost:8083/uploads/20220425/1650858094798.jpg',493,1,0,'长租特惠 • 玻璃花园洋房丨 独门独院 iapm陕西南路地铁站丨淮海中路丨新天地','玻璃花园民宿','北京市/市辖区/西城区\r\n','抢手房源,超赞房东,可以做饭,自助入住,近地铁站',7,0),(25,'http://localhost:8083/uploads/20220425/1650858179639.jpg',498,1,1,'【星期八sunday+】北外滩全新装修智能巨幕江景loft，独有ps4pro，步行外滩5分钟','loft民宿','北京市/市辖区/西城区\r\n','抢手房源,超赞房东,可以做饭,灵活取消,可开发票',7,5),(26,'http://localhost:8083/uploads/20220425/1650858247623.jpg',450,1,0,'【外滩轻奢·PLUS】全江景落地窗观景/外滩全景/听外滩钟声/俯瞰百年外滩「不接吵闹型活动聚会」','观景民宿','北京市/市辖区/西城区','抢手房源,可以做饭,灵活取消,可开发票,近地铁站',7,0),(27,'http://localhost:8083/uploads/20220425/1650858625739.jpg',999,1,1,'逸夏整栋可麻将烧烤一间房近迪士尼','逸夏民宿','北京市/市辖区/东城区\r\n','抢手房源,可以做饭,免费停车,近地铁站,自助入住',7,5),(28,'http://localhost:8083/uploads/20220427/1650990378039.jpg',230,1,0,'昔舍-告白|河滨大楼里法式生活|200平外滩两房艺术公寓|50平环形阳台|中古家具集合|独享法式浪漫','魅力四射民宿','北京市/市辖区/东城区','抢手房源,可以做饭,近地铁站,行李寄存,灵活取消',7,0),(29,'http://localhost:8083/uploads/20220427/1651027288891.jpg',100,1,1,'Dune 静安寺愚园路南京西路花园洋房拍摄请询价请勿聚会','东方明珠民宿','北京市/市辖区/东城区\r\n','抢手房源，可以做饭，近地铁站，行李寄存，自助入住',3,5),(30,'http://localhost:8083/uploads/20220427/1651036777584.jpg',800,1,0,'房子很好','mengyi','北京市/市辖区/西城区','抢手房源,可以做饭,行李寄存,灵活取消,可开发票',3,0),(33,'http://localhost:8083/uploads/202205231653290132718/1653290132719.jpg,http://localhost:8083/uploads/202205231653290135870/1653290135871.jpg,http://localhost:8083/uploads/202205231653290139363/1653290139364.jpg,http://localhost:8083/uploads/202205231653290144617/1653290144618.jpg,http://localhost:8083/uploads/202205231653290148708/1653290148709.jpg',90,1,1,'『应天』栖息屋星级网红民宿/近森林公园/独享三层/垂钓采摘/桌球篮球/ktv双麻将/烤羊篝火烧烤做饭','天空蓝蓝民宿','北京市/市辖区/东城区','抢手房源,可以做饭,行李寄存,灵活取消,可开发票,超赞房东,自助入住,近地铁站',3,5),(34,'http://localhost:8083/uploads/202205241653392876828/1653392876834.jpg,http://localhost:8083/uploads/202205241653392882501/1653392882501.jpg,http://localhost:8083/uploads/202205241653392897285/1653392897286.jpg,http://localhost:8083/uploads/202205241653392902406/1653392902406.jpg,http://localhost:8083/uploads/202205241653392907124/1653392907125.jpg',80,1,1,'整套独享 万达商业广场配套公寓 月租优惠电梯直达商场超市 、下楼就是地铁口、提供迪士尼班车','猪猪连锁民宿','北京市/市辖区/东城区','抢手房源,超赞房东,自助入住,近地铁站,可开发票,可以做饭',3,5),(35,'http://localhost:8083/uploads/202205241653394074438/1653394074439.jpg,http://localhost:8083/uploads/202205241653394079012/1653394079013.jpg,http://localhost:8083/uploads/202205241653394085546/1653394085547.jpg,http://localhost:8083/uploads/202205241653394091970/1653394091972.jpg,http://localhost:8083/uploads/202205241653394098662/1653394098663.jpg',50,1,1,'[城景]外滩景观大三居，赏东方明珠黄浦江陆家嘴美景，近豫园/外滩/南京路步行街/新天地，地铁10号线','新天地民宿','北京市/市辖区/东城区','抢手房源,可以做饭,行李寄存,灵活取消,可开发票,超赞房东',3,5),(36,'http://localhost:8083/uploads/202205251653436827691/1653436827692.jpg,http://localhost:8083/uploads/202205251653436835455/1653436835456.jpg,http://localhost:8083/uploads/202205251653436848675/1653436848675.jpg,http://localhost:8083/uploads/202205251653436859872/1653436859872.jpg,http://localhost:8083/uploads/202205251653436869796/1653436869797.jpg',30,1,0,'非常舒适','天籁之家民宿','北京市/市辖区/东城区','抢手房源,可以做饭,行李寄存,超赞房东,可开发票,灵活取消',3,5),(37,'http://localhost:8083/uploads/202205251653437111032/1653437111033.jpg,http://localhost:8083/uploads/202205251653437116039/1653437116040.jpg,http://localhost:8083/uploads/202205251653437120843/1653437120844.jpg,http://localhost:8083/uploads/202205251653437125878/1653437125879.jpg,http://localhost:8083/uploads/202205251653437132417/1653437132418.jpg',20,1,0,'非常好','随便民宿','北京市/市辖区/东城区','抢手房源,自助入住,近地铁站,可开发票,超赞房东,可以做饭',3,5),(38,'http://localhost:8083/uploads/202205291653839988584/1653839988586.jpg,http://localhost:8083/uploads/202205291653839995405/1653839995406.jpg,http://localhost:8083/uploads/202205301653840002594/1653840002595.jpg,http://localhost:8083/uploads/202205301653840014231/1653840014232.jpg,http://localhost:8083/uploads/202205301653840019254/1653840019255.jpg',111,1,1,'小筑清溪尾 江南临水独栋','小煮民宿','北京市/市辖区/西城区','抢手房源,超赞房东,自助入住,近地铁站,可开发票,可以做饭',3,5),(39,'http://localhost:8083/uploads/202205301653872851621/1653872851622.jpg,http://localhost:8083/uploads/202205301653872859477/1653872859479.jpg,http://localhost:8083/uploads/202205301653872867493/1653872867494.jpg,http://localhost:8083/uploads/202205301653872875169/1653872875171.jpg,http://localhost:8083/uploads/202205301653872882944/1653872882945.jpg',99,1,0,'房子非常安静','生活乐民宿','北京市/市辖区/西城区','抢手房源,超赞房东,自助入住,近地铁站,可开发票,可以做饭',3,5);

/*Table structure for table `order` */

DROP TABLE IF EXISTS `order`;

CREATE TABLE `order` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `nowtime` varchar(50) NOT NULL COMMENT '当前时间',
  `checktime` varchar(50) NOT NULL COMMENT '入住时间',
  `leavetime` varchar(50) NOT NULL COMMENT '离开时间',
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  `oname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '个人姓名',
  `onumber` int(11) NOT NULL COMMENT '房间数',
  `priced` double DEFAULT NULL COMMENT '折后价格',
  `ou_id` int(11) DEFAULT NULL COMMENT '订单用户外键',
  `oh_id` int(11) DEFAULT NULL COMMENT '订单房源外键',
  PRIMARY KEY (`order_id`),
  KEY `fko` (`ou_id`),
  KEY `fkoh` (`oh_id`),
  CONSTRAINT `fko` FOREIGN KEY (`ou_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `fkoh` FOREIGN KEY (`oh_id`) REFERENCES `house` (`house_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

/*Data for the table `order` */

insert  into `order`(`order_id`,`nowtime`,`checktime`,`leavetime`,`state`,`oname`,`onumber`,`priced`,`ou_id`,`oh_id`) values (1,'2022-04-09','2022-04-20','2022-04-30',2,'杨航',1,600,5,20),(2,'2022-04-09','2022-04-11','2022-04-13',1,'杨航',1,1290,5,5),(3,'2022-04-09','2022-04-18','2022-04-19',2,'杨航',1,200,5,15),(4,'2022-04-09','2022-04-19','2022-04-20',2,'杨航',1,1009,5,8),(5,'2022-04-09','2022-04-21','2022-04-23',2,'杨航',1,300,5,9),(6,'2022-04-13','2022-04-20','2022-04-30',2,'杨航',1,4104,5,14),(7,'2022-04-16','2022-04-18','2022-04-19',1,'杨航',1,456,5,14),(8,'2022-04-16','2022-05-01','2022-05-04',0,'杨航',1,1368,5,14),(9,'2022-04-17','2022-04-20','2022-04-22',0,'杨航',1,912,5,14),(10,'2022-04-23','2022-04-23','2022-04-25',1,'赵世宇',1,400,6,15),(11,'2022-04-25','2022-04-25','2022-04-27',1,'小明',1,1200,8,20),(12,'2022-04-25','2022-04-25','2022-04-27',1,'小明',1,996,8,25),(13,'2022-04-27','2022-04-27','2022-04-28',2,'小梅',1,500,10,10),(14,'2022-04-27','2022-04-27','2022-04-28',1,'小梅',1,999,10,27),(15,'2022-04-27','2022-04-27','2022-04-28',1,'小李',1,500,11,4),(16,'2022-04-27','2022-04-27','2022-04-28',2,'白子',1,100,12,29),(17,'2022-04-27','2022-04-27','2022-04-28',1,'baizi',1,100,12,29),(18,'2022-05-01','2022-05-01','2022-05-04',0,'杨航',1,6000,10,7),(19,'2022-05-01','2022-05-01','2022-05-04',0,'杨航',1,1479,10,24),(20,'2022-05-24','2022-05-24','2022-05-25',1,'杨航',1,90,8,33),(21,'2022-05-24','2022-05-24','2022-05-25',1,'杨航',1,80,8,34),(22,'2022-05-25','2022-05-25','2022-05-26',0,'杨航',1,20,8,37),(23,'2022-05-29','2022-05-29','2022-05-30',1,'杨航',1,50,10,35),(24,'2022-05-30','2022-05-30','2022-05-31',1,'杨航',1,111,10,38);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电话',
  `category` int(1) NOT NULL COMMENT '权限',
  `integral` int(11) DEFAULT '0' COMMENT '积分',
  `style` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '风格',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`user_id`,`username`,`password`,`phone`,`category`,`integral`,`style`) values (1,'小小管理员','xiaoxiao123','13758793478',2,0,NULL),(2,'菜菜管理员','caicai123','15353676785',2,0,NULL),(3,'yang','yang123','15353533355',1,0,NULL),(4,'wang','wang123','17878982343',1,0,''),(5,'zhang','zhang123','13434562346',0,5,'超赞房东,\r\n灵活取消,\r\n行李寄存,\r\n自助入住,\r\n免费停车'),(6,'zhao','zhao123','14536788976',0,5,NULL),(7,'zhaosi','zhaosi123','14356567878',1,0,''),(8,'xiaoming','xiaoming123','15353675487',0,20,'抢手房源,超赞房东,可以做饭,灵活取消,可开发票'),(9,'xiaohua','xiaohua123','13456745678',0,0,NULL),(10,'xiaomei','xiaomei123','13245636548',0,15,'抢手房源,可以做饭,免费停车,近地铁站,自助入住'),(11,'xiaoli','xioali123','13232490987',0,5,'超赞房东,可开发票,自助入住,近地铁站,可以做饭'),(12,'bai','bai123','14526780098',0,5,'抢手房源，可以做饭，近地铁站，行李寄存，自助入住');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
