package Server.ServerNetwork;

public class CommunicationCodes {

    // Administration Operation Codes (0x00 - 0x0F)
    public static final byte ADMIN_ASSIGN_ID = 0x0;
    public static final byte ADMIN_UPDATE_PLAYERS = 0x2;
    public static final byte ADMIN_GROUP_INFO = 0x3;
    public static final byte ADMIN_LOGIN_ATTEMPT = 0x4;
    public static final byte ADMIN_LOGIN_RESULT = 0x5;
    public static final byte ADMIN_CREATE_ACCOUNT = 0x6;
    public static final byte ADMIN_CREATE_RESULT = 0x7;

    // Lobby Operation Codes (0x10 - 0x1F)
    public static final byte LOBBY_JOIN_GAMELOBBY = 0x10;
    public static final byte LOBBY_EXIT_GAMELOBBY = 0x11;
    public static final byte LOBBY_JOIN_LOBBY = 0x12;
    public static final byte LOBBY_EXIT_LOBBY = 0x13;
    public static final byte LOBBY_LISTS_CHANGED = 0x14;
    public static final byte LOBBY_LOBBY_FULL = 0x15;
    public static final byte LOBBY_GAMELOBBY_FULL = 0x16;
    public static final byte LOBBY_OBSERVER_FULL = 0x17;
    public static final byte LOBBY_JOIN_OBSERVER = 0x18;
    public static final byte LOBBY_EXIT_OBSERVER = 0x19;

    // Chatroom Operation Codes (0x20 - 0x2F)
    public static final byte CHAT_SEND_IND_MSG = 0x20;
    public static final byte CHAT_REDIRECT_IND_MSG = 0x21;
    public static final byte CHAT_SEND_GRP_MSG = 0x22;
    public static final byte CHAT_REDIRECT_GRP_MSG = 0x23;

    // Gameplay Operation Codes (0x30 - 0x3F)
    public static final byte GAME_DRAW = 0x30;
    public static final byte GAME_CARD_COUNT = 0x31;
    public static final byte GAME_START = 0x32;
    public static final byte GAME_REQUEST_START = 0x33;
    public static final byte GAME_REQUEST_QUIT = 0x34;
    public static final byte GAME_REQUEST_PAUSE = 0x35;
    public static final byte GAME_REQUEST_UNPAUSE = 0x36;
}
