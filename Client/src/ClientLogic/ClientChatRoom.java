package ClientLogic;

import ClientNetwork.ClientMessageHandler;
import ClientNetwork.ClientNetworkManager;
import ClientNetwork.CommunicationCodes;
import GUI.Chat;

import java.util.ArrayList;

public class ClientChatRoom implements ClientNetworkManager.ChatMessageListener, Chat.ChatGUIListener, ClientGameLobby.ChatListListener {

    public static final int CHATROOM_GRP_MSG = 0;
    public static final int CHATROOM_IND_MSG = 1;

    private Client client;
    private Chat chat;
    private ClientNetworkManager networkManager;

    private ArrayList<Integer> inChat = new ArrayList<>();

    public ClientChatRoom(Client client) {
        this.client = client;
        this.networkManager = ClientNetworkManager.getInstance();
        networkManager.register(ClientMessageHandler.LISTENER_CHAT, this);
    }

    /**
     * Callback to ClientGameLobby. This is called when the lobby lists are updated. Updates the inChat list.
     *
     * @param inChat The IDs of the players who are in the chat room.
     */
    @Override
    public void onLobbyListUpdates(ArrayList<Integer> inChat) {
        this.inChat = inChat;

        // Get player names
        String[] inChatList = new String[inChat.size()];
        for (int i = 0; i < inChat.size(); i++) {
            inChatList[i] = client.getPlayerList().get(inChat.get(i)).getName();
        }

        chat.updateInChatList(inChatList);
    }

    /**
     * Callback to Chat. This is called when the user presses send in the chat area. Sends the message to the server
     * depending on the type of message the user wanted to send.
     *
     * @param type Public or private chat
     * @param destUser The player the user wants to send the message to. Chosen from inChatList. -1 if no one chosen.
     * @param msg The text the user is trying to send.
     */
    @Override
    public void onClickSendMsg(int type, int destUser, String msg) {
        byte[] textBytes = Utilities.stringToByteArray(msg);
        if (type == CHATROOM_IND_MSG && destUser != -1) {

            // Put message onto the message area
            chat.updateChatArea(CHATROOM_IND_MSG, client.getMe().getName(), msg);

            // Send the message to the individual
            int destId = inChat.get(destUser);
            byte[] destIdBytes = Utilities.intToByteArray(destId);
            byte[] payload = Utilities.appendByteArrays(destIdBytes, textBytes);
            byte[] message = Utilities.prepareMessage(CommunicationCodes.CHAT_SEND_IND_MSG, client.getMe().getId(), payload);
            networkManager.send(message);

        } else if (type == CHATROOM_GRP_MSG) {

            // Put message onto the message area
            chat.updateChatArea(CHATROOM_GRP_MSG, client.getMe().getName(), msg);

            // Send the message to the group
            byte[] message = Utilities.prepareMessage(CommunicationCodes.CHAT_SEND_GRP_MSG, client.getMe().getId(), textBytes);
            networkManager.send(message);
        }
    }

    /**
     * Callback to the communicator. This is called when the client receives a private chat message. Displays the
     * message with the senders name in the private chat area.
     *
     * @param senderId The id of the person who sent the message.
     * @param text The message that was sent.
     */
    @Override
    public void onClientRcvIndvMsg(int senderId, String text) {
        chat.updateChatArea(CHATROOM_IND_MSG, client.getPlayerList().get(senderId).getName(), text);
    }

    /**
     * Callback to the communicator. This is called when the client receives a public chat message. Displays the
     * message with the senders name in the public chat area.
     *
     * @param senderId The id of the person who sent the message.
     * @param text The message that was sent.
     */
    @Override
    public void onClientRcvGrpMsg(int senderId, String text) {
        chat.updateChatArea(CHATROOM_GRP_MSG, client.getPlayerList().get(senderId).getName(), text);
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
