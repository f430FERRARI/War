package ServerLogic;

import ServerNetwork.CommunicationCodes;
import ServerNetwork.ServerMessageHandler;
import ServerNetwork.ServerNetworkManager;

public class ChatRoomManager implements ServerNetworkManager.ChatMessageListener {

    public static final int CHATROOM_GRP_MSG = 1;
    public static final int CHATROOM_IND_MSG = 2;

    private ServerNetworkManager networkManager;

    public ChatRoomManager() {
        this.networkManager = ServerNetworkManager.getInstance();
        networkManager.register(ServerMessageHandler.LISTENER_CHAT, this);
    }

    /**
     * Callback to a chat message from the client to a single client. It redirects the message to the appropriate recipient.
     *
     * @param senderID The client's ID.
     * @param destID The ID of the intended recipient
     * @param text The message from the client.
     */
    @Override
    public void onServerRcvIndMsg(byte[] senderID, int destID, byte[] text) {
        byte[] payload = Utilities.appendByteArrays(senderID, text);
        byte[] message = Utilities.prepareMessage(CommunicationCodes.CHAT_REDIRECT_IND_MSG, payload);
        networkManager.send(destID, message);
    }


    /**
     * Callback to a chat message from the client to all players. It redirects the message to the appropriate recipient.
     *
     * @param senderIDBytes The clients ID as an int
     * @param senderID The client's ID as a byte array.
     * @param text The message from the client.
     */
    @Override
    public void onServerRcvGrpMsg(int senderID, byte[] senderIDBytes, byte[] text) {
        byte[] payload = Utilities.appendByteArrays(senderIDBytes, text);
        byte[] message = Utilities.prepareMessage(CommunicationCodes.CHAT_REDIRECT_GRP_MSG, payload);
        networkManager.sendToAllButOne(senderID, message);
    }
}
