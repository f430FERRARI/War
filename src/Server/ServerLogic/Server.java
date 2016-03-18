package Server.ServerLogic;

import Server.ServerNetwork.CommunicationCodes;
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
    }

    @Override
    public void onDisconnect(int id) {
    }

    @Override
    public void onCreateAccount(int id, String accountInfo) {
        String[] parts = accountInfo.split(Utilities.PARSE_SPLITTER_TYPE);

        if (Login.createNewLogin(parts[0], parts[1])) {

            addPlayer(id, parts[0]);

            // Send the player the result of creating an account
            byte[] result = Utilities.stringToByteArray(Login.LOGIN_SUCCESS);
            networkManager.send(id, Utilities.prepareMessage(CommunicationCodes.ADMIN_CREATE_RESULT, result));

        } else {
            byte[] result = Utilities.stringToByteArray(Login.LOGIN_FAILURE);
            networkManager.send(id, Utilities.prepareMessage(CommunicationCodes.ADMIN_CREATE_RESULT, result));
        }
    }

    @Override
    public void onReceiveLogin(int id, String loginInfo) {
        String[] parts = loginInfo.split(Utilities.PARSE_SPLITTER_TYPE);

        if (Login.verifyLogin(parts[0], parts[1])) {

            addPlayer(id, parts[0]);

            // Send the player the login result
            byte[] result = Utilities.stringToByteArray(Login.LOGIN_SUCCESS);
            networkManager.send(id, Utilities.prepareMessage(CommunicationCodes.ADMIN_LOGIN_RESULT, result));

        } else {
            byte[] result = Utilities.stringToByteArray(Login.LOGIN_FAILURE);
            networkManager.send(id, Utilities.prepareMessage(CommunicationCodes.ADMIN_LOGIN_RESULT, result));
        }
    }

    private void addPlayer(int id, String username) {
        // Create a new player with the id and name
        Player newPlayer = new Player(id);
        newPlayer.setName(username);

        if (!playerList.isEmpty()) {
            // Send the list of players to the new player
            StringBuilder builder = new StringBuilder();
            for (int playerId : playerList.keySet()) {
                builder.append(playerId);
                builder.append(Utilities.PARSE_SPLITTER_ITEMS);
                builder.append(playerList.get(playerId).getName());
                builder.append(Utilities.PARSE_SPLITTER_TYPE);
            }
            byte[] idsAndNames = Utilities.stringToByteArray(builder.toString());
            networkManager.send(id, Utilities.prepareMessage(CommunicationCodes.ADMIN_GROUP_INFO, idsAndNames));
        }

        // Send name and id of the new player to all connected players including the new player
        byte[] idBytes = Utilities.intToByteArray(id);
        byte[] nameBytes = Utilities.stringToByteArray(username);
        byte[] payload = Utilities.appendByteArrays(idBytes, nameBytes);
        networkManager.sendToAll(Utilities.prepareMessage(CommunicationCodes.ADMIN_UPDATE_PLAYERS, payload));

        // Add the player to the playerlist
        playerList.put(id, newPlayer);

        // TODO: Add the player to the lobby
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }
}
