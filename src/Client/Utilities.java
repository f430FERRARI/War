package Client;

import java.io.UnsupportedEncodingException;

public class Utilities {

    /**
     * Appends two byte arrays together.
     *
     * @param a The front array
     * @param b The back array
     * @return The combined arrays
     */
    public static byte[] appendByteArrays(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);

        return c;
    }

    /**
     * Converts an int into a byte array in Big-Endian format.
     *
     * @param inputInt The int to be converted into a byte array.
     * @return The inputInt in a Big-Endian byte array format.
     */
    public static byte[] intToByteArray(int inputInt) {
        return new byte[]{
                (byte) ((inputInt >> 24) & 0xFF),
                (byte) ((inputInt >> 16) & 0xFF),
                (byte) ((inputInt >> 8) & 0xFF),
                (byte) (inputInt & 0xFF)
        };
    }


    /**
     * Converts a byte array into an int.
     *
     * @param inputArray The byte array representing an int
     * @return An int from the byte array
     */
    public static int byteArrayToInt(byte[] inputArray) {
        return inputArray[3] & 0xFF |
                (inputArray[2] & 0xFF) << 8 |
                (inputArray[1] & 0xFF) << 16 |
                (inputArray[0] & 0xFF) << 24;
    }

    /**
     * Prepends the message with a byte indicating the length of the message.
     * All messages must have their first byte indicating the
     * length of the message, so this method takes care of that.
     *
     * @param message The message to send to the player.
     */
    public static byte[] prependMessageLength(byte[] message) { // TODO: Dont get this
        int messageLength = message.length;
        byte[] outputMessage = new byte[messageLength + 1];

        outputMessage[0] = (byte) messageLength;

        for (int i = 0, j = 1; i < messageLength; i++, j++) {
            outputMessage[j] = message[i];
        }

        return outputMessage;
    }

    /**
     * This method turns a byte array into a string that uses UTF-8 encoding.
     *
     * @param message The message to be decoded.
     * @return A string representing the message.
     */
    public static String byteArrayToString(byte[] message) {
        try {
            String text = new String(message, "UTF-8");
            return text;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null; // TODO
        }

    }

    /**
     * This method turns a string using UTF-8 encoding to a byte array.
     *
     * @param text The string to be encoded.
     * @return The byte array representing the string.
     */
    public static byte[] stringToByteArray(String text) {
        try {
            byte[] bytes = text.getBytes("UTF-8");
            return bytes;
        } catch (UnsupportedEncodingException e) {
            System.out.println("Text not in UTF-8!");
            return null; // TODO
        }
    }

    /**
     * This method prepares a message to be sent by forming a byte array with an opcode,
     * client ID, payload and length.
     *
     * @param opCode The operation code.
     * @param payload The value to be sent.
     * @param clientID The id of the client who is sending the message.
     * @return A send ready byte array.
     */
    public static byte[] prepareMessage(byte opCode, int clientID, byte[] payload) {
        byte[] ops = {opCode};
        byte[] senderID = intToByteArray(clientID);
        byte[] metadata = appendByteArrays(ops, senderID);
        byte[] message = prependMessageLength(appendByteArrays(metadata,payload));
        return message;
    }

    /**
     * This method prepares a message that sends an operation code with the client id.
     *
     * @param opCode The operation code.
     * @param clientID The ID of the client that is sending the message.
     * @return A send ready byte array.
     */
    public static byte[] prepareOperationMessage(byte opCode, int clientID) {
        byte[] ops = {opCode};
        byte[] senderID = intToByteArray(clientID);
        byte[] message = prependMessageLength(appendByteArrays(ops, senderID));
        return message;
    }
}
