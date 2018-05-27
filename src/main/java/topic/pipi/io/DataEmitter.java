package topic.pipi.io;

import topic.pipi.model.DataRecord;

import java.util.List;

public interface DataEmitter {
    List<DataRecord> readData(List<String> filePath);
}
