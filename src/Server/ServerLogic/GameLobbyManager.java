package Server.ServerLogic;

import Server.ServerNetwork.CommunicationCodes;
import Server.ServerNetwork.ServerMessageHandler;
import Server.ServerNetwork.ServerNetworkManager;

import java.util.ArrayList;

public class GameLobbyManager implements ServerNetworkManager.LobbyMessageListener {

    private Server server;
    private ServerNetworkManager networkManager;
    private int lobbyCapacity = 10;
    private int gameLobbyCapacity = 4;
    private int observerLobbyCapacity = 4;
    private ArrayList<Integer> lobbyList = new ArrayList<>();
    private ArrayList<Integer> gameLobbyList = new ArrayList<>();
    private ArrayList<Integer> observerList = new ArrayList<>();

    public GameLobbyManager() {
//        this.server = server;
        this.networkManager = ServerNetworkManager.getInstance();
        networkManager.register(ServerMessageHandler.LISTENER_LOBBY, this);
    }

    /**
     * Callback method to the communicator. This is called when a player joins the general lobby waiting area.
     *
     * @param id The id of the player who joined the waiting area.
     */
    @Override
    public void onClientJoinLobby(int id) {
        if (lobbyList.size() < lobbyCapacity) {
            lobbyList.add(id);
            notifyLobbyListChanged();
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
        notifyLobbyListChanged();
    }


    /**
     * Callback method to the communicator. This is called when a player joins the game lobby waiting area.
     *
     * @param id The id of the player who joined the waiting area.
     */
    @Override
    public void onClientJoinGameLobby(int id) {
        if (gameLobbyList.size() < gameLobbyCapacity) {
            gameLobbyList.add(id);
            notifyLobbyListChanged();
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
        notifyLobbyListChanged();
    }

    /**
     * Callback method to the communicator. This is called when a player joins the observer lobby waiting area.
     *
     * @param id The id of the player who joined the waiting area.
     */
    @Override
    public void onClientJoinObserver(int id) {
        if (observerList.size() < observerLobbyCapacity) {
            observerList.add(id);
            notifyLobbyListChanged();
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
        notifyLobbyListChanged();
    }

    /**
     * This method notifies all players about a change in the lobby lists. It sends a string with the id of the players
     * in each list.
     */
    private void notifyLobbyListChanged() {
        if (!lobbyList.isEmpty() || !gameLobbyList.isEmpty()) {
            // Build string containing the list of ids
            StringBuilder builder = new StringBuilder();
            for (int lobbyGuy : lobbyList) {
                builder.append(lobbyGuy);
                builder.append(Utilities.PARSE_SPLITTER_FIELD);
            }

            builder.append(Utilities.PARSE_SPLITTER_ENTRY);

            for (int gameGuy : gameLobbyList) {
                builder.append(gameGuy);
                builder.append(Utilities.PARSE_SPLITTER_FIELD);
            }

            builder.append(Utilities.PARSE_SPLITTER_ENTRY);

            for (int observerDude : observerList) {
                builder.append(observerDude);
                builder.append(Utilities.PARSE_SPLITTER_FIELD);
            }

            // Send the string
            byte[] lists = Utilities.stringToByteArray(builder.toString());
            byte[] message = Utilities.prepareMessage(CommunicationCodes.LOBBY_LISTS_CHANGED, lists);
            networkManager.sendToAll(message);
        }
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
