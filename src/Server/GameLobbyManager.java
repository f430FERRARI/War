package Server;

import Server.ServerNetwork.ServerMessageHandler;
import Server.ServerNetwork.ServerNetworkManager;

/**
 * Created by mlee43 on 2016-03-02.
 */
public class GameLobbyManager implements ServerNetworkManager.LobbyMessageListener {

    private ServerNetworkManager networkManager;
    private int gameLobbyCapacity;
    private int lobbyCapacity;

    public GameLobbyManager() {
        this.networkManager = ServerNetworkManager.getInstance();
        networkManager.register(ServerMessageHandler.LISTENER_LOBBY, this);
    } 

    @Override
    public void onClientJoinLobby(int id) {
    }

    @Override
    public void onClientExitLobby(int id) {
    }

    @Override
    public void onClientCreateLobby(int id, int playerCount) {
    }
}
