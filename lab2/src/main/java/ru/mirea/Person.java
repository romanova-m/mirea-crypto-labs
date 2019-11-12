package ru.mirea;

import java.math.BigInteger;
import java.util.Random;
import java.util.logging.Logger;

public class Person {

    private int a; // Private key
    Logger logger = Logger.getLogger(Person.class.getName());

    public Person(){
        this.a = new Random().nextInt(Integer.MAX_VALUE >> (Integer.SIZE/2));
        logger.info("Private key = " + a);
    }

    public BigInteger countFunc(BigInteger first, BigInteger second){
        return first.pow(a).mod(second);
    }
}
