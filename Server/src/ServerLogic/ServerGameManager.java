package ServerLogic;

import ServerNetwork.ServerMessageHandler;
import ServerNetwork.ServerNetworkManager;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ServerGameManager implements ServerMessageHandler.GameMessageListener {

    private int[] cards;
    private ArrayList<Integer> players;
    private int playerCount;
    private ArrayList<ArrayList<Integer>> playerCards = new ArrayList<>();
    private ServerNetworkManager networkManager;

    public ServerGameManager() {
        this.networkManager = ServerNetworkManager.getInstance();
        networkManager.register(ServerMessageHandler.LISTENER_GAME, this);
    }

    public void setPlayers(ArrayList<Integer> playerIds) {
        players = playerIds;
    }

    /**
     * This method starts a new game. It resets everyone's score, shuffle's the deck and distributes the cards to each player.
     */
    public void startNewGame() {

        // Set player count
        playerCount = players.size();

        // Create a deck of cards
        cards = new int[52];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = i + 1;
        }

        // Shuffle the cards
        shuffleArray(cards);

        // Add empty lists of player cards
        for (int i = 0; i < playerCount; i++) {
            playerCards.add(new ArrayList<>());
        }

        // Distribute the cards evenly to the players
        for (int i = 0; i < cards.length; i++) {
            if ((i % playerCount) == 0) {
                if (i + playerCount <= 52) {
                    playerCards.get(i % playerCount).add(cards[i]);
                }
            } else {
                playerCards.get(i % playerCount).add(cards[i]);
            }
        }
    }

    /**
     * This method initiates a new round of War. It sends a message to each client prompting the
     * user to press draw. If the timer runs out on the client side, the client automatically submits draw.
     */
    public void initiateRound() {
        // Prompt user to press draw
    }

    /**
     * This is a callback method to a client draw. It draws a card from the appropriate client's deck and
     * broadcasts the card to everyone immediately. Once it receives everyone's draw, it checks if the game is complete.
     */
    public void onReceiveDraw() {
        // Draw card and broadcast result to everyone, check if game is done
    }

    /**
     * This method is called when the game is over. It broadcasts the winner and everyone's scores to the client.
     */
    public void endGame() {
        // Broadcast winners to everyone
    }

    /**
     * This method is a callback method that handles a player quitting. When a player quits, the cards are evenly distributed among the remaining players,
     * the event is announced to the other players and the player is removed from the Game.
     *
     * @param id The id number of the player who quit.
     */
    public void onReceiveQuit(int id) {

    }

    /**
     * This method is a callback method that handles a player pressing pause. When a player presses pause, the rest of the players are notified and their
     * game pauses as well.
     *
     * @param id The id of the player who pressed pause
     */
    public void onReceivePause(int id) {
        // Send pause message to all the other players
    }

    /**
     * This method shuffles an array of integers using Fisher-Yates shuffle.
     *
     * @param ar The array to be shuffled.
     */
    private void shuffleArray(int[] ar) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
