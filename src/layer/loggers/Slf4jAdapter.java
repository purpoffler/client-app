package layer.loggers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jAdapter implements AppLogger {
    private final Logger log;

    public Slf4jAdapter(Class<?> className) {
        this.log = LoggerFactory.getLogger(className);
    }

    @Override
    public void info(String string, Object... args) {
        log.info(string, args);
    }

    @Override
    public void debug(String string, Object... args) {
        log.debug(string, args);
    }

    @Override
    public void warn(String string, Object... args) {
        log.warn(string, args);
    }

    @Override
    public void error(String string, Object... args) {
        log.error(string, args);
    }

    @Override
    public void error(String string, Throwable t, Object... args) {
        log.error(string, t, args);
    }
}
