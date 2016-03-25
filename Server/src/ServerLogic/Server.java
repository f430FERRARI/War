package ServerLogic;

import ServerNetwork.CommunicationCodes;
import ServerNetwork.ServerMessageHandler;
import ServerNetwork.ServerNetworkManager;

import java.util.ArrayList;
import java.util.HashMap;

// TODO: Create modular client chat room
// TODO: Bug- Server login, client login, quit, login same account
// TODO: Create file on first run
// TODO: Change int ids to bytes
public class Server implements ServerNetworkManager.AdminMessageListener, GameLobbyManager.LobbyManagerListener {

    private HashMap<Integer, Player> playerList = new HashMap<>();
    private ServerNetworkManager networkManager;
    private ChatRoomManager chatRoomManager;
    private GameLobbyManager gameLobbyManager;
    private ServerGameManager serverGameManager;

    public Server() {
        // Create system components
        chatRoomManager = new ChatRoomManager();
        gameLobbyManager = new GameLobbyManager(this);
        serverGameManager = new ServerGameManager();

        // Start the server network and register this class as a listener
        networkManager = ServerNetworkManager.getInstance();
        networkManager.register(ServerMessageHandler.LISTENER_ADMIN, this);
        networkManager.startServerNetwork();
    }

    /**
     * Callback to the communicator. This is called when a client is quitting. Removes player from all of his lists.
     *
     * @param id The id of the player who wants to quit.
     */
    @Override
    public void onPlayerDisconnect(int id) {
        System.out.println("This guy is quitting: " + playerList.get(id).getName());
        networkManager.onDisconnect(id);        // Shut down player network connection
        playerList.remove(id);                  // Remove player from the playerlist
        gameLobbyManager.onClientExitLobby(id); // Remove player from the lobbies

        byte[] toRemove = Utilities.intToByteArray(id);
        byte[] message = Utilities.prepareMessage(CommunicationCodes.ADMIN_REMOVE_PLAYER, toRemove);
        networkManager.sendToAll(message);
    }

    /**
     * Callback to the communicator. This is called when a server receives a message from the client wanting to create
     * an account. Gets the account info, creates the account and sends back the result.
     *
     * @param id The id number of the player.
     * @param accountInfo A string containing the username and password for the new account.
     */
    @Override
    public void onCreateAccount(int id, String accountInfo) {
        String[] parts = accountInfo.split(Utilities.PARSE_SPLITTER_ENTRY);

        String result = Login.createNewLogin(parts[0], parts[1]);
        if (result.equals("Success")) {

            // Send the player the result of creating an account
            byte[] resultBytes = Utilities.stringToByteArray(result);
            networkManager.send(id, Utilities.prepareMessage(CommunicationCodes.ADMIN_CREATE_RESULT, resultBytes));

        } else {
            byte[] resultBytes = Utilities.stringToByteArray(result);
            networkManager.send(id, Utilities.prepareMessage(CommunicationCodes.ADMIN_CREATE_RESULT, resultBytes));
        }
    }

    /**
     * Callback to the communicator. This is called when the server receives a login attempt from a user. Checks if it
     * is a valid login and adds the player to the system if it is. Sends the result back to the user.
     *
     * @param id The id of the player.
     * @param loginInfo A string containing the username and password entered by the player.
     */
    @Override
    public void onReceiveLogin(int id, String loginInfo) {
        String[] parts = loginInfo.split(Utilities.PARSE_SPLITTER_ENTRY);

        // Check if the player is already logged in
        for (int player : playerList.keySet()) {
            if(playerList.get(player).getName().equals(parts[0])) {
                String alreadyIn = "This user is already logged in. Try again with another username.";
                byte[] resultBytes = Utilities.stringToByteArray(alreadyIn);
                networkManager.send(id, Utilities.prepareMessage(CommunicationCodes.ADMIN_LOGIN_RESULT, resultBytes));
                return;
            }
        }

        // Check if the lobby is full
        if (gameLobbyManager.getLobbyList().size() >= GameLobbyManager.LOBBY_LOBBY_CAPACITY) {
            String lobbyFull = "Lobby full. Please wait till somebody leaves.";
            byte[] resultBytes = Utilities.stringToByteArray(lobbyFull);
            networkManager.send(id, Utilities.prepareMessage(CommunicationCodes.ADMIN_LOGIN_RESULT, resultBytes));
        } else {
            // Check login information and send the results
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
    }

    /**
     * Callback to GameLobbyManager. This is called when a player starts the game.
     *
     * @param players The player ids of the players who are going to play the game.
     */
    @Override
    public void onStartGame(ArrayList<Integer> players) {
        serverGameManager.setPlayers(players);
        System.out.println("Game set.");
        serverGameManager.startNewGame();
    }

    /**
     * A helper method that adds the player to the system by sending them system info and putting them into the
     * player list.
     *
     * @param id The id of the player.
     * @param username The username of the player.
     */
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
        gameLobbyManager.onClientJoinLobby(id);
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}