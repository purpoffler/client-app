package layer;

import utlis.ConsoleHelper;

import java.io.*;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

public class ClientLevel implements Runnable {
    private final BlockingQueue<String> packetQueue;

    public ClientLevel(BlockingQueue<String> packetQueue) {
        this.packetQueue = packetQueue;
    }

    @Override
    public void run() {
        try (Connection connection = new Connection()) {
            while (true) {
                String packet = packetQueue.take();// Получаем данные из очереди dataQueue
                //ConsoleHelper.writeMessage(packet);//Нужно будет перенаправить в файл
                connection.send(packet + "\n"); // отправляем сообщение на сервер
                String serverWord = connection.receive(); // ждём, что скажет сервер
                ConsoleHelper.writeSystemMessage(serverWord); // получив - выводим на экран
                if (serverWord.equalsIgnoreCase("false")) {
                    ConsoleHelper.writeSystemMessage("Ошибка при отправке пакета");
                    connection.send(packet + "\n"); // повторно отправляем сообщение на сервер
                }
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException("Неправильный хост", e);
        } catch (IOException e) {
            throw new RuntimeException("Потеря соединения", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Ошибка при получении данных от сервера", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Ошибка во время получения данных из packetQueue в ClientLevel", e);
        }
    }
}
