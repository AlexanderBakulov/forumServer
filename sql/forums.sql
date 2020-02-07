
DROP DATABASE IF EXISTS forums;
CREATE DATABASE `forums`;
USE `forums`;

CREATE TABLE user (
id INT(11) NOT NULL AUTO_INCREMENT,
name VARCHAR(20) NOT NULL,
password VARCHAR(30) NOT NULL,
email VARCHAR(20) NOT NULL,
deleted BOOLEAN NOT NULL DEFAULT FALSE,
privilege ENUM('REGULAR','SUPER') NOT NULL DEFAULT 'REGULAR',
registered DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
status ENUM('FULL', 'LIMITED') NOT NULL DEFAULT 'FULL',
ban_exit_time DATETIME DEFAULT NULL,
ban_count INT(4) NOT NULL DEFAULT 0,
PRIMARY KEY (id),
UNIQUE KEY name (name),
KEY password (password),
UNIQUE KEY email (email)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE session (
id INT(11) NOT NULL AUTO_INCREMENT,
token CHAR(36) DEFAULT NULL,
user_id INT(11),
PRIMARY KEY (id),
UNIQUE KEY token (token),
UNIQUE KEY user_id (user_id),
FOREIGN KEY (user_id) REFERENCES user (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO user (name, password, email, privilege)
VALUES ('admin', 'SuperUnbreakablePassword1!', 'admin@forums.com', 'SUPER');

CREATE TABLE forum (
id INT(11) NOT NULL AUTO_INCREMENT,
name VARCHAR(50) NOT NULL,
moderate ENUM('MODERATED', 'UNMODERATED') DEFAULT 'UNMODERATED',
read_only BOOLEAN DEFAULT FALSE,
user_id INT(11),
PRIMARY KEY (id),
UNIQUE KEY name (name),
KEY moderate (moderate),
KEY read_only (read_only),
FOREIGN KEY (user_id) REFERENCES user (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE header (
id INT(11) NOT NULL AUTO_INCREMENT,
subject VARCHAR(50) NOT NULL,
priority ENUM('HIGH', 'NORMAL', 'LOW') DEFAULT 'NORMAL',
forum_id INT(11),
user_id INT(11),
PRIMARY KEY (id),
KEY subject (subject),
KEY priority (priority),
FOREIGN KEY (forum_id) REFERENCES forum (id) ON DELETE CASCADE,
FOREIGN KEY (user_id) REFERENCES user (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE tags (
id INT(11) NOT NULL AUTO_INCREMENT,
tag VARCHAR(50),
header_id INT(11) NOT NULL,
PRIMARY KEY (id),
KEY tag (tag),
FOREIGN KEY (header_id) REFERENCES header (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE post (
id INT(11) NOT NULL AUTO_INCREMENT,
publish_date DATETIME DEFAULT NULL,
header_id INT(11),
user_id INT(11),
ancestor_id INT(11),
PRIMARY KEY (id),
KEY header_id (header_id),
KEY user_id (user_id),
KEY ancestor_id (ancestor_id),
FOREIGN KEY (header_id) REFERENCES header (id) ON DELETE CASCADE,
FOREIGN KEY (user_id) REFERENCES user (id),
FOREIGN KEY (ancestor_id) REFERENCES post (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE history (
id INT(11) NOT NULL AUTO_INCREMENT,
post_body TEXT(1000),
published ENUM('PUBLISHED','UNPUBLISHED') DEFAULT 'UNPUBLISHED',
post_id INT(11),
PRIMARY KEY (id),
KEY published (published),
KEY (post_id),
FOREIGN KEY (post_id) REFERENCES post (id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE rating (
id INT(11) NOT NULL AUTO_INCREMENT,
rate INT(1) NOT NULL,
post_id INT(11),
user_id INT(11),
PRIMARY KEY (id),
KEY rate (rate),
UNIQUE KEY post_user (post_id, user_id),
FOREIGN KEY (post_id) REFERENCES post (id) ON DELETE CASCADE,
FOREIGN KEY (user_id) REFERENCES user (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE empty (
id INT(11) NOT NULL AUTO_INCREMENT,
PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

