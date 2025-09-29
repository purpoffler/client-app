package layer;

import layer.dto.Message;
import layer.enums.ExpectedDataType;
import utlis.ClientConfig;
import utlis.ConsoleHelper;

import java.util.concurrent.BlockingQueue;
import java.util.zip.CRC32;

public class PackagingLevel implements Runnable {
    private final String signature = ClientConfig.getSignature();
    private final BlockingQueue<Message> dataQueue = ClientConfig.getDataQueue();
    private final BlockingQueue<String> packetQueue = ClientConfig.getPacketQueue();

    // Собираем пакет
    @Override
    public void run() {
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                Message message = dataQueue.take(); // Получаем данные из очереди dataQueue
                ExpectedDataType dataType = message.getDataType();
                String data = message.getData();
                // Очищаем билдер
                sb.setLength(0);
                // Собираем пакет
                sb.append(signature).append("|");
                sb.append(dataLength(data)).append("|");
                sb.append(dataType(dataType)).append("|");
                sb.append(data).append("|");
                sb.append(crc32(data));
                // Отравляем пакет в очередь packetQueue
                String packet = sb.toString();
                packetQueue.put(packet);
                //ConsoleHelper.writeMessage(sb.toString()); // v file nado
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage("Поток прерван во время работы с очередью");
            }
        }
    }

    // Длинна данных - 3 символа
    public String dataLength(String data) {
        return String.format("%03d", data.length());
    }

    // Тип данных - 7 символов
    public String dataType(ExpectedDataType dataType) {
        return String.format("%-7s", dataType);
    }

    public String crc32(String data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data.getBytes());
        long value = crc32.getValue();
        return String.valueOf(value);
    }

}
