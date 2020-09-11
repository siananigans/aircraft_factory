package factory;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.HashMap;

public class Robot implements Runnable {
    private ArrayList<String> parts;
    private Aircraft aircraft;
    private HashMap<String, Integer> partTimes = new HashMap<String, Integer>();
    private ConcurrentLinkedQueue<Aircraft> development_queue;
    private Semaphore Robots;

    public Robot(ArrayList<String> parts, Aircraft craft, Semaphore Robots, ConcurrentLinkedQueue<Aircraft> development_queue){
        this.development_queue = development_queue;
        this.parts = parts;
        this.aircraft = craft;
        this.Robots = Robots;
        // Adding to instalation time hashmap below
        this.partTimes.put("bus-wings", 1500);
        this.partTimes.put("b777-wings", 1400);
        this.partTimes.put("b2461-wings", 1700);
        this.partTimes.put("fuselage", 900);
        this.partTimes.put("bus-stabilizer", 1300);
        this.partTimes.put("245-stabilizer", 1530);
        this.partTimes.put("common-rudder", 1645);
        this.partTimes.put("max-rudder", 1200);
        this.partTimes.put("b777-engine", 3000);
        this.partTimes.put("b2461-engine", 3180);
        this.partTimes.put("ab-engine", 3699);
        this.partTimes.put("200x217-spoiler", 1599);
        this.partTimes.put("256x217-spoiler", 1478);
    }

    @Override
    public void run() {
        for(int i = 0; i<parts.size(); i++) {
            // Look up build times from Hashmap
            Integer buildTime = partTimes.get(parts.get(i));
            // Sleep for that interval
            try {
            Thread.sleep(buildTime);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            // Remove Part from Aircraft parts needed
            aircraft.PartAcquisition(parts.get(i));
        }
        // TODO push aircraft to queue
        development_queue.add(aircraft);
        Robots.release();
    }
}
