import layer.ClientLevel;
import layer.DataLevel;
import layer.PackagingLevel;
import layer.loggers.AppLogger;
import layer.loggers.CustomLogger;
import layer.loggers.Slf4jAdapter;
import utlis.ClientConfig;

public class Main {
    public static void main(String[] args) throws Exception {
        ClientConfig.init();

        //AppLogger log = new Slf4jAdapter(Main.class);
        AppLogger log = new CustomLogger(Main.class);

        log.info("Программа запустилась");

        Thread dataThread = new Thread(new DataLevel());
        Thread packagingThread = new Thread(new PackagingLevel());
        Thread sendThread = new Thread(new ClientLevel());

        dataThread.start();
        packagingThread.start();
        sendThread.start();
    }
}
