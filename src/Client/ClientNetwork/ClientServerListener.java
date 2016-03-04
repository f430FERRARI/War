package Client.ClientNetwork;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ClientServerListener implements Callable {

    Socket clientSocket = null;
    byte[] message = new byte[1024];    // TODO: This might be too big
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

        BufferedInputStream bufferedInputStream = new BufferedInputStream(clientSocket.getInputStream());
        byte[] receivedMessage = null;
        int messageLength = 0;

        while (isConnected) {
            if ((messageLength = bufferedInputStream.read()) != -1) {
                receivedMessage = new byte[messageLength];
                bufferedInputStream.read(receivedMessage, 0, messageLength);
                manager.handleMessage(receivedMessage);
            }
        }
        return -1;
    }
}
