package Client.ClientLogic;

import Client.ClientNetwork.ClientMessageHandler;
import Client.ClientNetwork.ClientNetworkManager;
import Client.ClientNetwork.CommunicationCodes;
import Client.GUI.GameLobbyForm;

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

    private Client client;
    private ClientNetworkManager networkManager;

    private GameLobbyForm lobbyScreen;

    public ClientGameLobby(Client client, GameLobbyForm gameLobbyForm) {
        this.client = client;
        this.lobbyScreen = gameLobbyForm;
        this.networkManager = ClientNetworkManager.getInstance();
        networkManager.register(ClientMessageHandler.LISTENER_LOBBY, this);
        lobbyScreen.register(this);
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

            lobbyScreen.updateLobbyLists(lobbyGuysNames, gameGuysNames, observerDudesNames);
        }
    }

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

    @Override
    public void onClickStart() {

    }

    /**
     * Callback to the communicator. This is called when the player tried to join a lobby that
     * was full. Displays a lobby full message.
     */
    @Override
    public void onLobbyFull() {

    }

    /**
     * Callback to the communicator. This is called when the player tried to join a gamelobby that
     * was full. Displays a gamelobby full message.
     */
    @Override
    public void onGameLobbyFull() {

    }

    /**
     * Callback to the communicator. This is called when the player tried to join a gamelobby that
     * was full. Displays a observer lobby full message.
     */
    @Override
    public void onObserverLobbyFull() {

    }
}
