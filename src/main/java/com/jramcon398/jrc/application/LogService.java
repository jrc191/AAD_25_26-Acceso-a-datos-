package com.jramcon398.jrc.application;

import com.jramcon398.jrc.model.LogEvent;
import com.jramcon398.jrc.repository.LogRepository;
import com.jramcon398.jrc.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService implements CustomService<LogEvent> {

    private final LogRepository logRepository;

    /**
     * @param entity
     * @return
     */
    @Override
    public boolean validate(LogEvent entity) {
        return entity != null
                && entity.getTimestamp() != null && !entity.getTimestamp().isEmpty()
                && entity.getMessage() != null && !entity.getMessage().isEmpty();
    }

    public LogEvent addLogEvent(String message) {
        LogEvent logEvent = new LogEvent(message);
        if (validate(logEvent)) {
            return logRepository.create(logEvent);
        }
        log.warn(Constant.INVALID_LOG_EVENT);
        return null;
    }

    public List<LogEvent> getEventsByDate(String date) {

        return null;
    }

    public List<LogEvent> getAllEvents() {
        // Llamar a logRepository para leer todos
        return null;
    }

    public boolean changeEncoding(String encoding) {
        if (!encoding.equals(Constant.UTF_8.toString()) && !encoding.equals(Constant.ISO_8859_1.toString())) {
            log.warn(Constant.INVALID_ENCODING);
            return false;
        }

        if (encoding.equals(logRepository.getCurrentEncoding())) {
            log.info("Encoding is already set to: {}", encoding);
            return true;
        }

        return true; //A IMPLEMENTAR EN LogRepository
    }

    public String getCurrentEncoding() {
        return logRepository.getCurrentEncoding();
    }
}
