import layer.ClientLevel;
import layer.DataLevel;
import layer.PackagingLevel;
import config.ClientConfig;
import layer.logger.CustomLogger;

public class Main {
    public static void main(String[] args) throws Exception {
        ClientConfig.getInstance();

        CustomLogger log = new CustomLogger(Main.class.getSimpleName());

        log.info("Программа запустилась");

        Thread dataThread = new Thread(new DataLevel());
        Thread packagingThread = new Thread(new PackagingLevel());
        Thread sendThread = new Thread(new ClientLevel());

        dataThread.start();
        packagingThread.start();
        sendThread.start();
    }
}
