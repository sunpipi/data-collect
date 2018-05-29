package topic.pipi.context;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import topic.pipi.model.DataRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReadTaskSubscriber extends BaseSubscriber<List<DataRecord>> {

    private TaskHub context;
    private List<DataRecord> completed = new ArrayList<>();

    public ReadTaskSubscriber(TaskHub context) {
        this.context = context;
    }

    @Override
    protected void hookOnSubscribe(Subscription subscription) {
        request(1);
    }

    @Override
    protected void hookOnNext(List<DataRecord> value) {
        completed.addAll(value);
        request(1);
    }

    @Override
    protected void hookOnComplete() {
        System.out.println("ReadTaskSubscriber:" + completed.size() + " recv.");
        List<List<DataRecord>> records = dispatchByWriterCoreSize(completed);
        for (int i = 0; i < records.size(); i++) {
            Flux.just(records.get(i)).subscribe(context.WRITERS.get(i)::writeData);
        }
    }

    private List<List<DataRecord>> dispatchByWriterCoreSize(List<DataRecord> records) {
        List<List<DataRecord>> ret = new ArrayList<>();
        if (records.isEmpty()) {
            return ret;
        }
        int len = Math.max(1, records.size() / context.getWriterSize());
        int workLeft = records.size() % context.getWriterSize();
        for (int i = 0; i < context.getWriterSize(); i++) {
            int limit = i == context.getWriterSize() - 1 ? len + workLeft : len;
            ret.add(records.stream().skip(i * len).limit(limit).collect(Collectors.toList()));
        }
        return ret;
    }

    @Override
    protected void hookOnError(Throwable throwable) {
        super.hookOnError(throwable);
    }
}