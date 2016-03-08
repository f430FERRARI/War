package Server;

import Server.ServerNetwork.ServerMessageHandler;
import Server.ServerNetwork.ServerNetworkManager;

public class ChatRoomManager implements ServerNetworkManager.ChatMessageListener {

    private ServerNetworkManager networkManager;

    public ChatRoomManager() {
        this.networkManager = ServerNetworkManager.getInstance();
        networkManager.register(ServerMessageHandler.LISTENER_CHAT, this);
    }

    /**
     * Callback to a chat message from the client. It redirects the message to the appropriate recipient.
     *
     * @param senderID The client's ID.
     * @param destID The ID of the intended recipient
     * @param text The message from the client.
     */
    @Override
    public void onServerReceiveChatMessage(byte[] senderID, int destID, byte[] text) {
        byte[] payload = Utilities.appendByteArrays(senderID, text);
        byte[] message = Utilities.prepareMessage(CommunicationCodes.CHAT_REDIRECT_MSG, payload);
        networkManager.send(destID, message);
    }
}
