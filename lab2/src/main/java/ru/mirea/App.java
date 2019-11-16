package ru.mirea;

import ru.mirea.primes.ProbablePrime;

import java.math.BigInteger;

public class App {

    static BigInteger p = ProbablePrime.generate(16).value(); // 16 >= bitsLen > 1
    static BigInteger g = BigInteger.valueOf(ProbablePrime.primitiveRoot(p));


    public static void main(String[] args) {
        System.out.println("g = " + g + " p = " + p);
        Person personA = new Person();
        Person personB = new Person();
        BigInteger A = personA.countFunc(g, p); // public key
        BigInteger B = personB.countFunc(g, p);
        System.out.println("A = " + A + " B = " + B);
        System.out.println(personA.countFunc(B, p)); // Общий закрытый ключ
        System.out.println(personB.countFunc(A, p));
    }
}
