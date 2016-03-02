import ServerNetwork.ServerMessageHandler;
import ServerNetwork.ServerNetworkManager;

public class Server implements ServerMessageHandler.AdminMessageListener {


	public void main(String[] args) { // TODO: make a main class
		ServerNetworkManager networkManager = ServerNetworkManager.getInstance();
        networkManager.startServerNetwork();
        networkManager.getServerMessageHandler().register(ServerMessageHandler.LISTENER_ADMIN, this);
	}

	public void onStartNewGame() {
		
	}

    public void onReceivePlayerInfo(int id, String name, String password) {

    }
}
