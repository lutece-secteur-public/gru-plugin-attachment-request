--
-- Structure for table attachment_request
--

DROP TABLE IF EXISTS `attachment_request`;
CREATE TABLE `attachment_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_file` int(11) NOT NULL DEFAULT '0',
  `customer_id` mediumtext,
  `client_code` varchar(255) DEFAULT '',
  `provider_name` varchar(255) DEFAULT '',
  `date_creation` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
);