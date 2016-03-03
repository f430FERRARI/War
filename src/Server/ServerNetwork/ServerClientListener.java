package Server.ServerNetwork;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ServerClientListener implements Callable {

    private Socket clientSocket;
    private byte[] message = new byte[1024];  // TODO: May be too big
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
     * @return Returns -1 when it stops listening
     */
    private int listen() throws IOException { // TODO: Handle reading length
        System.out.println("Listening...");
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
