package Server.ServerNetwork;


import Server.CommunicationCodes;
import Server.Utilities;

import java.util.Arrays;

public class ServerMessageHandler {

    public static final int LISTENER_GAME = 1;
    public static final int LISTENER_ADMIN = 2;
    public static final int LISTENER_CHAT = 3;
    public static final int LISTENER_LOBBY = 4;

    protected GameMessageListener gameMessageListener;
    protected AdminMessageListener adminMessageListener;
    protected ChatMessageListener chatMessageListener;
    protected LobbyMessageListener lobbyMessageListener;

    public interface GameMessageListener {
        void onReceiveDraw();

        void onReceiveQuit(int id);

        void onReceivePause(int id);
    }

    public interface AdminMessageListener {
        void onReceivePlayerInfo(int id, String name); // TODO: Get password from this eventually

        void onDisconnect(int id);
    }

    public interface ChatMessageListener {
        void onServerReceiveChatMessage(byte[] senderID, int destID, byte[] text);
    }

    public interface LobbyMessageListener {
        void onClientJoinLobby(int id);

        void onClientExitLobby(int id);

        void onClientJoinGameLobby(int id);

        void onClientExitGameLobby(int id);
    }

    /**
     * This method handles all messages by delegating the task to the appropriate class.
     *
     * @param message The message received over the network.
     */
    public void handleMessage(byte[] message) {

        byte opCode = message[0];
        int senderID;

        switch (opCode) {

            case CommunicationCodes.ADMIN_RESPONSE_INFO:       // Client sent info
                System.out.println("Got the info response.");
                senderID = Utilities.byteArrayToInt(Arrays.copyOfRange(message, 1, 5));
                String name = Utilities.byteArrayToString(Arrays.copyOfRange(message, 5, message.length));
                adminMessageListener.onReceivePlayerInfo(senderID, name);
                break;

            case CommunicationCodes.CHAT_SEND_MSG:       // Client sent chat message
                System.out.println("Got client chat message!");
                byte[] senderIDBytes = Arrays.copyOfRange(message, 1, 5);
                int destID = Utilities.byteArrayToInt(Arrays.copyOfRange(message, 5, 9));
                byte[] text = Arrays.copyOfRange(message, 9, message.length);
                chatMessageListener.onServerReceiveChatMessage(senderIDBytes, destID, text);
                break;
        }

    }

    /**
     * This method is used to register an object as a listener to one of the following interfaces.
     *
     * @param kind     The kind of events the listener wants to hear.
     * @param listener A reference to the object that is going to listen.
     */
    public void register(int kind, Object listener) {
        switch (kind) {
            case 1:
                gameMessageListener = (GameMessageListener) listener;
                break;
            case 2:
                adminMessageListener = (AdminMessageListener) listener;
                break;
            case 3:
                chatMessageListener = (ChatMessageListener) listener;
                break;
            case 4:
                lobbyMessageListener = (LobbyMessageListener) listener;
                break;
        }
    }
}
