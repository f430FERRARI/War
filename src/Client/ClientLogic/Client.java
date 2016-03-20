package Client.ClientLogic;

import Client.ClientNetwork.ClientMessageHandler;
import Client.ClientNetwork.ClientNetworkManager;
import Client.ClientNetwork.CommunicationCodes;
import Client.GUI.AccountDialog;
import Client.GUI.GameLobbyForm;
import Client.GUI.LoginDialog;

import javax.swing.*;
import java.util.HashMap;

public class Client implements LoginDialog.LoginDialogListener, AccountDialog.AccountDialogListener, ClientNetworkManager.AdminMessageListener {

    private Player me;
    private HashMap<Integer, Player> playerList = new HashMap<>();

    private ClientNetworkManager networkManager;
    private ClientChatRoom chatRoom;
    private ClientGameLobby lobby;
    private ClientGameLogic game;

    private JFrame theFrame;
    private LoginDialog lDialog;
    private AccountDialog accountDialog;
    private GameLobbyForm gameLobbyForm;

    public Client() {
        // Create an empty player object
        this.me = new Player();

        // Start client network and register this class as a listener
        networkManager = ClientNetworkManager.getInstance();
        networkManager.register(ClientMessageHandler.LISTENER_ADMIN, this);
        networkManager.startServerConnection();

        // Initiate GUI classes
        theFrame = new JFrame("WELCOME TO WAR");
        lDialog = new LoginDialog(this, theFrame);
        gameLobbyForm = new GameLobbyForm();

        // Create the remaining system components
        lobby = new ClientGameLobby(this, gameLobbyForm);

        // Make login dialog visible
        theFrame.setContentPane(lDialog.getContentPane());
        theFrame.setLocationRelativeTo(null); // centre
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.pack();
        theFrame.setVisible(true);
    }

    public void onQuitSystem() {

    }

    @Override
    public void onReceiveID(int id) {
        me.setId(id);
    }

    @Override
    public void onClickLogin(String usernameField, String passwordField) {
        // Get the username and password the user entered
        String username = usernameField;
        String password = passwordField;

        // Set my name to the username entered
        me.setName(username);

        // Send login info to be verified
        String loginInfo = username + Utilities.PARSE_SPLITTER_ENTRY + password;
        byte[] message = Utilities.prepareMessage(CommunicationCodes.ADMIN_LOGIN_ATTEMPT, me.getId(),
                Utilities.stringToByteArray(loginInfo));
        networkManager.send(message);
    }

    @Override
    public void onReceiveLoginResult(String result) {

        if (result.equals("Success")) {
            System.out.println("Login successful.");
            enterGameLobbyScreen();
            lDialog.dispose();
        } else {
            // TODO: Display error message
            System.out.println(result);
        }
    }

    private void enterGameLobbyScreen() {
        // Change screen to game lobby
        theFrame.setContentPane(gameLobbyForm.getContentPane());
        theFrame.setLocationRelativeTo(null);
        theFrame.pack();
        theFrame.setVisible(true);
    }

    @Override
    public void onClickCreateAccount() {
        accountDialog = new AccountDialog(this, lDialog, theFrame);
        theFrame.setContentPane(accountDialog.getContentPane());
        theFrame.setLocationRelativeTo(null);
        theFrame.pack();
        theFrame.setVisible(true);
    }

    @Override
    public void onCreateAccountAttempt(String username, String password) {
        String accountInfo = username + Utilities.PARSE_SPLITTER_ENTRY + password;
        byte[] message = Utilities.prepareMessage(CommunicationCodes.ADMIN_CREATE_ACCOUNT, me.getId(),
                Utilities.stringToByteArray(accountInfo));
        networkManager.send(message);
    }

    @Override
    public void onReceiveCreateResult(String result) {
        if (result.equals("Success")) {
            System.out.println("Account created successfully.");
            enterGameLobbyScreen();  // TODO: Make another dialog
            accountDialog.dispose();
        } else {
            // TODO: Display error message
            System.out.println(result);
        }
    }

    @Override
    public void onNewPlayerJoined(int id, String name) {
        Player newPlayer = new Player();
        newPlayer.setId(id);
        newPlayer.setName(name);
        playerList.put(id, newPlayer);
        System.out.println("New player added: " + playerList.size() + "players.");
    }

    @Override
    public void onReceiveIdsAndNames(String idsAndNames) {
        if (idsAndNames != null && !idsAndNames.isEmpty()) {
            String[] players = idsAndNames.split(Utilities.PARSE_SPLITTER_ENTRY);

            for (int i = 0; i < players.length; i++) {
                String[] parts = players[i].split(Utilities.PARSE_SPLITTER_FIELD);
                int id = Integer.parseInt(parts[0]);
                Player player = new Player();
                player.setId(id);
                player.setName(parts[1]);
                playerList.put(id, player);
            }
        }
    }

    public Player getMe() {
        return me;
    }

    public HashMap<Integer, Player> getPlayerList() {
        return playerList;
    }

    public static void main(String[] args) {
        Client client = new Client();
    }
}
