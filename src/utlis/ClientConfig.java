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
    private final String signature = "zWj`Jjkg";
    private final BlockingQueue<Message> dataQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> packetQueue = new LinkedBlockingQueue<>();
    private final static String filePath = "src/config/system.properties";
    private volatile boolean isContinue = true;

    private String host;
    private int port;
    private String colorBlue;
    private String colorRed;
    private String colorDefault;
    private String logFileName;
    
    public static void init() {
        if (instance == null) {
            instance = new ClientConfig();
        }
    }

    private ClientConfig() {
        System.setProperty("log4j.configurationFile", "src/config/log4j2.xml");
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(filePath));
            this.host = properties.getProperty("host");
            this.port = Integer.parseInt(properties.getProperty("port"));
            this.colorBlue = properties.getProperty("colorBlue");
            this.colorRed = properties.getProperty("colorRed");
            this.colorDefault = properties.getProperty("colorDefault");
            this.logFileName = properties.getProperty("logFileName");
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Файл c property не найден");
        }
    }

    public static ClientConfig getInstance() {
        if (instance == null) {
            ConsoleHelper.writeSystemMessage("Синглтон еще не инициализирован, треш. А должен как бы. Сначала надо вызвать ClientConfig.init()");
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

    public boolean isContinue() {
        return isContinue;
    }

    public void stop() {
        this.isContinue = false;
    }
}
