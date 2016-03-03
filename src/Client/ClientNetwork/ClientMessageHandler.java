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

    }

    public interface AdminMessageListener {

    }

    public interface ChatMessageListener {
        void onReceiveMessage(int id, String text);
    }

    public interface LobbyMessageListener {

    }

    public void handleMessage(byte[] message) {

    }

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
