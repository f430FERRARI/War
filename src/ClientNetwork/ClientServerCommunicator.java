package ClientNetwork;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mlee43 on 2016-02-27.
 */
public class ClientServerCommunicator {

    private boolean connected = false;
    private String name;  //TODO: Get name from prompt
    private int id;     // TODO: Retrieve ID

    Socket clientSocket = null;

    public ClientServerCommunicator(String name) {
        this.name = name;
    }


    /**
     * This method starts communication between the client and the server. It then creates a seperate thread that always
     * listens for incoming messages from the server.
     */
    public void startServerConnection() {

        // Create a new ongoing listener
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            Socket clientSocket = new Socket("127.0.0.1", 2000); // TODO: Update these
            executorService.submit(new ClientListener(clientSocket));
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