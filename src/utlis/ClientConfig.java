package utlis;

import layer.dto.Message;
import layer.loggers.AppLogger;
import layer.loggers.CustomLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientConfig {
    private static ClientConfig instance;
    private AppLogger log;
    private final String signature = "zWj`Jjkg";
    private final BlockingQueue<Message> dataQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> packetQueue = new LinkedBlockingQueue<>();
    private final String filePath = "src/config/system.properties";


    private String host;
    private int port;
    private String colorBlue;
    private String colorRed;
    private String colorDefault;
    private String logFileName;


    public static void init() {
        if (instance == null) {
            instance = new ClientConfig();
            System.setProperty("log4j.configurationFile", "src/config/log4j2.xml");
            instance.log = new CustomLogger(ClientConfig.class);
        }
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(instance.filePath));
            instance.host = properties.getProperty("host");
            instance.port = Integer.parseInt(properties.getProperty("port"));
            instance.colorBlue = properties.getProperty("colorBlue");
            instance.colorRed = properties.getProperty("colorRed");
            instance.colorDefault = properties.getProperty("colorDefault");
            instance.logFileName = properties.getProperty("logFileName");
        } catch (IOException e) {
            instance.log.error("Файл не найден [{}]", instance.filePath, e);
        }
    }

    private ClientConfig() {
    }

    public static ClientConfig getInstance() {
        if (instance == null) {
            instance.log.warn("Синглтон еще не инициализирован, треш. А должен как бы [{}]", instance.getClass());
            ConsoleHelper.writeSystemMessage("Произошла ошибка непредвиденная ошибка, перезапустите приложение");
        }
        return instance;
    }

    public BlockingQueue<Message> getDataQueue() {
        return dataQueue;
    }

    public BlockingQueue<String> getPacketQueue() {
        return packetQueue;
    }

    public String getSignature() {
        return signature;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getColorBlue() {
        return colorBlue;
    }

    public String getColorDefault() {
        return colorDefault;
    }

    public String getColorRed() {
        return colorRed;
    }

    public String getLogFileName() {
        return logFileName;
    }
}
