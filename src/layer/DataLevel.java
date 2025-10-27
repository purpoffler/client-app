package layer;

import layer.dto.Message;
import layer.enums.ExpectedDataType;
import config.ClientConfig;
import utlis.ConsoleHelper;

import java.util.concurrent.BlockingQueue;

public class DataLevel implements Runnable {
    private final ClientConfig clientConfig = ClientConfig.getInstance();
    private final BlockingQueue<Message> dataQueue = clientConfig.getDataQueue();
    private static final AppLogger log = new CustomLogger(DataLevel.class);

    @Override
    public void run() {
        while (true) {
            ConsoleHelper.getInstruction();
            ExpectedDataType dataType = chooseDataType();
            try {
                if (dataType == null) {
                    clientConfig.stop();
                    dataQueue.put(new Message(false));
                    log.info("Поток прерван {}", this.getClass());
                    ConsoleHelper.write("Программа завершает работу, до новых встреч)");
                    break;
                }
                String data = collectData();
                dataQueue.put(new Message(data, dataType));
                log.debug("Пользователь ввел data: " + data + " dataType: " + dataType);
            } catch (InterruptedException e) {
                ConsoleHelper.write(clientConfig.getColorRed() + "Произошла ошибка, повторите ввод данных" + clientConfig.getColorDefault());
                log.error("Ошибка при добавлении в очередь [{}]", this.getClass(), e);
            }
        }
    }

    private String collectData() {
        while (true) {
            String data = ConsoleHelper.readString();
            if (data.length() < 200) {
                log.debug("Пользователь ввел данные: " + data);
                return data;
            }
            ConsoleHelper.write("Упс, количество символов больше 200");
        }
    }

    private ExpectedDataType chooseDataType() {
        while (true) {
            String dataType = ConsoleHelper.readString();
            if (dataType.equalsIgnoreCase("EXIT")) {
                return null;
            }
            try {
                ExpectedDataType type = ExpectedDataType.valueOf(dataType.toUpperCase());
                ConsoleHelper.write(String.format("Окей, тогда собираем %s", dataType));
                log.debug("Пользователь выбрал тип данных: " + type);
                return type;
            } catch (IllegalArgumentException e) {
                ConsoleHelper.write("Упс, неправильный формат данных");
            }
        }
    }
}
