/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RT_LGCNN;

import com.leapmotion.leap.Controller;
import java.io.IOException;

/**
 *
 * @author Wolez
 */

public class ThreadMouse extends Thread{
    
    private Thread t;
//    private boolean jalanMouse = true;
    
    void Mouse() {
        LeapMouse listener = new LeapMouse();
        Controller controller = new Controller();
        controller.addListener(listener);
        
//        listener.onExit(controller.);        
        try {
            System.in.read();
        } catch (Exception e) {

        }

        controller.removeListener(listener);
    }
    
    public void run() {
//        System.out.println("Thread RUN!");
//        if(jalanMouse) {
//            System.out.println("Apakah jalan ? "+jalanMouse);
            Mouse();
//        }
    }
    
//    public void setJalanMouse(boolean jalanMousePar){
//        this.jalanMouse = jalanMousePar;
//    }
    
    public void start(){
//            System.out.println("Thread START!");
            if(t == null){
                t = new Thread();
                t.start();
            }
    }
    
//    public void pauseThread() throws InterruptedException {
//        synchronized (t) {
//            t.wait();
//        }
//    }
//
//    public void resumeThread() {
//        synchronized (t) {
//            t.notify();
//        }
//    }
    
    
//    public void stop(){
//        System.out.println("Thread START!");
//            if(t != null){
////            
//                t = new Thread();
//                t.interrupt();
//            }
//    }
    
}
