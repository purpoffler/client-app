package layer.socket;

import utlis.ClientConfig;

import java.io.*;
import java.net.Socket;

public class Connection implements Closeable {
    private final Socket socket;
    private final BufferedWriter out;
    private final BufferedReader in;


    public Connection() throws IOException {
        this.socket = new Socket(ClientConfig.getHost(), ClientConfig.getPort());
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void send(String string) throws IOException {
        out.write(string);
        out.flush();
    }

    public String receive() throws IOException, ClassNotFoundException {
        return in.readLine();
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
