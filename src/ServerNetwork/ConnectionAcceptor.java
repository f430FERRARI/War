package ServerNetwork;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * Created by mlee43 on 2016-02-28.
 */
public class ConnectionAcceptor implements Callable {

    private boolean acceptMore = true;

    private ConnectionListener mListener;

    public interface ConnectionListener {
        void connect(int id, Socket newSocket);
    }

    public ConnectionAcceptor(ServerNetworkManager manager) {
        mListener = manager;
    }

    @Override
    public Object call() throws Exception {
        return receiveConnection();
    }

    public int receiveConnection() {

        int nextId = 1;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(2000);
            while (acceptMore) {
                Socket clientSocket = serverSocket.accept();
                mListener.connect(nextId++, clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return 1;
    }
}
