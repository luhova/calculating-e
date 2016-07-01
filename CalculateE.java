package CalculateE;

import java.io.*;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.lang.*;
import java.math.*;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ConcurrentHashMap;

public class CalculateE {
    static int n_members;
    static int n_threads;
    static String fileName = "result.txt";
    static boolean quiet = false;
    static BigDecimal final_result = BigDecimal.valueOf(0);

    public static void main(String[] args) {
        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("-p")) {
                n_members = new Integer(args[i+1]);
                if(n_members <= 0) {
                    System.out.println("Number of members must be bigger than 0.");
                    return;
                }
            }
            if(args[i].equals("-t")) {
                n_threads = new Integer(args[i+1]);
                if(n_threads <= 0) {
                    System.out.println("Number of threads must be bigger than 0.");
                    return;
                    }
            }
            if(args[i].equals("-o")) {
                fileName = args[i+1];
            }
            if(args[i].equals("-q")) {
                quiet = true;
            }
        }
        long start = Calendar.getInstance().getTimeInMillis();;

        WorkerThread[] workers = new WorkerThread[n_members];
        ExecutorService executor = Executors.newFixedThreadPool(n_threads);
        ConcurrentHashMap<BigDecimal, BigDecimal> factorials = new ConcurrentHashMap<BigDecimal, BigDecimal>();

        for(int i = 0; i < n_members; i++) {
            WorkerThread worker = new WorkerThread(i, factorials, quiet);
            executor.execute(worker);
            workers[i] = worker;
        }

        executor.shutdown();
        while(!executor.isTerminated()) { }

        long end =  Calendar.getInstance().getTimeInMillis();;
        for(int i = 0; i < workers.length; i++){
            final_result = final_result.add(workers[i].getResult());
        }

        try(PrintWriter out = new PrintWriter(fileName)) {
            out.println(final_result);
        }catch(FileNotFoundException e) {
            System.out.println("File not found!");
        }

        if(!quiet){
            System.out.println("final_result: " + final_result);
        }
        System.out.printf("Task took " + (end - start) + " ms to complete.");
    }
}
