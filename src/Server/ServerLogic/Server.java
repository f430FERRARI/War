package Server.ServerLogic;

import Server.ServerNetwork.CommunicationCodes;
import Server.ServerNetwork.ServerMessageHandler;
import Server.ServerNetwork.ServerNetworkManager;

import java.util.HashMap;

// TODO: Make sure there is only one player with a certain name online
// TODO: Create file on first run
// TODO: Change int ids to bytes
public class Server implements ServerNetworkManager.AdminMessageListener {

    private HashMap<Integer, Player> playerList = new HashMap<>();
    private ServerNetworkManager networkManager;
    private ChatRoomManager chatRoomManager;
    private GameLobbyManager gameLobbyManager;
    private ServerGameManager serverGameManager;

    public Server() {
        // Create system components
        chatRoomManager = new ChatRoomManager();
        gameLobbyManager = new GameLobbyManager();
//        serverGameManager = new ServerGameManager();

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
        String[] parts = accountInfo.split(Utilities.PARSE_SPLITTER_ENTRY);

        // TODO: Check if lobby full

        String result = Login.createNewLogin(parts[0], parts[1]);
        if (result.equals("Success")) {

            addPlayer(id, parts[0]);

            // Send the player the result of creating an account
            byte[] resultBytes = Utilities.stringToByteArray(result);
            networkManager.send(id, Utilities.prepareMessage(CommunicationCodes.ADMIN_CREATE_RESULT, resultBytes));

        } else {
            byte[] resultBytes = Utilities.stringToByteArray(result);
            networkManager.send(id, Utilities.prepareMessage(CommunicationCodes.ADMIN_CREATE_RESULT, resultBytes));
        }
    }

    @Override
    public void onReceiveLogin(int id, String loginInfo) {
        String[] parts = loginInfo.split(Utilities.PARSE_SPLITTER_ENTRY);

        // TODO: Check if lobby full or if the user is already logged in

        String result = Login.verifyLogin(parts[0], parts[1]);
        if (result.equals("Success")) {

            addPlayer(id, parts[0]);

            // Send the player the login result
            byte[] resultBytes = Utilities.stringToByteArray(result);
            networkManager.send(id, Utilities.prepareMessage(CommunicationCodes.ADMIN_LOGIN_RESULT, resultBytes));

        } else {
            byte[] resultBytes = Utilities.stringToByteArray(result);
            networkManager.send(id, Utilities.prepareMessage(CommunicationCodes.ADMIN_LOGIN_RESULT, resultBytes));
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
                builder.append(Utilities.PARSE_SPLITTER_FIELD);
                builder.append(playerList.get(playerId).getName());
                builder.append(Utilities.PARSE_SPLITTER_ENTRY);
            }
            byte[] idsAndNames = Utilities.stringToByteArray(builder.toString());
            networkManager.send(id, Utilities.prepareMessage(CommunicationCodes.ADMIN_GROUP_INFO, idsAndNames));
        }

        // Send name and id of the new player to all connected players including the new player
        byte[] idBytes = Utilities.intToByteArray(id);
        byte[] nameBytes = Utilities.stringToByteArray(username);
        byte[] payload = Utilities.appendByteArrays(idBytes, nameBytes);
        networkManager.sendToAll(Utilities.prepareMessage(CommunicationCodes.ADMIN_UPDATE_PLAYERS, payload));

        // Add the player to the playerlist and the lobby
        playerList.put(id, newPlayer);
        gameLobbyManager.onClientJoinLobby(id);  // TODO: this changed first
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}