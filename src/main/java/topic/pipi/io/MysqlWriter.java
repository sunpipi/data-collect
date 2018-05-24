package topic.pipi.io;

import topic.pipi.db.MysqlConnector;
import topic.pipi.model.DataRecord;

import java.util.List;

public class MysqlWriter {

    public MysqlWriter(MysqlConnector connector) {
        // try connect
    }

    /**
     * 数据写入db
     *
     * @param records 待写入数据
     * @return 写入失败记录
     */
    public List<DataRecord> writeData(List<DataRecord> records) {
        return null;
    }
}
