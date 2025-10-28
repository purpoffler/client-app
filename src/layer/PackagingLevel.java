package layer;

import layer.dto.Message;
import layer.enums.ExpectedDataType;
import config.ClientConfig;
import layer.logger.CustomLogger;
import utlis.ConsoleHelper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;

public class PackagingLevel implements Runnable {
    private final ClientConfig clientConfig = ClientConfig.getInstance();
    private final BlockingQueue<Message> dataQueue = clientConfig.getDataQueue();
    private final BlockingQueue<String> packetQueue = clientConfig.getPacketQueue();
    private static final CustomLogger log = new CustomLogger(PackagingLevel.class.getSimpleName());

    @Override
    public void run() {
        StringBuilder sb = new StringBuilder();
        while (clientConfig.isContinue()) {
            Message message = dataQueue.poll();
            if (message != null) {
                ExpectedDataType dataType = message.getDataType();
                String data = message.getData();
                log.debug("Получили DTO message data: " + data + " dataType: " + dataType);
                // Очищаем билдер
                sb.setLength(0);

                sb.append(clientConfig.getSignature()).append("|");
                sb.append(dataLength(data)).append("|");
                sb.append(dataType(dataType)).append("|");
                sb.append(data).append("|");
                sb.append(crc32(data));

                String packet = sb.toString();

                if (packetQueue.offer(packet)) {
                    log.debug("Пакет добавлен в очередь: " + sb.toString());
                } else {
                    log.debug("Очередь переполнена, данные утеряны");
                }
            }
        }
    }

    public String dataLength(String data) {
        return String.format("%03d", data.length());
    }

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
