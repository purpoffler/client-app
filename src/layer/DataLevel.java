package layer;

import dto.Message;
import layer.enums.ExpectedDataType;
import utlis.ConsoleHelper;

import java.util.concurrent.BlockingQueue;

public class DataLevel implements Runnable {
    private final BlockingQueue<Message> dataQueue;

    public DataLevel(BlockingQueue<Message> dataQueue) {
        this.dataQueue = dataQueue;
    }

    @Override
    public void run() {
        while (true) {
            ConsoleHelper.getInstruction();
            ExpectedDataType dataType = chooseDataType();
            String data = collectData();
            try {
                dataQueue.put(new Message(data, dataType));
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage("Ошибка при добавлении в очередь на уровне layer.DataLevel");
            }
        }
    }

    private String collectData() {
        while (true) {
            String data = ConsoleHelper.readString();
            if (data.length() < 200) {
                return data;
            }
            ConsoleHelper.writeMessage("Упс, количество символов больше 200");
        }
    }

    private ExpectedDataType chooseDataType() {
        while (true) {
            String dataType = ConsoleHelper.readString();
            try {
                ExpectedDataType type = ExpectedDataType.valueOf(dataType.toUpperCase());
                ConsoleHelper.writeMessage(String.format("Окей, тогда собираем %s", dataType));
                return type;
            } catch (IllegalArgumentException e) {
                ConsoleHelper.writeMessage("Упс, неправильный формат данных");
            }
        }
    }
}
