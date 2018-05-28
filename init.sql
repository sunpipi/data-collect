DROP database IF EXISTS `topic`;
CREATE DATABASE `topic`;
USE `topic`;

DROP TABLE IF EXISTS `time_series_data`;
CREATE TABLE  time_series_data (
    item_id char(36),
    trading_date date,
    stock_code varchar(100),
    item_value double,
    PRIMARY KEY (item_id, trading_date, stock_code)
);