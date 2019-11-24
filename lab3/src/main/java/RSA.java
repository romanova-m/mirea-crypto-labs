import java.math.BigInteger;
import java.util.logging.Logger;

import ru.mirea.primes.ProbablePrime;

public class RSA {

    BigInteger e; // Public exponent
    BigInteger d; //Private exponent
    final int PRIME_LEN = 32;
    final BigInteger MIN_VAL_FOR_COPRIME = BigInteger.valueOf(1024);
    BigInteger n;
    private Logger logger = Logger.getLogger(RSA.class.getName());

    RSA(){
        BigInteger p =  ProbablePrime.generate(PRIME_LEN).value();
        BigInteger q =  ProbablePrime.generate(PRIME_LEN).value();
        n = p.multiply(q);
        BigInteger eulerFunc = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        e = coprime(eulerFunc, MIN_VAL_FOR_COPRIME);
        d = mmInverse(e, eulerFunc);
        logger.info("RSA init ended with params: " +
            "n = " + n + ", e = " + e + ", d = " + d);
    }

    // d⋅e ≡ 1 (mod λ(n))
    // mmInverse * a = 1 (mod b)
    private BigInteger mmInverse(BigInteger a, BigInteger b) {
        BigInteger x = BigInteger.ZERO;
        BigInteger lastx = BigInteger.ONE;
        BigInteger y = BigInteger.ONE;
        BigInteger lasty = BigInteger.ZERO;
        while (!b.equals(BigInteger.ZERO))
        {
            BigInteger[] quotientAndRemainder = a.divideAndRemainder(b);
            BigInteger quotient = quotientAndRemainder[0];
            a = b;
            b = quotientAndRemainder[1];
            BigInteger temp = x;
            x = lastx.subtract(quotient.multiply(x));
            lastx = temp;
            temp = y;
            y = lasty.subtract(quotient.multiply(y));
            lasty = temp;
        }
        return a;
    }

    private BigInteger coprime(BigInteger toNumber, BigInteger minValue) {
        while (!minValue.gcd(toNumber).equals(BigInteger.ONE)) {
            minValue = minValue.add(BigInteger.ONE);
        }
        return minValue;
    }

    BigInteger enc(BigInteger data){
        return data.modPow(e,n);
    }

    BigInteger dec(BigInteger data){
        return data.modPow(d,n);
    }

}
