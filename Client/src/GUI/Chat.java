package GUI;

import ClientLogic.ClientChatRoom;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mlee43 on 2016-03-24.
 */
public class Chat {
    private JPanel chat;
    private JTabbedPane chatTabs;
    private JTextArea grpChatArea;
    private JTextArea indChatArea;
    private JButton sendButton;
    private JList inChatList;
    private JTextField messageField;

    private DefaultListModel inChatListModel = new DefaultListModel();
    private ChatGUIListener chatGUIListener;

    public interface ChatGUIListener {
        void onClickSendMsg(int type, int destUser, String msg);
    }

    public Chat() {
        // Listener for the send button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Got click send!");

                int chatType = chatTabs.getSelectedIndex();
                String text = messageField.getText();
                int dest = inChatList.getSelectedIndex();
                chatGUIListener.onClickSendMsg(chatType, dest, text);
            }
        });
    }

    /**
     * This method updates the chat text areas for the public and private chats. Posts the message with a name.
     *
     * @param area The chat area to update, public or private.
     * @param player The player who sent the message.
     * @param msg The message that is to be displayed.
     */
    public void updateChatArea(int area, String player, String msg) {
        if (area == ClientChatRoom.CHATROOM_GRP_MSG) {
            grpChatArea.append(player + ": " + msg + "\n");
            grpChatArea.update(grpChatArea.getGraphics());
        } else if (area == ClientChatRoom.CHATROOM_IND_MSG){
            indChatArea.append(player + ": " + msg + "\n");
        }
    }

    /**
     * This method update the list of players in the chat room.
     *
     * @param inChatNames The names of the players in the chat room.
     */
    public void updateInChatList(String[] inChatNames) {
        inChatListModel.removeAllElements();
        for (int i = 0; i < inChatNames.length; i++) {
            inChatListModel.addElement(inChatNames[i]);
        }
        inChatList.setModel(inChatListModel);
    }

    /**
     * This method is used to register listeners of this class.
     *
     * @param listener A reference to the object that is listening to the class.
     */
    public void register(Object listener) {
        chatGUIListener = (ChatGUIListener) listener;
    }
}
