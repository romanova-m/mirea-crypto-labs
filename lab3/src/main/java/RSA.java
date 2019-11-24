import java.math.BigInteger;
import java.util.logging.Logger;

import ru.mirea.primes.ProbablePrime;

public class RSA {

    BigInteger e; // Public exponent
    BigInteger d; //Private exponent
    final int PRIME_LEN = 32;
    final BigInteger MIN_VAL_FOR_COPRIME = BigInteger.valueOf(2048);
    BigInteger n;
    private Logger logger = Logger.getLogger(RSA.class.getName());

    RSA(){
        BigInteger p =  ProbablePrime.generate(PRIME_LEN).value();
        BigInteger q =  ProbablePrime.generate(PRIME_LEN).value();
        n = p.multiply(q);
        BigInteger eulerFunc = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        e = coprime(eulerFunc, MIN_VAL_FOR_COPRIME);
        d = inverse(e, eulerFunc);
        logger.info("RSA init ended with params: " +
            "n = " + n + ", e = " + e + ", d = " + d + ", euler func: " + eulerFunc);
    }

    // d⋅e ≡ 1 (mod λ(n))
    // inverse * a ≡ 1 (mod n)
    private BigInteger inverse(BigInteger a, BigInteger n) {
        BigInteger t = BigInteger.ZERO;
        BigInteger r = new BigInteger(n.toString());
        BigInteger newt = BigInteger.ONE;
        BigInteger newr = new BigInteger(a.toString());
        while (!newr.equals(BigInteger.ZERO))
        {
            BigInteger quotient = r.divide(newr);
            BigInteger temp = t;
            t = newt;
            newt = temp.subtract(quotient.multiply(newt));
            temp = r;
            r = newr;
            newr = temp.subtract(quotient.multiply(newr));
        }
        if (r.compareTo(BigInteger.ONE) > 0) {
            logger.severe(a + " IS NOT INVERTIBLE");
            return null;
        }
        if (t.compareTo(BigInteger.ZERO) <= 0)
            t = t.add(n);
        return t;
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
