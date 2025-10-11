package layer.loggers;

public interface AppLogger {
    void info(String string, Object... args);
    void debug(String string, Object... args);
    void warn(String string, Object... args);
    void error(String string,Object... args);
    void error(String string, Throwable t, Object... args);
}
