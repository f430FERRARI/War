package Client.ClientLogic;

import Client.ClientNetwork.ClientMessageHandler;
import Client.ClientNetwork.ClientNetworkManager;
import Client.ClientNetwork.CommunicationCodes;
import Client.GUI.AccountDialog;
import Client.GUI.GameLobbyForm;
import Client.GUI.LoginDialog;

import javax.swing.*;
import java.util.HashMap;

import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

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

        // Initiate the program window and add a listener for closing
        theFrame = new JFrame("WELCOME TO WAR");
        theFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int action = JOptionPane.showConfirmDialog(theFrame,
                        "Are you want to quit and log off?", "Really Closing?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (action == 1) {
                    theFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                } else {
                    onQuitSystem();
                }
            }
        });

        // Initiate the other GUI windows
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

    /**
     * Callback method to the player closing the window and quitting the game. Sends a quit message to the server and
     * then closes the application.
     */
    public void onQuitSystem() {
        byte[] message = Utilities.prepareOperationMessage(CommunicationCodes.ADMIN_PLAYER_QUIT, me.getId());
        networkManager.send(message);
        System.exit(0);
    }

    /**
     * Callback method to the communicator. Sets the players ID to an ID sent by the server.
     *
     * @param id The ID assigned by the server.
     */
    @Override
    public void onReceiveID(int id) {
        me.setId(id);
    }

    /**
     * Callback method to LoginDialog. This is called when the login button is called. Sets the users player name and
     * sends the login info to the server.
     *
     * @param usernameField The username entered into the username field.
     * @param passwordField The password entered into the password field.
     */
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

    /**
     * Callback to the communicator. This is called when the client receives the result of their login attempt. Switches
     * the window to the game lobby if successful. Displays error message otherwise.
     *
     * @param result A string containing the result of the login attempt.
     */
    @Override
    public void onReceiveLoginResult(String result) {
        if (result.equals("Success")) {
            System.out.println("Login successful.");
            // Change screen to game lobby
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

    /**
     * Callback to LoginDialog. This is called when the user presses the create account button. Opens up an account
     * creation dialog.
     */
    @Override
    public void onClickCreateAccount() {
        accountDialog = new AccountDialog(this, lDialog, theFrame);
        theFrame.setContentPane(accountDialog.getContentPane());
        theFrame.setLocationRelativeTo(null);
        theFrame.pack();
        theFrame.setVisible(true);
    }

    /**
     * Callback to the AccountDialog. This is called when the user presses the button to submit his account info. Sends
     * the account info to the server.
     *
     * @param username
     * @param password
     */
    @Override
    public void onCreateAccountAttempt(String username, String password) {
        String accountInfo = username + Utilities.PARSE_SPLITTER_ENTRY + password;
        byte[] message = Utilities.prepareMessage(CommunicationCodes.ADMIN_CREATE_ACCOUNT, me.getId(),
                Utilities.stringToByteArray(accountInfo));
        networkManager.send(message);
    }

    /**
     * Callback to the communicator. This is called when the client receives the result of his attempt to create an
     * account. Returns the user back to the login dialog if the attempt was successful.
     *
     * @param result A string indicating the result of the account creation attempt.
     */
    @Override
    public void onReceiveCreateResult(String result) {
        if (result.equals("Success")) {
            System.out.println("Account created successfully.");
            theFrame.setContentPane(lDialog.getContentPane());
            // TODO: Display success message
            theFrame.setLocationRelativeTo(null);
            theFrame.pack();
            theFrame.setVisible(true);
            accountDialog.dispose();
        } else {
            // TODO: Display error message
            System.out.println(result);
        }
    }

    /**
     * Callback to the communicator. This is called when a the client receives a message about a new player including
     * themselves joining the system. Creates a new player object and adds it to the list.
     *
     * @param id The id of the new player.
     * @param name The name of the new player.
     */
    @Override
    public void onNewPlayerJoined(int id, String name) {
        Player newPlayer = new Player();
        newPlayer.setId(id);
        newPlayer.setName(name);
        playerList.put(id, newPlayer);
        System.out.println("New player added: " + playerList.size() + "players.");
    }

    /**
     * Callback to the communicator. This is called when the client successfully logs in and includes the names and ids
     * of all players currently in the system. Creates a new player object for each individual.
     *
     * @param idsAndNames A single string that packages a list of all players in system other than the client himself.
     */
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

    /**
     * Callback to the communicator. This is called when the client receives a message about a player exiting the
     * system. Removes the player from the player list.
     *
     * @param id The id of the player who quit.
     */
    @Override
    public void onPlayerRemoved(int id) {
        playerList.remove(id);
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
