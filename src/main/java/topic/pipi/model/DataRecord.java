package topic.pipi.model;

import topic.pipi.exception.TaskErrorException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class DataRecord {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private String uuid = UUID.randomUUID().toString().replaceAll("-", "");
    /**
     * 股票代码
     */
    private String stockCode;

    private double value;

    /**
     * 时间
     */
    private String tradeDate;
    private long tradeDateInMillis;

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

    public long getTradeDateInMillis() {
        return tradeDateInMillis;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
        try {
            this.tradeDateInMillis = DATE_FORMAT.parse(this.tradeDate).getTime();
        } catch (ParseException e) {
            throw new TaskErrorException("cant parse date of record with format yyyy-MM-dd:" + tradeDate, e);
        }
    }

    @Override
    public String toString() {
        return "DataRecord{" +
                "uuid='" + uuid + '\'' +
                ", stockCode='" + stockCode + '\'' +
                ", value=" + value +
                ", tradeDate='" + tradeDate + '\'' +
                '}';
    }
}
