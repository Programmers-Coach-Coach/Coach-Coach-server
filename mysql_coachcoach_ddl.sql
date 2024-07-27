CREATE DATABASE  IF NOT EXISTS `coachcoach` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `coachcoach`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: coachcoach
-- ------------------------------------------------------
-- Server version	8.0.37

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
-- Table structure for table `actions`
--

DROP TABLE IF EXISTS `actions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actions` (
                           `action_id` int NOT NULL AUTO_INCREMENT,
                           `routine_category_id` int NOT NULL,
                           `action_name` varchar(45) COLLATE utf8mb4_bin NOT NULL,
                           `count_or_minutes` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL,
                           `set` int DEFAULT NULL,
                           `description` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
                           PRIMARY KEY (`action_id`),
                           KEY `routine_category_id_idx` (`routine_category_id`),
                           CONSTRAINT `fk_actions_routine_category_id` FOREIGN KEY (`routine_category_id`) REFERENCES `routine_categories` (`routine_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='운동 actions';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `coaches`
--

DROP TABLE IF EXISTS `coaches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coaches` (
                           `coach_id` int NOT NULL AUTO_INCREMENT,
                           `user_id` int NOT NULL,
                           `coach_introduction` text COLLATE utf8mb4_bin,
                           `created_at` timestamp NOT NULL,
                           `active_center` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
                           `active_hours_on` int DEFAULT NULL,
                           `active_hours_off` int DEFAULT NULL,
                           `chatting_url` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
                           `is_open` tinyint DEFAULT '1',
                           PRIMARY KEY (`coach_id`),
                           KEY `fk_coaches_user_id_idx` (`user_id`),
                           CONSTRAINT `fk_coaches_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='코치 정보';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `coaching_sports`
--

DROP TABLE IF EXISTS `coaching_sports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coaching_sports` (
                                   `coaching_sports_id` int NOT NULL AUTO_INCREMENT,
                                   `coach_id` int NOT NULL,
                                   `sports_id` int NOT NULL,
                                   PRIMARY KEY (`coaching_sports_id`),
                                   KEY `fk_coaching_sports_coach_id_idx` (`coach_id`),
                                   KEY `fk_coaching_sports_sports_id_idx` (`sports_id`),
                                   CONSTRAINT `fk_coaching_sports_coach_id` FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`coach_id`),
                                   CONSTRAINT `fk_coaching_sports_sports_id` FOREIGN KEY (`sports_id`) REFERENCES `sports` (`sports_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='코칭 종목';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `completed_categories`
--

DROP TABLE IF EXISTS `completed_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `completed_categories` (
                                        `completed_category_id` int NOT NULL AUTO_INCREMENT,
                                        `user_record_id` int NOT NULL,
                                        `routine_category_id` int NOT NULL,
                                        PRIMARY KEY (`completed_category_id`),
                                        KEY `fk_completed_categories_idx` (`user_record_id`),
                                        KEY `fk_completed_categories_routines_category_id_idx` (`routine_category_id`),
                                        CONSTRAINT `fk_completed_categories_id` FOREIGN KEY (`user_record_id`) REFERENCES `user_records` (`user_record_id`),
                                        CONSTRAINT `fk_completed_categories_routine_category_id` FOREIGN KEY (`routine_category_id`) REFERENCES `routine_categories` (`routine_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='완료 카테고리';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `interested_sports`
--

DROP TABLE IF EXISTS `interested_sports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interested_sports` (
                                     `interested_sports_id` int NOT NULL AUTO_INCREMENT,
                                     `user_id` int NOT NULL,
                                     `sports_id` int NOT NULL,
                                     PRIMARY KEY (`interested_sports_id`),
                                     KEY `user_id_idx` (`user_id`),
                                     KEY `sports_id_idx` (`sports_id`),
                                     CONSTRAINT `fk_interested_sports_sports_id` FOREIGN KEY (`sports_id`) REFERENCES `sports` (`sports_id`),
                                     CONSTRAINT `fk_interested_sports_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='관심 운동';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
                                 `notification_id` int NOT NULL AUTO_INCREMENT,
                                 `user_id` int NOT NULL,
                                 `message` varchar(100) COLLATE utf8mb4_bin NOT NULL,
                                 `is_reading` tinyint NOT NULL DEFAULT '0',
                                 `relation_function` varchar(45) COLLATE utf8mb4_bin NOT NULL,
                                 `created_at` timestamp NOT NULL,
                                 PRIMARY KEY (`notification_id`),
                                 KEY `fk_notifications_user_id_idx` (`user_id`),
                                 CONSTRAINT `fk_notifications_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='알림';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
                           `review_id` int NOT NULL AUTO_INCREMENT,
                           `user_id` int NOT NULL,
                           `coach_id` int NOT NULL,
                           `contents` text COLLATE utf8mb4_bin NOT NULL,
                           `stars` int NOT NULL,
                           `created_at` timestamp NOT NULL,
                           PRIMARY KEY (`review_id`),
                           KEY `fk_reviews_user_id_idx` (`user_id`),
                           KEY `fk_reviews_coach_id_idx` (`coach_id`),
                           CONSTRAINT `fk_reviews_coach_id` FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`coach_id`),
                           CONSTRAINT `fk_reviews_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='리뷰';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `routine_categories`
--

DROP TABLE IF EXISTS `routine_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `routine_categories` (
                                      `routine_category_id` int NOT NULL AUTO_INCREMENT,
                                      `routine_id` int NOT NULL,
                                      `category_name` varchar(45) COLLATE utf8mb4_bin NOT NULL,
                                      PRIMARY KEY (`routine_category_id`),
                                      KEY `routine_id_idx` (`routine_id`),
                                      CONSTRAINT `fk_routine_categories_routine_id` FOREIGN KEY (`routine_id`) REFERENCES `routines` (`routine_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='운동 루틴 카테고리';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `routines`
--

DROP TABLE IF EXISTS `routines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `routines` (
                            `routine_id` int NOT NULL AUTO_INCREMENT,
                            `user_id` int NOT NULL,
                            `coach_id` int DEFAULT NULL,
                            `sports_id` int NOT NULL,
                            `routine_name` varchar(45) COLLATE utf8mb4_bin NOT NULL,
                            PRIMARY KEY (`routine_id`),
                            KEY `user_id_idx` (`user_id`),
                            KEY `sports_id_idx` (`sports_id`),
                            KEY `coach_id_idx` (`coach_id`),
                            CONSTRAINT `fk_routines_coach_id` FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`coach_id`),
                            CONSTRAINT `fk_routines_sports_id` FOREIGN KEY (`sports_id`) REFERENCES `sports` (`sports_id`),
                            CONSTRAINT `fk_routines_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='운동 루틴';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sports`
--

DROP TABLE IF EXISTS `sports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sports` (
                          `sports_id` int NOT NULL AUTO_INCREMENT,
                          `sports_name` varchar(45) COLLATE utf8mb4_bin NOT NULL,
                          PRIMARY KEY (`sports_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='운동 종목';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_coach_likes`
--

DROP TABLE IF EXISTS `user_coach_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_coach_likes` (
                                    `user_coach_like_id` int NOT NULL AUTO_INCREMENT,
                                    `user_id` int NOT NULL,
                                    `coach_id` int NOT NULL,
                                    PRIMARY KEY (`user_coach_like_id`),
                                    KEY `fk_user_coach_likes_user_id_idx` (`user_id`),
                                    KEY `fk_user_coach_likes_coach_id_idx` (`coach_id`),
                                    CONSTRAINT `fk_user_coach_likes_coach_id` FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`coach_id`),
                                    CONSTRAINT `fk_user_coach_likes_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='관심 코치';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_coach_matching`
--

DROP TABLE IF EXISTS `user_coach_matching`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_coach_matching` (
                                       `user_coach_matching_id` int NOT NULL AUTO_INCREMENT,
                                       `user_id` int NOT NULL,
                                       `coach_id` int NOT NULL,
                                       `is_matching` tinyint NOT NULL DEFAULT '0',
                                       PRIMARY KEY (`user_coach_matching_id`),
                                       KEY `fk_user_coach_matching_user_id_idx` (`user_id`),
                                       KEY `fk_user_coach_matching_coach_id_idx` (`coach_id`),
                                       CONSTRAINT `fk_user_coach_matching_coach_id` FOREIGN KEY (`coach_id`) REFERENCES `coaches` (`coach_id`),
                                       CONSTRAINT `fk_user_coach_matching_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='매칭 회원';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_records`
--

DROP TABLE IF EXISTS `user_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_records` (
                                `user_record_id` int NOT NULL AUTO_INCREMENT,
                                `user_id` int NOT NULL,
                                `weight` int DEFAULT NULL,
                                `skeletal_muscle` int DEFAULT NULL,
                                `fat_percentage` int DEFAULT NULL,
                                `bmi` double DEFAULT NULL,
                                `created_at` date NOT NULL,
                                PRIMARY KEY (`user_record_id`),
                                KEY `fk_user_records_user_id_idx` (`user_id`),
                                CONSTRAINT `fk_user_records_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='유저 기록';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
                         `user_id` int NOT NULL AUTO_INCREMENT,
                         `nickname` varchar(45) COLLATE utf8mb4_bin NOT NULL,
                         `email` varchar(45) COLLATE utf8mb4_bin NOT NULL,
                         `password` varchar(45) COLLATE utf8mb4_bin NOT NULL,
                         `salt` varchar(128) COLLATE utf8mb4_bin NOT NULL,
                         `profile_image_url` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
                         `gender` enum('M','W') COLLATE utf8mb4_bin DEFAULT NULL,
                         `local_info` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
                         `introduction` text COLLATE utf8mb4_bin,
                         PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-27 18:49:19
