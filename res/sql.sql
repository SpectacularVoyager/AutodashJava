-- Temp.chat_history definition

CREATE TABLE `chat_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NULL DEFAULT NULL,
  `request` text,
  `response` text,
  PRIMARY KEY (`id`),
  KEY `chat_history_timestamp_IDX` (`timestamp`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;