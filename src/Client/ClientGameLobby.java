package Client;

import Client.ClientNetwork.ClientMessageHandler;
import Client.ClientNetwork.ClientNetworkManager;

/**
 * Created by mlee43 on 2016-03-02.
 */
public class ClientGameLobby implements ClientNetworkManager.LobbyMessageListener {

    private ClientNetworkManager networkManager;

    public ClientGameLobby() {
        this.networkManager = ClientNetworkManager.getInstance();
        networkManager.register(ClientMessageHandler.LISTENER_LOBBY, this);
    }

    @Override
    public void onSuccessfulLobbyEntry() {

    }

    @Override
    public void onSuccessfulLobbyExit() {

    }

    @Override
    public void onSuccessfulLobbyCreate() {

    }
}
