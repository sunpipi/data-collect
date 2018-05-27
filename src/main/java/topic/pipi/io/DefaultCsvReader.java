package topic.pipi.io;


import topic.pipi.model.DataRecord;

import java.util.ArrayList;
import java.util.List;

public class DefaultCsvReader implements DataEmitter {

    /**
     * 从csv文件里加载数据集
     */
    @Override
    public List<DataRecord> readData(List<String> filePath) {
        List<DataRecord> gen = new ArrayList<>();

        for (int i = 0; i < filePath.size(); i++) {
            // TODO create DataRecord
            gen.add(new DataRecord());
        }
        System.out.println(gen.size() + " data created.");
        return gen;
    }
}
