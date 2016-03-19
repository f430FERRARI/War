package Server.ServerLogic;

public class Login {

    /**
     * This method creates a new login by saving it to the login file.
     */
    public static String createNewLogin(String username, String password) {

        String account = FileInOut.readFromFile(FileInOut.FILE_ACCOUNTS, username);
        if (account != null && !account.isEmpty()) {
            // Account exists so send a fail message
            return "Username already exists. Please try again with a different username.";
        } else {
            // Account does not exist so create an account
            String usernameAndPassword = username + Utilities.PARSE_SPLITTER_FIELD + password;
            FileInOut.writeToFile(FileInOut.FILE_ACCOUNTS, usernameAndPassword);
            return "Success";
        }
    }

    /**
     * This method verifies the login information entered by the client.
     */
    public static String verifyLogin(String username, String password) {

        String login = FileInOut.readFromFile(FileInOut.FILE_ACCOUNTS, username);
        if (login != null && !login.isEmpty()) {
            // Login exists so check the password
            String[] parts = login.split(Utilities.PARSE_SPLITTER_FIELD);
            if (parts[1].equals(password)) {
                return "Success";
            } else {
                return "Password is incorrect.";
            }
        } else {
            // Could not find the username
            return "Username does not exist.";
        }
    }
}