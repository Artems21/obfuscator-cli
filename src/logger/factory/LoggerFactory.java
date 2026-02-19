package logger.factory;

import logger.Logger;
import logger.impl.SimpleLogger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class LoggerFactory {

    private static final ConcurrentMap<Class<?>, Logger> loggers = new ConcurrentHashMap<>();

    private LoggerFactory() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return loggers.computeIfAbsent(clazz, SimpleLogger::new);
    }
}
