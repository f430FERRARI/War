package Client.ClientNetwork;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mlee43 on 2016-02-27.
 */
public class ClientNetworkManager extends ClientMessageHandler {

    private boolean connected = false;
    Socket clientSocket = null;

    private static ClientNetworkManager clientNetworkManager = null;

    public static ClientNetworkManager getInstance() {
        if (clientNetworkManager == null) {
            clientNetworkManager = new ClientNetworkManager();
        }
        return clientNetworkManager;
    }

    public void resetClientNetwork() {
        connected = false;
        clientSocket = null;
    }

    public void shutdownClientNetwork() {
        // TODO: Close threads
    }

    /**
     * This method starts communication between the client and the server. It then creates a seperate thread that always
     * listens for incoming messages from the server.
     */
    public void startServerConnection(int port) { // TODO: Port for testing
        resetClientNetwork();

        // Create a new ongoing listener
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            Socket clientSocket = new Socket("127.0.0.1", port); // TODO: Update these
            executorService.submit(new ClientServerListener(this, clientSocket));
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            executorService.shutdownNow();
        }
    }

    public void send(byte[] message) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        dataOutputStream.writeInt(message.length); // write length of the message
        dataOutputStream.write(message);
    }

    public boolean getConnected(){
        return connected;
    }

    public void setConnected(boolean status) {
        this.connected = status;
    }
}