package Server.ServerLogic;

import Server.ServerNetwork.CommunicationCodes;
import Server.ServerNetwork.ServerMessageHandler;
import Server.ServerNetwork.ServerNetworkManager;

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

    public GameLobbyManager() {
        this.networkManager = ServerNetworkManager.getInstance();
        networkManager.register(ServerMessageHandler.LISTENER_LOBBY, this);
    }

    @Override
    public void onRequestLobbyList(int id) {
        sendLobbyLists();
    }

    /**
     * Callback method to the communicator. This is called when a player joins the general lobby waiting area.
     *
     * @param id The id of the player who joined the waiting area.
     */
    @Override
    public void onClientJoinLobby(int id) {
        if (lobbyList.size() < LOBBY_LOBBY_CAPACITY) {
            lobbyList.add(id);
            sendLobbyLists();
        } else {
            notifyLobbyFull(id);
        }
    }

    /**
     * Callback method to the communicator. This is called when a player exits the general lobby waiting area.
     *
     * @param id The id of the player who joined the waiting area.
     */
    @Override
    public void onClientExitLobby(int id) {
        lobbyList.remove(new Integer(id));  // Remove object with id = id
        sendLobbyLists();
    }


    /**
     * Callback method to the communicator. This is called when a player joins the game lobby waiting area.
     *
     * @param id The id of the player who joined the waiting area.
     */
    @Override
    public void onClientJoinGameLobby(int id) {
        if (gameLobbyList.size() < LOBBY_GAMELOBBY_CAPACITY) {
            if (!gameLobbyList.contains(new Integer(id))) {
                gameLobbyList.add(id);

                // Remove player from observer list if he's on it
                if (observerList.contains(new Integer(id))) {
                    observerList.remove(new Integer(id));
                }
                sendLobbyLists();
            }
        } else {
            notifyGameLobbyFull(id);
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
        if (observerList.size() < LOBBY_OBSERVER_CAPACITY) {
            if (!observerList.contains(new Integer(id))) {
                observerList.add(id);

                // Remove player from game lobby list if he's on it
                if (gameLobbyList.contains(new Integer(id))) {
                    gameLobbyList.remove(new Integer(id));
                }
                sendLobbyLists();
            }
        } else {
            notifyObserverLobbyFull(id);
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


    /**
     * This notifies a single player if the lobby is full.
     */
    private void notifyLobbyFull(int id) {
        networkManager.send(id, Utilities.prepareOperationMessage(CommunicationCodes.LOBBY_LOBBY_FULL));
    }

    /**
     * This notifies a single player if the game lobby is full.
     */
    private void notifyGameLobbyFull(int id) {
        networkManager.send(id, Utilities.prepareOperationMessage(CommunicationCodes.LOBBY_GAMELOBBY_FULL));
    }

    /**
     * This notifies a single player if the observer lobby is full.
     */
    private void notifyObserverLobbyFull(int id) {
        networkManager.send(id, Utilities.prepareOperationMessage(CommunicationCodes.LOBBY_OBSERVER_FULL));
    }


    public ArrayList<Integer> getLobbyList() {
        return lobbyList;
    }

    // For debug
    public static void main(String[] args) {
        GameLobbyManager gameLobbyManager = new GameLobbyManager();
        gameLobbyManager.onClientJoinGameLobby(2);
        gameLobbyManager.onClientJoinGameLobby(3);
        gameLobbyManager.onClientJoinGameLobby(4);
        gameLobbyManager.onClientExitGameLobby(3);

        gameLobbyManager.onClientJoinLobby(2);
        gameLobbyManager.onClientJoinLobby(3);
        gameLobbyManager.onClientJoinLobby(4);
        gameLobbyManager.onClientExitLobby(3);

        gameLobbyManager.onClientJoinObserver(5);
        gameLobbyManager.onClientJoinObserver(6);
        gameLobbyManager.onClientJoinObserver(5);
        gameLobbyManager.onClientExitLobby(6);
    }
}
