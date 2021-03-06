# ************************************************************
# Sequel Pro SQL dump
# Version 3408
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.5.12)
# Database: malice
# Generation Time: 2011-11-11 17:36:33 +0100
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table CHIUSURE
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CHIUSURE`;

CREATE TABLE `CHIUSURE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `meseRiferimento` datetime DEFAULT NULL,
  `numeroIncassiMese` int(11) DEFAULT NULL,
  `numeroSospesiMese` int(11) DEFAULT NULL,
  `totaleCommissioniMese` bigint(20) DEFAULT NULL,
  `totaleImportoRimessaLloydsMese` bigint(20) DEFAULT NULL,
  `totalePremiMese` bigint(20) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `CHIUSURE` WRITE;
/*!40000 ALTER TABLE `CHIUSURE` DISABLE KEYS */;

INSERT INTO `CHIUSURE` (`id`, `meseRiferimento`, `numeroIncassiMese`, `numeroSospesiMese`, `totaleCommissioniMese`, `totaleImportoRimessaLloydsMese`, `totalePremiMese`, `version`)
VALUES
	(1,'2011-11-00 00:00:00',22,3232,4343,333,654656,1);

/*!40000 ALTER TABLE `CHIUSURE` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table CONTRAENTE
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CONTRAENTE`;

CREATE TABLE `CONTRAENTE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cap` varchar(255) DEFAULT NULL,
  `cfPiva` varchar(255) DEFAULT NULL,
  `citta` varchar(255) DEFAULT NULL,
  `identificativo` varchar(255) DEFAULT NULL,
  `indirizzo1` varchar(255) DEFAULT NULL,
  `indirizzo2` varchar(255) DEFAULT NULL,
  `persona` varchar(255) DEFAULT NULL,
  `ragSoc` varchar(255) DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `CONTRAENTE` WRITE;
/*!40000 ALTER TABLE `CONTRAENTE` DISABLE KEYS */;

INSERT INTO `CONTRAENTE` (`id`, `cap`, `cfPiva`, `citta`, `identificativo`, `indirizzo1`, `indirizzo2`, `persona`, `ragSoc`, `version`)
VALUES
	(1,'20900','crmdta90e21e609m','Monza (MB)','Mario','via zucchi 7','via Remigio 4','Fisica','',1);

/*!40000 ALTER TABLE `CONTRAENTE` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table DOCUMENTO
# ------------------------------------------------------------

DROP TABLE IF EXISTS `DOCUMENTO`;

CREATE TABLE `DOCUMENTO` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `DOCUMENTO` WRITE;
/*!40000 ALTER TABLE `DOCUMENTO` DISABLE KEYS */;

INSERT INTO `DOCUMENTO` (`id`, `name`, `version`)
VALUES
	(1,'POL_FU966057567',1),
	(2,'COL_232895643',1),
	(3,'POL_ED232148467',1);

/*!40000 ALTER TABLE `DOCUMENTO` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table MESE_DA_CHIUDERE
# ------------------------------------------------------------

DROP TABLE IF EXISTS `MESE_DA_CHIUDERE`;

CREATE TABLE `MESE_DA_CHIUDERE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `importoProvvigioniVal` bigint(20) NOT NULL,
  `importoRimessaVal` bigint(20) NOT NULL,
  `mese` datetime DEFAULT NULL,
  `titoliIncassatiNum` bigint(20) NOT NULL,
  `titoliSospesiNum` bigint(20) NOT NULL,
  `version` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `MESE_DA_CHIUDERE` WRITE;
/*!40000 ALTER TABLE `MESE_DA_CHIUDERE` DISABLE KEYS */;

INSERT INTO `MESE_DA_CHIUDERE` (`id`, `importoProvvigioniVal`, `importoRimessaVal`, `mese`, `titoliIncassatiNum`, `titoliSospesiNum`, `version`)
VALUES
	(1,4234,432,'2011-09-00 00:00:00',423,423,1);

/*!40000 ALTER TABLE `MESE_DA_CHIUDERE` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table PREMIO
# ------------------------------------------------------------

DROP TABLE IF EXISTS `PREMIO`;

CREATE TABLE `PREMIO` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agenziaName` varchar(255) DEFAULT NULL,
  `percentualePremio` bigint(20) NOT NULL,
  `premioLordo` bigint(20) NOT NULL,
  `premioNetto` bigint(20) NOT NULL,
  `provvigioni` bigint(20) NOT NULL,
  `tasse` bigint(20) NOT NULL,
  `version` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `PREMIO` WRITE;
/*!40000 ALTER TABLE `PREMIO` DISABLE KEYS */;

INSERT INTO `PREMIO` (`id`, `agenziaName`, `percentualePremio`, `premioLordo`, `premioNetto`, `provvigioni`, `tasse`, `version`)
VALUES
	(1,'Reale Mutua',76,234200,210000,430000,96000,1),
	(2,'Furness',23,56677,20000,10000,5000,1);

/*!40000 ALTER TABLE `PREMIO` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table TITOLO
# ------------------------------------------------------------

DROP TABLE IF EXISTS `TITOLO`;

CREATE TABLE `TITOLO` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `broker` varchar(255) DEFAULT NULL,
  `cfpi` varchar(255) DEFAULT NULL,
  `contraenteIdentificativo` varchar(255) DEFAULT NULL,
  `inizio` datetime DEFAULT NULL,
  `numero` varchar(20) DEFAULT NULL,
  `premioTotale` varchar(255) DEFAULT NULL,
  `scadenza` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `contraente_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK938329331045F8EB` (`contraente_id`),
  CONSTRAINT `FK938329331045F8EB` FOREIGN KEY (`contraente_id`) REFERENCES `CONTRAENTE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `TITOLO` WRITE;
/*!40000 ALTER TABLE `TITOLO` DISABLE KEYS */;

INSERT INTO `TITOLO` (`id`, `broker`, `cfpi`, `contraenteIdentificativo`, `inizio`, `numero`, `premioTotale`, `scadenza`, `status`, `tipo`, `version`, `contraente_id`)
VALUES
	(1,'FURNESS','BNC LGU 92D04 A794L','Mario Rossi','2011-11-09 00:00:00','FU12578912','4433','2012-09-09 00:00:00','INCASSATA','RC_P',3,1),
	(2,'FURNESS','BNCLGU92D04A794L','Luigi Bianchi','2011-11-09 00:00:01','FU12578912','125000','2012-11-09 00:00:00','SOSPESA','RC_P',1,NULL),
	(3,'FURNESS','RSSMRA98D04A794F','Mario Rossi','2011-11-09 00:00:02','FU56874919','189000','2012-11-09 00:00:01','EMESSA','RC_P',1,NULL),
	(4,'FURNESS','STPFNC88M13A794S','Franco Stoppa','2011-11-09 00:00:03','FU25698742','78200','2012-11-09 00:00:02','INCASSATA','RC_P',1,NULL),
	(5,'BDB','TGNGNN45C22H501H','Gianni Togni','2011-11-09 00:00:04','BD23588974','84500','2012-11-09 00:00:03','ARRETRATA','RC_P',5,NULL),
	(6,'BDB','RVINZE35A26H501W','Enzo Riva','2011-11-09 00:00:05','BD22567891','112000','2012-11-09 00:00:04','ARRETRATA','RC_P',2,NULL),
	(7,'BDB','BNCLGU80D04A794L','Luigi Bianchi','2011-11-09 00:00:06','BD22568986','136000','2012-11-09 00:00:05','INCASSATA','RC_P',1,NULL),
	(8,'FURNESS','RSSMRA73D04A794F','Enzo Tartaglia','2011-11-09 00:00:07','FU13242625','170000','2012-11-09 00:00:06','SOSPESA','RC_P',1,NULL),
	(9,'FURNESS','TRTNZE78H28H501N','Gianni Trotta','2011-11-09 00:00:08','FU23689871','156000','2012-11-09 00:00:07','SOSPESA','RC_P',1,NULL),
	(10,'BDB','TRTGNN65C22H501M','Ugo Della Valle','2011-11-09 00:00:09','BD22658743','136000','2012-11-09 00:00:08','ARRETRATA','RC_P',1,NULL),
	(11,'FURNESS','DLLGUO63A24F205P','Andrea Zucconi','2011-11-09 00:00:10','FU33698745','187000','2012-11-09 00:00:09','INCASSATA','RC_P',1,NULL),
	(12,'BDB','ZCCNDR71D20F205W','Francesco Giorgi','2011-11-09 00:00:11','BD22358714','98000','2012-11-09 00:00:10','EMESSA','RC_P',1,NULL),
	(13,'FURNESS','GRGFNC55C16F205O','Alessandro De Felicibus','2011-11-09 00:00:12','FU22568741','89000','2012-11-09 00:00:11','INCASSATA','RC_P',1,NULL),
	(14,'FURNESS','DFLLSN48C16F839R','Antonio Conte','2011-11-09 00:00:13','FU44541257','118700','2012-11-09 00:00:12','INCASSATA','RC_P',1,NULL),
	(15,'BDB','CNTNTN69M17F839A','Francesco Lippi','2011-11-09 00:00:14','BD55657841','298000','2012-11-09 00:00:13','EMESSA','RC_P',1,NULL),
	(16,'FURNESS','LPPFNC88P13D612F','Carlo Ancellotti','2011-11-09 00:00:15','FU55657783','116800','2012-11-09 00:00:14','INCASSATA','RC_P',1,NULL),
	(17,'FURNESS','NCLCRL95M17L219R','Dario Bianchi','2011-11-09 00:00:16','FU88475512','155800','2012-11-09 00:00:15','ARRETRATA','RC_P',1,NULL),
	(18,'BDB','BNCDRA79T21L219F','Luigi Bianchi','2011-11-09 00:00:17','BD22315677','158700','2012-11-09 00:00:16','INCASSATA','RC_P',1,NULL),
	(19,'BDB','BNCLGU80D04A794L','Giuseppe Stagliano','2011-11-09 00:00:18','BD22535158','198500','2012-11-09 00:00:17','SOSPESA','RC_P',1,NULL),
	(20,'BDB','STGGPP72L15L219P','Andrea De Martiniis','2011-11-09 00:00:19','BD22320577','88000','2012-11-09 00:00:18','EMESSA','RC_P',1,NULL),
	(21,'FURNESS','DMRNDR72R11G224K','Luigi Bianchi','2011-11-09 00:00:20','FU87898123','75000','2012-11-09 00:00:19','INCASSATA','RC_P',1,NULL),
	(22,'FURNESS','BNCLGU92D04A794L','Mario Rossi','2011-11-09 00:00:21','FU12578912','125000','2012-11-09 00:00:20','EMESSA','RC_P',1,NULL),
	(23,'FURNESS','RSSMRA98D04A794F','Franco Stoppa','2011-11-09 00:00:22','FU56874919','189000','2012-11-09 00:00:21','EMESSA','RC_P',1,NULL),
	(24,'FURNESS','STPFNC88M13A794S','Gianni Togni','2011-11-09 00:00:23','FU25698742','78200','2012-11-09 00:00:22','INCASSATA','RC_P',1,NULL),
	(25,'BDB','TGNGNN45C22H501H','Enzo Riva','2011-11-09 00:00:24','BD23588974','84500','2012-11-09 00:00:23','SOSPESA','RC_P',1,NULL),
	(26,'BDB','RVINZE35A26H501W','Luigi Bianchi','2011-11-09 00:00:25','BD22567891','112000','2012-11-09 00:00:24','SOSPESA','RC_P',1,NULL),
	(27,'BDB','BNCLGU80D04A794L','Enzo Tartaglia','2011-11-09 00:00:26','BD22568986','136000','2012-11-09 00:00:25','ARRETRATA','RC_P',1,NULL),
	(28,'FURNESS','RSSMRA73D04A794F','Gianni Trotta','2011-11-09 00:00:27','FU13242625','170000','2012-11-09 00:00:26','INCASSATA','RC_P',1,NULL),
	(29,'FURNESS','TRTNZE78H28H501N','Ugo Della Valle','2011-11-09 00:00:28','FU23689871','156000','2012-11-09 00:00:27','EMESSA','RC_P',1,NULL),
	(30,'BDB','TRTGNN65C22H501M','Andrea Zucconi','2011-11-09 00:00:29','BD22658743','136000','2012-11-09 00:00:28','INCASSATA','RC_P',1,NULL),
	(31,'FURNESS','DLLGUO63A24F205P','Francesco Giorgi','2011-11-09 00:00:30','FU33698745','187000','2012-11-09 00:00:29','INCASSATA','RC_P',1,NULL),
	(32,'BDB','ZCCNDR71D20F205W','Alessandro De Felicibus','2011-11-09 00:00:31','BD22358714','98000','2012-11-09 00:00:30','EMESSA','RC_P',1,NULL),
	(33,'FURNESS','GRGFNC55C16F205O','Antonio Conte','2011-11-09 00:00:32','FU22568741','89000','2012-11-09 00:00:31','INCASSATA','RC_P',1,NULL),
	(34,'FURNESS','DFLLSN48C16F839R','Francesco Lippi','2011-11-09 00:00:33','FU44541257','118700','2012-11-09 00:00:32','ARRETRATA','RC_P',1,NULL),
	(35,'BDB','BNCLGU92D04A794L','Carlo Ancellotti','2011-11-09 00:00:34','BD55657841','298000','2012-11-09 00:00:33','INCASSATA','RC_P',1,NULL),
	(36,'FURNESS','RSSMRA98D04A794F','Dario Bianchi','2011-11-09 00:00:35','FU55657783','116800','2012-11-09 00:00:34','SOSPESA','RC_P',1,NULL),
	(37,'FURNESS','STPFNC88M13A794S','Luigi Bianchi','2011-11-09 00:00:36','FU88475512','155800','2012-11-09 00:00:35','EMESSA','RC_P',1,NULL),
	(38,'BDB','TGNGNN45C22H501H','Giuseppe Stagliano','2011-11-09 00:00:37','BD22315677','158700','2012-11-09 00:00:36','INCASSATA','RC_P',1,NULL),
	(39,'BDB','RVINZE35A26H501W','Andrea De Martiniis','2011-11-09 00:00:38','BD22535158','125000','2012-11-09 00:00:37','EMESSA','RC_P',1,NULL),
	(40,'BDB','BNCLGU80D04A794L','Luigi Bianchi','2011-11-09 00:00:39','BD22320577','189000','2012-11-09 00:00:38','EMESSA','RC_P',1,NULL),
	(41,'FURNESS','RSSMRA73D04A794F','Mario Rossi','2011-11-09 00:00:40','FU87898123','78200','2012-11-09 00:00:39','INCASSATA','RC_P',1,NULL),
	(42,'FURNESS','TRTNZE78H28H501N','Franco Stoppa','2011-11-09 00:00:41','FU12578912','84500','2012-11-09 00:00:40','SOSPESA','RC_P',1,NULL),
	(43,'FURNESS','TRTGNN65C22H501M','Gianni Togni','2011-11-09 00:00:42','FU56874919','112000','2012-11-09 00:00:41','SOSPESA','RC_P',1,NULL),
	(44,'FURNESS','DLLGUO63A24F205P','Enzo Riva','2011-11-09 00:00:43','FU25698742','136000','2012-11-09 00:00:42','INCASSATA','RC_P',2,NULL),
	(45,'BDB','ZCCNDR71D20F205W','Luigi Bianchi','2011-11-09 00:00:44','BD23588974','170000','2012-11-09 00:00:43','INCASSATA','RC_P',1,NULL),
	(46,'BDB','GRGFNC55C16F205O','Enzo Tartaglia','2011-11-09 00:00:45','BD22567891','156000','2012-11-09 00:00:44','EMESSA','RC_P',1,NULL),
	(47,'BDB','DFLLSN48C16F839R','Gianni Trotta','2011-11-09 00:00:46','BD22568986','136000','2012-11-09 00:00:45','INCASSATA','RC_P',1,NULL),
	(48,'FURNESS','CNTNTN69M17F839A','Ugo Della Valle','2011-11-09 00:00:47','FU13242625','187000','2012-11-09 00:00:46','INCASSATA','RC_P',1,NULL),
	(49,'FURNESS','LPPFNC88P13D612F','Andrea Zucconi','2011-11-09 00:00:48','FU23689871','98000','2012-11-09 00:00:47','EMESSA','RC_P',1,NULL),
	(50,'BDB','NCLCRL95M17L219R','Francesco Giorgi','2011-11-09 00:00:49','BD22658743','89000','2012-11-09 00:00:48','INCASSATA','RC_P',1,NULL),
	(51,'FURNESS','BNCDRA79T21L219F','Alessandro De Felicibus','2011-11-09 00:00:50','FU33698745','118700','2012-11-09 00:00:49','ARRETRATA','RC_P',1,NULL),
	(52,'BDB','BNCLGU80D04A794L','Antonio Conte','2011-11-09 00:00:51','BD22358714','298000','2012-11-09 00:00:50','INCASSATA','RC_P',1,NULL),
	(53,'FURNESS','STGGPP72L15L219P','Francesco Lippi','2011-11-09 00:00:52','FU22568741','116800','2012-11-09 00:00:51','SOSPESA','RC_P',1,NULL),
	(54,'FURNESS','DMRNDR72R11G224K','Carlo Ancellotti','2011-11-09 00:00:53','FU44541257','155800','2012-11-09 00:00:52','EMESSA','RC_P',1,NULL),
	(55,'BDB','BNCLGU92D04A794L','Dario Bianchi','2011-11-09 00:00:54','BD55657841','158700','2012-11-09 00:00:53','INCASSATA','RC_P',1,NULL),
	(56,'FURNESS','RSSMRA98D04A794F','Luigi Bianchi','2011-11-09 00:00:55','FU55657783','125000','2012-11-09 00:00:54','EMESSA','RC_P',1,NULL),
	(57,'FURNESS','STPFNC88M13A794S','Giuseppe Stagliano','2011-11-09 00:00:56','FU88475512','189000','2012-11-09 00:00:55','EMESSA','RC_P',1,NULL),
	(58,'BDB','TGNGNN45C22H501H','Andrea De Martiniis','2011-11-09 00:00:57','BD22315677','78200','2012-11-09 00:00:56','INCASSATA','RC_P',1,NULL),
	(59,'BDB','RVINZE35A26H501W','Luigi Bianchi','2011-11-09 00:00:58','BD22535158','84500','2012-11-09 00:00:57','SOSPESA','RC_P',1,NULL),
	(60,'BDB','BNCLGU80D04A794L','Mario Rossi','2011-11-09 00:00:59','BD22320577','112000','2012-11-09 00:00:58','SOSPESA','RC_P',1,NULL),
	(61,'FURNESS','RSSMRA73D04A794F','Franco Stoppa','0000-00-00 00:00:00','FU87898123','136000','2012-11-09 00:00:59','ARRETRATA','RC_P',1,NULL);

/*!40000 ALTER TABLE `TITOLO` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table TITOLO_DOCUMENTO
# ------------------------------------------------------------

DROP TABLE IF EXISTS `TITOLO_DOCUMENTO`;

CREATE TABLE `TITOLO_DOCUMENTO` (
  `TITOLO_id` bigint(20) NOT NULL,
  `documenti_id` bigint(20) NOT NULL,
  PRIMARY KEY (`TITOLO_id`,`documenti_id`),
  UNIQUE KEY `documenti_id` (`documenti_id`),
  KEY `FK1B89E288D129866B` (`TITOLO_id`),
  KEY `FK1B89E288D5660FCF` (`documenti_id`),
  CONSTRAINT `FK1B89E288D129866B` FOREIGN KEY (`TITOLO_id`) REFERENCES `TITOLO` (`id`),
  CONSTRAINT `FK1B89E288D5660FCF` FOREIGN KEY (`documenti_id`) REFERENCES `DOCUMENTO` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `TITOLO_DOCUMENTO` WRITE;
/*!40000 ALTER TABLE `TITOLO_DOCUMENTO` DISABLE KEYS */;

INSERT INTO `TITOLO_DOCUMENTO` (`TITOLO_id`, `documenti_id`)
VALUES
	(1,1),
	(1,2),
	(1,3);

/*!40000 ALTER TABLE `TITOLO_DOCUMENTO` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table TITOLO_PREMIO
# ------------------------------------------------------------

DROP TABLE IF EXISTS `TITOLO_PREMIO`;

CREATE TABLE `TITOLO_PREMIO` (
  `TITOLO_id` bigint(20) NOT NULL,
  `premi_id` bigint(20) NOT NULL,
  PRIMARY KEY (`TITOLO_id`,`premi_id`),
  UNIQUE KEY `premi_id` (`premi_id`),
  KEY `FKF3C0655CD129866B` (`TITOLO_id`),
  KEY `FKF3C0655CF8227C3C` (`premi_id`),
  CONSTRAINT `FKF3C0655CD129866B` FOREIGN KEY (`TITOLO_id`) REFERENCES `TITOLO` (`id`),
  CONSTRAINT `FKF3C0655CF8227C3C` FOREIGN KEY (`premi_id`) REFERENCES `PREMIO` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `TITOLO_PREMIO` WRITE;
/*!40000 ALTER TABLE `TITOLO_PREMIO` DISABLE KEYS */;

INSERT INTO `TITOLO_PREMIO` (`TITOLO_id`, `premi_id`)
VALUES
	(1,1),
	(1,2);

/*!40000 ALTER TABLE `TITOLO_PREMIO` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
