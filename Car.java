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
    protected Semaphore carSema;
    String[][] outputGeneration;

    // Constructor, it only requires the carNum integer
    public Car(int carNum, char direction, Semaphore carSema, String[][] outputGeneration) {
        this.carNum = carNum;
        this.direction = direction;
        this.carSema = carSema;
        this.outputGeneration = outputGeneration;
        //System.out.printf( "%2d%c is waiting.%n", carNum, direction);
    }

    public int getCarNum() {
        return carNum;
    }

    // Returns the direction the car is coming from
    public char getDirection() {
        return direction;
    }

    public void run() {
        try {
            boolean isDone = false;
            carSema.acquire();          // Aquire a carSema permit
            outputGeneration [carNum][0] = ( carNum + "" + direction + " is moving through the tunnel." );
            //System.out.printf( "%2d%c is moving through the tunnel.%n", carNum, direction );
            Thread.sleep(1000);         // Time it takes to drive through the tunnel
            outputGeneration [carNum][1] = ( carNum + "" + direction + " has exited the tunnel." );
            //System.out.printf( "%2d%c has exited the tunnel.%n", carNum, direction );
            
        } catch (InterruptedException e) { 
            e.printStackTrace(); 
        }
        carSema.release();          // Release the carSema permit
    }
}