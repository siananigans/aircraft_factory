package factory;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.System;

public class FactoryStartUp {
    public static void main(String[] args){
        Long start = System.currentTimeMillis();

        ConcurrentHashMap<String, AtomicInteger> parts =
                new ConcurrentHashMap<>(); // acts as a database for parts. Critical section

        ConcurrentLinkedQueue<Aircraft> development_queue =
                new ConcurrentLinkedQueue<>();

        ConcurrentLinkedQueue<Aircraft> completed_queue =
                new ConcurrentLinkedQueue<>();

        Semaphore parts_access = new Semaphore(1);

        ConcurrentLinkedQueue<String> output_queue =
                new ConcurrentLinkedQueue<>();



        // Parts workstation setup
        ExecutorService parts_station = Executors.newSingleThreadExecutor();
        parts_station.execute(new PartsWorkshop(parts, parts_access));

        // Line manager setup
        ExecutorService line_manager = Executors.newSingleThreadExecutor();
        line_manager.execute(new LineManager(parts, development_queue, parts_access, completed_queue, output_queue, start));

        // Moitor setup
        ExecutorService parts_monitor = Executors.newSingleThreadExecutor();
        parts_monitor.execute(new Monitor(parts));

        // Output Witer setup
        ExecutorService output_monitor = Executors.newSingleThreadExecutor();
        output_monitor.execute(new OutputWriter(output_queue));

        Long id = 1000L; 

        while(true){
            try {
                Thread.sleep(ThreadLocalRandom.current()
                        .nextInt(300, 1500 + 300));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Randomly pick aircraft class and push to development queue
            int order = new Random().nextInt(3);
            if (order == 0){
                development_queue.add(new Boeing2461(id));
            }
            else if(order ==1){
                development_queue.add(new Airbus(id));
            }
            else{
                development_queue.add(new Boeing777(id));
            }
            id++;
        }
    }
}
