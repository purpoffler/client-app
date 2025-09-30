package layer;

import layer.socket.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utlis.ClientConfig;
import utlis.ConsoleHelper;

import java.io.*;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

public class ClientLevel implements Runnable {
    private final BlockingQueue<String> packetQueue = ClientConfig.getPacketQueue();
    private static final Logger log = LoggerFactory.getLogger(ClientLevel.class);

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
                    log.error("Ошибка при отправке пакета. Попытка повторной отправки");
                    connection.send(packet + "\n"); // повторно отправляем сообщение на сервер
                }
            }
        } catch (UnknownHostException e) {
            log.error("Неправильный хост");
            ConsoleHelper.writeSystemMessage("Неправильный хост, перезапустите приложение с исправленными параметрами");
        } catch (IOException e) {
            log.error("Потеря соединения c сервером");
            ConsoleHelper.writeSystemMessage("Потеря соединения c сервером, перезапустите приложение");
        } catch (ClassNotFoundException e) {
            log.error("Ошибка при получении данных от сервера");
            ConsoleHelper.writeSystemMessage("Сервер не отвечает, перезапустите приложение");
        } catch (InterruptedException e) {
            log.error("Ошибка во время получения данных из packetQueue, данные утеряны");
            ConsoleHelper.writeSystemMessage("Ошибка, данные утеряны, перезапустите приложение");
        }
    }
}
