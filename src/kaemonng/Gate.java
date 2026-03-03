package kaemonng;

/**
 *
 * @author kaemonng
 */
public class Gate {
    private final int id; 
    
    public Gate(int id){
        this.id = id;
    }
    
    public int getId(){
        return id;
    }
    
    public void services(Plane plane, RefuelCar refuelCar) throws InterruptedException{
        System.out.println(plane.getName() + " dock at Gate " + id + ".");
        Thread disembark = new Thread(new Runnable(){
            @Override
            public void run(){
                System.out.println("   --> [" + plane.getName() + "] Passenger Disembarking...");
                try{
                    Thread.sleep(200);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        disembark.start();
        disembark.join();
        
        Thread cleanTeam = new Thread(new Runnable(){
            @Override
            public void run(){
                System.out.println("   --> [" + plane.getName() + "] Cleaning Team is working...");
                try{
                    Thread.sleep(400);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("   --> [" + plane.getName() + "] Cleaning Done...");
            }
        });
        
        Thread foodTeam = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("   --> [" + plane.getName() + "] Food Supply Team is working...");
                try {
                    Thread.sleep(400);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("   --> [" + plane.getName() + "] Food Supply Done...");
            }
        });
        
        cleanTeam.start();
        foodTeam.start();
        
        while (!refuelCar.requestRefuel(plane)){
            Thread.sleep(200);
        }
        synchronized(plane){
            plane.wait();
        }
        
        cleanTeam.join();
        foodTeam.join();
        
        Thread embark = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("   --> [" + plane.getName() + "] Passenger Embarking (" + plane.getPassengerNumber() + " pax)...");
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        embark.start();
        embark.join();
        
        System.out.println(plane.getName() + " All services at Gate " + id + " done... Ready to depart...");
    }
}
