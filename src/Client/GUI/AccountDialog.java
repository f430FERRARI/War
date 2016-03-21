package Client.GUI;

import Client.ClientLogic.Client;

import javax.swing.*;
import java.awt.event.*;

public class AccountDialog extends JDialog {

    private AccountDialogListener accountDialogListener;

    private LoginDialog lDialog;
    private JFrame theFrame;
    private JPanel contentPane;
    private JButton buttonCreate;
    private JButton buttonCancel;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public interface AccountDialogListener {
        void onCreateAccountAttempt(String username, String password);
    }

    public AccountDialog(Client client, LoginDialog lDialog, JFrame theFrame) {

        this.accountDialogListener = client;
        this.lDialog = lDialog;
        this.theFrame = theFrame;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCreate);

        buttonCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get username and password from input
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                accountDialogListener.onCreateAccountAttempt(username, password);
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
     * Callback for when the cancel button is clicked. Returns to the login dialog.
     */
    private void onCancel() {
        theFrame.setTitle("Welcome to War!");
        theFrame.setContentPane(lDialog.getContentPane());
        theFrame.setLocationRelativeTo(null);
        theFrame.pack();
        theFrame.setVisible(true);
        dispose();
    }

    public JPanel getContentPane() {
        return this.contentPane;
    }
}
