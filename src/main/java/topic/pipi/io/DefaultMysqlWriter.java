package topic.pipi.io;

import topic.pipi.db.MysqlConnector;
import topic.pipi.exception.TaskErrorException;
import topic.pipi.model.DataRecord;

import java.sql.Connection;
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
        Connection conn = MysqlConnector.getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("insert into loadtest (id, data) values (?, ?)");
            for (int i = 0; i <= records.size(); i++) {
//            pstmt.clearParameters();
//            pstmt.setInt(1, i);
//            pstmt.setString(2, DATA);

                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();

            pstmt.close();
            return null;
        } catch (Exception e) {
            throw new TaskErrorException("mysql insertion error! ", e);
        }finally {
            MysqlConnector.returnConnection(conn);
        }
    }
}
