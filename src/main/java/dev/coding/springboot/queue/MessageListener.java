package dev.coding.springboot.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

import java.io.IOException;

@Slf4j
public abstract class MessageListener<T> {

    private final Class<T> type;
    private final ObjectMapper objectMapper;

    public MessageListener(final Class<T> type, final ObjectMapper objectMapper) {
        this.type = type;
        this.objectMapper = objectMapper;
    }

    protected T fromMessage(final Message message) throws IOException {
        final T obj = objectMapper.readValue(message.getBody(), type);
        log.debug("Message converted to [{}]", obj);
        return obj;
    }
}