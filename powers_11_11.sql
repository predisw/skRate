-- MySQL dump 10.13  Distrib 5.5.44, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: tyh_rate
-- ------------------------------------------------------
-- Server version	5.5.44-0ubuntu0.14.10.1-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `powers`
--

DROP TABLE IF EXISTS `powers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `powers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `key_name` varchar(50) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `parentId` int(11) DEFAULT NULL,
  `isMenu` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `powers`
--

LOCK TABLES `powers` WRITE;
/*!40000 ALTER TABLE `powers` DISABLE KEYS */;
INSERT INTO `powers` VALUES (1,'邮件报价',NULL,'',0,''),(2,'客户Rate',NULL,'',0,''),(3,'供应商Rate',NULL,'',0,''),(4,'客户/用户',NULL,'',0,''),(6,'员工管理',NULL,'',0,''),(7,'Others',NULL,'',0,''),(8,'日志',NULL,'',0,''),(9,'系统',NULL,'',0,''),(10,'发送报价',NULL,'getEmps',1,''),(11,'发送记录',NULL,'getEmps',1,''),(12,'历史报价',NULL,'getRateRecord',2,''),(13,'报价code',NULL,'getRateList',2,''),(14,'历史报价',NULL,'toRRateRecord',3,''),(15,'添加报价',NULL,'toAddRRate',3,''),(16,'添加员工',NULL,'toAddEmp',6,''),(17,'修改员工',NULL,'toUpdateEmp',6,''),(18,'地区详情',NULL,'cc/getCountrys',7,''),(19,'属性',NULL,'props/toAddProp',7,''),(20,'模板',NULL,'excelTp/getExcelTp',7,''),(21,'操作日志',NULL,'log/getPageLog',8,''),(22,'系统日志',NULL,'log/getSysLog',8,''),(23,'系统性能',NULL,'sys/toPerformance',9,''),(24,'about',NULL,'sys/getVerInfo',9,''),(25,'系统操作',NULL,'sys/toSysOperation',9,''),(26,'在线人数',NULL,'sys/getOnliner',9,''),(27,'权限',NULL,'',0,''),(29,'用户权限',NULL,'powers/toUserRole',27,''),(30,'配置权限',NULL,'powers/getRoles',27,''),(34,'移除角色','userRole-rm','',29,'\0'),(36,'增加角色','userRole-add','',29,'\0'),(37,'更新角色','userRole-update','powers/saveOrUpdateRolesToUser',29,'\0');
/*!40000 ALTER TABLE `powers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-11  9:28:41
