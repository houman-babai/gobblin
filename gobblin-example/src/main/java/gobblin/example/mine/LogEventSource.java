package gobblin.example.mine;

import gobblin.configuration.WorkUnitState;
import gobblin.source.extractor.Extractor;
import gobblin.source.extractor.extract.kafka.KafkaSource;

import java.io.IOException;

public class LogEventSource extends KafkaSource<String, LogEvent> {

    @Override
    public Extractor<String, LogEvent> getExtractor(WorkUnitState state) throws IOException {
        // not sure if I could reuse the same thing here...so just create a new one
        return new LogEventExtractor(state);
    }

}
