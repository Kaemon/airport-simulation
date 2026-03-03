package kaemonng;

/**
 *
 * @author kaemonng
 */
public class RefuelCar extends Thread{
    private boolean isBusy = false;
    private Plane currentPlane = null;
    
    public synchronized boolean requestRefuel(Plane plane){
        System.out.println("   --> [" + plane.getName() + "] REQUESTING Refuel Car...");
        if (isBusy){
          System.out.println(">> [REFUEL CAR MSG] -> " + plane.getName() + " REQUEST REJECTED...CAR is BUSY...");
          return false;
        }else{
            isBusy = true;
            currentPlane = plane;
            System.out.println(">> [REFUEL CAR MSG] -> " + plane.getName() + " REQUEST ACCEPTED...DRIVING TO GATE...");
            notify();
            return true;
        }
    }
    
    @Override
    public void run(){
        while (true){
            Plane p;
            synchronized(this){
                while (currentPlane == null){
                    try{
                        wait();
                    }catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                p = currentPlane;
            }
            System.out.println("   --> RefuelCar is refueling " + p.getName() + "...");
            try {
                Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("   --> Refuel finished refueling " + p.getName() + ".");
            
            synchronized(p){
                p.notify();
            }
            
            synchronized(this){
                currentPlane = null;
                isBusy = false;
            }
        }
    }
    
}
