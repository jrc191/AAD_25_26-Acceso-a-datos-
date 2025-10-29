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

/**
 * LogService class providing services related to LogEvent management.
 * Implements validation and business logic for creating, retrieving, and managing log events.
 *
 * @see LogEvent
 * @see LogRepository
 */


@Service
@RequiredArgsConstructor
@Slf4j
public class LogService implements CustomService<LogEvent> {

    private final LogRepository logRepository;

    @Override
    public boolean validate(LogEvent entity) {
        return entity != null
                && entity.getTimestamp() != null && !entity.getTimestamp().isEmpty()
                && entity.getMessage() != null && !entity.getMessage().isEmpty();
    }

    /**
     * Adds a new LogEvent with the given message after validation.
     *
     * @param message message of the log event.
     * @return created LogEvent or null if validation fails.
     */

    public LogEvent addLogEvent(String message) {
        LogEvent logEvent = new LogEvent(message);
        if (validate(logEvent)) {
            return logRepository.create(logEvent);
        }
        log.warn(Constant.INVALID_LOG_EVENT);
        return null;
    }

    /**
     * Retrieves log events for a specific date.
     * Uses Constant.DATE_FORMAT for date validation.
     *
     * @param date date in YYYY-MM-DD format.
     * @return list of LogEvent for the specified date.
     */

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

    /**
     * Changes the encoding used for reading log files.
     * Supported encodings are UTF-8 and ISO-8859-1.
     *
     * @param encoding Encoding to set.
     * @return true if encoding changed successfully, false otherwise.
     */

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
