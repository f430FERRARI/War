package Server;

import java.io.*;
import java.util.ArrayList;

public class FileInOut {

    public static final String FILE_PASSWORDS = "Passwords.txt";
    public static final String FILE_WINS = "Wins.txt";
    public static final String FILE_SCORES = "Scores.txt";

    public static final String PARSE_SPLITTER_1 = "%";
    public static final String PARSE_LOGIN = "~";
    public static final String PARSE_HIGH_SCORE = "`";
    public static final String PARSE_WINS = "$";

    private static final String PATH = ""; // TODO: Get the right path

    /**
     * Reads the latest entry for a player from a file.
     *
     * @param filename The name of the file to be read.
     * @param id       The id of the player.
     * @return The latest entry for the player or null if the player did not have an entry.
     */
    public static String readFromFile(String filename, int id) {

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
            String[] parts = tmp.get(i).split(PARSE_SPLITTER_1);
            if (Integer.parseInt(parts[0]) == id) {
                return parts[1];
            }
        }

        return null;
    }

    /**
     * Writes a line to a file.
     *
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

    public static void main(String[] args) {
        FileInOut fileInOut = new FileInOut();
        writeToFile(FILE_SCORES, "2232%yo");
        writeToFile(FILE_SCORES, "1212%My name is Joe");
        writeToFile(FILE_SCORES, "11111%I work in a button factory");
        System.out.println(readFromFile(FILE_SCORES, 1212));
    }
}
