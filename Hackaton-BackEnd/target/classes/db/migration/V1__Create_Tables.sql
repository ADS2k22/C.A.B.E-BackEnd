DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `nome` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `account_non_expired` bit(1) DEFAULT NULL,
  `account_non_locked` bit(1) DEFAULT NULL,
  `credentials_non_expired` bit(1) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_stmxd7hj3xgi78gsojura7pm7` (`username`)
);

DROP TABLE IF EXISTS `permission`;
CREATE TABLE IF NOT EXISTS `permission`(
	`id` bigint(20) NOT NULL auto_increment,
    `description` varchar(255) DEFAULT NULL,
    PRIMARY KEY(`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `user_permission`;
CREATE TABLE IF NOT EXISTS `user_permission` (
	`id_user` bigint(20) NOT NULL,
    `id_permission` bigint(20) NOT NULL,
    PRIMARY KEY (`id_user`, `id_permission`),
    KEY `fk_user_permission_permission` (`id_permission`),
    CONSTRAINT `fk_user_permission` foreign key (`id_user`) references `user`(`id`),
    CONSTRAINT `fk_user_permission_permission` foreign key (`id_permission`) references `permission`(`id`)
) ENGINE=InnoDB;