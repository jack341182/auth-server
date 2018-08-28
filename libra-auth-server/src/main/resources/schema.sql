create table if not exists oauth_client_details (
  client_id VARCHAR(255) PRIMARY KEY,
  resource_ids VARCHAR(255),
  client_secret VARCHAR(255),
  scope VARCHAR(255),
  authorized_grant_types VARCHAR(255),
  web_server_redirect_uri VARCHAR(255),
  authorities VARCHAR(255),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(255)
);
INSERT INTO `oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) VALUES ('MANAGEMENT', 'api-gateway', '$2a$10$2MClDEqwu2Qby4rKtQuX2unPYTbgrAfHBHzBalxiTOGNMgk/VKSbe', 'MANAGEMENT', 'password,refresh_token', NULL, NULL, '2592000', '2592000', NULL, NULL);
INSERT INTO `oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) VALUES ('MOBILE_SERVICE_CAR', 'api-gateway', '$2a$10$HnIMN7ipRgetPH9DrUfnZ.zo1SDjABCc2u2EWoQgxm4J5ilIzCKB6', 'MOBILE_SERVICE_CAR', 'password,refresh_token', NULL, NULL, '2592000', '2592000', NULL, NULL);
INSERT INTO `oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) VALUES ('TRUCKER', 'api-gateway', '$2a$10$By1N1IAuZB4nVm52svL3UuGC7n20I7knwSjNBjyRkXdg0T9EfF.fC', 'TRUCKER', 'password,refresh_token', NULL, NULL, '2592000', '2592000', '{\"name\":\"123\"}', NULL);
