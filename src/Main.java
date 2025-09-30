import layer.ClientLevel;
import layer.DataLevel;
import layer.PackagingLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utlis.ClientConfig;

public class Main {
    public static void main(String[] args) throws Exception {
        ClientConfig clientConfig = ClientConfig.getInstance();

        Logger log = LoggerFactory.getLogger(Main.class);
        log.info("Программа запустилась");

        Thread dataThread = new Thread(new DataLevel());
        Thread packagingThread = new Thread(new PackagingLevel());
        Thread sendThread = new Thread(new ClientLevel());

        dataThread.start();
        packagingThread.start();
        sendThread.start();
    }
}
