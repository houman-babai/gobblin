package gobblin.example.mine;

import gobblin.configuration.WorkUnitState;
import gobblin.source.extractor.extract.kafka.KafkaExtractor;

import java.io.IOException;

import kafka.message.MessageAndOffset;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogEventExtractor extends KafkaExtractor<String, LogEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaExtractor.class);

    private static final LogEvent LOG_EVENT = new LogEvent("hostName", "logType", 1234L, "level", "hash", "body");

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
        LOGGER.info("Decoding " + messageAndOffset.offset());
        try {
            decoder = DecoderFactory.get().binaryDecoder(messageAndOffset.message().payload().array(), decoder);
            return reader.read(null, decoder);
        } catch (Throwable t) {
            LOGGER.error("Failed to decode record " + messageAndOffset, t);
            LOG_EVENT.setTime(messageAndOffset.offset());
            return LOG_EVENT;
        }

    }
}
