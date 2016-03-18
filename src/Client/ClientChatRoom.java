package Client;

import Client.ClientNetwork.ClientMessageHandler;
import Client.ClientNetwork.ClientNetworkManager;

import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientChatRoom implements ClientNetworkManager.ChatMessageListener {

    private Client client;
    private boolean inChat = false;
    private ClientNetworkManager networkManager;

    public ClientChatRoom(Client client) {
        this.client = client;
        this.networkManager = ClientNetworkManager.getInstance();
        networkManager.register(ClientMessageHandler.LISTENER_CHAT, this);
    }

    public void startChatRoom() {
        inChat = true;
        System.out.println("Welcome to the chat room!");
        runChat();
    }

    private void runChat() {
        // Create a thread for sending messages
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            executorService.submit(new Callable() {
                @Override
                public Object call() throws Exception {

                    // Create a scanner so we can read the command-line input
                    Scanner scanner = new Scanner(System.in);

                    while (inChat) {
//                        System.out.println("WHERE: ");
//                        String destination = scanner.next().trim();
//                        int destID = client.getPlayerList().get(destination).getId();

                        // get their input as a String
                        System.out.println("Write your message: ");
                        String text = scanner.next();
                        byte[] destIDBytes = Utilities.intToByteArray(2);  // TODO: FOR DEBUG
                        byte[] textBytes = Utilities.stringToByteArray(text);
                        byte[] payload = Utilities.appendByteArrays(destIDBytes, textBytes);
                        networkManager.send(Utilities.prepareMessage(CommunicationCodes.CHAT_SEND_IND_MSG, client.getMe().
                                getId(), payload));
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
    public void onClientRcvIndvMsg(int id, String text) {

    }

    @Override
    public void onClientRcvGrpMsg(int id, String text) {

    }

    @Override
    public void onSuccessfulChatroomEntry() {

    }

    @Override
    public void onCSuccessfulChatroomExit() {

    }

    public void setInChat(boolean inChat) {
        this.inChat = inChat;
    }
}
