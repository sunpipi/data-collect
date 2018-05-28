package topic.pipi.context;

import reactor.core.publisher.Flux;
import topic.pipi.exception.TaskErrorException;
import topic.pipi.io.DefaultCsvReader;
import topic.pipi.io.DataEmitter;
import topic.pipi.io.DataSinker;
import topic.pipi.io.DefaultMysqlWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 任务处理中心。
 * <p>
 * 接受待处理文件列表->读取文件内容->批量处理文件内容
 * </p>
 */
public class TaskHub {

    private static final int DEFAULT_READER_SIZE = 4;
    private static final int DEFAULT_WRITER_SIZE = 2;

    private int readerSize;
    private int writerSize;

    private Class<? extends DataEmitter> readerType = DefaultCsvReader.class;
    private Class<? extends DataSinker> writerType = DefaultMysqlWriter.class;

    private AtomicBoolean working = new AtomicBoolean();

    Map<Integer, DataEmitter> READERS = new ConcurrentHashMap<>();
    Map<Integer, DataSinker> WRITERS = new ConcurrentHashMap<>();
    private Queue<Task> taskQueue = new LinkedBlockingQueue<>();

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
        this.readerSize = Math.max(1, readerSize);
        this.writerSize = Math.max(1, writerSize);
    }

    /**
     * 任务执行接口
     *
     * @param filepathList 将要处理的文件路径集合
     */
    // TODO 返回Future
    public void execute(List<String> filepathList) {
        if (working.get()) {
            taskQueue.add(new Task(filepathList));
            return;
        }
        working.set(true);
        try {
            // prepare
            for (int i = 0; i < writerSize; i++) {
                WRITERS.put(i, writerType.getConstructor().newInstance());
            }
            for (int i = 0; i < readerSize; i++) {
                READERS.put(i, readerType.getConstructor().newInstance());
            }

            List<List<String>> split = dispatchByReaderCoreSize(filepathList);
            Flux[] readFluxArray = new Flux[readerSize];
            for (int i = 0; i < readerSize; i++) {
                readFluxArray[i] = Flux.just(READERS.get(i).readData(split.get(i)));
            }
            Flux readFlux = Flux.merge(readFluxArray);
            readFlux.subscribe(new ReadTaskSubscriber(this));

        } catch (Exception e) {
            working.set(false);
            throw new TaskErrorException("task execution failed.", e);
        } finally {
            cleanup();
            fetchNext();
        }
    }

    private List<List<String>> dispatchByReaderCoreSize(List<String> filepathList) {
        List<List<String>> ret = new ArrayList<>();
        if (filepathList.isEmpty()) {
            return ret;
        }
        int len = Math.max(1, filepathList.size() / readerSize);
        int workLeft = filepathList.size();
        for (int i = 0; i < readerSize; i++) {
            int limit = i == readerSize - 1 ? len + workLeft : len;
            workLeft -= len;
            ret.add(filepathList.stream().skip(i * len).limit(limit).collect(Collectors.toList()));
        }
        return ret;
    }

    private void cleanup() {
        WRITERS.clear();
        READERS.clear();
    }

    private void fetchNext() {
        Task task = taskQueue.poll();
        if (task != null) {
            this.execute(task.filepathList);
        }
    }

    public int getReaderSize() {
        return readerSize;
    }

    public int getWriterSize() {
        return writerSize;
    }

    public boolean isWorking() {
        return working.get();
    }

    public Class<?> getWriterType() {
        return writerType;
    }

    public void setWriterType(Class<? extends DataSinker> writerType) {
        this.writerType = writerType;
    }

    public void setReaderType(Class<? extends DataEmitter> readerType) {
        this.readerType = readerType;
    }

    /**
     * 一次提交的任务，在任务队列中缓存。
     */
    private class Task {
        List<String> filepathList;

        Task(List<String> filepathList) {
            this.filepathList = filepathList;
        }
    }
}
