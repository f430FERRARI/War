import ServerNetwork.ServerConnectionManager;

/**
 * Created by mlee43 on 2016-02-27.
 */
public class Server {

    public static void main(String[] args) {

        Player mike = new Player("Mike");
        ServerConnectionManager comms = new ServerConnectionManager();
        comms.startServerNetwork();

        byte[] nameRequest = {0x3, 0x0, 0x0};
        comms.send(1, nameRequest);
    }

}
