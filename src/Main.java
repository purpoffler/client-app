
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws Exception {
        // Инструкция
        System.out.println("Привет!");
        System.out.println("С помощью меня ты можешь отправить данные различных типов:");
        System.out.println("""
                    CONSOLE – сервер выведет данные в консоль;
                    PLAIN – сервер сохранит данные в обычный файл свободного формата;
                    JSON – сервер сохранить данные в файл с типом .json и json-форматированием
                """);
        System.out.println("Введи формат, в котором ты хочешь отправить данные, например, CONSOLE");

        // Очереди для связи между потоками
        BlockingQueue<ArrayList<String>> dataQueue = new LinkedBlockingQueue<>();
        BlockingQueue<String> packetQueue = new LinkedBlockingQueue<>();

        // Создаем потоки и передаем в них очереди, с которыми они должны работать
        Thread dataThread = new Thread(new DataType(dataQueue));
        Thread packagingThread = new Thread(new Packaging(dataQueue, packetQueue));
        Thread sendThread = new Thread(new Client(packetQueue));

        // Запускаем потоки
        dataThread.start();
        packagingThread.start();
        sendThread.start();

        //System.out.println(packetQueue.take());

    }
}
