package Client.ClientNetwork;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    /**
     * Resets state of the client network.
     */
    public void resetClientNetwork() {
        connected = false;
        clientSocket = null;
    }

    /**
     * Shuts down the network by closing all network related threads.
     */
    public void shutdownClientNetwork() {
        // TODO: Close threads
    }

    /**
     * This method starts communication between the client and the server. It then creates a seperate thread that always
     * listens for incoming messages from the server.
     */
    public void startServerConnection() {
        resetClientNetwork();

        // Create a new ongoing listener
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            clientSocket = new Socket("127.0.0.1", 2000); // TODO: Update these
            executorService.submit(new ClientServerListener(this, clientSocket));
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            executorService.shutdownNow();  // TODO: May be shutting down early
        }
    }

    /**
     * This method sends a message to the server.
     *
     * @param message The byte array message.
     * @throws IOException
     */
    public void send(byte[] message) {
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(clientSocket.getOutputStream());
            bufferedOutputStream.write(message);
            bufferedOutputStream.flush();
        } catch (IOException e) {
            System.out.println("Error while sending!");
        }

    }

    public boolean getConnected(){
        return connected;
    }

    public void setConnected(boolean status) {
        this.connected = status;
    }
}