-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        10.3.14-MariaDB-1:10.3.14+maria~bionic - mariadb.org binary distribution
-- 서버 OS:                        debian-linux-gnu
-- HeidiSQL 버전:                  10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- sb201 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `sb201` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `sb201`;

-- 테이블 sb201.adt_audit 구조 내보내기
CREATE TABLE IF NOT EXISTS `adt_audit` (
  `audit_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `member_id` bigint(20) NOT NULL COMMENT 'Member ID',
  `category` varchar(16) NOT NULL COMMENT 'Category',
  `ip` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT 'IP',
  `message` varchar(4096) DEFAULT NULL COMMENT 'Message',
  `created` datetime NOT NULL DEFAULT current_timestamp() COMMENT 'Created date',
  PRIMARY KEY (`audit_id`),
  KEY `ix_adtAudit_category_created` (`category`,`created`),
  KEY `ix_adtAudit_category_action_created` (`category`,`created`),
  KEY `ix_created` (`created`)
) ENGINE=InnoDB AUTO_INCREMENT=5231 DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 sb201.ast_asset 구조 내보내기
CREATE TABLE IF NOT EXISTS `ast_asset` (
  `asset_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Asset ID',
  `parent_id` bigint(20) NOT NULL COMMENT '0: Root',
  `type` int(10) unsigned NOT NULL COMMENT 'Asset type: 1:Organization, 2:Group',
  `category` int(10) unsigned NOT NULL COMMENT 'Category',
  `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Name',
  `code` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Code',
  `seq` int(10) unsigned NOT NULL DEFAULT 0 COMMENT 'Sequence for sorting',
  `created` datetime NOT NULL DEFAULT current_timestamp() COMMENT 'Created date',
  `updated` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Updated date',
  PRIMARY KEY (`asset_id`),
  KEY `ix_astAsset_assetId_code` (`asset_id`,`code`),
  KEY `ix_astAsset_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 sb201.ast_device 구조 내보내기
CREATE TABLE IF NOT EXISTS `ast_device` (
  `device_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `asset_id` bigint(20) NOT NULL COMMENT 'Asset ID',
  `category` int(11) NOT NULL COMMENT '1:Server, 2:Agent, 3:Camera',
  `type` int(11) NOT NULL COMMENT '1-1:VMS, 1-2:VAS, 1-3:NVR',
  `code` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Code',
  `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Name',
  `hostname` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Hostname',
  `port1` int(10) unsigned NOT NULL DEFAULT 0 COMMENT 'Port',
  `port2` int(10) unsigned NOT NULL DEFAULT 0 COMMENT 'Port',
  `port3` int(10) unsigned NOT NULL DEFAULT 0 COMMENT 'Port',
  `port4` int(10) unsigned NOT NULL DEFAULT 0 COMMENT 'Port',
  `manufactured_by` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Manufactured by',
  `model` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Model',
  `version` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Version',
  `firmware` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Firmware',
  `username` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Username',
  `password` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Password',
  `api_key` varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'API-Key',
  `url` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'URL',
  `timezone` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Timezone',
  `enabled` bit(1) NOT NULL DEFAULT b'0' COMMENT 'Enabled',
  `status` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'Status',
  `created` datetime NOT NULL DEFAULT current_timestamp() COMMENT 'Created date',
  `updated` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Updated date',
  PRIMARY KEY (`device_id`),
  KEY `ix_astDevice_assetId` (`asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 sb201.log_factory 구조 내보내기
CREATE TABLE IF NOT EXISTS `log_factory` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `factory_id` bigint(20) NOT NULL,
  `camera_id` bigint(20) NOT NULL,
  `event_type` int(11) NOT NULL,
  `origin_event_type` int(11) NOT NULL,
  `path` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `archived` int(11) NOT NULL DEFAULT 0,
  `target_id` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
  `meta` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`log_id`),
  KEY `ix_logFactory_date` (`date`),
  KEY `ix_logFactory_date_cameraId` (`date`,`camera_id`),
  KEY `ix_logFactory_date_factoryId` (`date`,`factory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 sb201.mbr_allowed_ip 구조 내보내기
CREATE TABLE IF NOT EXISTS `mbr_allowed_ip` (
  `network_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Network ID',
  `member_id` bigint(20) NOT NULL COMMENT 'Member ID',
  `ip_cidr` varchar(19) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'IP CIDR Notation',
  `created` datetime NOT NULL DEFAULT current_timestamp() COMMENT 'Created date',
  PRIMARY KEY (`network_id`),
  KEY `fk_mbrAllowedIp_netId` (`member_id`),
  CONSTRAINT `fk_mbrAllowedIp_netId` FOREIGN KEY (`member_id`) REFERENCES `mbr_member` (`member_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=175 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 sb201.mbr_member 구조 내보내기
CREATE TABLE IF NOT EXISTS `mbr_member` (
  `member_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Member ID',
  `group_id` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Group ID',
  `username` varchar(32) NOT NULL COMMENT 'Username',
  `email` varchar(256) NOT NULL COMMENT 'Email',
  `name` varchar(64) NOT NULL COMMENT 'Name',
  `enabled` bit(1) NOT NULL DEFAULT b'0' COMMENT 'Enabled',
  `roles` int(11) NOT NULL DEFAULT 0 COMMENT 'Role',
  `timezone` varchar(64) NOT NULL DEFAULT '' COMMENT 'Timezone',
  `password` varchar(72) NOT NULL DEFAULT '' COMMENT 'Password',
  `login_count` int(11) unsigned NOT NULL DEFAULT 0 COMMENT 'Login count',
  `failed_login_count` int(11) unsigned NOT NULL DEFAULT 0 COMMENT 'Failed login count',
  `last_success_login` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT 'Last success login',
  `last_failed_login` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT 'Last failed login',
  `last_read_message` int(11) unsigned NOT NULL DEFAULT 0 COMMENT 'Last read message',
  `last_password_change` datetime DEFAULT current_timestamp() COMMENT 'Last password change',
  `session_id` varchar(64) NOT NULL DEFAULT '' COMMENT 'Session ID',
  `created` datetime NOT NULL DEFAULT current_timestamp() COMMENT 'Created date',
  `updated` datetime NOT NULL DEFAULT current_timestamp() COMMENT 'Updated date',
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=262 DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 sb201.Members 구조 내보내기
CREATE TABLE IF NOT EXISTS `Members` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `age` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `Members_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 sb201.msg_message 구조 내보내기
CREATE TABLE IF NOT EXISTS `msg_message` (
  `message_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Message ID',
  `created` datetime NOT NULL COMMENT 'Created date',
  `receiver_id` bigint(20) NOT NULL COMMENT 'Receiver ID',
  `sender_id` bigint(20) NOT NULL COMMENT 'Sender ID',
  `category` varchar(16) NOT NULL COMMENT 'same as adt_audit''s category',
  `priority` int(11) NOT NULL COMMENT 'Priority',
  `message` varchar(256) NOT NULL COMMENT 'Message',
  `read` bit(1) NOT NULL DEFAULT b'0' COMMENT 'Read',
  `alarm` int(11) NOT NULL DEFAULT 0 COMMENT '0: none, 1: ui, 2: sound',
  `url` varchar(1024) NOT NULL COMMENT 'URL',
  PRIMARY KEY (`message_id`),
  KEY `ix_msgMessage_date_receiverId` (`created`,`receiver_id`),
  KEY `ix_msgMessage_date_receiverId_status` (`created`,`receiver_id`,`read`),
  KEY `fk_log_message_memberId` (`receiver_id`),
  CONSTRAINT `fk_log_message_memberId` FOREIGN KEY (`receiver_id`) REFERENCES `mbr_member` (`member_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
