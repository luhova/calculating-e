package CalculateE;

import java.io.*;
import java.math.*;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

public class WorkerThread implements Runnable {
    private int nth_member;
    private boolean quiet;
    private BigDecimal result;
    private ConcurrentHashMap<BigDecimal, BigDecimal> factorials;

    public WorkerThread(int nth_member, ConcurrentHashMap<BigDecimal, BigDecimal> factorials, boolean quiet) {
        this.nth_member = nth_member;
        this.factorials = factorials;
        this.quiet = quiet;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void run() {
        long start = Calendar.getInstance().getTimeInMillis();
        if(!quiet){
            System.out.println(Thread.currentThread().getName() + " started calculating member number " + nth_member );
        }
        result = calc_nth_member(nth_member);
        long end = Calendar.getInstance().getTimeInMillis();
        if(!quiet){
            System.out.println(Thread.currentThread().getName() + " stopped.");
            System.out.println(Thread.currentThread().getName() + " Execution time was (millis): " + (end - start) + " ms.");
            System.out.println(Thread.currentThread().getName() + " Result from thread is: " + result);
        }
    }

    private BigDecimal factorial(BigDecimal number){
        BigDecimal fact;
        if(factorials.containsKey(number))
            fact = factorials.get(number);
        else{
            if (number.compareTo(BigDecimal.ZERO) == 0 || number.compareTo(BigDecimal.ONE) == 0)
                fact =  BigDecimal.ONE;
            else
                fact = number.multiply(factorial(number.subtract(BigDecimal.ONE)));
        }
        factorials.putIfAbsent(number, fact);
        return fact;
    }

    private BigDecimal calc_nth_member(int k){
        BigDecimal num = BigDecimal.valueOf(Math.pow(3*k, 2) + 1);
        BigDecimal denom = factorial(BigDecimal.valueOf(3*k));
        return num.divide(denom, 15, RoundingMode.CEILING);
    }
}
