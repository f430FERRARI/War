package Client;

import Client.ClientNetwork.ClientMessageHandler;
import Client.ClientNetwork.ClientNetworkManager;

import java.util.HashMap;
import java.util.Scanner;

public class Client implements ClientNetworkManager.AdminMessageListener {


    private Player me;
    private HashMap<String, Player> playerList;
    private ClientNetworkManager networkManager;

    public Client() {
        playerList = new HashMap<>();
    }

    public void startClient() {
        // Create an empty player object
        this.me = new Player();
        // TODO: Put myself in the playerlist and check if changes to me affect it

        // Start client and register this class as a listener
        networkManager = ClientNetworkManager.getInstance();
        networkManager.register(ClientMessageHandler.LISTENER_ADMIN, this);
        networkManager.startServerConnection();

        // Create the chatroom      // TODO: This will move
//        ClientChatRoom chatRoom = new ClientChatRoom(this);
//        chatRoom.startChatRoom();
    }

    @Override
    public void onRequestInfo(int id) {
        // Assign myself the ID given by the server
        me.setId(id);

        // TODO: This is where login starts
        // Create a scanner so we can read the command-line input
        Scanner scanner = new Scanner(System.in);
        System.out.println("What's yo name: ");
        String name = scanner.next();

        // Update player name
        me.setName(name);

        // Send name back to the server
        byte[] payload = Utilities.stringToByteArray(name);
        networkManager.send(Utilities.prepareMessage(CommunicationCodes.ADMIN_RESPONSE_INFO, me.getId(), payload));
    }

    @Override
    public void onConnectionComplete() {
    }

    @Override
    public void onNewPlayerJoined(int id, String name) {
        Player newPlayer = new Player();
        newPlayer.setId(id);
        newPlayer.setName(name);
        playerList.put(name, newPlayer);
    }

    @Override
    public void onReceiveIdsAndNames(String idsAndNames) {
        if (idsAndNames != null && !idsAndNames.isEmpty()) {
            String[] players = idsAndNames.split(Utilities.PARSE_SPLITTER_2);

            for (int i = 0; i < players.length; i++) {
                String[] parts = players[i].split(Utilities.PARSE_SPLITTER_1);
                Player player = new Player();
                player.setId(Integer.parseInt(parts[0]));
                player.setName(parts[1]);
                playerList.put(parts[1], player);
            }
        }
    }

    public Player getMe() {
        return me;
    }

    public HashMap<String, Player> getPlayerList() {
        return playerList;
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.startClient();
    }

    // TODO: Receive player list
}
