package ClientNetwork;

import ClientLogic.Utilities;

import java.util.Arrays;

import static ClientLogic.Utilities.byteArrayToInt;

public class ClientMessageHandler {

    public static final int LISTENER_GAME = 1;
    public static final int LISTENER_ADMIN = 2;
    public static final int LISTENER_CHAT = 3;
    public static final int LISTENER_LOBBY = 4;

    private GameMessageListener gameMessageListener;
    private AdminMessageListener adminMessageListener;
    private ChatMessageListener chatMessageListener;
    private LobbyMessageListener lobbyMessageListener;

    public interface GameMessageListener {
        void onBeginGame();

        void onBeginRound();

        void onRoundEnd();

        void onReceiveScores();

        void onGameFinished();
    }

    public interface AdminMessageListener {
        void onReceiveID(int id);

        void onReceiveLoginResult(String result);

        void onReceiveCreateResult(String result);

        void onNewPlayerJoined(int id, String name);

        void onReceiveIdsAndNames(String idsAndNames);

        void onPlayerRemoved(int id);
    }

    public interface ChatMessageListener {
        void onClientRcvIndvMsg(int id, String text);

        void onClientRcvGrpMsg(int id, String text);
    }

    public interface LobbyMessageListener {
        void onReceiveLobbyLists(String lists);

        void onReceiveGameStart();
    }

    /**
     * This method handles all messages by delegating the task to the appropriate class.
     *
     * @param message The message received over the network.
     */
    public void handleMessage(byte[] message) {

        byte opCode = message[0];

        switch (opCode) {

            case CommunicationCodes.ADMIN_ASSIGN_ID:
                int myId = Utilities.byteArrayToInt(Arrays.copyOfRange(message, 1, 5));
                System.out.println("Got my ID! My ID is: " + myId);
                adminMessageListener.onReceiveID(myId);
                break;

            case CommunicationCodes.ADMIN_CREATE_RESULT:
                System.out.println("Received result from account creation.");
                String result = Utilities.byteArrayToString(Arrays.copyOfRange(message, 1, message.length));
                adminMessageListener.onReceiveCreateResult(result);
                break;

            case CommunicationCodes.ADMIN_LOGIN_RESULT:
                System.out.println("Got login result!");
                String accountResult = Utilities.byteArrayToString(Arrays.copyOfRange(message, 1, message.length));
                adminMessageListener.onReceiveLoginResult(accountResult);
                break;

            case CommunicationCodes.ADMIN_GROUP_INFO:
                System.out.println("Received player names and ids!");
                String idsAndNames = Utilities.byteArrayToString(Arrays.copyOfRange(message, 1, message.length));
                adminMessageListener.onReceiveIdsAndNames(idsAndNames);
                break;

            case CommunicationCodes.ADMIN_UPDATE_PLAYERS:
                System.out.println("Received player updates!");
                int id = byteArrayToInt(Arrays.copyOfRange(message, 1, 5));
                String name = Utilities.byteArrayToString(Arrays.copyOfRange(message, 5, message.length));
                adminMessageListener.onNewPlayerJoined(id, name);
                break;

            case CommunicationCodes.ADMIN_REMOVE_PLAYER:
                System.out.println("Received message. Player needs to be removed");
                int toRemove = byteArrayToInt(Arrays.copyOfRange(message, 1, 5));
                adminMessageListener.onPlayerRemoved(toRemove);
                break;

            case CommunicationCodes.LOBBY_LISTS_SEND:
                System.out.println("Got message! Lobby list changed!");
                String lists = Utilities.byteArrayToString(Arrays.copyOfRange(message, 1, message.length));
                lobbyMessageListener.onReceiveLobbyLists(lists);
                break;

            case CommunicationCodes.LOBBY_GAME_START:
                System.out.println("Received message! Game start.");
                lobbyMessageListener.onReceiveGameStart();
                break;

            case CommunicationCodes.CHAT_REDIRECT_IND_MSG:
                System.out.println("Got individual chat message from other guy.");
                int senderID1 = byteArrayToInt(Arrays.copyOfRange(message, 1, 5));
                String text1 = Utilities.byteArrayToString(Arrays.copyOfRange(message, 5, message.length));
                chatMessageListener.onClientRcvIndvMsg(senderID1, text1);
                break;

            case CommunicationCodes.CHAT_REDIRECT_GRP_MSG:
                System.out.println("Got group message from other guy!");
                int senderID2 = byteArrayToInt(Arrays.copyOfRange(message, 1, 5));
                String text2 = Utilities.byteArrayToString(Arrays.copyOfRange(message, 5, message.length));
                chatMessageListener.onClientRcvGrpMsg(senderID2, text2);
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
