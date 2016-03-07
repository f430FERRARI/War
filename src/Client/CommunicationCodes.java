package Client;

public class CommunicationCodes {

    // Administration Operation Codes (0x00 - 0x0F)
    public static final byte ADMIN_REQUEST_INFO = 0x0;
    public static final byte ADMIN_RESPONSE_INFO = 0x1;
    public static final byte ADMIN_UPDATE_PLAYERS = 0x2;
    public static final byte ADMIN_GET_PLAYERS = 0x3;

    // Gameplay Operation Codes (0x10 - 0x1F)
    public static final byte GAME_DRAW = 0x30;
    public static final byte GAME_CARD_COUNT = 0x31;
    public static final byte GAME_START = 0x32;
    public static final byte GAME_REQUEST_START = 0x33;
    public static final byte GAME_REQUEST_QUIT = 0x34;
    public static final byte GAME_REQUEST_PAUSE = 0x35;
    public static final byte GAME_REQUEST_UNPAUSE = 0x36;

    // Chatroom Operation Codes (0x20 - 0x2F)
    public static final byte CHAT_SEND_MSG = 0x20;
    public static final byte CHAT_REDIRECT_MSG = 0x21;

    // Lobby Operation Codes (0x30 - 0x3F)

}
