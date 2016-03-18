package Client.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by enanthav on 3/17/16.
 */
public class GameLobbyForm {
    private JList onlineList;
    private JButton joinButton;
    private JButton observeButton;
    private JList playerList;
    private JList observerList;
    private JList inChatList;
    private JTabbedPane chatWindows;
    private JPanel lobbyPanel;
    private JTextField messageField;
    private JButton sendButton;
    private JLabel gameLobbyLabel;
    private JTextArea publicChatArea;
    private JTextArea privateChatArea;

    public GameLobbyForm() {
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        observeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }
    public JPanel getContentPane() {
        return this.lobbyPanel;
    }
    /*
    public static void main(String[] args){
        JFrame lobbyFrame = new JFrame("Welcome to War");
        lobbyFrame.setContentPane(new GameLobbyForm().lobbyPanel);
        lobbyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lobbyFrame.setLocationRelativeTo(null); // centre
        lobbyFrame.pack();
        lobbyFrame.setVisible(true);
    }
    */
}
