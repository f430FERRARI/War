package ServerLogic;

import ServerNetwork.CommunicationCodes;
import ServerNetwork.ServerMessageHandler;
import ServerNetwork.ServerNetworkManager;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class ServerGameManager implements ServerMessageHandler.GameMessageListener {

    private int[] cards;
    private ArrayList<Integer> players;
    private int playerCount;
    private ArrayList<ArrayList<Integer>> playerCards = new ArrayList<>();
    private ServerNetworkManager networkManager;
    private int roundNumber;
    private ArrayList<Integer> drawnCards = new ArrayList<>();
    private ArrayList<Integer> playerScores = new ArrayList<>();
    private ArrayList<Integer> otherPlayersScores = new ArrayList<>();

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
        roundNumber = 0;
        // Set player count
        playerCount = players.size();

        // Initialize all player scores to 0
        for (int i = 0; i < playerCount; i++) {
            playerScores.add(i, 0);
        }

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

        /*
        for (int i =0; i < playerCards.size(); i++) {
            System.out.println(playerCards.size());
            System.out.print(playerCards.get(i));
        }
        */
        for (int i = 0; i < players.size(); i++) {
            System.out.println(players.size());
            System.out.print(players.get(i));
        }
        updateRound(); // initialize round of war
    }

    /**
     * This method initiates a new round of War. It sends a message to each client prompting the
     * user to press draw. If the timer runs out on the client side, the client automatically submits draw.
     */
    public void updateRound() {
        roundNumber++;
        drawnCards.clear();
        // Initialize values to drawn cards array
        for (int i = 0; i < drawnCards.size(); i++) {
            drawnCards.add(-1);
        }

        // Prompt users to press draw and start timer
        byte[] round = Utilities.intToByteArray(roundNumber);
        byte[] message = Utilities.prepareMessage(CommunicationCodes.GAME_REQUEST_DRAW, round);
        networkManager.sendToAll(message);

        //Lets everyone know game is happening
        byte[] message2 = Utilities.prepareOperationMessage(CommunicationCodes.LOBBY_GAME_NOTIFICATION);
        networkManager.sendToAll(message2);
    }

    /**
     * This method translates the value of the card into a string
     */
    public String translateIntToCard(int cardValue) {
        int cardNumber = (int) Math.ceil(cardValue / 4) + 1;
        int suit = cardValue % 4;


        System.out.println("Suit: " + suit);
        System.out.println("Card Number: " + cardNumber);
        String stringSuit = cardNumber + " of ";

        if (cardValue < 5) {
            stringSuit = "Ace of ";
        }
        if (cardValue > 40) {
            stringSuit = "Jack of ";
        }
        if (cardValue > 44) {
            stringSuit = "Queen of ";
        }
        if (cardValue > 48) {
            stringSuit = "King of ";
        }

        switch (suit) {
            case 0:
                stringSuit = stringSuit + "♠";
                break;
            case 1:
                stringSuit = stringSuit + "♥";
                break;
            case 2:
                stringSuit = stringSuit + "♦";
                break;
            case 3:
                stringSuit = stringSuit + "♣";
                break;
        }
        return stringSuit;
    }

    /**
     * This method determines whether the game is over by determing if the deck is empty
     *
     * @return true if game is over, else return false
     */
    public Boolean determineGameOver() {
        if (playerCards.get(0).isEmpty()) { // check if deck is empty
            return true;
        }
        return false;
    }

    /**
     * This is a callback method to a client draw. It draws a card from the appropriate client's deck and
     * broadcasts the card to everyone immediately. Once it receives everyone's draw, it checks if the game is complete.
     */
    public void onReceiveDraw(int id) {
        System.out.println("ON RECEIVE DRAW SERVER ");

        int randomCard = playerCards.get(players.indexOf(id)).remove(0);
        System.out.println(randomCard);

        drawnCards.add(players.indexOf(id), randomCard);


        System.out.println("drawn card " + drawnCards.get(players.indexOf(id)));


        String card = translateIntToCard(drawnCards.get(players.indexOf(id)));

        System.out.println(card);

        byte[] cardMessage = Utilities.stringToByteArray(card);
        byte[] message = Utilities.prepareMessage(CommunicationCodes.GAME_RETURN_DRAW, cardMessage);
        networkManager.send(id, message);

        System.out.println("server's " + message);


        if (drawnCards.size() == playerCount) {
            int winningPlayerID = determineRoundWinner();
            winningPlayerID = winningPlayerID - 1;

            System.out.println("WINNING PLAYER ID: " + winningPlayerID);

            int oldScore = playerScores.get(winningPlayerID);
            System.out.println("OLD SCORE: " + oldScore);
            int newScore = oldScore + 1;
            System.out.println("NEW SCORE: " + newScore);
            playerScores.set(winningPlayerID, newScore);

            System.out.println("PLAYER SCORES: " + playerScores);

            for (int i = 0; i < players.size(); i++) {
                byte[] playersScore = Utilities.intToByteArray(playerScores.get(i));
                byte[] pointsMessage = ServerLogic.Utilities.prepareMessage(ServerNetwork.CommunicationCodes.GAME_UPDATE_SCORE_PRIVATE, playersScore);
                networkManager.send(players.get(i), pointsMessage);

                // updates other players' scores on each client
                for (int j = 0; j < players.size(); j++) {

                    otherPlayersScores.clear();
                    otherPlayersScores.addAll(playerScores);
                    //System.out.println("otherPlayersScores size BEFORE: " + otherPlayersScores.size());
                    if (players.get(i) != players.get(j)) {
                        //otherPlayersScores.remove(players.get(i));

                        //System.out.println("otherPlayersScores size AFTER: " + otherPlayersScores.size());
                        for (int k = 0; k < otherPlayersScores.size(); k++) {
                            byte[] otherPlayersScore = Utilities.intToByteArray(otherPlayersScores.get(k));
                            if ((k == 0) && (players.get(j) == players.get(k))) {
                                byte[] p1ScoreMessage = ServerLogic.Utilities.prepareMessage(ServerNetwork.CommunicationCodes.GAME_UPDATE_SCORE_PLAYER_1, otherPlayersScore);
                                networkManager.send(players.get(i), p1ScoreMessage);
                            }
                            if ((k == 1) && (players.get(j) == players.get(k))) {
                                byte[] p2ScoreMessage = ServerLogic.Utilities.prepareMessage(ServerNetwork.CommunicationCodes.GAME_UPDATE_SCORE_PLAYER_2, otherPlayersScore);
                                networkManager.send(players.get(i), p2ScoreMessage);
                            }
                            if ((k == 2) && (players.get(j) == players.get(k))) {
                                byte[] p3ScoreMessage = ServerLogic.Utilities.prepareMessage(ServerNetwork.CommunicationCodes.GAME_UPDATE_SCORE_PLAYER_3, otherPlayersScore);
                                networkManager.send(players.get(i), p3ScoreMessage);
                            }
                        }
                    }
                }
            }

            if (determineGameOver()) endGame(winningPlayerID);

            // update points here who won, send to all players
            updateRound();
        }

    }



    /**
     * This method will determine the round winner
     * @return int returns player ID of round winner
     */
    public int determineRoundWinner(){
        int largestCard = 0;
        int winningPlayer = 0;
        for(int i = 0; i <= players.size()-1; i++) { // determine winner
            System.out.println("drawn cards " + drawnCards.get(i));
            System.out.println("largest card " + largestCard);
            if (drawnCards.get(i) > largestCard){
                largestCard = drawnCards.get(i);
                winningPlayer = players.get(i);
            }
        }
        System.out.println("WINNER " + winningPlayer);
        return winningPlayer;
    }

    /**
     * This method is called when the game is over. It broadcasts the winner and everyone's scores to the client.
     */
    public void endGame(int id) {

        System.out.println("GAME OVER");
        // Broadcast game winner to everyone
        byte[] gameWinnerID = Utilities.intToByteArray(id);
        byte[] message = Utilities.prepareMessage(CommunicationCodes.GAME_WINNER_ID, gameWinnerID);
        networkManager.sendToAll(message);

    }

    /**
     * This method is a callback method that handles a player quitting. When a player quits, the cards are evenly distributed among the remaining players,
     * the event is announced to the other players and the player is removed from the Game.
     *
     * @param id The id number of the player who quit.

     */
    public void onReceiveQuit(int id) {
        int indexOfID = players.indexOf(id);
        playerCards.remove(indexOfID);
        playerScores.remove(indexOfID);
        players.remove(indexOfID);
        playerCount--;



        byte [] byteID = Utilities.intToByteArray(id);
        byte [] message = Utilities.prepareMessage(CommunicationCodes.GAME_QUIT, byteID);
        networkManager.sendToAll(message);
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

    public static void main(String[] args) {
        ServerGameManager gm = new ServerGameManager();
        System.out.println(gm.translateIntToCard(48));
    }
}
