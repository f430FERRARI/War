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
    private HashMap<String, Player> playerList = new HashMap<>();
    private ClientNetworkManager networkManager;

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

        // Create GUI window for the system
        theFrame = new JFrame("WELCOME TO WAR");
        lDialog = new LoginDialog(this, theFrame);
        theFrame.setContentPane(lDialog.getContentPane());
        theFrame.setLocationRelativeTo(null); // centre
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.pack();
        theFrame.setVisible(true);
    }

    @Override
    public void onClickLogin(String usernameField, String passwordField) {
        // Get the username and password the user entered
        String username = usernameField;
        String password = passwordField;

        // Set my name to the username entered
        me.setName(username);

        // Send login info to be verified
        String loginInfo = username + Utilities.PARSE_SPLITTER_TYPE + password;
        byte[] message = Utilities.prepareMessage(CommunicationCodes.ADMIN_LOGIN_ATTEMPT, me.getId(),
                Utilities.stringToByteArray(loginInfo));
        networkManager.send(message);
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
        String accountInfo = username + Utilities.PARSE_SPLITTER_TYPE + password;
        byte[] message = Utilities.prepareMessage(CommunicationCodes.ADMIN_CREATE_ACCOUNT, me.getId(),
                Utilities.stringToByteArray(accountInfo));
        networkManager.send(message);
    }

    @Override
    public void onReceiveCreateResult(String result) {
        if (result.equals("Success")) {
            System.out.println("Account created successfully.");
            gameLobbyForm = new GameLobbyForm();
            theFrame.setContentPane(gameLobbyForm.getContentPane());
            theFrame.setLocationRelativeTo(null);
            theFrame.pack();
            theFrame.setVisible(true);
            accountDialog.dispose();
        } else {
            // TODO: Display error message
            System.out.println(result);
        }
    }

    @Override
    public void onReceiveID(int id) {
        // Assign myself the ID given by the server
        me.setId(id);
    }

    @Override
    public void onReceiveLoginResult(String result) {
        if (result.equals("Success")) {
            System.out.println("Login successful.");
            gameLobbyForm = new GameLobbyForm();
            theFrame.setContentPane(gameLobbyForm.getContentPane());
            theFrame.setLocationRelativeTo(null);
            theFrame.pack();
            theFrame.setVisible(true);
            lDialog.dispose();
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
        playerList.put(name, newPlayer);
        System.out.println("New player added: " + playerList.size() + "players.");
    }

    @Override
    public void onReceiveIdsAndNames(String idsAndNames) {
        if (idsAndNames != null && !idsAndNames.isEmpty()) {
            String[] players = idsAndNames.split(Utilities.PARSE_SPLITTER_TYPE);

            for (int i = 0; i < players.length; i++) {
                String[] parts = players[i].split(Utilities.PARSE_SPLITTER_ITEMS);
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

    public static void main(String[] args) {
        Client client = new Client();
    }
}
