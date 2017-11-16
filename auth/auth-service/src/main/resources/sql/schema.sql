--create db
create database auth;
CREATE USER 'dl_auth'@'%' IDENTIFIED BY 'I9dj@';
GRANT SELECT,INSERT,UPDATE,DELETE ON auth.* TO 'dl_auth'@'%';

--create authentication table
create table users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE KEY,
  password VARCHAR(128) NOT NULL,
  email VARCHAR(128) NOT NULL,
  ssid VARCHAR(50),
  status TINYINT NOT NULL DEFAULT 1,
  locked BOOLEAN NOT NULL DEFAULT false,
  expired BOOLEAN NOT NULL DEFAULT false,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp of initial creation.',
  password_modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp of password update.',
  modified TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
);

create table authorities (
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(50) NOT NULL,
  status TINYINT NOT NULL DEFAULT 1,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp of initial creation.',
  modified TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  constraint fk_authorities_users foreign key(username) references users(username),
  constraint PRIMARY KEY pk_auth_username(username, authority)
);

create table permissions (
  permission VARCHAR(50) NOT NULL PRIMARY KEY,
  display_name VARCHAR(150) NOT NULL,
  description VARCHAR(255),
  status TINYINT NOT NULL DEFAULT 1,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp of initial creation.',
  modified TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
);

create table authority_permissions (
  authority VARCHAR(50) NOT NULL,
  permission VARCHAR(50) NOT NULL,
  constraint fk_authority_permissions_perm foreign key(permission) references permissions(permission),
  constraint PRIMARY KEY pk_auth_perm(authority, permission)
);

create table account (
  account_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(127) NOT NULL UNIQUE KEY,
  description VARCHAR(255),
  status TINYINT NOT NULL DEFAULT 1,
  type TINYINT NOT NULL DEFAULT 0,
  account_balance BIGINT(20) NOT NULL DEFAULT 0 COMMENT 'In micros',
  currency_code CHAR(3) NOT NULL DEFAULT 'CNY',
  timezone VARCHAR(32) NOT NULL DEFAULT 'Asia/Shanghai',
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp of initial creation.',
  modified TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
);

create table user_account (
  user_id INT NOT NULL,
  account_id INT NOT NULL,
  permission VARCHAR(50) NOT NULL,
  last_login TIMESTAMP,
  constraint fk_user_account_user foreign key(user_id) references users(user_id),
  constraint fk_user_account_account foreign key(account_id) references account(account_id),
  constraint fk_user_account_perm foreign key(permission) references permissions(permission),
  constraint PRIMARY KEY pk_user_account(user_id, account_id)
);

-- create oauth2 table
create table oauth_client_details (
  client_id VARCHAR(128) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
);

create table oauth_client_token (
  token_id VARCHAR(256),
  token BLOB,
  authentication_id VARCHAR(128) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256)
);

create table oauth_access_token (
  token_id VARCHAR(256),
  token BLOB,
  authentication_id VARCHAR(128) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication BLOB,
  refresh_token VARCHAR(256)
);

create table oauth_refresh_token (
  token_id VARCHAR(256),
  token BLOB,
  authentication BLOB
);

create table oauth_code (
  code VARCHAR(256), authentication BLOB
);

create table oauth_approvals (
    userId VARCHAR(256),
    clientId VARCHAR(256),
    scope VARCHAR(256),
    status VARCHAR(10),
    expiresAt TIMESTAMP,
    lastModifiedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

