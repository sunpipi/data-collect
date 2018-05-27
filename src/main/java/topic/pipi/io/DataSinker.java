package topic.pipi.io;

import topic.pipi.model.DataRecord;

import java.util.List;

public interface DataSinker {

    List<DataRecord> writeData(List<DataRecord> records);
}
