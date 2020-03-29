/*Kipras Sivickas INFO 4
* A simple implementation of the Barrier in parallel programming.
* It is about thread work. Barrier makes sure that all of the threads do their assigned job before the next task.
* So one thread can do further tasks if other threads did not finish their work.
* */
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {

    private static int numberOfThreads;                 //number of threads
    static Scanner scan = new Scanner(System.in);
    public static List<Thread> listOfThreads;
    public static Barrier barrier;
    public static void main(String[] args) {
        listOfThreads = new ArrayList<Thread>();

        System.out.println("Enter the number of threads \n");

        numberOfThreads = scan.nextInt();              //user input of thread amount
        barrier = new Barrier(numberOfThreads);

        for(int i = 0; i < numberOfThreads;i++){      //loop for creating threads and putting them in an array list

            Thread thread = new Thread(new ThreadTask(barrier),Integer.toString(i+1)+" THREAD");
            listOfThreads.add(thread);
        }
        for(int i=0;i<numberOfThreads;i++){           //loop to start the threads
            listOfThreads.get(i).start();
        }
    }
}

class Barrier {
    private int numberOfThreads;                      //total amount of threads that will be used in the program
    private int threadsToWaitFor;                     // that amount of threads that did not reach a certain point so other threads have to wait

    public Barrier(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        this.threadsToWaitFor = numberOfThreads;
    }

    public synchronized void waitBarrier() throws InterruptedException {
        threadsToWaitFor--;                           //threadsToWaitFor is the amount of threads that still did not use waitBarrier() method
        if (threadsToWaitFor > 0) {
            this.wait();                              //while not all threads used the waitBarrier() method, the ones that used it, had to wait
        }
        else {
            threadsToWaitFor = numberOfThreads;       //when all threads use the waitBarrier() method, threadsToWaitFor value becomes the same
            notifyAll();                              //all threads ar notified of the fact that all of them used waitBarrier()
        }
    }
}

class ThreadTask implements Runnable {

    private Barrier barrier;
    private static int timeToWait;

    public ThreadTask(Barrier barrier) {
        this.barrier = barrier;
        timeToWait = 1000;                            //limit of time for the thread, to make sure that none of the threads did not go through the barrier
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 6; i++) {            //every thread has to make a set number of iterations and after every iteration wait for
                Thread.sleep(timeToWait);            //the other threads

                System.out.printf("%s done %d iteration\n\n", Thread.currentThread().getName(), i+1);

                this.barrier.waitBarrier();          // thread after doing it's job waits for other threads to finish before starting the next job
            }
            System.out.println(Thread.currentThread().getName() + " FINISHED WORK");    //shows when a thread finishes it's work
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}