package ClientLogic;

import ClientNetwork.ClientMessageHandler;
import ClientNetwork.ClientNetworkManager;
import ClientNetwork.CommunicationCodes;
import GUI.GameForm;

import java.util.ArrayList;

public class ClientGameLogic implements ClientNetworkManager.GameMessageListener {

    private Client client;
    private ClientNetworkManager networkManager;

    private ArrayList<Integer> players = new ArrayList<>();

    private GameForm gameScreen;

    public ClientGameLogic(Client client, GameForm gameScreen) {
        this.client = client;
        this.gameScreen = gameScreen;
        this.networkManager = ClientNetworkManager.getInstance();
        networkManager.register(ClientMessageHandler.LISTENER_GAME, this);
    }

    /**
     * This method sets the players in the game. It is called when the game starts.
     *
     * @param playerIds A list of the player ids in the game.
     */
    public void setPlayerList(ArrayList<Integer> playerIds) {
        players = playerIds;
    }

    /**
     * This method starts a round of War. It enables the draw button and begins a timer for the user to press draw.
     */
    public void startRound() {

    }

    /**
     * This method sends a draw message to the server.
     */
    public void draw() {
        // Send draw to the server
        byte[] message = Utilities.prepareOperationMessage(CommunicationCodes.GAME_REQUEST_DRAW, client.getMe().getId());
        networkManager.send(message);
    }

    /**
     * This is a callback method that responds to a player pressing the quit button. It sends a quit message to the server.
     */
    public void quit() {
        // Send quit to the server
//        byte[] message = Utilities.prepareOperationMessage(CommunicationCodes.GAME_REQUEST_QUIT, client.getMe().getId());
//        networkManager.send(message);
    }

    /**
     * This is a callback method that responds to a player pressing the pause button. It sends a pause message to the server.
     */
    public void pause() {
        // Send pause to the server
        byte[] message = Utilities.prepareOperationMessage(CommunicationCodes.GAME_REQUEST_PAUSE, client.getMe().getId());
        networkManager.send(message);
    }

    public void unPause() {
        // Send unpause to the server
        byte[] message = Utilities.prepareOperationMessage(CommunicationCodes.GAME_REQUEST_UNPAUSE, client.getMe().getId());
        networkManager.send(message);
    }

    /**
     * This method updates the scoreboard.
     */
    public void updateScoreboard() {

    }

    @Override
    public void onBeginGame() {

    }

    @Override
    public void onBeginRound() {

    }

    @Override
    public void onRoundEnd() {

    }

    @Override
    public void onReceiveScores() {

    }

    @Override
    public void onGameFinished() {

    }
}
