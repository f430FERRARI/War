package ServerLogic;

import ServerNetwork.CommunicationCodes;
import ServerNetwork.ServerMessageHandler;
import ServerNetwork.ServerNetworkManager;

import java.util.ArrayList;

public class GameLobbyManager implements ServerNetworkManager.LobbyMessageListener {

    private static final String PARSE_LOBBY_GUY = "L";
    private static final String PARSE_GAME_GUY = "G";
    private static final String PARSE_OBSERVER_DUDE = "O";

    public static final int LOBBY_LOBBY_CAPACITY = 10;
    public static final int LOBBY_GAMELOBBY_CAPACITY = 4;
    public static final int LOBBY_OBSERVER_CAPACITY = 4;

    private ServerNetworkManager networkManager;
    private ArrayList<Integer> lobbyList = new ArrayList<>();
    private ArrayList<Integer> gameLobbyList = new ArrayList<>();
    private ArrayList<Integer> observerList = new ArrayList<>();

    private LobbyManagerListener listener;

    public interface LobbyManagerListener {
        void onStartGame(ArrayList<Integer> players);
    }

    public GameLobbyManager(LobbyManagerListener listener) {
        this.listener = listener;
        this.networkManager = ServerNetworkManager.getInstance();
        networkManager.register(ServerMessageHandler.LISTENER_LOBBY, this);
    }

    /**
     * Callback method to Server. This is called when a player joins the general lobby waiting area.
     *
     * @param id The id of the player who joined the waiting area.
     */
    public void onClientJoinLobby(int id) {
        lobbyList.add(id);
        sendLobbyLists();
    }

    /**
     * Callback method to Server. This is called when a player exits the general lobby waiting area.
     * It removes the player from all lists because the user is quitting the game.
     *
     * @param id The id of the player who joined the waiting area.
     */
    @Override
    public void onClientExitLobby(int id) {
        lobbyList.remove(new Integer(id));  // Remove object with id = id
        if (gameLobbyList.contains(new Integer(id))) {
            gameLobbyList.remove(new Integer(id));
        }
        if (observerList.contains(new Integer(id))) {
            observerList.remove(new Integer(id));
        }
        sendLobbyLists();
    }


    /**
     * Callback method to the communicator. This is called when a player joins the game lobby waiting area.
     *
     * @param id The id of the player who joined the waiting area.
     */
    @Override
    public void onClientJoinGameLobby(int id) {
        if (!gameLobbyList.contains(new Integer(id))) {
            gameLobbyList.add(id);

            // Remove player from observer list if he's on it
            if (observerList.contains(new Integer(id))) {
                observerList.remove(new Integer(id));
            }
            sendLobbyLists();
        }
    }

    /**
     * Callback method to the communicator. This is called when a player exits the game lobby waiting area.
     *
     * @param id The id of the player who joined the waiting area.
     */
    @Override
    public void onClientExitGameLobby(int id) {
        gameLobbyList.remove(new Integer(id));  // Remove object with id = id
        sendLobbyLists();
    }

    /**
     * Callback method to the communicator. This is called when a player joins the observer lobby waiting area.
     *
     * @param id The id of the player who joined the waiting area.
     */
    @Override
    public void onClientJoinObserver(int id) {
        if (!observerList.contains(new Integer(id))) {
            observerList.add(id);

            // Remove player from game lobby list if he's on it
            if (gameLobbyList.contains(new Integer(id))) {
                gameLobbyList.remove(new Integer(id));
            }
            sendLobbyLists();
        }
    }

    /**
     * Callback method to the communicator. This is called when a player exits the observer lobby waiting area.
     *
     * @param id The id of the player who joined the waiting area.
     */
    @Override
    public void onClientExitObserver(int id) {
        observerList.remove(new Integer(id));
        sendLobbyLists();
    }

    /**
     * Callback to the communicator. This is called when a player requested a game start. Prompts all other
     * players in the game lobby list and the observer list to start game.
     *
     * @param id The id of the player who pressed start.
     */
    @Override
    public void onReceiveGameStart(int id) {
        byte[] message = Utilities.prepareOperationMessage(CommunicationCodes.LOBBY_GAME_START);
        System.out.println("Game Starting...");
        // Send a game start message to all those in the game lobby list
        for (int player : gameLobbyList) {
            if (player != id) {
                networkManager.send(player, message);
            }
        }
        listener.onStartGame(gameLobbyList); // TODO: Add observers
    }

    /**
     * This method notifies all players about a change in the lobby lists. It sends a string with the id of the players
     * in each list.
     */
    private void sendLobbyLists() {
        // Build string containing the list of ids
        StringBuilder builder = new StringBuilder();
        for (int lobbyGuy : lobbyList) {
            builder.append(PARSE_LOBBY_GUY);
            builder.append(lobbyGuy);
            builder.append(Utilities.PARSE_SPLITTER_ENTRY);
        }

        for (int gameGuy : gameLobbyList) {
            builder.append(PARSE_GAME_GUY);
            builder.append(gameGuy);
            builder.append(Utilities.PARSE_SPLITTER_ENTRY);
        }

        for (int observerDude : observerList) {
            builder.append(PARSE_OBSERVER_DUDE);
            builder.append(observerDude);
            builder.append(Utilities.PARSE_SPLITTER_ENTRY);
        }

        // Send the string
        byte[] lists = Utilities.stringToByteArray(builder.toString());
        byte[] message = Utilities.prepareMessage(CommunicationCodes.LOBBY_LISTS_SEND, lists);
        networkManager.sendToAll(message);
    }

    public ArrayList<Integer> getLobbyList() {
        return lobbyList;
    }

}
