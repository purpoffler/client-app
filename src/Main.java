import layer.ClientLevel;
import layer.DataLevel;
import layer.dto.Message;
import layer.PackagingLevel;
import utlis.ClientConfig;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        ClientConfig clientConfig = ClientConfig.getInstance();

        Thread dataThread = new Thread(new DataLevel());
        Thread packagingThread = new Thread(new PackagingLevel());
        Thread sendThread = new Thread(new ClientLevel());

        dataThread.start();
        packagingThread.start();
        sendThread.start();
    }
}
