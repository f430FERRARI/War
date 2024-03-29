package GUI;

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
    private JButton quitButton;
    private JPanel gamePanel;
    private JLabel roundLabel;
    private JLabel winnerLabel;
    private Chat chatPanel;
    private JLabel myCardLabel;
    private JLabel player1Points;
    private JLabel player2Points;
    private JLabel player3Points;
    private JLabel myPointsLabel;
    private JLabel warLabel;

    private GameFormListener gameFormListener;

    public interface GameFormListener {
        void draw();
        void quit();
    }

    public GameForm() {

    }

    public void addGameLogicListener(ClientGameLogic gameLogic) {
        this.gameFormListener = gameLogic;

        drawACardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gameFormListener.draw();
                disableDrawButtonColour();
            }
        });
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFormListener.quit();
            }
        });
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public void setMyPointsLabel(String text) {
        myPointsLabel.setText(text);
    }

    public void setPlayer1PointsLabel(String text) {
        player1Points.setText(text);
    }

    public void setPlayer2PointsLabel(String text) {
        player2Points.setText(text);
    }

    public void setPlayer3PointsLabel(String text) {
        player3Points.setText(text);
    }

    public void setWinnerLabel(String text){
        winnerLabel.setText(text);
    }

    public void setMyCardLabel(String text) {
        System.out.println("Update card label");
        myCardLabel.setText(text);
    }
    public void setWarLabel(String text) {
        System.out.println("Update war label");
        warLabel.setText(text);
    }
    public void setRoundLabel(String text) {
        System.out.println("Update round label");
        roundLabel.setText(text);
    }

    public void enableDrawButtonColour(){
        //drawACardButton.setEnabled(true);
        drawACardButton.setBackground(Color.GREEN);
    }

    public void disableDrawButton(){
        drawACardButton.setEnabled(false);
    }
    public void disableDrawButtonColour(){
        drawACardButton.setBackground(Color.GRAY);
    }

    public JPanel getContentPane() {
        return gamePanel;
    }

    public Chat getChatPanel() {
        return chatPanel;
    }
}
