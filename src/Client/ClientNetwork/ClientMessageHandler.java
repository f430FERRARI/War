package Client.ClientNetwork;

import Client.CommunicationCodes;
import Client.Utilities;

import java.util.Arrays;

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
        void onRequestInfo(int id);

        void onNewPlayerJoined(int id, String name);

        void onConnectionComplete();

        void onReceiveIdsAndNames(String idsAndNames);
    }

    public interface ChatMessageListener {
        void onClientRcvIndvMsg(int id, String text);

        void onClientRcvGrpMsg(int id, String text);

        void onSuccessfulChatroomEntry();

        void onCSuccessfulChatroomExit();
    }

    public interface LobbyMessageListener {
        void onLobbyListChanged(String lists);

        void onLobbyFull();

        void onGameLobbyFull();

        void onObserverLobbyFull();
    }

    /**
     * This method handles all messages by delegating the task to the appropriate class.
     *
     * @param message The message received over the network.
     */
    public void handleMessage(byte[] message) {

        byte opCode = message[0];

        switch (opCode) {

            case CommunicationCodes.ADMIN_REQUEST_INFO:
                int myId = Server.Utilities.byteArrayToInt(Arrays.copyOfRange(message, 1, 5));
                System.out.println("Got the info request! My ID is: " + myId);
                adminMessageListener.onRequestInfo(myId);
                break;

            case CommunicationCodes.ADMIN_GET_PLAYERS:
                System.out.println("Received player names and ids!");
                String idsAndNames = Utilities.byteArrayToString(Arrays.copyOfRange(message, 1, message.length));
                adminMessageListener.onReceiveIdsAndNames(idsAndNames);
                break;

            case CommunicationCodes.ADMIN_UPDATE_PLAYERS:
                System.out.println("Received player updates!");
                int id = Utilities.byteArrayToInt(Arrays.copyOfRange(message, 1, 5));
                String name = Utilities.byteArrayToString(Arrays.copyOfRange(message, 5, message.length));
                adminMessageListener.onNewPlayerJoined(id, name);
                break;

            case CommunicationCodes.LOBBY_LISTS_CHANGED:
                System.out.println("Got message! Lobby list changed!");
                String lists = Utilities.byteArrayToString(Arrays.copyOfRange(message, 1, message.length));
                lobbyMessageListener.onLobbyListChanged(lists);
                break;

            case CommunicationCodes.LOBBY_LOBBY_FULL:
                System.out.println("Got message! Lobby full.");
                lobbyMessageListener.onLobbyFull();
                break;

            case CommunicationCodes.LOBBY_GAMELOBBY_FULL:
                System.out.println("Got message! Game lobby full.");
                lobbyMessageListener.onGameLobbyFull();
                break;

            case CommunicationCodes.LOBBY_OBSERVER_FULL:
                System.out.println("Got message! Observer list full!");
                lobbyMessageListener.onObserverLobbyFull();
                break;

            case CommunicationCodes.CHAT_REDIRECT_IND_MSG:
                System.out.println("Got individual chat message from other guy.");
                int senderID1 = Utilities.byteArrayToInt(Arrays.copyOfRange(message, 1, 5));
                String text1 = Utilities.byteArrayToString(Arrays.copyOfRange(message, 5, message.length));
                chatMessageListener.onClientRcvIndvMsg(senderID1, text1);
                break;

            case CommunicationCodes.CHAT_REDIRECT_GRP_MSG:
                System.out.println("Got group message from other guy!");
                int senderID2 = Utilities.byteArrayToInt(Arrays.copyOfRange(message, 1, 5));
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
