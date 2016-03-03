package Client;

import Client.ClientNetwork.ClientMessageHandler;
import Client.ClientNetwork.ClientNetworkManager;
import Server.Server;

import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mlee43 on 2016-03-02.
 */
public class ClientChatRoom implements ClientNetworkManager.ChatMessageListener {

    public static final byte CHAT_SEND_MSG = 0x0;
    public static final byte CHAT_RECEIVE_MSG = 0x1;

    private Server server;
    private boolean inChat = false;
    private ClientNetworkManager networkManager;

    public ClientChatRoom(Server server) {
        this.server = server;
        this.networkManager = ClientNetworkManager.getInstance();
        networkManager.register(ClientMessageHandler.LISTENER_CHAT, this);
    }

    private void startChatRoom() {
        inChat = true;
        System.out.println("Welcome to the chat room!");
        sendChatMessage();
    }

    private void sendChatMessage() {
        // Create a thread for sending messages
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            executorService.submit(new Callable() {
                @Override
                public Object call() throws Exception {

                    // Create a scanner so we can read the command-line input
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Write your message: ");    // TODO: Get user name

                    while (inChat) {
                        // get their input as a String
                        String text = scanner.next();
                        byte[] textBytes = text.getBytes();     // Message text
                        byte[] metadata = {CHAT_SEND_MSG, 0x1}; // Code, receiver
                        networkManager.send(Utilities.appendByteArrays(metadata,textBytes)); // TODO: Get length
                    }

                    return -1;
                }
            });
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            executorService.shutdownNow();
        }
    }

    @Override
    public void onReceiveMessage(int id, String text) {
        System.out.println("\n" + id + ": " + text);
    }

    public void setInChat(boolean inChat) {
        this.inChat = inChat;
    }
}
