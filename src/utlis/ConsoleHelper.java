package utlis;

import config.ClientConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ConsoleHelper {
    private static final BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

    public static void getInstruction() {
        System.out.println();
        System.out.println("Привет!");
        System.out.println("С помощью меня ты можешь отправить данные различных типов:");
        System.out.println("""
                    CONSOLE – сервер выведет данные в консоль;
                    PLAIN – сервер сохранит данные в обычный файл свободного формата;
                    JSON – сервер сохранить данные в файл с типом .json и json-форматированием;
                    EXIT - если хочешь закончить работу.
                """);
        System.out.println("Введи формат, в котором ты хочешь отправить данные, например, CONSOLE");
    }

    public static void write(String message) {
        System.out.println(message);
    }

    public static void writeSystemMessage(String message) {
        System.out.println(ClientConfig.getInstance().getColorBlue() + "Системное сообщение:\n" + message + ClientConfig.getInstance().getColorDefault() + "\n");
    }

    public static String readString() {
        while (true) {
            try {
                return bf.readLine();
            } catch (IOException e) {
                ConsoleHelper.writeSystemMessage("Ошибка при считывании строки \n Повторите ввод сначала");
            }
        }
    }
}
