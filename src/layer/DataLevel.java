package layer;

import layer.dto.Message;
import layer.enums.ExpectedDataType;
import config.ClientConfig;
import layer.logger.CustomLogger;
import utlis.ConsoleHelper;

import java.util.concurrent.BlockingQueue;

public class DataLevel implements Runnable {
    private final ClientConfig clientConfig = ClientConfig.getInstance();
    private final BlockingQueue<Message> dataQueue = clientConfig.getDataQueue();
    private static final CustomLogger log = new CustomLogger(DataLevel.class.getSimpleName());

    @Override
    public void run() {
        while (clientConfig.isContinue()) {
            ConsoleHelper.getInstruction();
            ExpectedDataType dataType = chooseDataType();
            if (dataType == null) { break; }
            try {
                String data = collectData();
                dataQueue.put(new Message(data, dataType));
                log.debug("Пользователь ввел data: " + data + " dataType: " + dataType);
            } catch (InterruptedException e) {
                ConsoleHelper.write(clientConfig.getColorRed() + "Произошла ошибка, повторите ввод данных" + clientConfig.getColorDefault());
                log.error("Ошибка при добавлении в очередь", e);
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
            try {
                String userInput = ConsoleHelper.readString();
                if (checkExitInput(userInput)){ return null; }
                ExpectedDataType dataType = ExpectedDataType.valueOf(userInput.toUpperCase());
                ConsoleHelper.write(String.format("Окей, тогда собираем %s", dataType));
                log.debug("Пользователь выбрал тип данных: " + dataType);
                return dataType;
            } catch (IllegalArgumentException e) {
                ConsoleHelper.write("Упс, неправильный формат данных");
            }
        }
    }

    private boolean checkExitInput(String dataType) {
        if (dataType.equalsIgnoreCase("EXIT")) {
            clientConfig.stop();
            ConsoleHelper.write("Завершаем работу приложения");
            log.info("Пользователь решил завершить работу");
            return true;
        }
        return false;
    }
}
