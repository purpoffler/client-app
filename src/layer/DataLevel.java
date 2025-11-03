package layer;

import config.ClientConfig;
import exceptions.ExitException;
import layer.dto.Message;
import layer.enums.ExpectedDataType;
import layer.logger.CustomLogger;
import utlis.ConsoleHelper;

public class DataLevel implements Runnable {
    private final ClientConfig clientConfig = ClientConfig.getInstance();
    private static final CustomLogger log = new CustomLogger(DataLevel.class.getSimpleName());

    @Override
    public void run() {
        while (clientConfig.isContinue()) {
            try {
                ConsoleHelper.getInstruction();
                ExpectedDataType dataType = chooseDataType();
                String data = collectData();
                clientConfig.getDataQueue().put(new Message(data, dataType));
                log.debug("Пользователь ввел data: " + data + " dataType: " + dataType);
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(clientConfig.getColorRed() + "Произошла ошибка, повторите ввод данных" + clientConfig.getColorDefault());
                log.error("Ошибка при добавлении в очередь", e);
            } catch (ExitException e) {
                clientConfig.stop();
                ConsoleHelper.writeMessage("Завершаем работу приложения");
                log.info("Пользователь решил завершить работу");
                break;
            }
        }
    }

    private String collectData() throws ExitException {
        while (true) {
            String data = ConsoleHelper.readString();
            checkExitCommand(data);
            if (data.length() < 200) {
                log.debug("Пользователь ввел данные: " + data);
                return data;
            }
            ConsoleHelper.writeMessage("Упс, количество символов больше 200");
        }
    }

    private ExpectedDataType chooseDataType() throws ExitException {
        while (true) {
            try {
                String userInput = ConsoleHelper.readString();
                checkExitCommand(userInput);
                ExpectedDataType dataType = ExpectedDataType.valueOf(userInput.toUpperCase());
                ConsoleHelper.writeMessage(String.format("Окей, тогда собираем %s", dataType));
                log.debug("Пользователь выбрал тип данных: " + dataType);
                return dataType;
            } catch (IllegalArgumentException e) {
                ConsoleHelper.writeMessage("Упс, неправильный формат данных, повторите ввод");
            }
        }
    }

    private void checkExitCommand(String userInput) throws ExitException {
        if (userInput.equalsIgnoreCase("EXIT")) {
            throw new ExitException("Пользователь решил завершить программу");
        }
    }
}
