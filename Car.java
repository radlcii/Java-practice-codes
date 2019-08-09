/* 
    Robert De La Cruz II
    8/8/2019
    Java Practice

    Problem statement:
        There is a one way tunnel that cars must pass through from both directions.
        Only cars from one direction of travel can be moving through the tunnel at any given time.
        Each car is a program thread waiting to be processed.
        A single direction should not limited to sending just one car through the tunnel.
        The order of cars passing through the tunnel from one direction does not need to be maintained
*/
import java.util.concurrent.Semaphore;

public class Car extends Thread {
    protected char direction;           // Set at random, determines which side of the tunnel the car starts on.
    protected int carNum;               // Set by operator program, for user examination.  Not needed for function.
    protected Semaphore sideSema;

    // Constructor, it only requires the carNum integer
    public Car(int carNum, char direction, Semaphore sideSema) {
        this.carNum = carNum;
        this.direction = direction;
        this.sideSema = sideSema;
        System.out.printf( "%2d%c has arrived and is waiting to move through the tunnel.%n", carNum, direction);
    }

    public int getCarNum() {
        return carNum;
    }

    // Returns the direction the car is coming from
    public char getDirection() {
        return direction;
    }

    public void run() {
        try { sideSema.acquire(); 
            System.out.printf( "%2d%c is moving through the tunnel.%n", carNum, direction);
            try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }         // Time it takes to drive through the tunnel
            System.out.printf( "%2d%c has exited the tunnel.%n", carNum, direction);
            Thread.sleep(1000);
        } catch (InterruptedException e) { 
            e.printStackTrace(); 
        } finally {
            sideSema.release();
        }
    }
}