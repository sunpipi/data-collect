CREATE TABLE  time_series_data (

    item_id uuid,

    trading_date date,

    stock_code text,

    item_value double,

    PRIMARY KEY (item_id, trading_date, stock_code)

);