package srp;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Logger;

public class App {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        final int BIT_LEN = 1000;
        Logger logger = Logger.getLogger(App.class.getName());
        BigInteger n = BigInteger.probablePrime(BIT_LEN, new Random()).multiply(BigInteger.valueOf(2).add(BigInteger.ONE));
        BigInteger g = BigInteger.valueOf(2);
        BigInteger k = BigInteger.valueOf(3);

        String s = "some salt";
        String password = "pass";
        String login = "login";

        // REGISTER
        BigInteger x = new BigInteger(hash(s + password));
        BigInteger v = g.modPow(x, n);

        BigInteger a = new BigInteger(BIT_LEN, new Random());
        BigInteger A = g.modPow(a, n);
        if (A.mod(n).equals(BigInteger.ZERO)) {
            System.out.println("CONNECTION FAILED");
            return;
        }

        BigInteger b = new BigInteger(BIT_LEN, new Random());
        BigInteger B = k.multiply(v).add(g.modPow(b,n));
        if (B.mod(n).equals(BigInteger.ZERO)) {
            System.out.println("CONNECTION FAILED");
            return;
        }

        BigInteger u = new BigInteger(hash(A.toString() + B.toString()));
        //server key
        String KS = A.multiply(v.modPow(u, n)).modPow(b, n).toString();
        //String HKS = hash(KS);
        logger.info("Server: A = " + A + ", B = " + B + "; K = " + KS);

        // LOGIN
        x = new BigInteger(hash(s + "fake"));
        //x = new BigInteger(hash(s + "pass"));
        //client key
        String KC = (B.remainder(k.multiply(g.modPow(x,n)))).modPow(a.add(u.multiply(x)), n).toString();
        //String HKC = SRP.hash(KS);
        logger.info("Client: A = " + A + ", B = " + B + "; K = " + KC);

        String M = hash(A.toString() + B + KC);
        String M1 = hash(A.toString() + B + KS);
        if (!M.equals(M1)) {
            System.out.println("Server closed connection");
            return;
        }
        String R = hash(A + M + KS);
        String R1 = hash(A + M + KC);
        if (!R.equals(R1)) {
            System.out.println("Client closed connection");
            return;
        }
        logger.info("Client: M  = " + M);
        logger.info("Server: M1 = " + M1);
        logger.info("Client: R  = " + R );
        logger.info("Server: R1 = " + R1);
        System.out.println("Connection established");
    }

    static String hash(String originalString) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final byte[] hashbytes = digest.digest(
                originalString.getBytes(StandardCharsets.UTF_8));
        return String.valueOf(new BigInteger(hashbytes));
    }
}
