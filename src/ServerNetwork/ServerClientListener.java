package ServerNetwork;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * Created by mlee43 on 2016-02-28.
 */
public class ServerClientListener implements Callable {

    private int id;
    private Socket clientSocket;
    private byte[] message = new byte[1024];  // TODO: May be too big
    private boolean isConnected = false;

    private ListenerListener listener;

    public interface ListenerListener {
        void onReceiveMessage(byte[] message);
    }

    public ServerClientListener(ServerNetworkManager manager, int id, Socket clientSocket) {
        this.listener = manager;
        this.id = id;
        this.clientSocket = clientSocket;
        this.isConnected = true;
    }

    /**
     * This thread method initiates the listen method and returns when the client disconnects.
     *
     * @return
     * @throws Exception
     */
    @Override
    public Object call() throws Exception {
        return listen();
    }

    /**
     * This method listens to all incoming messages from the server and passes the message onto the message handler.
     *
     * @return Returns -1 when it stops listening
     */
    private int listen() throws IOException { // TODO: Handle reading length
        System.out.println("Listening...");
        DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
        while (isConnected) {
            int length = dataInputStream.readInt();                    // read length of incoming message
            if (length > 0) {
                dataInputStream.readFully(message, 0, message.length); // read the message
                listener.onReceiveMessage(message);
            }
        }
        return -1;
    }
}
