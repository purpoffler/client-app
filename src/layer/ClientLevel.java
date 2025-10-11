package layer;

import layer.loggers.AppLogger;
import layer.loggers.CustomLogger;
import layer.socket.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utlis.ClientConfig;
import utlis.ConsoleHelper;

import java.io.*;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

public class ClientLevel implements Runnable {
    private final ClientConfig clientConfig = ClientConfig.getInstance();
    private final BlockingQueue<String> packetQueue = clientConfig.getPacketQueue();
    private static final AppLogger log = new CustomLogger(ClientLevel.class);

    @Override
    public void run() {
        try (Connection connection = new Connection()) {
            while (true) {
                String packet = packetQueue.take();// Получаем данные из очереди dataQueue
                log.debug("Из очереди packetQueue получили пакет: " + packet);
                connection.send(packet + "\n"); // отправляем сообщение на сервер
                String serverWord = connection.receive(); // ждём, что скажет сервер
                log.info("Получили ответ от сервера: " + serverWord);
                if (serverWord.equalsIgnoreCase("false")) {
                    log.warn("Ошибка при отправке пакета. Попытка повторной отправки[{}]", this.getClass());
                    connection.send(packet + "\n"); // повторно отправляем сообщение на сервер
                }
            }
        } catch (UnknownHostException e) {
            log.error("Неправильный хост[{}]", this.getClass(), e);
            ConsoleHelper.writeSystemMessage("Неправильный хост, перезапустите приложение с исправленными параметрами");
        } catch (IOException e) {
            log.error("Потеря соединения c сервером[{}]", this.getClass(), e);
            ConsoleHelper.writeSystemMessage("Потеря соединения c сервером, перезапустите приложение");
        } catch (ClassNotFoundException e) {
            log.error("Ошибка при получении данных от сервера[{}]", this.getClass(), e);
            ConsoleHelper.writeSystemMessage("Сервер не отвечает, перезапустите приложение");
        } catch (InterruptedException e) {
            log.error("Ошибка во время получения данных из packetQueue, данные утеряны[{}]", this.getClass(), e);
            ConsoleHelper.writeSystemMessage("Ошибка, данные утеряны, перезапустите приложение");
        }
    }
}
