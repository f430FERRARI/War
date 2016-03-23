package ClientLogic;

import ClientNetwork.ClientMessageHandler;
import ClientNetwork.ClientNetworkManager;
import ClientNetwork.CommunicationCodes;
import GUI.GameLobbyForm;

import java.util.ArrayList;

public class ClientChatRoom implements ClientNetworkManager.ChatMessageListener, ChatListener, ClientGameLobby.ChatListListener {

    public static final int CHATROOM_GRP_MSG = 0;
    public static final int CHATROOM_IND_MSG = 1;

    private Client client;
    private GameLobbyForm chatRoom;  // TODO: Generalize to chat gui
    private ClientNetworkManager networkManager;

    private ArrayList<Integer> inChat = new ArrayList<>();

    public ClientChatRoom(Client client, GameLobbyForm lobbyScreen) {
        this.client = client;
        this.chatRoom = lobbyScreen;
        lobbyScreen.register(GameLobbyForm.LISTENER_CHAT, this);
        this.networkManager = ClientNetworkManager.getInstance();
        networkManager.register(ClientMessageHandler.LISTENER_CHAT, this);
    }

    @Override
    public void onLobbyListUpdates(ArrayList<Integer> inChat) {
        this.inChat = inChat;
    }

    @Override
    public void onClickSendMsg(int type, int destUser, String msg) {
        System.out.println("Will send message");

        byte[] textBytes = Utilities.stringToByteArray(msg);
        if (type == CHATROOM_IND_MSG && destUser != -1) {

            // Put message onto the message area
            chatRoom.updateChatArea(CHATROOM_IND_MSG, client.getMe().getName(), msg);

            // Send the message to the individual
            int destId = inChat.get(destUser);
            byte[] destIdBytes = Utilities.intToByteArray(destId);
            byte[] payload = Utilities.appendByteArrays(destIdBytes, textBytes);
            byte[] message = Utilities.prepareMessage(CommunicationCodes.CHAT_SEND_IND_MSG, client.getMe().getId(), payload);
            networkManager.send(message);

        } else if (type == CHATROOM_GRP_MSG) {

            // Put message onto the message area
            chatRoom.updateChatArea(CHATROOM_GRP_MSG, client.getMe().getName(), msg);

            // Send the message to the group
            byte[] message = Utilities.prepareMessage(CommunicationCodes.CHAT_SEND_GRP_MSG, client.getMe().getId(), textBytes);
            networkManager.send(message);
        }
    }

    @Override
    public void onClientRcvIndvMsg(int senderId, String text) {
        chatRoom.updateChatArea(CHATROOM_IND_MSG, client.getPlayerList().get(senderId).getName(), text);
    }

    @Override
    public void onClientRcvGrpMsg(int senderId, String text) {
        chatRoom.updateChatArea(CHATROOM_GRP_MSG, client.getPlayerList().get(senderId).getName(), text);

    }
}
