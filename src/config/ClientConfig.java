package config;

import layer.dto.Message;
import utlis.ConsoleHelper;

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
    private final static String configFilePath = "src/config/system.properties";
    private volatile boolean isContinue = true;

    private String host;
    private int port;
    private String colorBlue;
    private String colorRed;
    private String colorDefault;
    private String logFilePath;

    private ClientConfig() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(configFilePath));
            this.host = properties.getProperty("host");
            this.port = Integer.parseInt(properties.getProperty("port"));
            this.colorBlue = properties.getProperty("colorBlue");
            this.colorRed = properties.getProperty("colorRed");
            this.colorDefault = properties.getProperty("colorDefault");
            this.logFilePath = properties.getProperty("logFilePath");
        } catch (IOException e) {
            ConsoleHelper.write("Файл c property не найден");
        }
    }

    public static ClientConfig getInstance() {
        if (instance == null) {
            instance = new ClientConfig();
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

    public String getLogFilePath() {
        return logFilePath;
    }

    public boolean isContinue() {
        return isContinue;
    }

    public void stop() {
        this.isContinue = false;
    }
}
