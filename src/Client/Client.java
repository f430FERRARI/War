package Client;

import Client.ClientNetwork.ClientMessageHandler;
import Client.ClientNetwork.ClientNetworkManager;

import java.util.ArrayList;

/**
 * Created by mlee43 on 2016-02-27.
 */
public class Client implements ClientNetworkManager.AdminMessageListener {

    private ArrayList<Player> playerList;
    private ClientNetworkManager networkManager;

    public Client() {
        playerList = new ArrayList<>();
    }

    public void startClient() {
        // Start client and register this class as a listener
        networkManager = ClientNetworkManager.getInstance();
        networkManager.register(ClientMessageHandler.LISTENER_ADMIN, this);
        networkManager.startServerConnection();
    }

    @Override
    public void onRequestInfo() {

    }

    @Override
    public void onSuccessfulJoin() {

    }

    public static void main(String[] args) {
        Client client = new Client();
        client.startClient();
    }
}
