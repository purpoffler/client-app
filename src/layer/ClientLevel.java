package layer;

import layer.logger.CustomLogger;
import layer.socket.Connection;
import config.ClientConfig;
import utlis.ConsoleHelper;

import java.io.*;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ClientLevel implements Runnable {
    private final ClientConfig clientConfig = ClientConfig.getInstance();
    private final BlockingQueue<String> packetQueue = clientConfig.getPacketQueue();
    private static final CustomLogger log = new CustomLogger(ClientLevel.class.getSimpleName());

    @Override
    public void run() {
        try (Connection connection = new Connection()) {
            while (clientConfig.isContinue()) {
                String packet = packetQueue.poll();
                if (packet != null) {
                    log.debug("Из очереди packetQueue получили пакет: " + packet);
                    connection.send(packet + "\n");
                    String serverWord = connection.receive();
                    log.info("Ответ от сервера" + serverWord);
                    // Повторная отправка
                    if (serverWord.equalsIgnoreCase("false")) {
                        log.debug("Ошибка при отправке пакета. Попытка повторной отправки");
                        connection.send(packet + "\n");
                    }
                }
            }
        } catch (UnknownHostException e) {
            log.error("Неправильный хост", e);
            ConsoleHelper.writeSystemMessage("Неправильный хост, перезапустите приложение с исправленными параметрами");
        } catch (IOException e) {
            log.error("Потеря соединения c сервером", e);
            ConsoleHelper.writeSystemMessage("Потеря соединения c сервером, перезапустите приложение");
        } catch (ClassNotFoundException e) {
            log.error("Ошибка при получении данных от сервера", e);
            ConsoleHelper.writeSystemMessage("Сервер не отвечает, перезапустите приложение");
        }
    }
}
