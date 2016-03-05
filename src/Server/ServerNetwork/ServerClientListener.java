package Server.ServerNetwork;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ServerClientListener implements Callable {

    private Socket clientSocket;
    private boolean isConnected = false;

    private ServerNetworkManager manager;

    public ServerClientListener(ServerNetworkManager manager, Socket clientSocket) {
        this.manager = manager;
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
     * @return Returns -1 when it stops listening.
     */
    private int listen() throws IOException {

        BufferedInputStream bufferedInputStream = new BufferedInputStream(clientSocket.getInputStream());
        byte[] receivedMessage = null;
        int messageLength = 0;

        while (isConnected) {
            if ((messageLength = bufferedInputStream.read()) != -1) {
                receivedMessage = new byte[messageLength];
                bufferedInputStream.read(receivedMessage, 0, messageLength);
                manager.handleMessage(receivedMessage); // TODO: May need this on its own thread
            }
        }
        return -1;
    }
}
