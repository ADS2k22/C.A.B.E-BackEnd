INSERT INTO `permission` (`id`, `description`) VALUES (1, 'manager');
INSERT INTO `permission` (`id`, `description`) VALUES (2, 'common_user');

INSERT INTO `user`(`id`, `nome`, `username`, `password`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`, `enabled`)
values (1, 'Admin', 'admin@admin', '9ed193a96d4f76433bcc53f10220ac85254fb5dbd4e1cb068cdc123103c34de5e4a4717d99529d49', 1, 1, 1, 1);