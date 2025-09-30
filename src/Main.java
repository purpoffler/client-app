import layer.ClientLevel;
import layer.DataLevel;
import layer.dto.Message;
import layer.PackagingLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utlis.ClientConfig;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Logger log = LoggerFactory.getLogger(Main.class);
        log.info("Программа запустилась");

        ClientConfig clientConfig = ClientConfig.getInstance();

        Thread dataThread = new Thread(new DataLevel());
        Thread packagingThread = new Thread(new PackagingLevel());
        Thread sendThread = new Thread(new ClientLevel());

        dataThread.start();
        packagingThread.start();
        sendThread.start();
    }
}
