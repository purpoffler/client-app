import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.zip.CRC32;

public class Packaging implements Runnable {
    static final String signature;
    private final BlockingQueue<ArrayList<String>> dataQueue;
    private final BlockingQueue<String> packetQueue;
    StringBuilder sb = new StringBuilder();

    public Packaging(BlockingQueue dataQueue, BlockingQueue packetQueue){
        this.dataQueue = dataQueue;
        this.packetQueue = packetQueue;
    }

    // Сигнатура длинной 8 симоволов
    static{
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();

        for (int i=0; i < 8; i++){
            char symbol = (char) (33 + rand.nextInt(94));
            sb.append(symbol);
        }
        signature = sb.toString();
    }

    // Собираем пакет
    @Override
    public void run() {
        while (true) {
            try {
                ArrayList<String> list = dataQueue.take(); // Получаем данные из очереди dataQueue
                String dataType = list.get(0);
                String data = list.get(1);
                // Собираем пакет
                sb.append(signature);
                sb.append(dataLength(data));
                sb.append(dataType(dataType));
                sb.append(getData(data));
                sb.append(CRC32(data));
                // Отравляем пакет в очередь packetQueue
                try {
                    packetQueue.put(sb.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Длинна данных - 3 символа
    private String dataLength(String data) {
        return String.format("%03d", data.length());
    }

    // Тип данных - 7 символов
    private String dataType(String dataType) {
        return String.format("%-7s", dataType);
    }

    // Сами данные
    private String getData(String data) {
        return data;
    }

    // Считаем контрольную сумму от всего пакета - длина 8 символов
    private String CRC32(String data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data.getBytes());
        long value = crc32.getValue();
        return String.format("%08X", value);
    }
}
