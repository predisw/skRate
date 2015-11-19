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
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `u_id` int(11) NOT NULL AUTO_INCREMENT,
  `u_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `modified_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  PRIMARY KEY (`u_id`)
) ENGINE=InnoDB AUTO_INCREMENT=182 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (60,'黄永乐','123456','2015-08-10 00:00:00',NULL,0),(70,'root','root','2015-08-10 00:00:00',NULL,1),(73,'jerry','123','2015-11-17 10:58:14',NULL,0),(77,'黄永乐','123456','2015-08-10 00:00:00',NULL,0),(78,'root','root','2015-08-10 00:00:00',NULL,1),(79,'jerry','123','2015-11-17 10:58:14',NULL,0),(84,'黄永乐','123456','2015-08-10 00:00:00',NULL,0),(85,'root','root','2015-08-10 00:00:00',NULL,1),(86,'jerry','123','2015-11-17 10:58:14',NULL,0),(91,'黄永乐','123456','2015-08-10 00:00:00',NULL,0),(92,'root','root','2015-08-10 00:00:00',NULL,1),(93,'jerry','123','2015-11-17 10:58:14',NULL,0),(98,'黄永乐','123456','2015-08-10 00:00:00',NULL,0),(99,'root','root','2015-08-10 00:00:00',NULL,1),(100,'jerry','123','2015-11-17 10:58:14',NULL,0),(105,'黄永乐','123456','2015-08-10 00:00:00',NULL,0),(106,'root','root','2015-08-10 00:00:00',NULL,1),(107,'jerry','123','2015-11-17 10:58:14',NULL,0),(112,'黄永乐','123456','2015-08-10 00:00:00',NULL,0),(113,'root','root','2015-08-10 00:00:00',NULL,1),(114,'jerry','123','2015-11-17 10:58:14',NULL,0),(119,'黄永乐','123456','2015-08-10 00:00:00',NULL,0),(120,'root','root','2015-08-10 00:00:00',NULL,1),(121,'jerry','123','2015-11-17 10:58:14',NULL,0),(126,'黄永乐','123456','2015-08-10 00:00:00',NULL,0),(127,'root','root','2015-08-10 00:00:00',NULL,1),(128,'jerry','123','2015-11-17 10:58:14',NULL,0),(133,'黄永乐','123456','2015-08-10 00:00:00',NULL,0),(134,'root','root','2015-08-10 00:00:00',NULL,1),(135,'jerry','123','2015-11-17 10:58:14',NULL,0),(140,'黄永乐','123456','2015-08-10 00:00:00',NULL,0),(141,'root','root','2015-08-10 00:00:00',NULL,1),(142,'jerry','123','2015-11-17 10:58:14',NULL,0),(147,'黄永乐','123456','2015-08-10 00:00:00',NULL,0),(148,'root','root','2015-08-10 00:00:00',NULL,1),(149,'jerry','123','2015-11-17 10:58:14',NULL,0),(154,'黄永乐','123456','2015-08-10 00:00:00',NULL,0),(155,'root','root','2015-08-10 00:00:00',NULL,1),(156,'jerry','123','2015-11-17 10:58:14',NULL,0),(161,'黄永乐','123456','2015-08-10 00:00:00',NULL,0),(162,'root','root','2015-08-10 00:00:00',NULL,1),(163,'jerry','123','2015-11-17 10:58:14',NULL,0),(168,'黄永乐','123456','2015-08-10 00:00:00',NULL,0),(169,'root','root','2015-08-10 00:00:00',NULL,1),(170,'jerry','123','2015-11-17 10:58:14',NULL,0),(175,'黄永乐','123456','2015-08-10 00:00:00',NULL,0),(176,'root','root','2015-08-10 00:00:00',NULL,1),(177,'jerry','123','2015-11-17 10:58:14',NULL,0),(180,'predisw','admin','2015-11-18 11:53:40',NULL,1),(181,'admin','admin','2015-11-18 11:54:44',NULL,0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-19  9:52:19
