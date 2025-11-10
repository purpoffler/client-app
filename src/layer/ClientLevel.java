package layer;

import config.ClientConfig;
import layer.logger.CustomLogger;
import layer.socket.Connection;
import utlis.ConsoleHelper;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class ClientLevel implements Runnable {
    private final ClientConfig clientConfig = ClientConfig.getInstance();
    private static final CustomLogger log = new CustomLogger(ClientLevel.class.getSimpleName());

    @Override
    public void run() {
        try (Connection connection = new Connection()) {
            while (clientConfig.isContinue()) {
                String packet = clientConfig.getPacketQueue().poll(500, TimeUnit.MILLISECONDS);
                if (packet != null) {
                    connection.send(packet + "\n");
                    log.debug("Отправляем пакет серверу" + packet);
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
        } catch (InterruptedException e) {
            log.error("Ошибка извлечения данных из очереди", e);
            ConsoleHelper.writeSystemMessage("Непредвиденная ошибка, перезапустите приложение");
        }
    }
}
