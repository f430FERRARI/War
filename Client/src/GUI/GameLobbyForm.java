package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
    private JPanel lobbyPanel;
    private JButton sendButton;
    private JLabel gameLobbyLabel;
    private JPanel Chat;
    private JTextField messageField;
    private JButton leaveButton;
    private JButton startGameButton;

    private DefaultListModel onlineListModel, playerListModel, observerListModel;

    private LobbyGUIListener lobbyGUIListener;

    public interface LobbyGUIListener {
        void onClickJoin();

        void onClickObserve();

        void onClickStart();
    }

    public GameLobbyForm() {

        onlineListModel = new DefaultListModel();
        playerListModel = new DefaultListModel();
        observerListModel = new DefaultListModel();

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                lobbyGUIListener.onClickJoin();
            }
        });

        observeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                lobbyGUIListener.onClickObserve();
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }

    /**
     * This method updates all lobby lists.
     *
     * @param online The list of names of players in the online list and the in chat list.
     * @param game The list of names of players in the game lobby list.
     * @param observers The list of names of players in the observers list.
     */
    public void updateLobbyLists(ArrayList<String> online, ArrayList<String> game, ArrayList<String> observers) {
        // Clear all the old lists
        onlineListModel.removeAllElements();
        playerListModel.removeAllElements();
        observerListModel.removeAllElements();

        // Populate the online list and chat room list with the updated lists
        for (String onlineGuy : online) {
            onlineListModel.addElement(onlineGuy);
        }
        onlineList.setModel(onlineListModel);
        inChatList.setModel(onlineListModel);

        // Populate the game lobby list with the updated list
        for (String gameGuy : game) {
            playerListModel.addElement(gameGuy);
        }
        playerList.setModel(playerListModel);

        // Populate the observer list with the updated list
        for (String observerDude : observers) {
            observerListModel.addElement(observerDude);
        }
        observerList.setModel(observerListModel);
    }

    public JPanel getContentPane() {
        return this.lobbyPanel;
    }

    /**
     * This method is used to register listeners of this class.
     *
     * @param listener A reference to the object that is listening to the class.
     */
    public void register(Object listener) {
        lobbyGUIListener = (LobbyGUIListener) listener;
    }
}
