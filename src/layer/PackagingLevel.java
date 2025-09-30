package layer;

import layer.dto.Message;
import layer.enums.ExpectedDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utlis.ClientConfig;
import utlis.ConsoleHelper;

import java.util.concurrent.BlockingQueue;
import java.util.zip.CRC32;

public class PackagingLevel implements Runnable {
    private final String signature = ClientConfig.getSignature();
    private final BlockingQueue<Message> dataQueue = ClientConfig.getDataQueue();
    private final BlockingQueue<String> packetQueue = ClientConfig.getPacketQueue();
    private static final Logger log = LoggerFactory.getLogger(PackagingLevel.class);

    // Собираем пакет
    @Override
    public void run() {
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                Message message = dataQueue.take();
                ExpectedDataType dataType = message.getDataType();
                String data = message.getData();
                log.debug("Получили DTO message data: " + data + " dataType: " + dataType);
                // Очищаем билдер
                sb.setLength(0);

                sb.append(signature).append("|");
                sb.append(dataLength(data)).append("|");
                sb.append(dataType(dataType)).append("|");
                sb.append(data).append("|");
                sb.append(crc32(data));

                String packet = sb.toString();
                if (packetQueue.offer(packet)) {
                    log.debug("Пакет добавлен в очередь: " + sb.toString());
                } else {
                    log.error("Очередь переполнена, данные утеряны");
                }
            } catch (InterruptedException e) {
                log.error("Ошибка во время получения данных из dataQueue");
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
