package topic.pipi;

import topic.pipi.db.MysqlConnector;
import topic.pipi.io.CsvReader;
import topic.pipi.io.MysqlWriter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskHub {

    private static final int DEFAULT_READER_SIZE = 4;
    private static final int DEFAULT_WRITER_SIZE = 4;

    private int readerSize;
    private int writerSize;

    private volatile boolean working;
    private Map<Integer, CsvReader> READERS = new ConcurrentHashMap<Integer, CsvReader>();
    private Map<Integer, MysqlWriter> WRITERS = new ConcurrentHashMap<Integer, MysqlWriter>();

    private static TaskHub INSTANCE;

    public static TaskHub getInstance(int readerSize, int writerSize) {
        if (INSTANCE == null) {
            INSTANCE = new TaskHub(readerSize, writerSize);
        }
        return INSTANCE;
    }

    public static TaskHub getInstance() {
        return getInstance(DEFAULT_READER_SIZE, DEFAULT_WRITER_SIZE);
    }

    private TaskHub(int readerSize, int writerSize) {
        readerSize = Math.max(1, readerSize);
        writerSize = Math.max(1, writerSize);
    }

    public TaskHub() {
        this(DEFAULT_READER_SIZE, DEFAULT_WRITER_SIZE);
    }

    public void execute(List<String> filepathList, MysqlConnector connector) {
        working = true;
        try {
            for (int i = 0; i < writerSize; i++) {
                WRITERS.put(i, new MysqlWriter(connector));
            }
            for (int i = 0; i < readerSize; i++) {
                READERS.put(i, new CsvReader());
            }

        } catch (Exception e) {
            working = false;
            throw new RuntimeException(e);
        } finally {
            cleanup();
        }
    }

    private void cleanup() {
        WRITERS.clear();
        READERS.clear();
    }

}
