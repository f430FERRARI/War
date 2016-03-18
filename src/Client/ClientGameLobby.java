package Client;

import Client.ClientNetwork.ClientMessageHandler;
import Client.ClientNetwork.ClientNetworkManager;

import java.util.ArrayList;

public class ClientGameLobby implements ClientNetworkManager.LobbyMessageListener {

    private ArrayList<Integer> lobbyList = new ArrayList<>();
    private ArrayList<Integer> gameLobbyList = new ArrayList<>();
    private ArrayList<Integer> observerList = new ArrayList<>();

    private ClientNetworkManager networkManager;

    public ClientGameLobby() {
        this.networkManager = ClientNetworkManager.getInstance();
        networkManager.register(ClientMessageHandler.LISTENER_LOBBY, this);
    }

    /**
     * Callback to the communicator. This is called when the lobbylists have changed. Updates
     * the players lobby lists.
     *
     * @param lists A string containing the ids of the players in each list.
     */
    @Override
    public void onLobbyListChanged(String lists) {
        if (lists != null && !lists.isEmpty()) {
            String[] lobbies = lists.split(Utilities.PARSE_SPLITTER_2);

            String[] lobbyGuys = lobbies[0].split(Utilities.PARSE_SPLITTER_1);
            for (int i = 0; i < lobbyGuys.length; i++) {
                lobbyList.add(Integer.parseInt(lobbyGuys[i]));
            }

            String[] gameGuys = lobbies[1].split(Utilities.PARSE_SPLITTER_1);
            for (int i = 0; i < gameGuys.length; i++) {
                gameLobbyList.add(Integer.parseInt(gameGuys[i]));
            }

            String[] observerDudes = lobbies[2].split(Utilities.PARSE_SPLITTER_1);
            for (int i = 0; i < observerDudes.length; i++) {
                observerList.add(Integer.parseInt(observerDudes[i]));
            }
        }
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

    // For debug
    public static void main(String[] args) {
        ClientGameLobby clientGameLobby = new ClientGameLobby();
        String test = "3~4~`3~4~54~`33~4~43~42~";
        clientGameLobby.onLobbyListChanged(test);
    }
}
