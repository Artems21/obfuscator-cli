package logger.impl;

import logger.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleLogger implements Logger {

    private final String name;

    public SimpleLogger(Class<?> clazz) {
        this.name = clazz.getSimpleName();
    }

    private String format(String level, String message) {
        String time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return String.format("%s [%s] [%s] %s",
                time,
                level,
                name,
                message
        );
    }

    @Override
    public void info(String message) {
        System.out.println(format("INFO", message));
    }

    @Override
    public void debug(String message) {
        System.out.println(format("DEBUG", message));
    }

    @Override
    public void warn(String message) {
        System.out.println(format("WARN", message));
    }

    @Override
    public void error(String message) {
        System.err.println(format("ERROR", message));
    }

    @Override
    public void error(String message, Throwable throwable) {
        System.err.println(format("ERROR", message));
        throwable.printStackTrace(System.err);
    }
}
