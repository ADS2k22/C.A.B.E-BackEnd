INSERT INTO permission (id, description) VALUES
(3, 'subscribed-user');

ALTER TABLE `curso` ADD COLUMN feedback INT DEFAULT NULL;

UPDATE `curso` SET feedback = 5 WHERE id BETWEEN 5 AND 10;
UPDATE `curso` SET feedback = 4 WHERE id < 5;
UPDATE `curso` SET feedback = 4 WHERE id BETWEEN 11 AND 20;
UPDATE `curso` SET feedback = 3 WHERE id BETWEEN 21 AND 25;
UPDATE `curso` SET feedback = 5 WHERE id > 25;

DROP TABLE IF EXISTS `video_user_relacao`;
CREATE TABLE `video_user_relacao`(
	`id` BIGINT NOT NULL AUTO_INCREMENT,
    `concluido` BIT(1) NOT NULL,
    `video_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY(`id`),
    KEY `FK_video_id` (`video_id`),
    KEY `FK_user_id` (`user_id`),
    CONSTRAINT `FK_video_id` FOREIGN KEY (`video_id`) REFERENCES `video` (`id`),
    CONSTRAINT `FK_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);