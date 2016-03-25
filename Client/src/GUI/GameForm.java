package GUI;

import javax.swing.*;

/**
 * Created by hfirdaus on 3/17/16.
 */
public class GameForm {
    private JButton drawACardButton;
    private JButton pauseButton;
    private JButton quitButton;
    private JPanel gamePanel;
    private JLabel roundLabel;
    private JLabel timeLeftLabel;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public JLabel getRoundLabel() {return roundLabel;}
    public JLabel getTimeLeft() {return roundLabel;}

    public void setRoundLabel(String text) {roundLabel.setText(text);}
    public void setTimeLeft(JLabel time) {timeLeftLabel = time;}

    public JPanel getContentPane() {
        return gamePanel;
    }
}
