package layer.socket;

import config.ClientConfig;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Connection implements Closeable {
    private final Socket socket;
    private final BufferedWriter out;
    private final BufferedReader in;

    public Connection() throws IOException {
        ClientConfig clientConfig = ClientConfig.getInstance();
        this.socket = new Socket(clientConfig.getHost(), clientConfig.getPort());
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void send(String string) throws IOException {
        out.write(string);
        out.flush();
    }

    public String receive() throws IOException, ClassNotFoundException, SocketTimeoutException {
        return in.readLine();
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
