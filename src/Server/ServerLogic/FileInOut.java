package Server.ServerLogic;

import java.io.*;
import java.util.ArrayList;

public class FileInOut {

    public static final String FILE_ACCOUNTS = "Accounts.txt";

    private static final String PATH = ""; // TODO: Get the right path

    /**
     * Reads the latest entry for a player from a file.
     *
     * @param filename The name of the file to be read.
     * @param username The username of the player
     * @return The latest entry for the player or null if the player did not have an entry.
     */
    public static String readFromFile(String filename, String username) {

        // Read in the file from top to bottom
        ArrayList<String> tmp = new ArrayList<>();
        String ch;

        try {
            FileReader fr = new FileReader(PATH + filename);
            BufferedReader br = new BufferedReader(fr);

            do {
                ch = br.readLine();
                tmp.add(ch);
            } while (ch != null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read the array from the back to the front.
        // There's an extra blank line at the bottom of the file so its -2
        for (int i = (tmp.size() - 2); i >= 0; i--) {
            String[] parts = tmp.get(i).split(Utilities.PARSE_SPLITTER_FIELD);
            if (parts[0].equals(username)) {
                return tmp.get(i);
            }
        }

        return null;
    }

    /**
     * Writes a line to a file.
     *
     * @param filename The name of the file to write to.
     * @param line The line to be written.
     */
    public static void writeToFile(String filename, String line) {
        try {
            FileWriter fw = new FileWriter(PATH + filename, true); //the true will append the new data
            fw.write(line + "\n");//appends the string to the file
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    /**
     * Utility for clearing all the entries in a file.
     *
     * @param filename The file to be cleared.
     */
    public static void clearFile(String filename) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        pw.close();
    }

    public static void main(String[] args) {
        FileInOut.clearFile(FILE_ACCOUNTS);
    }
}
