//sellerstockdb

DROP TABLE IF EXISTS `stocks`;
CREATE TABLE `stocks`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `sellerid` bigint(20) NOT NULL,
  `pri` int(11) NULL DEFAULT NULL,
  `country` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `countrycode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `statename` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `statecode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `countyname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `countycode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cityname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `citycode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zipcode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lat` decimal(10, 6) NULL DEFAULT NULL,
  `lng` decimal(10, 6) NULL DEFAULT NULL,
  `ct` datetime(0) NOT NULL,
  `ut` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `seller_sites`;
CREATE TABLE `seller_sites`  (
  `id` bigint(20) NOT NULL,
  `sellerid` bigint(20) NULL DEFAULT NULL,
  `siteid` int(11) NULL DEFAULT NULL,
  `mode` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `seller_sitestocks`;
CREATE TABLE `seller_sitestocks`  (
  `id` bigint(20) NOT NULL,
  `sellerid` bigint(20) NULL DEFAULT NULL,
  `siteid` int(11) NULL DEFAULT NULL,
  `stockid` bigint(20) NULL DEFAULT NULL,
  `pri` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `seller_stockpolicy`;
CREATE TABLE `seller_stockpolicy`  (
  `id` bigint(20) NOT NULL,
  `sellerid` bigint(20) NULL DEFAULT NULL,
  `siteid` int(11) NULL DEFAULT NULL,
  `countrycode` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `statecode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `countycode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `stockidlist` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ct` datetime(0) NULL DEFAULT NULL,
  `ut` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

//skustockdb
DROP TABLE IF EXISTS `sku_stockinfos`;
CREATE TABLE `sku_stockinfos`  (
  `id` bigint(20) NOT NULL,
  `skuid` bigint(20) NULL DEFAULT NULL,
  `stockid` bigint(20) NULL DEFAULT NULL,
  `sellerid` bigint(20) NULL DEFAULT NULL,
  `realnum` int(11) NULL DEFAULT NULL,
  `locknum` int(11) NULL DEFAULT NULL,
  `ut` datetime(0) NULL DEFAULT NULL,
  `ct` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `sku_lockinfos`;
CREATE TABLE `sku_lockinfos`  (
  `id` bigint(20) NOT NULL,
  `orderid` bigint(20) NULL DEFAULT NULL,
  `skuid` bigint(20) NULL DEFAULT NULL,
  `stockid` bigint(20) NULL DEFAULT NULL,
  `locknum` int(11) NULL DEFAULT NULL,
  `ct` datetime(0) NULL DEFAULT NULL,
  `ut` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `sku_safenums`;
CREATE TABLE `sku_safenums`  (
  `id` bigint(20) NOT NULL,
  `siteid` int(11) NULL DEFAULT NULL,
  `skuid` bigint(20) NULL DEFAULT NULL,
  `safenum` int(11) NULL DEFAULT NULL,
  `ct` datetime(0) NULL DEFAULT NULL,
  `ut` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

//stocklogdb
DROP TABLE IF EXISTS `stock_logs`;
CREATE TABLE `stock_logs`  (
  `id` bigint(20) NOT NULL,
  `uid` bigint(20) NULL DEFAULT NULL,
  `orderid` bigint(20) NULL DEFAULT NULL,
  `siteid` int(11) NULL DEFAULT NULL,
  `from` tinyint(4) NULL DEFAULT NULL,
  `optype` tinyint(4) NULL DEFAULT NULL,
  `skustockinfo` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ct` datetime(0) NULL DEFAULT NULL,
  `ut` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
