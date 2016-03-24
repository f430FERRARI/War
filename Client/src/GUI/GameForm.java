package GUI;

import javax.swing.*;

/**
 * Created by hfirdaus on 3/17/16.
 */
public class GameForm {
    private JButton drawACardButton;
    private JTabbedPane chatWindows;
    private JTextArea publicChatArea;
    private JTextArea privateChatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JList inChatList;
    private JButton pauseButton;
    private JButton quitButton;
    private JPanel Chat;
    private JPanel gamePanel;
    private JLabel roundLabel;
    private JLabel timeLeftLabel;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public JLabel getRoundLabel() {return roundLabel;}
    public JLabel getTimeLeft() {return roundLabel;}

    public void setRoundLabel(JLabel x) {roundLabel = JLabel.;}
    public void setTimeLeft(JLabel time) {timeLeftLabel = time;}

    public JPanel getContentPane() {
        return gamePanel;
    }
}
