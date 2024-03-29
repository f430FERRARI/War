package ClientLogic;

import ClientNetwork.ClientMessageHandler;
import ClientNetwork.ClientNetworkManager;
import ClientNetwork.CommunicationCodes;
import GUI.GameForm;

import java.util.ArrayList;

public class ClientGameLogic implements GameForm.GameFormListener, ClientNetworkManager.GameMessageListener {

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
        // start timer

    }

    @Override
    /**
     * This method will update the label with the game winner!
     */
    public void displayWinner(int id){
        System.out.println("Display winner");
        String text = "Winner is..." + client.getPlayerList().get(id).getName();
        gameScreen.disableDrawButtonColour();
        gameScreen.setWinnerLabel(text);
        gameScreen.setWarLabel("GAME OVER");

    }
    /**
     * This method sends a draw message to the server.
     */
    @Override
    public void draw() {
        // Send draw to the server
        System.out.println("draw");
        byte[] message = Utilities.prepareOperationMessage(CommunicationCodes.GAME_DRAW, client.getMe().getId());
        networkManager.send(message);
    }

    /**
     * This is a callback method that responds to a player pressing the quit button. It sends a quit message to the server.
     */
    public void quit() {
        // Send quit to the server
        byte[] message = Utilities.prepareOperationMessage(CommunicationCodes.GAME_REQUEST_QUIT, client.getMe().getId());
        networkManager.send(message);
    }

    /**
     * This is a callback method that responds to a player pressing the pause button. It sends a pause message to the server.
     */
    public void pause() {
        // Send pause to the server
//        byte[] message = Utilities.prepareOperationMessage(CommunicationCodes.GAME_REQUEST_PAUSE, client.getMe().getId());
 //       networkManager.send(message);
    }

    public void unPause() {
        // Send unpause to the server
//        byte[] message = Utilities.prepareOperationMessage(CommunicationCodes.GAME_REQUEST_UNPAUSE, client.getMe().getId());
 //       networkManager.send(message);
    }

    /**
     * This method updates the scoreboard.
     */
    /**
     * This method updates the scoreboard.
     */
    @Override
    public void updateScoreboard(int score) {
        String pointsLabel = "Points: " + score;
        gameScreen.setMyPointsLabel(pointsLabel);
    }

    @Override
    public void updateOtherScoreboards(int id, int score) {
        String notMyPointsLabel = "Points: " + score;
        switch (id) {
            case 1:
                gameScreen.setPlayer1PointsLabel(notMyPointsLabel);
                break;
            case 2:
                gameScreen.setPlayer2PointsLabel(notMyPointsLabel);
                break;
            case 3:
                gameScreen.setPlayer3PointsLabel(notMyPointsLabel);
                break;
        }
    }

    @Override
    public void onBeginGame() {

    }

    public void onQuit(int id) {
        System.out.println("SOMEONE QUIT THE GAME");
        client.returnToGameLobby(id);
    }

    @Override
    public void onBeginRound(int round) {
        System.out.println("ON BEGIN ROUND!!");

        gameScreen.enableDrawButtonColour();
        String roundLabel = "Round # " + round;
        gameScreen.setRoundLabel(roundLabel);
    }
    @Override
    public void displayCard(String card){
        System.out.println("Displaying card...");
        gameScreen.setMyCardLabel(card);
    }

    @Override
    public void onRoundEnd() {

    }

    @Override
    public void onReceiveScores() {
    }

    @Override
    public void onGameFinished() {
        // display winner

    }
}
