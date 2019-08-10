/* 
    Robert De La Cruz II
    Begun on 8/8/2019
    Java Practice

    Problem statement:
        There is a one way tunnel that cars must pass through from both directions.
        Only cars from one direction of travel can be moving through the tunnel at any given time.
        Each car is a program thread waiting to be processed.
        A single direction should not limited to sending just one car through the tunnel.
        The order of cars passing through the tunnel from one direction does not need to be maintained

    The language of using cars and tunnels is not to be taken literally, the key point here is 
        that the critical region (tunnel) will not accept/process threads from more than one direction at the time
        and that the critical region will not take threads from the other side before all the ones in it have finished.
*/

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;

public class CarGroup extends Thread {
    Semaphore groupSema;            // Belongs to the calling class, actually a Mutex (single-key semaphore)
    Semaphore carSema;              // Belongs to the calling class, this is the semaphore for individual cars
    ArrayList<Car> groupList;       // Belongs to the calling class, ArrayList of Cars that makes use of objects passed as references
    AtomicInteger carNum;           // Belongs to the calling class, a thread-safe counter
    char direction;                 // Provided by the calling class, a character denoting direction of travel
    String[][] outputGeneration;    // This is used to format the console output.  It is NOT synchronized
    
    public CarGroup (Semaphore groupSema, Semaphore carSema, ArrayList<Car> groupList, AtomicInteger carNum, char direction, String[][] outputGeneration ) {
        this.groupSema = groupSema;
        this.carSema = carSema;
        this.groupList = groupList;
        this.carNum = carNum;
        this.direction = direction;
        this.outputGeneration = outputGeneration;
    }

    public void createCar (int carNum) {    // Called by the class that created this thread
        groupList.add( new Car(carNum, direction, carSema, outputGeneration) );
    }
    
    public void run() {
        try { 
            groupSema.acquire();            // Aquire the groupSema lock
            while ( !groupList.isEmpty() ) {
                Thread.sleep(250);
                groupList.remove(0).start();
            }
        } catch (InterruptedException e) { 
            e.printStackTrace(); 
        }
        groupSema.release();            // Release the groupSema lock
    } // End of run method

} // End of class