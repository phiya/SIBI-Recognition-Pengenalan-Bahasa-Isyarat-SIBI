/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RT_RBLGCNN;

import com.leapmotion.leap.Controller;
import java.io.IOException;

/**
 *
 * @author Wolez
 */
public class RbThreadFitur extends Thread {

    private Thread t;
    int count;
    boolean jalan = true;

    void Fitur() {
        RbFitur listener2 = new RbFitur();
        Controller controller = new Controller();

        // Have the sample listener receive events from the controller
        controller.addListener(listener2);

        // Keep this process running until Enter is pressed
//        System.out.println("Press Enter to quit...");

        try {
            System.in.read();
        } catch (IOException e) {
        }
        controller.removeListener(listener2);
    }

    public void run() {
//        System.out.println("Thread RUN!");
        if(jalan)
            Fitur();
    }
    
    public void setJalan(boolean jalanPar){
        this.jalan=jalanPar;
    }

    public void start() {
//        System.out.println("Thread START!");
        if (t == null) {
            t = new Thread();
            t.start();
        }
    }
}
