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
        // Leer todas las líneas del fichero
        // Parsear cada línea: extraer [timestamp] y message
        // Retornar lista de LogEvent
        return null;
    }

    public List<LogEvent> findByDate(String date) {
        // Leer todos los eventos con readAll()
        // Filtrar por fecha (comparar solo YYYY-MM-DD)
        // Retornar lista filtrada
        return null;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
        log.info("Log encoding changed to: {}", encoding);
    }

    public String getCurrentEncoding() {
        return this.encoding;
    }
}
