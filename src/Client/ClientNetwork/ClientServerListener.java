package Client.ClientNetwork;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ClientServerListener implements Callable {

    Socket clientSocket = null;
    byte[] message = new byte[1024];
    private boolean isConnected = false;

    private ClientNetworkManager manager;


    public ClientServerListener(ClientNetworkManager manager, Socket socket) {
        this.manager = manager;
        this.clientSocket = socket;
        this.isConnected = true;
    }

    @Override
    public Object call() throws Exception {
        return listen();
    }

    /**
     * This method listens to all incoming messages from the server and passes the message onto the message handler.
     * @return Returns -1 when it stops listening
     */
    private int listen() throws IOException { // TODO: Handle reading length
        DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
        while (isConnected) {
            int length = dataInputStream.readInt();                    // read length of incoming message
            if (length > 0) {
                dataInputStream.readFully(message, 0, message.length); // read the message
                manager.handleMessage(message);
            }
        }
        return -1;
    }
}
