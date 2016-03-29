package GUI;

import ClientLogic.Client;
import ClientLogic.ClientGameLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private Chat chatPanel;
    private JLabel myCardLabel;

    private GameFormListener gameFormListener;

    public interface GameFormListener {
        void draw();
    }

    public GameForm() {

    }

    public void addGameLogicListener(ClientGameLogic gameLogic) {
        this.gameFormListener = gameLogic;

        drawACardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gameFormListener.draw();
            }
        });
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public JLabel getRoundLabel() {return roundLabel;}
    public JLabel getTimeLeft() {return roundLabel;}

    public void setMyCardLabel(String text) {
        System.out.println("Update label");
        myCardLabel.setText(text);
    }

    public void setRoundLabel(String text) {
        System.out.println("Update label");
        roundLabel.setText(text);
    }
    public void setTimeLeft(JLabel time) {timeLeftLabel = time;}

    public void setDrawButtonColour(){
        drawACardButton.setBackground(Color.GREEN);
    }

    public void resetDrawButtonColour(){
        drawACardButton.setBackground(Color.GRAY);
    }

    public JPanel getContentPane() {
        return gamePanel;
    }

    public Chat getChatPanel() {
        return chatPanel;
    }
}
