import ClientNetwork.ClientServerCommunicator;

/**
 * Created by mlee43 on 2016-02-27.
 */
public class Client {

    public static void main(String[] args) {
        Player mike = new Player("Mike");
        ClientServerCommunicator communicator = new ClientServerCommunicator(mike.getName());
        communicator.startServerConnection();
    }
}
