package kaemonng;

import java.util.LinkedList;

/**
 *
 * @author kaemonng
 */
public class Simulator {

    /**
     * @param args the command line arguments
     */
    public final static LinkedList<Plane> planeQueueList = new LinkedList<>();
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("\n=======================================================");
        System.out.println("======= ASIA PACIFIC AIRPORT - SIMULATION STARTS ======");
        System.out.println("=======================================================");
        Airport airport = new Airport(planeQueueList);
        airport.operationsBegin();
        for (int i = 1; i <= 6; i++){
            Plane plane = new Plane(i, airport);
            synchronized(planeQueueList){
                if (plane.isEmergency()){
                    System.out.println(">> [SIMULATOR] " + plane.getName() + " is EMERGENCY!!! PLACED AT THE FRONT OF THE QUEUE...");
                    planeQueueList.addFirst(plane);
                }else{
                    System.out.println(">> [SIMULATOR] " + plane.getName() + " ARRIVED AND JOIN THE QUEUE...");
                    planeQueueList.addLast(plane);
                }
            }
            plane.start();
            try{
                Thread.sleep(150);
            }catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
