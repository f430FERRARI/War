package Client.GUI;

import Client.ClientLogic.Client;

import javax.swing.*;
import java.awt.event.*;

public class LoginDialog extends JDialog {

    private Client client;
    private LoginDialogListener loginListener;

    private JFrame theFrame;
    private JPanel contentPane;
    private JButton loginButton;
    private JButton buttonCancel;
    private JButton createNewAccountButton;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public interface LoginDialogListener {
        void onClickLogin(String username, String password);
        void onClickCreateAccount();
    }

    public LoginDialog(Client client, JFrame theFrame) {

        // Register the frame and the listener
        this.client = client;
        this.loginListener = client;
        this.theFrame = theFrame;
        
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                loginListener.onClickLogin(username, password);
            }
        });

        createNewAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginListener.onClickCreateAccount();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Callback method to when the cancel button is clicked.
     */
    private void onCancel() {
        dispose();
    }

    public JPanel getContentPane() {
        return this.contentPane;
    }

}
