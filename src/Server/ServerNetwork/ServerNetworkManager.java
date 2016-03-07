package Server.ServerNetwork;

import Server.CommunicationCodes;
import Server.Utilities;

import java.io.BufferedOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNetworkManager extends ServerMessageHandler implements ConnectionAcceptor.ConnectionListener {

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

    /**
     * Resets state of the server network.
     */
    public void resetServerNetwork() {
        playerConnections.clear();
        // TODO: Restart threads
    }

    /**
     * Starts the server network by resetting its state and starting the connection acceptor.
     */
    public void startServerNetwork() {
        resetServerNetwork();
        executorService.submit(new ConnectionAcceptor(this));
    }

    /**
     * Shuts down the network by closing all network related threads.
     */
    public void shutdownNetwork() {
        // TODO: Shutdown threads
    }

    /**
     * This method starts a thread using the executor service to send a message by referencing the player id and socket
     * from the playerConnections ArrayList
     *
     * @param destinationId The integer ID of the player that the message is being sent to
     * @param message       The message the player wants to send
     */
    public void send(int destinationId, byte[] message) {
        Socket destinationSocket = null;
        if (playerConnections.containsKey(destinationId)) {
            destinationSocket = playerConnections.get(destinationId);
        } else {
            System.out.println("ID does not exist!");
            return;
        }

        final Socket finalDestinationSocket = destinationSocket;
        executorService.submit(new Callable() {
            @Override
            public Object call() throws Exception {
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(finalDestinationSocket.getOutputStream());
                bufferedOutputStream.write(message);
                bufferedOutputStream.flush();
                return -1;
            }
        });
    }

    /**
     * This method sends the same message to all players in the connection list.
     *
     * @param message The message to be sent to the client.
     */
    public void sendToAll(byte[] message) {
        for (int id : playerConnections.keySet()) {
            send(id, message);
        }
    }

    /**
     * Callback method to ConnectionAcceptor. Completes the connection by adding the new client to the connection list,
     * opening the client listener and requesting the player's info.
     *
     * @param id        The player's newly assigned id.
     * @param newSocket The player's associated socket info.
     */
    public void connect(int id, Socket newSocket) {
        // Add player to connection list
        playerConnections.put(id, newSocket);

        // Start listening to the new client
        executorService.submit(new ServerClientListener(this, newSocket));
        System.out.println("HEERRREESSS JOHHNNNYY! The Listeners ID: " + id);   // Debug

        // Send request for player info
        byte[] message = Utilities.prepareMessage(CommunicationCodes.ADMIN_REQUEST_INFO, Utilities.intToByteArray(id));
        send(id, message);
    }

    public void disconnect(int id) {
        playerConnections.remove(id);
        // TODO: Turn off listening thread
    }

    private void onIncompletedConnection() {
        // TODO: Case where a client does not send name
    }

    public HashMap<Integer, Socket> getPlayerConnections() {
        return playerConnections;
    }
}
