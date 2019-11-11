package ru.mirea;

import java.math.BigInteger;
import java.util.Arrays;
import ru.mirea.primes.ProbablePrime;

public class App {

    static int g = 5;
    static BigInteger p = ProbablePrime.generate(32).value();

    public static void main(String[] args) {
        //checkG(g, p);
        Person a = new Person(1);
        Person b = new Person(2);
        BigInteger A = a.countFunc(BigInteger.valueOf(g), p);
        BigInteger B = b.countFunc(BigInteger.valueOf(g), p);
        System.out.println(a.countFunc(B, p));
        System.out.println(b.countFunc(A, p));
    }

    private static void checkG(int g, int p) {
        double[] arr = new double[p - 1];
        for (int i = 2; i <= p; i++){
            arr[i - 2] = Math.pow((double) g,i)%p;
        }
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
