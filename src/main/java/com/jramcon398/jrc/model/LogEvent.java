package com.jramcon398.jrc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

import static com.jramcon398.jrc.util.Constant.DATE_TIME_FORMAT;

/**
 * LogEvent model representing a log entry with timestamp and message.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LogEvent {
    private String timestamp;
    private String message;

    public LogEvent(String message) {
        this.timestamp = LocalDateTime.now().format(DATE_TIME_FORMAT);
        this.message = message;
    }

}
