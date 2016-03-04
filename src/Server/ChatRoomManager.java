package Server;

import Server.ServerNetwork.ServerMessageHandler;
import Server.ServerNetwork.ServerNetworkManager;

public class ChatRoomManager implements ServerNetworkManager.ChatMessageListener {

    private ServerNetworkManager networkManager;

    public ChatRoomManager() {
        this.networkManager = ServerNetworkManager.getInstance();
        networkManager.register(ServerMessageHandler.LISTENER_CHAT, this);
    }

    @Override
    public void onServerReceiveChatMessage(int id, String text) {
    }
}
