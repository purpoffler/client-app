package utlis;

import layer.dto.Message;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class ClientConfig {
    private static ClientConfig instance;
    private static final String signature = "zWj`Jjkg";
    private static final BlockingQueue<Message> dataQueue = new LinkedBlockingQueue<>();
    private static final BlockingQueue<String> packetQueue = new LinkedBlockingQueue<>();
    private static Properties properties = new Properties();
    private static String filePath = "src/config/system.properties";
    private static String host;
    private static int port;
    private static String colorBlue;
    private static String colorDefault;

    {
        try {
            properties.load(new FileReader(filePath));
            host = properties.getProperty("host");
            port = Integer.parseInt(properties.getProperty("port"));
            colorBlue = properties.getProperty("colorBlue");
            colorDefault = properties.getProperty("colorDefault");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ClientConfig() {
    }

    public static ClientConfig getInstance() {
        if (instance == null) {
            instance = new ClientConfig();
        }
        return instance;
    }

    public static BlockingQueue<Message> getDataQueue() {
        return dataQueue;
    }

    public static BlockingQueue<String> getPacketQueue() {
        return packetQueue;
    }

    public static String getSignature() {
        return signature;
    }


    public static String getFilePath() {
        return filePath;
    }

    public static String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }

    public static String getColorBlue() {
        return colorBlue;
    }

    public static String getColorDefault() {
        return colorDefault;
    }
}
