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
import java.util.ArrayList;

public class CarGroup extends Thread {
    Semaphore groupSema;
    ArrayList<Car> groupList;
    char group;
    
    public CarGroup (Semaphore groupSema, ArrayList<Car> groupList, char group) {
        this.groupSema = groupSema;
        this.groupList = groupList;
        this.group = group;
    }

    public void run() {
        try { 
            groupSema.acquire(); 
            for(Car c: groupList) {
                c.start();
            }
        } catch (InterruptedException e) { 
            e.printStackTrace(); 
        } finally {
            groupSema.release();
        }
    }
}