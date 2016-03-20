package Server.ServerNetwork;


import Server.ServerLogic.Utilities;

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

    public interface AdminMessageListener {
        void onCreateAccount(int id, String accountInfo);

        void onReceiveLogin(int id, String loginInfo);

        void onDisconnect(int id);
    }

    public interface ChatMessageListener {
        void onServerRcvIndMsg(byte[] senderID, int destID, byte[] text);

        void onServerRcvGrpMsg(int senderId, byte[] senderIDBytes, byte[] text);
    }

    public interface LobbyMessageListener {
        void onRequestLobbyList(int id);

        void onClientJoinLobby(int id);

        void onClientExitLobby(int id);

        void onClientJoinGameLobby(int id);

        void onClientExitGameLobby(int id);

        void onClientJoinObserver(int id);

        void onClientExitObserver(int id);
    }

    public interface GameMessageListener {
        void onReceiveDraw();

        void onReceiveQuit(int id);

        void onReceivePause(int id);
    }

    /**
     * This method handles all messages by delegating the task to the appropriate class.
     *
     * @param message The message received over the network.
     */
    public void handleMessage(byte[] message) {

        byte opCode = message[0];
        int senderId = getSenderId(message);

        switch (opCode) {

            case CommunicationCodes.ADMIN_CREATE_ACCOUNT:
                System.out.println("Received create account request.");
                String accountInfo = Utilities.byteArrayToString(Arrays.copyOfRange(message, 5, message.length));
                adminMessageListener.onCreateAccount(senderId, accountInfo);
                break;

            case CommunicationCodes.ADMIN_LOGIN_ATTEMPT:
                System.out.println("Got player login info!");
                String loginInfo = Utilities.byteArrayToString(Arrays.copyOfRange(message, 5, message.length));
                adminMessageListener.onReceiveLogin(senderId, loginInfo);
                break;

            case CommunicationCodes.LOBBY_REQUEST_LISTS:
                System.out.println("Got request for lobby lists.");
                lobbyMessageListener.onRequestLobbyList(senderId);
                break;

            case CommunicationCodes.LOBBY_JOIN_GAMELOBBY:
                System.out.println("Got message! Client wants to join game lobby.");
                lobbyMessageListener.onClientJoinGameLobby(senderId);
                break;

            case CommunicationCodes.LOBBY_EXIT_GAMELOBBY:
                System.out.println("Got message! Client wants to exit game lobby!");
                lobbyMessageListener.onClientExitGameLobby(senderId);
                break;

            case CommunicationCodes.LOBBY_EXIT_LOBBY:
                System.out.println("Got message! Client wants to exit lobby!");
                lobbyMessageListener.onClientExitLobby(senderId);
                break;

            case CommunicationCodes.LOBBY_JOIN_OBSERVER:
                System.out.println("Got message! Client wants to join observers.");
                lobbyMessageListener.onClientJoinObserver(senderId);
                break;

            case CommunicationCodes.LOBBY_EXIT_OBSERVER:
                System.out.println("Got message! Client wants to leave observers.");
                lobbyMessageListener.onClientExitObserver(senderId);
                break;

            case CommunicationCodes.CHAT_SEND_IND_MSG:
                System.out.println("Got client chat message!");
                byte[] senderIDBytes1 = Arrays.copyOfRange(message, 1, 5);
                int destID1 = Utilities.byteArrayToInt(Arrays.copyOfRange(message, 5, 9));
                byte[] text1 = Arrays.copyOfRange(message, 9, message.length);
                chatMessageListener.onServerRcvIndMsg(senderIDBytes1, destID1, text1);
                break;

            case CommunicationCodes.CHAT_SEND_GRP_MSG:
                System.out.println("Got group chat message!");
                byte[] senderIDBytes2 = Arrays.copyOfRange(message, 1, 5);
                int senderID2 = Utilities.byteArrayToInt(senderIDBytes2);
                byte[] text2 = Arrays.copyOfRange(message, 5, message.length);
                chatMessageListener.onServerRcvGrpMsg(senderID2, senderIDBytes2, text2);
                break;
        }
    }

    /**
     * This is a helper method that gets integer representation of the sender's ID from the message.
     *
     * @param message The message with the sender's ID
     * @return An integer representing the sender's ID
     */
    private int getSenderId(byte[] message) {
        return Utilities.byteArrayToInt(Arrays.copyOfRange(message, 1, 5));
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
