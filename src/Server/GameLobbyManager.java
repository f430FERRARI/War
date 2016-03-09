package Server;

import Server.ServerNetwork.ServerMessageHandler;
import Server.ServerNetwork.ServerNetworkManager;

import java.util.ArrayList;

public class GameLobbyManager implements ServerNetworkManager.LobbyMessageListener {

    private ServerNetworkManager networkManager;
    private int gameLobbyCapacity;
    private int lobbyCapacity;
    private ArrayList<Player> gameLobbyList = new ArrayList<>();
    private ArrayList<Player> lobbyList = new ArrayList<>();

    public GameLobbyManager() {
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

    }

    /**
     * Callback method to the communicator. This is called when a player exits the general lobby waiting area.
     *
     * @param id The id of the player who joined the waiting area.
     */
    @Override
    public void onClientExitLobby(int id) {
    }


    /**
     * Callback method to the communicator. This is called when a player joins the game lobby waiting area.
     *
     * @param id The id of the player who joined the waiting area.
     */
    @Override
    public void onClientJoinGameLobby(int id) {

    }

    /**
     * Callback method to the communicator. This is called when a player exits the game lobby waiting area.
     *
     * @param id The id of the player who joined the waiting area.
     */
    @Override
    public void onClientExitGameLobby(int id) {

    }
}
