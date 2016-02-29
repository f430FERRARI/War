package ServerNetwork;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * Created by mlee43 on 2016-02-28.
 */
public class ServerMessageSender implements Callable {

    private int id;
    private byte[] message;
    private Socket clientSocket;

    public ServerMessageSender(byte[] message, int id, Socket socket) {
        this.message = message;
        this.id = id;
        this.clientSocket = socket;
    }

    /**
     * This thread method sends a single message.
     * @return
     * @throws Exception
     */
    @Override
    public Object call() throws IOException {
        send();
        return "Message sent!";
    }

    /**
     * This method sends the byte array to the player associated with the given id.
     * @return
     */
    private void send() throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        dataOutputStream.writeInt(message.length); // write length of the message
        dataOutputStream.write(message);
        dataOutputStream.close();
    }
}
