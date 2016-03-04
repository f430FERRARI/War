package Client;

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
    private static byte[] prependMessageLength(byte[] message) {
        int messageLength = message.length;
        byte[] outputMessage = new byte[messageLength + 1];

        outputMessage[0] = (byte) messageLength;

        for (int i = 0, j = 1; i < messageLength; i++, j++) {
            outputMessage[j] = message[i];
        }

        return outputMessage;
    }
}
