package Client.ClientNetwork;

/**
 * Created by mlee43 on 2016-02-27.
 */
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
        void onRequestInfo();
        void onSuccessfulJoin();
    }

    public interface ChatMessageListener {
        void onClientReceiveMessage(int id, String text);
        void onSuccessfulChatroomEntry();
        void onCSuccessfulChatroomExit();
    }

    public interface LobbyMessageListener {
        void onSuccessfulLobbyEntry();
        void onSuccessfulLobbyExit();
        void onSuccessfulLobbyCreate();
    }

    /**
     * This method handles all messages by delegating the task to the appropriate class.
     *
     * @param message The message received over the network.
     */
    public void handleMessage(byte[] message) {
        switch (message[1]) {

            case 0x0:
                break;
            case 0x1:
                break;
            case 0x2:
                break;
        }
    }

    /**
     * This method is used to register an object as a listener to one of the following interfaces.
     *
     * @param kind The kind of events the listener wants to hear.
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
