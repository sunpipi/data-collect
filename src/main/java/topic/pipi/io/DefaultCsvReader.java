package topic.pipi.io;


import topic.pipi.exception.TaskErrorException;
import topic.pipi.model.DataRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultCsvReader implements DataEmitter {

    // 捕获文件路径中的交易日期
    private static final Pattern PATTERN = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})\\.csv");

    /**
     * 从csv文件里加载数据集
     */
    @Override
    public List<DataRecord> readData(List<String> filePath) {
        List<DataRecord> gen = new ArrayList<>();

        String tradeDate = null;
        for (int i = 0; i < filePath.size(); i++) {
            final String path = filePath.get(i);
            if (i == 0) {
                Matcher m = PATTERN.matcher(path);
                m.find();
                tradeDate = m.group(1);
            }
            gen.addAll(createDataRecordOfCsvFile(path, tradeDate));
        }
        System.out.println("DefaultCsvReader: " + gen.size() + " emit");
        return gen;
    }

    private List<DataRecord> createDataRecordOfCsvFile(String path, String dateStr) {
        List<DataRecord> ret = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = null;
            boolean headerSkipped = false;
            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                String[] columns = line.split(",");
                DataRecord record = new DataRecord();
                record.setTradeDate(dateStr);
                record.setStockCode(columns[0].trim());
                record.setValue(Double.valueOf(columns[1]));
                ret.add(record);
            }
            return ret;
        } catch (Exception e) {
            throw new TaskErrorException("fail to load csv file:" + path, e);
        }
    }
}
