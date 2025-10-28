package com.jramcon398.jrc.application;

import com.jramcon398.jrc.model.LogEvent;
import com.jramcon398.jrc.repository.LogRepository;
import com.jramcon398.jrc.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
        if (date == null || date.isEmpty()) {
            log.warn("Date parameter cannot be null or empty");
            return new ArrayList<>();
        }

        try {
            // Validate date format
            LocalDate.parse(date, Constant.DATE_FORMAT);
            return logRepository.findByDate(date);
        } catch (Exception e) {
            log.warn("Invalid date format: {}", date);
            return new ArrayList<>();
        }
    }

    public List<LogEvent> getAllEvents() {
        return logRepository.readAll();
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
        
        logRepository.setEncoding(encoding);
        log.info("Encoding changed to: {}", encoding);
        return true;
    }

    public String getCurrentEncoding() {
        return logRepository.getCurrentEncoding();
    }
}
