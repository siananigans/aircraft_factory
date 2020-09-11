package factory;


import java.awt.Font;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;

public class Monitor implements Runnable {
    private ConcurrentHashMap<String, AtomicInteger> inventory;

    public Monitor(ConcurrentHashMap<String, AtomicInteger> inventory)
    {
        this.inventory = inventory;
    }

    public void run()
    {
        JFrame myFrame = new JFrame("Inventory");
        myFrame.setVisible(true);
        myFrame.setBounds(300, 200, 700, 400);

        JLabel parts_count = new JLabel(" ", 0);
        parts_count.setFont(new Font("TimesRoman", 0, 20));
        myFrame.getContentPane().add(parts_count, "Center");
        for (;;)
        {
            String t = "";
            for (Map.Entry<String, AtomicInteger> entry : inventory.entrySet()) {
                t = t + entry.getKey() + ": " + entry.getValue() + "\n";
            }
            parts_count.setText("<html>" + t.replaceAll("<","&lt;").replaceAll(">",
                    "&gt;").replaceAll("\n", "<br/>") + "</html>");
            try {
                Thread.sleep(0);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
