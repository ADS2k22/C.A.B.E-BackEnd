DROP TABLE IF EXISTS `categoria`;
CREATE TABLE `categoria`(
	`id` BIGINT NOT NULL AUTO_INCREMENT,
    `nome` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_categoria` (`nome`)
);

DROP TABLE IF EXISTS `curso`;
CREATE TABLE `curso`(
	`id` BIGINT NOT NULL AUTO_INCREMENT,
    `nome` VARCHAR(255) NOT NULL UNIQUE,
    `carga_horaria` INT NOT NULL,
    `categoria_id` BIGINT NOT NULL,
    `trilha` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_curso` (`nome`),
    KEY `K_categoria_id` (`categoria_id`),
    CONSTRAINT `FK_curso_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`id`)
);

DROP TABLE IF EXISTS `video`;
CREATE TABLE `video`(
	`id` BIGINT NOT NULL AUTO_INCREMENT,
    `nome` VARCHAR(255) NOT NULL,
    `url` VARCHAR(255) NOT NULL UNIQUE,
    `posicao` INT NOT NULL,
    `curso_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_capitulo_id` (`curso_id`),
    CONSTRAINT `FK_capitulo_video` FOREIGN KEY (`curso_id`) REFERENCES `curso` (`id`)
);