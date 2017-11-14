--advertiser table
create table advertiser (
  advertiser_id INT AUTO_INCREMENT PRIMARY KEY,
  account_id INT NOT NULL,
  advertiser_name VARCHAR(50) NOT NULL UNIQUE KEY,
  url VARCHAR(128) NOT NULL,
  status TINYINT NOT NULL DEFAULT 1,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp of initial creation.',
  modified TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  constraint fk_advertiser_account foreign key(account_id) references account(account_id)
);

