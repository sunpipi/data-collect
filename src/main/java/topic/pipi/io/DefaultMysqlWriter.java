package topic.pipi.io;

import topic.pipi.db.MysqlConnector;
import topic.pipi.exception.TaskErrorException;
import topic.pipi.model.DataRecord;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

public class DefaultMysqlWriter implements DataSinker {

    /**
     * 数据写入db
     *
     * @param records 待写入数据
     * @return 写入失败记录
     */
    @Override
    public List<DataRecord> writeData(List<DataRecord> records) {
        if (records.isEmpty()) {
            return null;
        }
        Connection conn = MysqlConnector.getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement statement = conn.prepareStatement("insert into " + MysqlConnector.TABLE + " (item_id , trading_date ,stock_code ,item_value ) values (?, ?, ?, ?)");
            for (int i = 0; i < records.size(); i++) {
                DataRecord record = records.get(i);
                statement.clearParameters();
                statement.setString(1, record.getUuid());
                statement.setDate(2, new Date(record.getTradeDateInMillis()));
                statement.setString(3, record.getStockCode());
                statement.setDouble(4, record.getValue());
                statement.addBatch();
            }
            statement.executeBatch();
            conn.commit();

            statement.close();
            System.out.println("DefaultMysqlWriter: " + records.size() + " sink");
            return null;
        } catch (Exception e) {
            throw new TaskErrorException("mysql insertion error! ", e);
        } finally {
            MysqlConnector.returnConnection(conn);
        }
    }
}
