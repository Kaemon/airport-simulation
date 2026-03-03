package kaemonng;
import java.util.Random;
/**
 *
 * @author kaemonng
 */
public class Plane extends Thread{
    private final Airport airport;
    private final boolean isEmergency;
    private final int passengerNumber;
    private Gate assignGate = null;
    private long startTime;
    
    public Plane(int id, Airport airport){
        this.airport = airport;
        if (id == 6){
            this.setName("Plane-" + id + " [EMERGENCY]");
            this.isEmergency = true;
        }else{
            this.setName("Plane-" + id);
            this.isEmergency = false;
        }
        this.passengerNumber = new Random().nextInt(50)+1;
    }
    
    public boolean isEmergency(){
        return isEmergency;
    }
    
    public int getPassengerNumber(){
        return passengerNumber;
    }
    
    public void assignGate(Gate g){
        this.assignGate = g;
    }
    
    @Override
    public void run(){
        try{
            System.out.println("   --> [" + this.getName() + "] RADIOING ATC: REQUESTING PERMISSION TO LAND...");
            startTime = System.currentTimeMillis();
            synchronized(this){
                while (assignGate == null){
                    try{
                        wait();
                    }catch (InterruptedException e) {
                    }
                }
            }
            long waitTime = System.currentTimeMillis() - startTime;
            airport.recordStats(passengerNumber, waitTime);
            airport.claimRunway(this, "LANDING");
            Thread.sleep(500);
            airport.freeRunway();
            System.out.println(this.getName() + " Coasting to Gate " + assignGate.getId() + "...");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            assignGate.services(this, airport.refuelCar);
            System.out.println(this.getName() + " Coasting to the runway for takeoff...");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            airport.claimRunway(this, "DEPART");
            Thread.sleep(500);
            airport.freeRunway();
            airport.depart(this, assignGate,waitTime);
        }catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
