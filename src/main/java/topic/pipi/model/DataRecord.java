package topic.pipi.model;

import java.util.UUID;

public class DataRecord {

    private String uuid = UUID.randomUUID().toString();
    /**
     * 股票代码
     */
    private String stockCode;

    private double value;

    /**
     * 时间
     */
    private String tradeDate;

    public String getUuid() {
        return uuid;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }
}
