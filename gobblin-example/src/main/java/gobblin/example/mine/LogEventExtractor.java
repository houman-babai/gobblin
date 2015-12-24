package gobblin.example.mine;

import gobblin.configuration.WorkUnitState;
import gobblin.source.extractor.extract.kafka.KafkaExtractor;

import java.io.IOException;

import kafka.message.MessageAndOffset;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;

public class LogEventExtractor extends KafkaExtractor<String, LogEvent> {
    final SpecificDatumReader<LogEvent> reader = new SpecificDatumReader<LogEvent>(LogEvent.class);
    BinaryDecoder decoder = null;

    public LogEventExtractor(WorkUnitState state) {
        super(state);
    }

    @Override
    public String getSchema() throws IOException {
        return this.topicName;
    }

    @Override
    protected LogEvent decodeRecord(MessageAndOffset messageAndOffset) throws IOException {
        decoder = DecoderFactory.get().binaryDecoder(messageAndOffset.message().payload().array(), decoder);
        return reader.read(null, decoder);
    }
}
