/*
LineManager reads from development queue and executes robot thread with aircraft and parts it needs sent in.
Sends completed aircraft to completed queue.
All requests to robots are stored in an output queue for OutPutWriter to read. 
*/

package factory;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Semaphore;
import java.lang.System;

public class LineManager implements Runnable{
    private Semaphore Robots = new Semaphore(10, true);
    private ConcurrentHashMap<String, AtomicInteger> inventory;
    private ConcurrentLinkedQueue<Aircraft> development_queue;
    private ConcurrentLinkedQueue<Aircraft> completed_queue;
    private Semaphore parts;
    private ConcurrentLinkedQueue<String> out_queue;
    private Long start;

    public LineManager(ConcurrentHashMap<String, AtomicInteger> inventory,
                       ConcurrentLinkedQueue<Aircraft> development_queue,
                       Semaphore parts, ConcurrentLinkedQueue<Aircraft> completed_queue, ConcurrentLinkedQueue<String> out_queue, Long start){
        
        this.inventory = inventory;
        this.development_queue = development_queue;
        this.parts = parts;
        this.completed_queue = completed_queue;
        this.out_queue = out_queue;
        this.start = start;

    }

    public void run(){
        while(true){
            // If something in development queue
            if(development_queue.peek()!=null) {
                ArrayList<String> part_installation = new ArrayList<>(2);
                Aircraft aircraft = development_queue.poll();
                Map<String, Boolean> partsNeeded = aircraft.PartsNeeded();
                ArrayList<String> parts_to_install = new ArrayList<>(2);

                partsNeeded.forEach(
                        (key, installed) -> {
                            // If something needs to be installed
                            if(!installed) {
                                // If in inventory, add to part needed list
                                if (inventory.get(key).get() != 0) {
                                    part_installation.add(key);
                                }
                                else {
                                    // If out of parts Get time elapsed and Send to terminal
                                    Long time = System.currentTimeMillis();
                                    Long current_time = time - start;
                                    System.out.println("Out of part: " + key + " at time: " + current_time);
                                }
                            }
                        }
                );
                if(part_installation.size()>2){                 // We want to send 2 to each robot, that is there work capacity at any given time
                    part_installation.trimToSize(); // Just to mix it up from one part each build
                }

                // If Aircraft completed
                if(part_installation.size()==0){
                    // Get time elapsed and send to terminal & add to completed queue
                    Long id = aircraft.IdRequest();
                    Long time = System.currentTimeMillis();
                    Long current_time = time - start;
                    System.out.println("Aircraft: " + id + " completed at time: " + current_time);

                    completed_queue.add(aircraft);
                }
                else {
                    // Otherwisw, pass to robot to do work
                    for (int i = 0; i < 2; i++) {
                        try {
                            if (part_installation.size() > 1) {
                                parts_to_install.add(part_installation.get(i));
                            }
                            // Acquire part semaphore as you are writing to map here
                            parts.acquire(); // Going into Critical section
                            inventory.get(part_installation.get(i)).decrementAndGet();
                            parts.release();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        // Acquire a robot
                        Robots.acquire();
                        // Get time elapsed and send to outqueue
                        Long time = System.currentTimeMillis();
                        Long current_time = time - start;
                        Long id = aircraft.IdRequest();
                        int size = parts_to_install.size();
                        if ( size == 2) {
                            out_queue.add("Line manager makes request to robot to install parts: " +  parts_to_install.get(0) + ", " + part_installation.get(1) + " in aircraft #" + id + " at time: " + current_time);
                        }
                        else {
                            out_queue.add("Line manager makes request to robot to install parts: " +  parts_to_install.get(0) + " in aircraft #" + id + " at time: " + current_time);
                           
                        }

                        // Execute new robot thread

                        new Thread(new Robot(parts_to_install, aircraft, Robots, development_queue)).start();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
