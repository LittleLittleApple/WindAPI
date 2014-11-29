CREATE TABLE IF NOT EXISTS `kdata` (
  `ktype` varchar(100) NOT NULL DEFAULT '',
  `stock_code` varchar(100) NOT NULL DEFAULT '',
  `w_time` varchar(100) NOT NULL DEFAULT '',
  `open` varchar(255) DEFAULT NULL,
  `close` varchar(255) DEFAULT NULL,
  `high` varchar(255) DEFAULT NULL,
  `low` varchar(255) DEFAULT NULL,
  `volume` varchar(255) DEFAULT NULL,
  `amt` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ktype`,`stock_code`,`w_time`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
