-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: fy_income_tracker
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `account_index` int DEFAULT '1',
  `status` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '正常',
  `notes` text COLLATE utf8mb4_unicode_ci,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `current_experience` bigint DEFAULT '0',
  `level` int DEFAULT '1',
  `sect_id` bigint DEFAULT NULL,
  `starting_money` bigint DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_account_index` (`account_index`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (1,'红尘',1,'正常','DT','2026-05-19 02:18:20','2026-05-28 00:19:42',123456789,159,1,4000000),(2,'守株待兔',1,'正常','NE','2026-05-19 02:18:32','2026-05-28 01:56:41',88399692,173,14,2138986),(3,'缅怀四季',1,'正常','WDD','2026-05-19 02:18:48','2026-05-27 10:00:01',0,89,13,0),(4,'缅怀夏天',1,'正常','','2026-05-27 06:17:17','2026-05-27 10:00:29',0,89,5,0),(5,'缅怀秋天',1,'正常','','2026-05-27 09:40:57','2026-05-27 09:40:57',0,89,5,0),(6,'缅怀冬天',1,'正常','','2026-05-27 10:00:47','2026-05-27 10:00:47',0,89,8,0),(7,'Smile旋律',1,'正常','','2026-05-27 10:01:02','2026-05-27 10:01:02',0,89,12,0),(8,'胡萝卜炒香干',1,'正常','','2026-05-27 10:01:36','2026-05-27 10:01:36',0,109,13,0);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `factions`
--

DROP TABLE IF EXISTS `factions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `factions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_e5v4rshlkexwkyr9ttmk2ksxl` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `factions`
--

LOCK TABLES `factions` WRITE;
/*!40000 ALTER TABLE `factions` DISABLE KEYS */;
INSERT INTO `factions` VALUES (1,'大唐官府','2026-05-27 05:56:17.885674','2026-05-27 05:56:17.885674'),(2,'化生寺','2026-05-27 05:56:17.912436','2026-05-27 05:56:17.912436'),(3,'龙宫','2026-05-27 05:56:17.918314','2026-05-27 05:56:17.918314'),(4,'方寸山','2026-05-27 05:56:17.921616','2026-05-27 05:56:17.921616'),(5,'普陀山','2026-05-27 05:56:17.925687','2026-05-27 05:56:17.925687'),(6,'五庄观','2026-05-27 05:56:17.929759','2026-05-27 05:56:17.929759'),(7,'天宫','2026-05-27 05:56:17.933391','2026-05-27 05:56:17.933391'),(8,'普陀','2026-05-27 05:56:17.938989','2026-05-27 05:56:17.938989'),(9,'地府','2026-05-27 05:56:17.944551','2026-05-27 05:56:17.944551'),(10,'盘丝洞','2026-05-27 05:56:17.947665','2026-05-27 05:56:17.947665'),(11,'狮驼岭','2026-05-27 05:56:17.951944','2026-05-27 05:56:17.951944'),(12,'神木林','2026-05-27 05:56:17.955912','2026-05-27 05:56:17.955912'),(13,'无底洞','2026-05-27 05:56:17.960307','2026-05-27 05:56:17.960307'),(14,'女儿村','2026-05-27 05:56:17.964338','2026-05-27 05:56:17.964338'),(15,'凌波城','2026-05-27 05:56:17.968632','2026-05-27 05:56:17.968632');
/*!40000 ALTER TABLE `factions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `income_entries`
--

DROP TABLE IF EXISTS `income_entries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `income_entries` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `detail` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `experience` int DEFAULT NULL,
  `money` decimal(15,2) DEFAULT NULL,
  `type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `income_record_id` bigint DEFAULT NULL,
  `activity_type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `activity_type_id` bigint DEFAULT NULL,
  `end_time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `item_id` bigint DEFAULT NULL,
  `item_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `level` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `start_time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9jsggk3y7cs1nvfteeiy4k2n5` (`income_record_id`),
  CONSTRAINT `FK9jsggk3y7cs1nvfteeiy4k2n5` FOREIGN KEY (`income_record_id`) REFERENCES `income_records` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `income_entries`
--

LOCK TABLES `income_entries` WRITE;
/*!40000 ALTER TABLE `income_entries` DISABLE KEYS */;
INSERT INTO `income_entries` VALUES (5,'30',1234567,222222.00,'小鬼',9,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6,'0',1111111,111111.00,'乌鸡',8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(7,NULL,1234567,222222.00,'副本',12,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(8,NULL,NULL,800000.00,'五宝',12,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(9,NULL,111111,11111.00,'副本',13,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(10,NULL,NULL,50000.00,'宝石',13,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(13,NULL,1111111,222222.00,'捉鬼',15,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(14,NULL,NULL,222222.00,'环装-装备',15,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(16,NULL,1111111111,222222.00,'周末活动',17,'门派闯关',8,'17:00',NULL,NULL,NULL,NULL,'15:00'),(17,NULL,0,500000.00,'捉鬼',14,'30小鬼',4,NULL,NULL,NULL,NULL,NULL,'17:31'),(18,NULL,1234567,200000.00,'捉鬼',19,'大小鬼',6,'19:53',NULL,NULL,NULL,NULL,'17:53');
/*!40000 ALTER TABLE `income_entries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `income_entry_items`
--

DROP TABLE IF EXISTS `income_entry_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `income_entry_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `item_id` bigint DEFAULT NULL,
  `item_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `item_type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `level` int DEFAULT NULL,
  `money` decimal(15,2) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `income_entry_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtcby8lluv8dm0avagly8t4v7r` (`income_entry_id`),
  CONSTRAINT `FKtcby8lluv8dm0avagly8t4v7r` FOREIGN KEY (`income_entry_id`) REFERENCES `income_entries` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `income_entry_items`
--

LOCK TABLES `income_entry_items` WRITE;
/*!40000 ALTER TABLE `income_entry_items` DISABLE KEYS */;
INSERT INTO `income_entry_items` VALUES (3,7,'朱雀石','强化石',NULL,70000.00,1,16),(4,1,'太阳石','宝石',NULL,50000.00,1,16),(5,1,'太阳石','宝石',NULL,50000.00,1,17);
/*!40000 ALTER TABLE `income_entry_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `income_records`
--

DROP TABLE IF EXISTS `income_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `income_records` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `account_id` bigint NOT NULL,
  `record_date` date NOT NULL,
  `total_income` decimal(15,2) NOT NULL,
  `dungeon_income` decimal(15,2) DEFAULT NULL,
  `stall_income` decimal(15,2) DEFAULT NULL,
  `task_income` decimal(15,2) DEFAULT NULL,
  `other_income` decimal(15,2) DEFAULT NULL,
  `item_cost` decimal(15,2) DEFAULT NULL,
  `gold_cost` decimal(15,2) DEFAULT NULL,
  `other_cost` decimal(15,2) DEFAULT NULL,
  `net_income` decimal(15,2) DEFAULT NULL,
  `time_spent` int DEFAULT NULL COMMENT '花费时间(分钟)',
  `notes` text COLLATE utf8mb4_unicode_ci,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_account_date` (`account_id`,`record_date`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_account_id` (`account_id`),
  KEY `idx_record_date` (`record_date`),
  KEY `idx_income_records_user_date` (`user_id`,`record_date`),
  KEY `idx_income_records_account_date` (`account_id`,`record_date`),
  CONSTRAINT `income_records_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `income_records_ibfk_2` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `income_records`
--

LOCK TABLES `income_records` WRITE;
/*!40000 ALTER TABLE `income_records` DISABLE KEYS */;
INSERT INTO `income_records` VALUES (1,1,1,'2026-05-19',1000000.00,2000000.00,10000000.00,2000000.00,0.00,0.00,0.00,0.00,1000000.00,0,'','2026-05-19 03:22:12','2026-05-19 03:22:12'),(2,1,2,'2026-05-19',500000.00,220000.00,0.00,0.00,0.00,0.00,0.00,0.00,500000.00,0,'','2026-05-19 03:40:06','2026-05-19 03:40:06'),(8,1,1,'2026-05-25',111111.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,111111.00,0,'','2026-05-25 00:39:12','2026-05-25 02:37:58'),(9,1,2,'2026-05-25',222222.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,222222.00,0,'','2026-05-25 00:42:24','2026-05-25 02:00:51'),(12,1,2,'2026-05-26',1022222.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1022222.00,0,'','2026-05-26 07:44:08','2026-05-26 07:44:08'),(13,1,3,'2026-05-26',61111.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,61111.00,0,'','2026-05-26 08:04:49','2026-05-26 08:04:49'),(14,1,1,'2026-05-26',550000.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,550000.00,0,'','2026-05-26 08:16:42','2026-05-28 09:39:41'),(15,1,4,'2026-05-27',444444.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,444444.00,0,'','2026-05-27 10:02:29','2026-05-27 10:02:29'),(17,1,3,'2026-05-28',342222.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,342222.00,0,'','2026-05-28 06:07:36','2026-05-28 06:07:36'),(19,1,2,'2026-05-28',200000.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,200000.00,0,'','2026-05-28 09:54:16','2026-05-28 09:54:16');
/*!40000 ALTER TABLE `income_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `income_type`
--

DROP TABLE IF EXISTS `income_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `income_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `income_type`
--

LOCK TABLES `income_type` WRITE;
/*!40000 ALTER TABLE `income_type` DISABLE KEYS */;
INSERT INTO `income_type` VALUES (3,'副本','车迟国','2026-05-26 01:53:52.147039','2026-05-26 03:47:28.510140'),(4,'捉鬼','小鬼','2026-05-26 01:54:01.013649','2026-05-28 10:03:25.782923'),(5,'副本','乌鸡国','2026-05-26 03:25:43.837402','2026-05-26 03:25:43.837402'),(6,'捉鬼','大小鬼','2026-05-26 03:44:10.510436','2026-05-26 03:44:10.510436'),(7,'副本','七绝山','2026-05-26 03:47:49.947245','2026-05-26 03:47:49.947245'),(8,'周末活动','门派闯关','2026-05-26 06:34:34.516579','2026-05-26 06:34:34.516579'),(9,'周末活动','慈心渡鬼','2026-05-26 06:35:52.805905','2026-05-26 06:35:52.805905'),(10,'周末活动','彩虹争霸','2026-05-26 06:37:07.529194','2026-05-26 06:37:07.529194'),(11,'周末活动','降妖伏魔','2026-05-26 06:37:29.845041','2026-05-26 06:47:35.271402'),(12,'周末活动','科举考试','2026-05-26 06:47:55.043595','2026-05-26 06:47:55.043595'),(13,'周末活动','长安保卫战','2026-05-26 06:48:59.238890','2026-05-26 06:48:59.238890'),(14,'师门','师门任务','2026-05-28 07:15:55.056370','2026-05-28 07:15:55.056370'),(15,'日常活动','皇宫飞贼','2026-05-28 10:03:14.988827','2026-05-28 10:03:14.988827');
/*!40000 ALTER TABLE `income_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `items`
--

DROP TABLE IF EXISTS `items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` decimal(15,2) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `items`
--

LOCK TABLES `items` WRITE;
/*!40000 ALTER TABLE `items` DISABLE KEYS */;
INSERT INTO `items` VALUES (1,'2026-05-19 02:00:56.696859',NULL,'太阳石',50000.00,'2026-05-19 02:00:56.696859'),(2,'2026-05-19 02:56:00.310665',NULL,'66',30000.00,'2026-05-19 02:56:00.310665'),(3,'2026-05-19 03:15:51.444746',NULL,'C66',200000.00,'2026-05-19 03:15:51.444746');
/*!40000 ALTER TABLE `items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `money_item`
--

DROP TABLE IF EXISTS `money_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `money_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `item_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `item_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `money_value` double NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `money_item`
--

LOCK TABLES `money_item` WRITE;
/*!40000 ALTER TABLE `money_item` DISABLE KEYS */;
INSERT INTO `money_item` VALUES (1,'宝石','太阳石',50000,'2026-05-26 01:55:09.580166','2026-05-26 05:59:36.064803'),(2,'宝石','光芒石',40000,'2026-05-26 01:55:18.676143','2026-05-26 05:59:59.027184'),(3,'宝石','黑宝石',15000,'2026-05-26 02:40:52.310491','2026-05-26 06:00:06.549662'),(4,'宝石','月亮石',60000,'2026-05-26 02:41:16.126899','2026-05-26 06:00:23.530506'),(5,'五宝','金刚石',800000,'2026-05-26 02:41:28.171968','2026-05-26 06:00:32.801648'),(6,'五宝','避水珠',50000,'2026-05-26 02:41:51.912951','2026-05-26 06:00:42.384817'),(7,'强化石','朱雀石',70000,'2026-05-26 02:55:49.007234','2026-05-26 02:55:49.007234'),(8,'宝石','红玛瑙',70000,'2026-05-26 06:38:33.989348','2026-05-26 06:38:33.989348'),(9,'强化石','白虎石',70000,'2026-05-26 06:38:53.156774','2026-05-26 06:38:53.156774'),(10,'强化石','玄武石',70000,'2026-05-26 06:39:53.058782','2026-05-26 06:39:53.058782'),(11,'强化石','青龙石',70000,'2026-05-26 06:40:08.607694','2026-05-26 06:40:08.607694'),(12,'环装-武器','60级',333333,'2026-05-26 06:54:52.482354','2026-05-26 06:57:53.764201'),(13,'环装-武器','70级',99999,'2026-05-26 06:55:00.853531','2026-05-26 06:57:59.215778'),(14,'环装-武器','80级',444444,'2026-05-26 06:55:11.955529','2026-05-26 06:58:22.916593'),(15,'环装-装备','60级',222222,'2026-05-26 06:55:38.384215','2026-05-26 06:55:38.384215'),(16,'环装-装备','70级',88888,'2026-05-26 06:55:51.982102','2026-05-26 06:55:51.982102'),(17,'环装-装备','80级',388888,'2026-05-26 06:56:07.050687','2026-05-26 06:56:07.050687'),(18,'宝石','星辉石',120000,'2026-05-28 10:02:49.321782','2026-05-28 10:02:49.321782');
/*!40000 ALTER TABLE `money_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `point_card`
--

DROP TABLE IF EXISTS `point_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `point_card` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `exchange_rate` double NOT NULL,
  `points_per_hour` double NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point_card`
--

LOCK TABLES `point_card` WRITE;
/*!40000 ALTER TABLE `point_card` DISABLE KEYS */;
INSERT INTO `point_card` VALUES (1,'2026-05-26 01:53:19.401258',12345,6,'2026-05-26 03:17:59.465808');
/*!40000 ALTER TABLE `point_card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `idx_username` (`username`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'yzj','yzj','123@qq.com',NULL,1,'2026-05-14 08:19:33','2026-05-14 08:19:33'),(2,'admin','$2a$10$hVei4wcneR2FaSmDOiN0IePWawbRVNRJxvP2Kp40bJkYuyR.oFfVG','123@qq.com','',1,'2026-05-14 11:59:48','2026-05-14 11:59:48');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'fy_income_tracker'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-28 18:05:06
