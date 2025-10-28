package com.jramcon398.jrc.util;

import lombok.experimental.UtilityClass;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

/**
 * Constant utility class containing application-wide constants.
 */

@UtilityClass
public class Constant {
    public static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final String LOG_FILE_PATH = "logs/app.log";
    public static final String INVALID_LOG_EVENT = "Invalid log event";
    public static final String LOG_FILE_NOT_FOUND = "Log file not found";
    public static final String INVALID_ENCODING = "Invalid encoding. Supported encodings: UTF-8, ISO-8859-1";
    public static final Charset UTF_8 = StandardCharsets.UTF_8;
    public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;

}
