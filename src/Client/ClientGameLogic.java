package Client;

import Client.ClientNetwork.ClientMessageHandler;
import Client.ClientNetwork.ClientNetworkManager;

public class ClientGameLogic implements ClientNetworkManager.GameMessageListener{

	private Player me;
	private ClientNetworkManager networkManager;

	public ClientGameLogic(Player player) {
		this.me = player;
        this.networkManager = ClientNetworkManager.getInstance();
        networkManager.register(ClientMessageHandler.LISTENER_GAME, this);
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
		
	} 
	
	/** 
	 * This is a callback method that responds to a player pressing the quit button. It sends a quit message to the server.
	 */
	public void quit() {
		
	} 
	
	/** 
	 * This is a callback method that responds to a player pressing the pause button. It sends a pause message to the server.  
	 */
	public void pause() {
		
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