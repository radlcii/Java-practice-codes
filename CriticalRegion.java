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

    The language of using cars and tunnels is not to be taken too literally, the key point here is 
        that the critical region (tunnel) will not accept/process threads from more than one direction at the time
        and that the critical region will not take threads from the other side before all the ones in it have finished.
*/

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;

public class CriticalRegion {
    private static final int MAX_NUM_CARS = 11;                     // Max number of cars tho simulate
    static Semaphore groupSema = new Semaphore(1);                  // Allows only 1 side to activate cars at the time
    
    private static final int MAX_RUNNING_CARS = 3;                  // Determines the number of keys available to the carSema semaphore
    static Semaphore carSema = new Semaphore(MAX_RUNNING_CARS);     // Allows MAX_RUNNING_CARS cars to be active at one time
    
    private static AtomicInteger carNum = new AtomicInteger(0);     // Threadsafe number for thread identification
    private static ArrayList<Car> left = new ArrayList<Car>();      // ArrayList to hold cars, passed by reference to the left group
    private static ArrayList<Car> right = new ArrayList<Car>();     // ArrayList to hold cars, passed by reference to the right group

    static String[][] outputGeneration = new String[MAX_NUM_CARS][2];   // This is used to format the console output.  It is NOT synchronized
    
    public static void main(String[] args) {

        CarGroup leftGroup  = new CarGroup(groupSema, carSema, left, carNum, 'L', outputGeneration );  // Each group is a sub-process of this class
        CarGroup rightGroup = new CarGroup(groupSema, carSema, right, carNum, 'R', outputGeneration );  
        
        while (carNum.get() < MAX_NUM_CARS) {                               // RNG determines the distribution of cars
            if (Math.random() < .5 )
                leftGroup.createCar( carNum.getAndIncrement() );
            else
                rightGroup.createCar( carNum.getAndIncrement() );
        }
        System.out.println("");         // Output formatting

        for (Car car: left) {
            System.out.printf("|%2d%c ", car.getCarNum(), car.getDirection() );     // Prints the arraylist of left-bound cars for verification
        }
        System.out.println("|");        // Output formatting
        for (Car car: right) {
            System.out.printf("|%2d%c ", car.getCarNum(), car.getDirection() );     // Prints the arraylist of right-bound cars for verification
        }
        System.out.printf("|%n%n");     // Output formatting

        try {
            leftGroup.start();          // Starts the left-bound traffic
            rightGroup.start();         // Starts the right-bound traffic
            
            leftGroup.join();           // Calling Thread.join here forces the leftGroup threads to finish before rightGroup can go
            rightGroup.join();          // Calling Thread.join here forces the rightGroup threads to finish before leftGroup can go
        } catch (InterruptedException e) { 
            e.printStackTrace(); 
        }

        for(int i=0; i<outputGeneration.length; i++) {
            System.out.println(outputGeneration[i][0] + " --> " + outputGeneration[i][1] );
        }
        
    }
}
