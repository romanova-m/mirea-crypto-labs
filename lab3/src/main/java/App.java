import java.math.BigInteger;
import java.util.Random;

public class App {
    public static void main(String[] args) {
        BigInteger data = new BigInteger(5, new Random()).add(BigInteger.ONE);
        RSA rsa = new RSA();
        System.out.println("data: " + data + ", enc: " + rsa.enc(data) +
                ", dec: " + rsa.dec(rsa.enc(data)));
    }
}
