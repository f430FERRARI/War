package ServerNetwork;

import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mlee43 on 2016-02-28.
 */
public class ServerConnectionManager implements ConnectionAcceptor.ConnectionListener {

    private HashMap<Integer, Socket> playerConnections = new HashMap<>();
    private ExecutorService executorService = Executors.newCachedThreadPool(); // TODO: Might not work like this

    public void startServerNetwork() {
        executorService.submit(new ConnectionAcceptor(this));
        System.out.println("startServerNetwork. Got here!");
    }

    /**
     * This method starts a thread using the executor service to send a message by referencing the player id and socket
     * from the playerConnections ArrayList
     *
     * @param id      The integer ID of the player that the message is being sent to
     * @param message The message the player wants to send
     */
    public void send(int id, byte[] message) {

        Socket destinationSocket = null;
        if (!playerConnections.containsKey(id)) {
            destinationSocket = playerConnections.get(id);
        } else {
            System.out.println("ID does not exist!");
        }

        executorService.submit(new ServerMessageSender(message, id, destinationSocket));
        System.out.println("send. Got here!");
    }

    public void connect(int id, Socket newSocket) {
        // Add player to connection list
        playerConnections.put(id, newSocket);

        // Start listening to the new client
        executorService.submit(new ServerClientListener(id, newSocket));
        System.out.println("send. Got here!");
    }

    public void disconnect(int id) {
        playerConnections.remove(id);
        // TODO: Turn off listening thread
    }
}
