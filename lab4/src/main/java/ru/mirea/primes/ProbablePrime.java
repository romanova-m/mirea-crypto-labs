package ru.mirea.primes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class ProbablePrime {

    private static final int RM_TESTS_NUM = 5;
    private static final int SMALL_PRIMES_MAX = 2000;

    private static final Logger logger = Logger.getLogger(ProbablePrime.class.getName());
    private BigInteger number;
    private int bits;
    private ArrayList<Integer> smallPrimes = generateArraySmall();

    /**
     * Generate probable prime number with known bits length
     *
     * @param bits number of bits in ProbablePrime, > 1
     * @return generated prime
     */
    public static ProbablePrime generate(int bits) {
        return new ProbablePrime(bits);
    }

    /**
     * Get value of prime
     * @return prime as BigInteger
     */
    public BigInteger value() {
        return number;
    }

    /**
     * Get bit length of prime
     * @return integer value of bit length
     */
    public int bitLength() {
        return bits;
    }


    private ProbablePrime(int bits) {
        this.bits = bits;
        do {
            number = rabinMillerTests(checkDiv(modify(generateRandom(bits))));
        } while (number == null);
        logger.info("PROBABLE PRIME = " + number);
    }

    /* Генерируем массив из 2000 простых с помощью решета Эратосфена */
    private ArrayList<Integer> generateArraySmall() {
        return filterArraySmall(mkNumArray(2, SMALL_PRIMES_MAX));
    }

    private ArrayList<Integer> filterArraySmall(ArrayList<Integer> sequence) { // CHECK
        int currIndex = 0;
        Integer current = sequence.get(currIndex);
        Integer maxElem = SMALL_PRIMES_MAX;

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

    private ArrayList<Integer> mkNumArray(int startNum, int finishNum) {
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = startNum; i < finishNum; i++) {
            array.add(i);
        }
        return array;
    }

    // If passes test returns number, else null
    private BigInteger rabinMillerTests(BigInteger candidate) {
        if (candidate == null) return null;
        if (candidate.compareTo(BigInteger.valueOf(2)) <= 0) return candidate;

        int s = countS(candidate);
        BigInteger d = countD(candidate, s);
        logger.info(candidate + " s " + s + " d " + d);

        for (int i = 0; i < RM_TESTS_NUM; i++) {
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
            x = x.modPow(BigInteger.valueOf(2),n);
            if (x.equals(BigInteger.ONE)) return false;
            if (x.equals(n.subtract(BigInteger.ONE))) return true;
        }
        return false;
    }

    private int countS(BigInteger candidate) {
        int s = 0;
        for (BigInteger num = candidate.subtract(BigInteger.ONE);
             num.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO);
             num = num.divide(BigInteger.valueOf(2))){
            s++;
        }
        return s;
    }

    private BigInteger countD(BigInteger candidate, int s) {
        return candidate.subtract(BigInteger.ONE).divide(
                BigInteger.valueOf((long)Math.pow(2, s)));
    }

    // If passes test returns number, else null
    private BigInteger checkDiv(BigInteger number) {
        boolean isDiv = false;
        for (Integer prime : smallPrimes) {
            if (BigInteger.valueOf(prime).compareTo(number) > 0) break;
            if (BigInteger.valueOf(prime).equals(number)) continue;
            if (number.mod(BigInteger.valueOf(prime)).equals(BigInteger.ZERO)) {
                isDiv = true;
            }
        }
        return isDiv ? null : number;
    }

    /*
     * Increase probability of being prime.
     * First and last bit of number are set to 1.
     */
    private BigInteger modify(BigInteger number) {
        return number.setBit(0).setBit(bits - 1);
    }

    private BigInteger generateRandom(int bits) {
        return new BigInteger(bits, new Random());
    }
}
