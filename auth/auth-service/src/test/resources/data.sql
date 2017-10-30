INSERT INTO `users` (`user_id`, `username`, `password`, `email`)
VALUES
    (1,'root','$2a$12$3uGBNkzQ0HWKE/BeiSk6seTUZ37iE5OePNzlZOB4RiwEk3vpwfQXG','root@admin.grp');
    
INSERT INTO `authorities` (`username`, `authority`)
VALUES
    ('root','ROLE_SUPER_LOGIN'),
    ('root','ROLE_ADMIN'),
    ('root','ROLE_USER');
    
INSERT INTO `permissions` (`permission`, `display_name`, `description`)
VALUES
    ('super_into','Super Into', 'Super into other accounts.'),
    ('admin_metrics','See Admin Metrics', null),
    ('account_write','Update Account Data', null),
    ('account_read','Read Account Data', null);
    
INSERT INTO `authority_permissions` (`permission`, `authority`)
VALUES
    ('super_into','ROLE_SUPER_LOGIN'),
    ('admin_metrics','ROLE_ADMIN'),
    ('account_write','ROLE_USER'),
    ('account_read','ROLE_USER');
    
INSERT INTO `oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`)
VALUES
    ('client',NULL,'nice-to-have','web,app','password,authorization_code,refresh_token',NULL,NULL,NULL,NULL,NULL,NULL);
    