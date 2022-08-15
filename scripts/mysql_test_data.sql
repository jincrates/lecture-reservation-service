-- MySQL dump 10.13  Distrib 8.0.27, for macos11 (x86_64)
--
-- Host: 127.0.0.1    Database: lecture-reservation-service
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `lecture`
--

DROP TABLE IF EXISTS `lecture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecture` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `closed_at` datetime(6) NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `description` longtext NOT NULL,
  `lecturer_name` varchar(255) NOT NULL,
  `limit_of_reservations` int NOT NULL,
  `opened_at` datetime(6) NOT NULL,
  `title` varchar(255) NOT NULL,
  `room_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKljp95a81uvc6kdkdr7lfvnx94` (`room_id`),
  CONSTRAINT `FKljp95a81uvc6kdkdr7lfvnx94` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecture`
--

LOCK TABLES `lecture` WRITE;
/*!40000 ALTER TABLE `lecture` DISABLE KEYS */;
INSERT INTO `lecture` VALUES (1,'2022-08-15 15:49:03.906917','2022-08-15 15:49:03.906917','2022-08-15 15:00:00.000000','00000','강연 상세내용','강연자명',10,'2022-08-15 13:00:00.000000','강연 제목',1),(2,'2022-08-15 15:49:26.769607','2022-08-15 15:54:27.289123','2022-08-15 18:30:00.000000','00000','강연 상세내용 - 수정','강연자명 - 수정',10,'2022-08-15 17:30:00.000000','강연 제목 - 수정',1),(3,'2022-08-15 15:49:41.787834','2022-08-15 15:49:41.787834','2022-08-14 12:00:00.000000','00000','강연 상세내용','강연자명',10,'2022-08-14 09:00:00.000000','강연 제목',1),(4,'2022-08-15 15:50:00.879773','2022-08-15 15:50:00.879773','2022-08-13 16:00:00.000000','00000','강연 상세내용','강연자명',10,'2022-08-13 13:00:00.000000','강연 제목',1),(5,'2022-08-15 15:50:21.145572','2022-08-15 15:50:21.145572','2022-08-10 16:00:00.000000','00000','강연 상세내용','강연자명',10,'2022-08-10 14:00:00.000000','강연 제목',1),(6,'2022-08-15 15:50:33.653592','2022-08-15 15:50:33.653592','2022-08-16 18:00:00.000000','00000','강연 상세내용','강연자명',10,'2022-08-16 17:00:00.000000','강연 제목',1),(7,'2022-08-15 15:50:53.821315','2022-08-15 15:50:53.821315','2022-08-17 15:00:00.000000','00000','강연 상세내용','강연자명',10,'2022-08-17 14:00:00.000000','강연 제목',1),(8,'2022-08-15 15:51:01.602516','2022-08-15 15:51:01.602516','2022-08-19 15:00:00.000000','00000','강연 상세내용','강연자명',10,'2022-08-19 14:00:00.000000','강연 제목',1),(9,'2022-08-15 15:51:21.609178','2022-08-15 15:51:21.609178','2022-08-15 15:00:00.000000','00000','강연 상세내용','강연자명',10,'2022-08-15 14:00:00.000000','강연 제목',2),(10,'2022-08-15 15:51:24.572399','2022-08-15 15:51:24.572399','2022-08-15 15:00:00.000000','00000','강연 상세내용','강연자명',10,'2022-08-15 14:00:00.000000','강연 제목',3),(11,'2022-08-15 15:51:26.718868','2022-08-15 15:51:26.718868','2022-08-15 15:00:00.000000','00000','강연 상세내용','강연자명',10,'2022-08-15 14:00:00.000000','강연 제목',5),(12,'2022-08-15 15:51:47.904610','2022-08-15 15:51:47.904610','2022-08-15 13:00:00.000000','00000','강연 상세내용','강연자명',20,'2022-08-15 12:00:00.000000','강연 제목',2),(13,'2022-08-15 15:51:54.750811','2022-08-15 15:51:54.750811','2022-08-16 13:00:00.000000','00000','강연 상세내용','강연자명',20,'2022-08-16 12:00:00.000000','강연 제목',2),(14,'2022-08-15 15:52:02.526405','2022-08-15 15:52:02.526405','2022-08-16 13:00:00.000000','00000','강연 상세내용','강연자명',20,'2022-08-16 12:00:00.000000','강연 제목',5),(15,'2022-08-15 15:52:09.223098','2022-08-15 15:52:09.223098','2022-08-16 13:00:00.000000','00000','강연 상세내용','강연자명',20,'2022-08-16 12:00:00.000000','강연 제목',7),(16,'2022-08-15 15:52:25.792211','2022-08-15 15:52:25.792211','2022-08-14 13:00:00.000000','00000','강연 상세내용','강연자명',20,'2022-08-14 12:00:00.000000','강연 제목',3);
/*!40000 ALTER TABLE `lecture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lecture_reservations`
--

DROP TABLE IF EXISTS `lecture_reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecture_reservations` (
  `lecture_id` bigint NOT NULL,
  `reservations_id` bigint NOT NULL,
  UNIQUE KEY `UK_nay1doym6svki7rktv2bxw9b5` (`reservations_id`),
  KEY `FKb4ontqqbpsmtl8gfhg9l01p6t` (`lecture_id`),
  CONSTRAINT `FKb4ontqqbpsmtl8gfhg9l01p6t` FOREIGN KEY (`lecture_id`) REFERENCES `lecture` (`id`),
  CONSTRAINT `FKbla56qu9jn0io7pnbal22qd0g` FOREIGN KEY (`reservations_id`) REFERENCES `reservation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecture_reservations`
--

LOCK TABLES `lecture_reservations` WRITE;
/*!40000 ALTER TABLE `lecture_reservations` DISABLE KEYS */;
INSERT INTO `lecture_reservations` VALUES (1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(2,15),(3,1),(3,2),(3,3);
/*!40000 ALTER TABLE `lecture_reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `lecture_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe96rew92kqanai11068upynaj` (`lecture_id`),
  CONSTRAINT `FKe96rew92kqanai11068upynaj` FOREIGN KEY (`lecture_id`) REFERENCES `lecture` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES (1,'2022-08-15 16:06:03.468122','2022-08-15 16:37:16.160664','WAITING','12345',3),(2,'2022-08-15 16:06:34.074910','2022-08-15 16:12:23.934724','CANCEL','23456',3),(3,'2022-08-15 16:06:41.514834','2022-08-15 16:06:41.514834','APPROVAL','34567',3),(4,'2022-08-15 16:07:44.611056','2022-08-15 16:07:44.611056','APPROVAL','45678',1),(5,'2022-08-15 16:07:50.967743','2022-08-15 16:07:50.967743','APPROVAL','56789',1),(6,'2022-08-15 16:07:55.890821','2022-08-15 16:07:55.890821','APPROVAL','11111',1),(7,'2022-08-15 16:07:59.954845','2022-08-15 16:07:59.954845','APPROVAL','22222',1),(8,'2022-08-15 16:08:05.329528','2022-08-15 16:08:05.329528','APPROVAL','33333',1),(9,'2022-08-15 16:08:10.208511','2022-08-15 16:08:10.208511','APPROVAL','44444',1),(10,'2022-08-15 16:08:15.753477','2022-08-15 16:08:15.753477','APPROVAL','55555',1),(11,'2022-08-15 16:08:34.862625','2022-08-15 16:08:34.862625','APPROVAL','66666',1),(12,'2022-08-15 16:08:40.080333','2022-08-15 16:08:40.080333','APPROVAL','77777',1),(13,'2022-08-15 16:08:45.326066','2022-08-15 16:08:45.326066','APPROVAL','88888',1),(14,'2022-08-15 16:08:51.955793','2022-08-15 16:08:51.955793','WAITING','99999',1),(15,'2022-08-15 16:09:35.301311','2022-08-15 16:09:35.301311','APPROVAL','99999',2);
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) NOT NULL,
  `limit_of_persons` int NOT NULL,
  `status` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,'2022-08-15 15:46:11.920419','2022-08-15 15:46:11.920419','00000',10,'ACTIVE','강연장1'),(2,'2022-08-15 15:46:19.257183','2022-08-15 15:47:20.289053','00000',22,'ACTIVE','강연장2'),(3,'2022-08-15 15:46:28.446118','2022-08-15 15:46:28.446118','00000',30,'ACTIVE','강연장3'),(5,'2022-08-15 15:46:44.057814','2022-08-15 15:46:44.057814','00000',50,'ACTIVE','강연장5'),(6,'2022-08-15 15:46:51.968713','2022-08-15 15:47:41.828601','00000',60,'INACTIVE','강연장6'),(7,'2022-08-15 15:46:59.007196','2022-08-15 15:46:59.007196','00000',70,'ACTIVE','강연장7');
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_lectures`
--

DROP TABLE IF EXISTS `room_lectures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_lectures` (
  `room_id` bigint NOT NULL,
  `lectures_id` bigint NOT NULL,
  UNIQUE KEY `UK_8fv7gf4i6icc9kkk70tx176yw` (`lectures_id`),
  KEY `FK5gu75t6m5gtw5me2raxnavfjv` (`room_id`),
  CONSTRAINT `FK2t7g0tj9n9fbcbjigd12bybip` FOREIGN KEY (`lectures_id`) REFERENCES `lecture` (`id`),
  CONSTRAINT `FK5gu75t6m5gtw5me2raxnavfjv` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_lectures`
--

LOCK TABLES `room_lectures` WRITE;
/*!40000 ALTER TABLE `room_lectures` DISABLE KEYS */;
INSERT INTO `room_lectures` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(2,9),(2,12),(2,13),(3,10),(3,16),(5,11),(5,14),(7,15);
/*!40000 ALTER TABLE `room_lectures` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-08-15 16:41:10
