package Server.ServerNetwork;


/**
 * Created by mlee43 on 2016-02-28.
 */
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
    }

    public interface ChatMessageListener {

    }

    public interface LobbyMessageListener {

    }

    public void handleMessage(byte[] message) {

        switch (message[1]) {

            case 0x0:       // Client pressed draw
                break;
            case 0x1:       // Client pressed quit
                break;
            case 0x2:       // Client pressed pause
                break;
            case 0x3:
                break;
            case 0x4:
                break;
            case 0x10:       // One-on-one chat message (Incoming)
                break;
            case 0x11:       // One-to-one chat message (Outgoing)
                break;
            case 0x12:       // One-to-many chat message (Incoming)
                break;
            case 0x13:       // One-to-many chat message (Outgoing)
        }

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
