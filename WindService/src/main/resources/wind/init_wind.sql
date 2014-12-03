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
) DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `wind_version` (
  `dbver` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`dbver`)
) DEFAULT CHARSET=utf8;
/*Note: please extend it to procedure when you want to write migrate script*/
INSERT INTO wind_version (dbver) values(1) ON DUPLICATE KEY UPDATE dbver=VALUES(dbver);