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

public class CriticalRegion {
    static Semaphore groupSema = new Semaphore(1);                  // Allows only 1 side to activate cars at the time
    static Semaphore carSema = new Semaphore(3);                    // Allows 3 cars to be active in the tunnel at the time
    private static final int MAX_NUM_CARS = 10;                     // Max number of cars tho simulate
    private static AtomicInteger carNum = new AtomicInteger(0);     // Threadsafe number for thread identification
    private static ArrayList<Car> left = new ArrayList<Car>();
    private static ArrayList<Car> right = new ArrayList<Car>();
    private static ArrayList<Car> tunnel = new ArrayList<Car>();
    
    public static void main(String[] args) {
        Semaphore whosTurnIsItAnyway = new Semaphore(1);
        while (carNum.get() < MAX_NUM_CARS) {
            if (Math.random() < .5 ) {
                left.add( new Car(carNum.getAndIncrement(), 'L', carSema) );
            }
            else {
                right.add( new Car(carNum.getAndIncrement(), 'R', carSema) );
            }
        }
        System.out.println("");
        //System.out.println(left.size() ); System.out.println(right.size() );

        for (Car car: left) {
            System.out.printf("|%2d%c", car.getCarNum(), car.getDirection() );
        }
        System.out.println("|");
        for (Car car: right) {
            System.out.printf("|%2d%c", car.getCarNum(), car.getDirection() );
        }
        System.out.println("|");
        System.out.println("");
        
        CarGroup leftGroup = new CarGroup(groupSema, left, 'L');
        CarGroup rightGroup = new CarGroup(groupSema, right, 'R');
        leftGroup.start();
        try { leftGroup.join(); } catch (InterruptedException e) { e.printStackTrace(); }
        rightGroup.start();
        try { rightGroup.join(); } catch (InterruptedException e) { e.printStackTrace(); }
    }
}
