package Server.ServerNetwork;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNetworkManager extends ServerMessageHandler implements ConnectionAcceptor.ConnectionListener {

    public static final byte LOGIN_REQUEST_INFO = 0x0;
    public static final byte LOGIN_RECEIVE_INFO = 0x1;

    private HashMap<Integer, Socket> playerConnections = new HashMap<>();
    private ExecutorService executorService = Executors.newCachedThreadPool(); // TODO: Might not work like this

    private static ServerNetworkManager serverNetworkManager = null;   // WATCH THIS GUY! ITS STATIC

    private ServerNetworkManager() {
        resetServerNetwork();
    }

    /**
     * Method used to return the singleton. If one does not exist, it is created.
     *
     * @return The single instance of the ServerNetworkManager object
     */
    public static ServerNetworkManager getInstance() {
        if (serverNetworkManager == null) {
            serverNetworkManager = new ServerNetworkManager();
        }
        return serverNetworkManager;
    }

    public void resetServerNetwork() {
        playerConnections.clear();
        // TODO: Restart threads
    }

    public void startServerNetwork() {
        resetServerNetwork();
        executorService.submit(new ConnectionAcceptor(this));
    }

    public void shutdownNetwork() {
        // TODO: Shutdown threads
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
            return;
        }

        final Socket finalDestinationSocket = destinationSocket;
        executorService.submit(new Callable() {
            @Override
            public Object call() throws Exception {
                DataOutputStream dataOutputStream = new DataOutputStream(finalDestinationSocket.getOutputStream());
                dataOutputStream.writeInt(message.length); // write length of the message
                dataOutputStream.write(message);
                dataOutputStream.close();
                return -1;
            }
        });
    }

    public void connect(int id, Socket newSocket) {
        // Add player to connection list
        playerConnections.put(id, newSocket);

        // Start listening to the new client
        executorService.submit(new ServerClientListener(this, newSocket));
        System.out.println("HEERRREESSS JOHHNNNYY!");
        // TODO: Request name and create the player
//        byte[] requestName = {0x1, LOGIN_RECEIVE_INFO};
//        send(id, requestName);
//        adminMessageListener.onReceivePlayerInfo(id, );
    }

    public void disconnect(int id) {
        playerConnections.remove(id);
        // TODO: Turn off listening thread
    }
}
