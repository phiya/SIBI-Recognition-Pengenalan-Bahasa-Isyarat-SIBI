package RT_RBLGCNN;

import com.leapmotion.leap.*;

import java.awt.Dimension;
import java.awt.Robot;

class RbLeapMouse extends Listener {

    public static Robot robot;
    
    @Override
    public void onFrame(Controller controller) {
        try {
            robot = new Robot();
        } catch (Exception e) {
        }

        Frame frame = controller.frame();
        InteractionBox box = frame.interactionBox();

        for (Hand hand : frame.hands()) {
            Vector palmPos = hand.stabilizedPalmPosition();
            Vector boxPalmPos = box.normalizePoint(palmPos);
            Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            robot.mouseMove((int) (screen.width * boxPalmPos.getX()), (int) (screen.height - boxPalmPos.getY() * screen.height));
//            System.out.println(screen.width * boxPalmPos.getX() + ", " + boxPalmPos.getY() * screen.height);
        }
    }


//    public static void main(String[] args) {
//        RbLeapMouse listener = new RbLeapMouse();
//        Controller controller = new Controller();
//        controller.addListener(listener);
//
//        try {
//            System.in.read();
//        } catch (Exception e) {
//
//        }
//
//        controller.removeListener(listener);
//    }
}
