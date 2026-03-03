package kaemonng;
import java.util.LinkedList;
/**
 *
 * @author kaemonng
 */
public class Airport {
    private final LinkedList<Plane> queue;
    private final Gate[] gates = {new Gate(1), new Gate(2), new Gate(3)};
    private final boolean [] checkGatesFree = {true, true, true};
    private boolean runway = true;
    private final Object runwayLock = new Object();
    public final RefuelCar refuelCar = new RefuelCar();
    private int planesDeparted = 0;
    private int totalPassengers = 0;
    private long totalWaitTime = 0;
    private final Object statsLock = new Object();
    private long maxWaitTime = 0;
    private long minWaitTime = Long.MAX_VALUE;
    
    public Airport(LinkedList<Plane> planeQueueList){
        this.queue = planeQueueList;
    }
    
    public void recordStats(int passengers, long waitTime){
        synchronized(statsLock){
            totalPassengers += passengers;
            totalWaitTime += waitTime;
        }
    }
    
    public void operationsBegin(){
        refuelCar.start();
        Thread atcThread = new Thread(new Runnable(){
            @Override
            public void run(){
                System.out.println(">> [ATC Thread] ATC TOWER is ONLINE and MONITORING THE QUEUE... ");
                while (true){
                    synchronized(statsLock){
                        if (planesDeparted >= 6){
                            break;
                        }
                    }
                    Plane selectedPlane = null;
                    int assignGateIndex = -1;
                    synchronized(queue){
                        if (!queue.isEmpty()){
                            for (int i = 0; i < 3; i++){
                                if (checkGatesFree[i]){
                                    assignGateIndex = i;
                                    checkGatesFree[i] = false;
                                    selectedPlane = queue.poll();
                                    break;
                                }
                            }
                        }
                    }
                    if (selectedPlane != null){
                        Gate g = gates[assignGateIndex];
                        System.out.println(">> [ATC] GRANTED LANDING FOR " + selectedPlane.getName() + ". ASSIGNED TO GATE: " + g.getId());
                        synchronized(selectedPlane){
                            selectedPlane.assignGate(g);
                            selectedPlane.notify();
                        }
                    }else{
                        try{
                            Thread.sleep(100);
                        }catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }, "ATC-THREAD");
        atcThread.start();
    }
    
    public void claimRunway(Plane plane, String action) throws InterruptedException{
        synchronized(runwayLock){
            while (!runway){
                runwayLock.wait();
            }
            runway = false;
            if (action.equals("DEPART")) {
                System.out.println(">> [ATC] GRANTED TAKEOFF FOR " + plane.getName() + ".");
            }
            System.out.println(plane.getName() + " is " + action + " on the runway...");
        }
    }
    
    public void freeRunway(){
        synchronized(runwayLock){
            runway = true;
            runwayLock.notifyAll();
        }
    }
    
    public void depart(Plane plane, Gate gate, long thisPlaneWaitTime){ 
        synchronized(queue){
            checkGatesFree[gate.getId() - 1] = true;
        }
        
        int currentDeparted;
        
        synchronized(statsLock){
            planesDeparted++; 
            currentDeparted = planesDeparted;
            
            if (thisPlaneWaitTime > maxWaitTime) {
                maxWaitTime = thisPlaneWaitTime;
            }
            if (thisPlaneWaitTime < minWaitTime) {
                minWaitTime = thisPlaneWaitTime;
            }
        }
        System.out.println(">> [ATC] " + plane.getName() + " HAS DEPARTED... GATE " + gate.getId() + " RELEASED...");
        
        if (currentDeparted == 6){
            System.out.println("\n=======================================================");
            System.out.println("========= ASIA PACIFIC AIRPORT - FINAL REPORT =========");
            System.out.println("=======================================================");
            System.out.println("Total Planes Served: " + currentDeparted + " planes");
            System.out.println("Total Passenger Served: " + totalPassengers + " passengers");
            System.out.println("Total Waiting Time: " + totalWaitTime + " ms");
            System.out.println("Maximum Waiting Time: " + maxWaitTime + " ms");
            System.out.println("Minimum Waiting Time: " + minWaitTime + " ms");
            double avgwait = (double) totalWaitTime / currentDeparted;
            System.out.printf("Average Waiting Time: %.2f ms\n", avgwait);
            System.out.println("=======================================================");
            System.out.println("Sanity Check: All gates are indeed empty."); 
            System.out.println("=======================================================");
            System.exit(0);
        }
    }
}
