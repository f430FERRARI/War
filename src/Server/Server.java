package Server;

import Server.ServerNetwork.ServerMessageHandler;
import Server.ServerNetwork.ServerNetworkManager;

import java.util.HashMap;

public class Server implements ServerNetworkManager.AdminMessageListener {

    private HashMap<Integer, Player> playerList;
    private ServerNetworkManager networkManager;

    public Server() {
        playerList = new HashMap<>();
    }

    public void startServer() {
        // Start the server network and register this class as a listener
        networkManager = ServerNetworkManager.getInstance();
        networkManager.register(ServerMessageHandler.LISTENER_ADMIN, this);
        networkManager.startServerNetwork();
        ChatRoomManager chatRoomManager = new ChatRoomManager();
    }

    @Override
    public void onDisconnect(int id) {
    }

    /**
     * Callback method to ServerNetworkManager. This method creates a player and puts them in the list.
     *
     * @param id   ID of the player to update.
     * @param name Name to be used in the update.
     */
    @Override
    public void onReceivePlayerInfo(int id, String name) {
        // Create a new player with the id and name
        Player newPlayer = new Player(id);
        newPlayer.setName(name);

        if (!playerList.isEmpty()) {
            // Send the list of players to the new player
            StringBuilder builder = new StringBuilder();
            for (int playerId : playerList.keySet()) {
                builder.append(playerId);
                builder.append("~");
                builder.append(playerList.get(playerId).getName());
                builder.append("`");
            }
            byte[] idsAndNames = Utilities.stringToByteArray(builder.toString());
            networkManager.send(id, Utilities.prepareMessage(CommunicationCodes.ADMIN_GET_PLAYERS, idsAndNames));


            // Send name and id of player to all connected players including the new player
            for (int toUpdate : playerList.keySet()) {
                byte[] idBytes = Utilities.intToByteArray(id);
                byte[] nameBytes = Utilities.stringToByteArray(name);
                byte[] payload = Utilities.appendByteArrays(idBytes, nameBytes);
                networkManager.send(toUpdate, Utilities.prepareMessage(CommunicationCodes.ADMIN_UPDATE_PLAYERS, payload));
            }
        }

        // Finally add the player to the playerlist
        playerList.put(id, newPlayer);
    }

    public Player getPlayer(int id) {
        return playerList.get(id);
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }
}
