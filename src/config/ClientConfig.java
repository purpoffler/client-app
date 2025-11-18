package config;

import layer.dto.Message;
import utlis.ConsoleHelper;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientConfig {
    private static ClientConfig INSTANCE;
    private static final String CONFIG_FILE_PATH = "src/config/application.properties";
    private final BlockingQueue<Message> dataQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> packetQueue = new LinkedBlockingQueue<>();
    private volatile boolean isContinue = true;

    private String host;
    private int port;
    private String colorBlue;
    private String colorRed;
    private String colorDefault;
    private String logFilePath;
    private String signature;

    private ClientConfig() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(CONFIG_FILE_PATH));
            this.host = properties.getProperty("host", "localhost");
            this.port = Integer.parseInt(properties.getProperty("port", "4004"));
            this.colorBlue = properties.getProperty("colorBlue", "");
            this.colorRed = properties.getProperty("colorRed", "");
            this.colorDefault = properties.getProperty("colorDefault", "");
            this.logFilePath = properties.getProperty("logFilePath", "");
            this.signature = "zWj`Jjkg";
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Файл c property не найден");
        }
    }

    public static ClientConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClientConfig();
        }
        return INSTANCE;
    }

    public BlockingQueue<Message> getDataQueue() {
        return dataQueue;
    }

    public BlockingQueue<String> getPacketQueue() {
        return packetQueue;
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

    public String getSignature() {
        return signature;
    }
}
