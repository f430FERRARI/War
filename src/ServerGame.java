import ServerNetwork.ServerMessageHandler;
import ServerNetwork.ServerNetworkManager;

import java.util.ArrayList;

public class ServerGame implements ServerMessageHandler.GameMessageListener {
	
	private ArrayList<Player> players;
	private int[] cards;  
	
	public ServerGame(ArrayList<Player> players) { 
		this.players = players;
        ServerNetworkManager.getInstance().getServerMessageHandler().register(ServerMessageHandler.LISTENER_GAME, this);
	}
	
	/** 
	 * This method starts a new game. It resets everyone's score, shuffle's the deck and distributes the cards to each player. 
	 */
	public void startNewGame() { 
		
		// Distribute cards evenly among the players
		cards = new int[52]; 
		for (int i = 0; i < cards.length; i++) { 
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
}
