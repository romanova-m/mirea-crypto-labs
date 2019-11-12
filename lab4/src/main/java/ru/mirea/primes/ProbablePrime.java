package ru.mirea.primes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Class for making probable primes with set bitLength.
 * Algorithm of making:
 * * Make random {@code BigInteger} with bitLength
 * * Check division by small primes up to {@code SMALL_PRIMES_MAX}
 * * Perform check by Rabin Miller tests {@code RABIN_MILLER_TESTS_NUM} times
 * * If checks fail generate another candidate
 *
 * @see BigInteger
 */
public class ProbablePrime {

    private static final int RABIN_MILLER_TESTS_NUM = 5;
    private static final int SMALL_PRIMES_MAX = 2000;
    // for debugging
    private static final Logger logger = Logger.getLogger(ProbablePrime.class.getName());
    private BigInteger probablePrime;
    private int bitLength;
    private ArrayList<Integer> smallPrimes = generateSmallPrimes();


    private ProbablePrime(int bitLength) {
        this.bitLength = bitLength;
        do {
            probablePrime = rabinMillerTests(checkPrimesDiv(modify(generateRandom(bitLength))));
        } while (probablePrime == null);
        logger.info("PROBABLE PRIME = " + probablePrime);
    }


    /**
     * Generate probable prime with chosen bitLength
     *
     * @param bitLength length of prime, > 0
     * @return generated number
     */
    public static ProbablePrime generate(int bitLength) {
        return new ProbablePrime(bitLength);
    }


    /**
     * Get prime value
     * @return prime as BigInteger, > 0
     */
    public BigInteger value() {
        return probablePrime;
    }


    /**
     * Get bitLength of prime
     * @return bitLength
     */
    public int bitLength() {
        return bitLength;
    }


    /* Прогоняем алгоритм эратосфена на числах от 2 до SMALL_PRIMES_MAX */
    private ArrayList<Integer> generateSmallPrimes() {
        return filterNonPrimes(generateSequence(2, SMALL_PRIMES_MAX));
    }


    private ArrayList<Integer> filterNonPrimes(ArrayList<Integer> sequence) {
        int currIndex = 0;
        Integer current = sequence.get(currIndex);
        Integer maxElem = SMALL_PRIMES_MAX;
        // delete numbers: number % current == 0
        while (current * current < maxElem) {
            Integer toDel = current * current;
            while (toDel < maxElem) {
                sequence.remove(toDel);
                toDel += current;
            }
            if (currIndex + 1 < sequence.size()) {
                currIndex++;
                current = sequence.get(currIndex);
            } else break;
        }
        return sequence;
    }

    private ArrayList<Integer> generateSequence(int startNum, int finishNum) {
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = startNum; i < finishNum; i++) {
            array.add(i);
        }
        return array;
    }

    // If passes test returns probablePrime, else null
    private BigInteger rabinMillerTests(BigInteger candidate) {
        if (candidate == null) return null;
        if (candidate.compareTo(BigInteger.valueOf(2)) <= 0) return candidate;

        // count s and d: (pow(2,s)*d) == (candidate - 1)
        int s = countS(candidate);
        BigInteger d = countD(candidate, s);
        logger.info(candidate + " s " + s + " d " + d);

        for (int i = 0; i < RABIN_MILLER_TESTS_NUM; i++) {
            if (!rabinMillerTest(candidate, s, d, countN(candidate)))
                return null;
        }
        return candidate;
    }

    private BigInteger countN(BigInteger candidate) {
        return new BigInteger(candidate.bitCount() - 1, new Random()).add(BigInteger.valueOf(1));
    }

    // returns true if passes test
    private boolean rabinMillerTest(BigInteger n, int s, BigInteger d, BigInteger a) {
        BigInteger x = a.modPow(d, n);
        if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE)))
            return true;
        for (int i = 0; i < s - 1; i++) {
            x = x.modPow(BigInteger.valueOf(2), n);
            if (x.equals(BigInteger.ONE)) return false;
            if (x.equals(n.subtract(BigInteger.ONE))) return true;
        }
        return false;
    }

    private int countS(BigInteger candidate) {
        int s = 0;
        for (BigInteger num = candidate.subtract(BigInteger.ONE);
             num.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO);
             num = num.divide(BigInteger.valueOf(2))) {
            s++;
        }
        return s;
    }

    private BigInteger countD(BigInteger candidate, int s) {
        return candidate.subtract(BigInteger.ONE).divide(
                BigInteger.valueOf((long) Math.pow(2, s)));
    }

    // If passes test returns probablePrime, else null
    private BigInteger checkPrimesDiv(BigInteger number) {
        for (Integer prime : smallPrimes) {
            if (BigInteger.valueOf(prime).compareTo(number) >= 0) return number;
            if (number.mod(BigInteger.valueOf(prime)).equals(BigInteger.ZERO)) return null;
        }
        return number;
    }

    /*
     * Increase probability of being prime.
     * First and last bit of probablePrime are set to 1.
     */
    private BigInteger modify(BigInteger number) {
        return number.setBit(0).setBit(bitLength - 1);
    }

    private BigInteger generateRandom(int bits) {
        return new BigInteger(bits, new Random());
    }
}
