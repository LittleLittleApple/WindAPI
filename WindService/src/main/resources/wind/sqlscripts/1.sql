CREATE TABLE `ws_kdata` (
  `stockCode` varchar(128) NOT NULL DEFAULT '' COMMENT '股票代码',
  `ktype` int(11) NOT NULL DEFAULT '-1' COMMENT 'K线类型',
  `w_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'stock时间',
  `open` bigint(20) DEFAULT '-1' COMMENT '开盘价',
  `high` bigint(20) DEFAULT '-1' COMMENT '最高价',
  `low` bigint(20) DEFAULT '-1' COMMENT '最低价',
  `close` bigint(20) DEFAULT '-1' COMMENT '收盘价',
  `volume` bigint(20) DEFAULT '-1' COMMENT '成效量',
  `amt` bigint(20) DEFAULT '-1' COMMENT '成交额',
  `ma5` bigint(20) DEFAULT '-1' COMMENT 'ma 5日均线',
  `ma10` bigint(20) DEFAULT '-1' COMMENT 'ma 10日均线',
  `ma20` bigint(20) DEFAULT '-1' COMMENT 'ma 20日均线',
  `adjfactor` bigint(20) DEFAULT NULL COMMENT '复权因子',
  `yearMonth` int(11) NOT NULL DEFAULT '-1' COMMENT '年月格式''201501''',
  `yearWeek` int(11) NOT NULL DEFAULT '-1' COMMENT '年周格式''201501''',
  `yearDate` int(11) NOT NULL DEFAULT '-1' COMMENT '年月日格式''20150101''',
  `createdAt` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '记录创建时间',
  `updatedAt` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '记录更新时间',
  PRIMARY KEY (`stockCode`,`ktype`,`w_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `db_version` (
  `dbVersion` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`dbVersion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO db_version (dbVersion) values(1);