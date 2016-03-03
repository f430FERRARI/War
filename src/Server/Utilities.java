package Server;

/**
 * Created by mlee43 on 2016-03-02.
 */
public class Utilities {

    public static byte[] appendByteArrays(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);

        return c;
    }
}
