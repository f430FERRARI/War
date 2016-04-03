package ClientLogic;

import ClientNetwork.ClientMessageHandler;
import ClientNetwork.ClientNetworkManager;
import ClientNetwork.CommunicationCodes;
import GUI.GameLobbyForm;

import java.util.ArrayList;

public class ClientGameLobby implements GameLobbyForm.LobbyGUIListener, ClientNetworkManager.LobbyMessageListener {

    private static final String PARSE_LOBBY_GUY = "L";
    private static final String PARSE_GAME_GUY = "G";
    private static final String PARSE_OBSERVER_DUDE = "O";

    public static final int LOBBY_LOBBY_CAPACITY = 10;
    public static final int LOBBY_GAMELOBBY_CAPACITY = 4;
    public static final int LOBBY_OBSERVER_CAPACITY = 4;

    private ArrayList<Integer> lobbyList = new ArrayList<>();
    private ArrayList<Integer> gameLobbyList = new ArrayList<>();
    private ArrayList<Integer> observerList = new ArrayList<>();

    private boolean gameInMotion = false;

    private Client client;
    private ClientGameLobbyListener lobbyListener;
    private ChatListListener chatListListener;
    private ClientNetworkManager networkManager;

    private GameLobbyForm lobbyScreen;

    public interface ClientGameLobbyListener {
        void onStartGame(ArrayList<Integer> gamers);
    }

    public interface ChatListListener {
        void onLobbyListUpdates(ArrayList<Integer> inChat);
    }

    public ClientGameLobby(Client client, GameLobbyForm gameLobbyForm, ChatListListener chatRoom) {
        this.client = client;
        this.lobbyListener = client;
        this.chatListListener = chatRoom;
        this.lobbyScreen = gameLobbyForm;
        lobbyScreen.register(this);
        this.networkManager = ClientNetworkManager.getInstance();
        networkManager.register(ClientMessageHandler.LISTENER_LOBBY, this);
    }

    /**
     * Callback to the communicator. This is called when the server sends the lobby lists. Updates
     * the players lobby lists.
     *
     * @param lists A string containing the ids of the players in each list.
     */
    @Override
    public void onReceiveLobbyLists(String lists) {
        if (lists != null && !lists.isEmpty()) {

            // Clear the old lists
            lobbyList.clear();
            gameLobbyList.clear();
            observerList.clear();

            String[] lobbyPeople = lists.split(Utilities.PARSE_SPLITTER_ENTRY);

            // Determine the proper lobby each name belongs and get the player name
            ArrayList<String> observerDudesNames = new ArrayList<>();
            ArrayList<String> gameGuysNames = new ArrayList<>();
            ArrayList<String> lobbyGuysNames = new ArrayList<>();

            for (int i = 0; i < lobbyPeople.length; i++) {
                String list = lobbyPeople[i].substring(0, 1);
                int id = Integer.parseInt(lobbyPeople[i].substring(1, lobbyPeople[i].length()));
                String name = client.getPlayerList().get(id).getName();
                switch (list) {
                    case PARSE_LOBBY_GUY:
                        lobbyGuysNames.add(name);
                        lobbyList.add(id);
                        break;
                    case PARSE_GAME_GUY:
                        gameGuysNames.add(name);
                        gameLobbyList.add(id);
                        break;
                    case PARSE_OBSERVER_DUDE:
                        observerDudesNames.add(name);
                        observerList.add(id);
                        break;
                }
            }

            // Update everyone who needs to know about the lists
            chatListListener.onLobbyListUpdates(lobbyList);
            lobbyScreen.updateLobbyLists(lobbyGuysNames, gameGuysNames, observerDudesNames);
        }
    }

    /**
     * Callback to GameLobbyForm. This is called when the user presses join. Adds the player to the game lobby list by
     * sending a request to the server.
     */
    @Override
    public void onClickJoin() {
        if (gameLobbyList.size() < LOBBY_GAMELOBBY_CAPACITY) {
            byte[] message = Utilities.prepareOperationMessage(CommunicationCodes.LOBBY_JOIN_GAMELOBBY,
                    client.getMe().getId());
            networkManager.send(message);
        } else {
            // TODO: Display lobby full message
        }
    }

    /**
     * Callback to GameLobbyForm. This is called when the user presses observe. Adds the player to the observer list by
     * sending a request to the server.
     */
    @Override
    public void onClickObserve() {
        if (observerList.size() < LOBBY_OBSERVER_CAPACITY) {
            byte[] message = Utilities.prepareOperationMessage(CommunicationCodes.LOBBY_JOIN_OBSERVER,
                    client.getMe().getId());
            networkManager.send(message);
        } else {
            // TODO: Display lobby full message
        }
    }

    /**
     * Callback to the GameLobbyForm. This is called when the user presses the start button. Starts a game with the
     * players in the game lobby list by opening the game screen for those in the game lobby list and observer list.
     */
    @Override
    public void onClickStart() {
        if (gameLobbyList.size() > 1 && gameInMotion == false) {
            byte[] message = Utilities.prepareOperationMessage(CommunicationCodes.LOBBY_REQUEST_GAMESTART, client.getMe().getId());
            networkManager.send(message);
            lobbyListener.onStartGame(gameLobbyList); // TODO: Add observers
            chatListListener.onLobbyListUpdates(lobbyList);
        }
    }

    /**
     * Callback to the communicator. This is called when the user receives a message that somebody has started the game.
     */
    @Override
    public void onReceiveGameStart() {
        lobbyListener.onStartGame(gameLobbyList); // TODO: Add observers
        chatListListener.onLobbyListUpdates(lobbyList);

    }

    public void gameInProgress(boolean i) {
        gameInMotion = i;
        System.out.println("GAME IN MOTION? " + i);
    }

    public void onClickLeave() {
        byte[] message = Utilities.prepareOperationMessage(CommunicationCodes.LOBBY_EXIT_GAMELOBBY, client.getMe().getId());
        networkManager.send(message);
    }

}
