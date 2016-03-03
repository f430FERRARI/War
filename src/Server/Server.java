package Server;

import Server.ServerNetwork.ServerMessageHandler;
import Server.ServerNetwork.ServerNetworkManager;

import java.util.ArrayList;

public class Server implements ServerNetworkManager.AdminMessageListener {

    private ArrayList<Player> playerList;
    private ServerNetworkManager networkManager;

    public Server() {
        playerList = new ArrayList<>();
    }

    public void startServer() {
        // Start the server network and register this class as a listener
        networkManager = ServerNetworkManager.getInstance();
        networkManager.register(ServerMessageHandler.LISTENER_ADMIN, this);
        networkManager.startServerNetwork();
    }

	public void onStartNewGame() {
		
	}

    @Override
    public void onReceivePlayerInfo(int id, String name) {
    }

    public static void main(String[] args) { // TODO: make a main class
        Server server = new Server();
        server.startServer();
    }
}
