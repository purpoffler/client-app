package layer;

import config.ClientConfig;
import layer.dto.Message;
import layer.enums.ExpectedDataType;
import layer.logger.CustomLogger;

import java.util.zip.CRC32;

public class PackagingLevel implements Runnable {
    private final ClientConfig clientConfig = ClientConfig.getInstance();
    private static final CustomLogger log = new CustomLogger(PackagingLevel.class.getSimpleName());

    @Override
    public void run() {
        StringBuilder sb = new StringBuilder();
        while (clientConfig.isContinue()) {
            Message message = clientConfig.getDataQueue().poll();
            if (message != null) {
                ExpectedDataType dataType = message.dataType();
                String data = message.data();
                log.debug("Получили DTO message data: " + data + " dataType: " + dataType);
                String packet = buildPacket(sb, data, dataType);
                if (clientConfig.getPacketQueue().offer(packet)) {
                    log.debug("Пакет добавлен в очередь: " + packet);
                } else {
                    log.debug("Очередь переполнена, данные утеряны");
                }
            }
        }
    }

    private String buildPacket(StringBuilder sb, String data, ExpectedDataType dataType) {
        // Очищаем билдер
        sb.setLength(0);
        sb.append(clientConfig.getSignature()).append("|");
        sb.append(dataLength(data)).append("|");
        sb.append(dataType(dataType)).append("|");
        sb.append(data).append("|");
        sb.append(crc32(data));
        return sb.toString();
    }

    private String dataLength(String data) {
        return String.format("%03d", data.length());
    }

    private String dataType(ExpectedDataType dataType) {
        return String.format("%-7s", dataType);
    }

    private String crc32(String data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data.getBytes());
        long value = crc32.getValue();
        return String.valueOf(value);
    }
}
