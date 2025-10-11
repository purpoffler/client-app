package layer.loggers;

import utlis.ClientConfig;
import utlis.ConsoleHelper;
import utlis.DateCalculator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class CustomLogger implements AppLogger {
    private ClientConfig clientConfig = ClientConfig.getInstance();
    private String logFileName;
    private String className;

    public CustomLogger(Class<?> className) {
        this.className = className.getSimpleName();
        this.logFileName = clientConfig.getLogFileName();
    }

    private String format(String level, String message, Object... args) {
        String formatted = message;
        if (args != null) {
            for (Object arg : args) {
                formatted = formatted.replaceFirst("\\{\\}", arg.toString());
            }
        }
        String timestamp = DateCalculator.getDate();
        return String.format("[%s] [%s] %s - %s", timestamp, level, className, formatted);
    }

    private void write(String message) {
        File file = getFile(logFileName);
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(message);
            writer.append('\n');
            writer.flush();
        } catch (IOException e) {
            ConsoleHelper.writeSystemMessage("Возникли проблемы с записью логов - их не будет");
        }
    }

    public File getFile(String fileName) {
        Path path = Paths.get(".", fileName).toAbsolutePath();
        return new File(path.toUri());
    }

    @Override
    public void info(String message, Object... args) {
        write(format("INFO", message, args));
    }

    @Override
    public void debug(String message, Object... args) {
        write(format("DEBUG", message, args));
    }

    @Override
    public void warn(String message, Object... args) {
        write(format("WARN", message, args));
    }

    @Override
    public void error(String message, Object... args) {
        Throwable t = null;
        if (args != null && args.length > 0 && args[args.length - 1] instanceof Throwable) {
            t = (Throwable) args[args.length - 1];
            args = Arrays.copyOf(args, args.length - 1);
        }
        write(format("ERROR", message, args));
        if (t != null) {
            writeStackTrace(t);
        }
    }

    @Override
    public void error(String message, Throwable t, Object... args) {
        Object[] merged = Arrays.copyOf(args, args.length + 1);
        merged[args.length] = t;
        error(message, merged);
    }

    private void writeStackTrace(Throwable t) {
        File file = getFile(logFileName);
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(t.toString());
            writer.write(System.lineSeparator());
            for (StackTraceElement el : t.getStackTrace()) {
                writer.write("\tat " + el.toString());
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            ConsoleHelper.writeSystemMessage("Ошибка при записи стека исключения");
        }
    }

}
