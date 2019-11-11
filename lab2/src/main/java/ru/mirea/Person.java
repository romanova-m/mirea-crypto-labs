package ru.mirea;

import java.math.BigInteger;

public class Person {
    private int a;

    public Person(int a){
        this.a = a;
    }

    public BigInteger countFunc(BigInteger first, BigInteger second){
        return first.pow(a).mod(second);
    }
}
