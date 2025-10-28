package com.jramcon398.jrc.repository;

import com.jramcon398.jrc.model.LogEvent;
import com.jramcon398.jrc.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class LogRepository implements CrudRepository<LogEvent> {

    private String encoding = Constant.UTF_8.toString(); // Configurable via application menu

    /**
     * @param entity
     * @return
     */
    @Override
    public LogEvent create(LogEvent entity) {

        ensureLogDirectoryExists();
        try (FileOutputStream fos = new FileOutputStream(Constant.LOG_FILE_PATH, true);
             OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName(encoding));
             BufferedWriter writer = new BufferedWriter(osw)
        ) {
            String logEntry = "[" + entity.getTimestamp() + "] " + entity.getMessage();

            // Add a new line if the file is not empty. Because of append mode.
            File logFile = new File(Constant.LOG_FILE_PATH);
            if (logFile.exists() && logFile.length() > 0) {
                writer.newLine();
            }
            writer.write(logEntry);
            writer.flush();

            log.info("Log event created with encoding {}: {}", encoding, entity);
            return entity;
        } catch (IOException e) {
            log.error("Error writing log event with encoding {}: {}", encoding, e.getMessage());
            return null;

        }

    }

    /**
     * Ensure the log directory exists; create it if it doesn't.
     */

    private void ensureLogDirectoryExists() {
        try {
            Path logPath = Paths.get(Constant.LOG_FILE_PATH);
            Path logDir = logPath.getParent();

            if (logDir != null && !Files.exists(logDir)) {
                Files.createDirectories(logDir);
                log.info("Created log directory: {}", logDir.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Failed to create log directory: {}", e.getMessage());
        }
    }


    /**
     * @param entity
     * @return
     */
    @Override
    public LogEvent read(LogEvent entity) {
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public LogEvent update(LogEvent entity) {
        return null;
    }

    /**
     * @param entity
     * @return
     */
    @Override
    public boolean delete(LogEvent entity) {
        return false;
    }

    public List<LogEvent> readAll() {
        List<LogEvent> logEvents = new ArrayList<>();
        File logFile = new File(Constant.LOG_FILE_PATH);

        if (!logFile.exists()) {
            log.warn("Log file does not exist: {}", Constant.LOG_FILE_PATH);
            return logEvents;
        }

        // Sequential file reading with InputStreamReader for encoding conversion
        try (FileInputStream fis = new FileInputStream(logFile);
             InputStreamReader isr = new InputStreamReader(fis, Charset.forName(encoding));
             BufferedReader reader = new BufferedReader(isr)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                LogEvent logEvent = parseLogLine(line);
                if (logEvent != null) {
                    logEvents.add(logEvent);
                } else if (!line.trim().isEmpty()) {
                    log.warn("Failed to parse line {}: {}", lineNumber, line);
                }
            }

            log.info("Read {} log events with encoding {}", logEvents.size(), encoding);

        } catch (FileNotFoundException e) {
            log.error("Log file not found: {}", Constant.LOG_FILE_PATH);
        } catch (IOException e) {
            log.error("Error reading log file with encoding {}: {}", encoding, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error reading log file: {}", e.getMessage());
        }

        return logEvents;
    }

    private LogEvent parseLogLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        int closingBracketIndex = line.indexOf(']');

        //Extracting timestamp and message from log line
        if (line.startsWith("[") && closingBracketIndex > 1) {
            String timestamp = line.substring(1, closingBracketIndex);
            String message = line.substring(closingBracketIndex + 1).trim();

            return new LogEvent(timestamp, message);
        }

        log.warn("Invalid log line format: {}", line);
        return null;

    }

    public List<LogEvent> findByDate(String date) {
        List<LogEvent> allEvents = readAll();
        List<LogEvent> filteredEvents = new ArrayList<>();

        for (LogEvent event : allEvents) {
            if (event.getTimestamp() != null && event.getTimestamp().startsWith(date)) {
                filteredEvents.add(event);
            }
        }

        log.info("Found {} log events for date: {}", filteredEvents.size(), date);
        return filteredEvents;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
        log.info("Log encoding changed to: {}", encoding);
    }

    public String getCurrentEncoding() {
        return this.encoding;
    }
}
