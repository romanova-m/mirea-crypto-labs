package ru.mirea;

import ru.mirea.primes.ProbablePrime;

public class App {
    public static void main(String[] args) {
        ProbablePrime prime = ProbablePrime.generate(50);
        System.out.println("len: " + prime.bitLength() + "\tvalue: " + prime.value());
    }
}
