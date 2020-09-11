/*
PartsWorkshop Class initializes the parts inventory as a hashmap.
Individual thread update inventory as it gets low.
*/

package factory;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;
import java.lang.Thread;

public class PartsWorkshop implements Runnable{
    private ConcurrentHashMap<String, AtomicInteger> inventory;
    private Semaphore parts;

    public PartsWorkshop(ConcurrentHashMap<String, AtomicInteger> inventory, Semaphore parts){
        this.inventory = inventory;
        this.inventory.put("bus-wings", new AtomicInteger(12));
        this.inventory.put("b777-wings", new AtomicInteger(12));
        this.inventory.put("b2461-wings", new AtomicInteger(12));
        this.inventory.put("fuselage", new AtomicInteger(12)); //Needs a lot, three classes use part
        this.inventory.put("bus-stabilizer", new AtomicInteger(12));
        this.inventory.put("245-stabilizer", new AtomicInteger(12));
        this.inventory.put("common-rudder", new AtomicInteger(12));
        this.inventory.put("max-rudder", new AtomicInteger(12));
        this.inventory.put("b777-engine", new AtomicInteger(12));
        this.inventory.put("b2461-engine", new AtomicInteger(12));
        this.inventory.put("ab-engine", new AtomicInteger(12));
        this.inventory.put("200x217-spoiler", new AtomicInteger(12));
        this.inventory.put("256x217-spoiler", new AtomicInteger(12));
        this.parts = parts;

    }

    public void run(){
        while(true){
            // Sleep
            try {
            Thread.sleep(5000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            Random r = new Random();
            try {
                // Acquire semaphore for parts
                parts.acquire();
                for(String key : inventory.keySet()) {
                    AtomicInteger val = inventory.get(key);
                    // If value falls low
                    if (val.intValue() < 6) {
                        // Add random from 0-12
                        val.addAndGet(r.nextInt(12));
                        inventory.put(key, val);
                    }
                }
                // Release semaphore
                parts.release();

            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

