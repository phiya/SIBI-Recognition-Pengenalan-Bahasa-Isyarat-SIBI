package RT_LGCNN;

import static RT_LGCNN.LgcnnTest.HASIL;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Vector;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

class Fitur extends Listener {

    static DecimalFormat df = new DecimalFormat("#.##");
    Vector bpJoint = new Vector();
    Vector palmPoint = new Vector();
    double disPoint[][] = new double[5][3];
    double itmPoint[][] = new double[5][3];
    double pxmPoint[][] = new double[5][3];
    double mcpPoint[][] = new double[5][3];
    double dinamic_point[][] = new double[10][2];
    double palm_point[][] = new double[10][3];
    double tempbPoint[][] = new double[5][3];

    double F_Dinamis[] = new double[9];
    int counter;

    double jarak_frame, jarak_spread;
    double sumJarak;
    double averageDistance;
    double sumSpread;
    double averageSpread;
    double Trispread;
    double sumTriSpread;
    double averageTriSpread;
    double ex_distance;

    Vector tempbpJoint = new Vector();

    GUI_RT_LGCNN real = new GUI_RT_LGCNN();
    LgcnnTest Lgcnntest = new LgcnnTest();

    LeapMouse mouse = new LeapMouse();

    private double[][] Datatesting;

    @Override
    public void onInit(Controller controller) {
//        System.out.println("Initialized");
        counter = 0;
    }

    @Override
    public void onConnect(Controller controller) {
//        System.out.println("Connected");
//        controller.removeListener(mouse);
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
    }

    @Override
    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
//        System.out.println("Disconnected");
    }

    @Override
    public void onExit(Controller controller) {
//        System.out.println("Exited");
    }
    public static int count = 0;
    int timeToProcess = 0;

    @Override
    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();

        if (count > 9) {
            count = 0;
        }
        //Get hands
        for (Hand hand : frame.hands()) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException ex) {
                Logger.getLogger(Fitur.class.getName()).log(Level.SEVERE, null, ex);
            }
            String handType = hand.isLeft() ? "Left hand" : "Right hand";
            int n = 0;
            for (Finger finger : hand.fingers()) { // 5x
                for (Bone.Type boneType : Bone.Type.values()) { // 20x
                    Bone bone = finger.bone(boneType);
                    //Mendapatkan titik X, Y dan Z pada setiap ujung jari
                    bpJoint = bone.prevJoint();
//                    System.out.println(bone.prevJoint());
                    if (boneType.equals(boneType.TYPE_DISTAL)) {
                        disPoint[n][0] = bpJoint.getX();
                        disPoint[n][1] = bpJoint.getY();
                        disPoint[n][2] = bpJoint.getZ();
//                        System.out.print(disPoint[n][0] + ", " + disPoint[n][1] + ", " + disPoint[n][2] + "\t ");
                    }
                    if (boneType.equals(boneType.TYPE_INTERMEDIATE)) {
                        itmPoint[n][0] = bpJoint.getX();
                        itmPoint[n][1] = bpJoint.getY();
                        itmPoint[n][2] = bpJoint.getZ();
//                        System.out.print(itmPoint[n][0]+", "+itmPoint[n][1]+", "+itmPoint[n][2]+"\t ");
                    }
                    if (boneType.equals(boneType.TYPE_PROXIMAL)) {
                        pxmPoint[n][0] = bpJoint.getX();
                        pxmPoint[n][1] = bpJoint.getY();
                        pxmPoint[n][2] = bpJoint.getZ();
//                        System.out.print(pxmPoint[n][0]+", "+pxmPoint[n][1]+", "+pxmPoint[n][2]+"\t ");
                    }
                    if (boneType.equals(boneType.TYPE_METACARPAL)) {
                        mcpPoint[n][0] = bpJoint.getX();
                        mcpPoint[n][1] = bpJoint.getY();
                        mcpPoint[n][2] = bpJoint.getZ();
//                        System.out.print(mcpPoint[n][0]+", "+mcpPoint[n][1]+", "+mcpPoint[n][2]+"\t ");
                    }
                }
//                System.out.println();
                n++;
            }
//            System.out.println();
//            System.out.println("----------------------------------------------------------------------------");

            //Mendapatkan titik X dan Z dari posisi palm(titik tengah tangan)
            palmPoint = hand.palmPosition();
            dinamic_point[count][0] = palmPoint.getX();
            dinamic_point[count][1] = palmPoint.getZ();

            //mendapatkan titik X, Y dan Z palm
            palm_point[count][0] = palmPoint.getX();
            palm_point[count][1] = palmPoint.getY();
            palm_point[count][2] = palmPoint.getZ();
        }
        System.out.println(count);

        BentukFitur();

        if (count == 9) {
            controller.removeListener(this);
        }
        count++;
    }

    //Proses Utama Pembentukan Fitur
    double maxExD = 0;

    void BentukFitur() {
        maxExD = Double.MIN_VALUE;
        if (count <= 9) {

            //Fitur rata-rata Jarak Penyebaran jari (Average Spread)
            jarak_spread = JarakSpread(disPoint);
            sumSpread = sumSpread + jarak_spread;

            //Fitur Average TriSpread
            Trispread = AverageTriSpread(disPoint, mcpPoint);
            sumTriSpread = sumTriSpread + Trispread;

            //Fitur Extended Distance
            ex_distance = ExtendedDistance(palm_point, disPoint, itmPoint, pxmPoint, mcpPoint);
//            System.out.println("ED = "+ex_distance);
            if (ex_distance > maxExD) {
                maxExD = ex_distance;
            }

            //Fitur Dinamis Hand gesture
            if (dinamic_point == null) {
                F_Dinamis[count] = 0;
            } else {
                F_Dinamis = DinamicGesture(dinamic_point);
            }

            if (count == 9) {
                //Rata-rata jarak antar frame dari total 10 frame
                averageSpread = sumSpread / 10;
                averageTriSpread = sumTriSpread / 10;

                //Buat array dari fitur
                Datatesting = new double[1][12];
                Datatesting[0][0] = averageSpread;
                Datatesting[0][1] = averageTriSpread;
                Datatesting[0][2] = maxExD;
                Datatesting[0][3] = F_Dinamis[0];
                Datatesting[0][4] = F_Dinamis[1];
                Datatesting[0][5] = F_Dinamis[2];
                Datatesting[0][6] = F_Dinamis[3];
                Datatesting[0][7] = F_Dinamis[4];
                Datatesting[0][8] = F_Dinamis[5];
                Datatesting[0][9] = F_Dinamis[6];
                Datatesting[0][10] = F_Dinamis[7];
                Datatesting[0][11] = F_Dinamis[8];

                //Meng-Nolkan :
                sumSpread = 0;
                sumTriSpread = 0;
                averageDistance = 0;
                averageSpread = 0;
                averageTriSpread = 0;
                maxExD = 0;

                System.out.println();
                for (int i = 0; i < Datatesting.length; i++) {
                    for (int j = 0; j < Datatesting[0].length; j++) {
                        System.out.print(df.format(Datatesting[i][j]) + "\t");
                    }
                    System.out.println();
                }

                real.show(swigCMemOwn);

                if (Datatesting[0][0] == 0 && Datatesting[0][1] == 0 && Datatesting[0][2] == 0) {
                    real.HasilHuruf.setText("!");
                    System.out.println("HASIL = Coba Lagi");
                } else {
                    try {
                        Lgcnntest.ProsesTestLGCNN(Datatesting);
                    } catch (IOException ex) {
                        Logger.getLogger(Fitur.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    real.HasilHuruf.setText(HASIL);
                    System.out.println("HASIL = " + HASIL);
                }
            }
        }
    }

    //Fungsi print Array 1-Dimensi
    static void printArray1D(double array[]) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(df.format(array[i]) + "\t");
        }
    }

    //Fungsi print Array 2-Dimensi
    void printArray2D(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                System.out.print(a[i][j] + "\t");
            }
            System.out.println();
        }
    }

    //Fungsi prosedur untuk copy Array 2-Dimensi
    void copyArray() {
        for (int i = 0; i < disPoint.length; i++) {
            for (int j = 0; j < disPoint[0].length; j++) {
                tempbPoint[i][j] = disPoint[i][j];
//                    System.out.println(tempbPoint[i][j]);
            }
        }
    }

    //Fungsi untuk menghitung hand dinamic feature
    public static double[] DinamicGesture(double[][] Array) {
        double[] DeltaX = new double[9];
        double[] DeltaZ = new double[9];
        double[] ArcTan = new double[9];
        double[] Quantisasi = new double[9];

        //Hitung Delta X dan Delta Z
        for (int i = 0; i < Array[0].length; i++) {
            for (int j = 0; j < Array.length; j++) {
                if (i == 0 && j <= 8) {
                    DeltaX[j] = Array[j + 1][i] - Array[j][i];
                }
                if (i == 1 && j <= 8) {
                    DeltaZ[j] = Array[j + 1][i] - Array[j][i];
                }
//                System.out.print("Delta : "+DeltaX[j]+"\t");
            }
//            System.out.println();
        }

        //Hitung sudut Orientasi 0-360        
        for (int i = 0; i < DeltaX.length; i++) {

            if (DeltaX[i] < 10 && DeltaX[i] > (-10)) {
                Quantisasi[i] = 0;
            } else {
                if (DeltaX[i] < 0) {
                    ArcTan[i] = Math.atan(DeltaZ[i] / DeltaX[i]) * (180 / Math.PI) + 180;
                } else if (DeltaZ[i] < 0) {
                    ArcTan[i] = Math.atan(DeltaZ[i] / DeltaX[i]) * (180 / Math.PI) + 360;
                } else if (DeltaX[i] > 0 || DeltaZ[i] >= 0) {
                    ArcTan[i] = Math.atan(DeltaZ[i] / DeltaX[i]) * (180 / Math.PI);
                }
                Quantisasi[i] = Math.round(ArcTan[i] / 45);
            }
        }
        return Quantisasi;
    }

    //Fungsi untuk menghitung jarak 2-Dimensi antar Frame
    public static double AverageFrame(double[][] Array1, double[][] Array2) {

        //Tranpose data
        double[][] transpose = new double[Array2[0].length][Array2.length];
        if (Array2.length > 0) {
            for (int i = 0; i < Array2[0].length; i++) {
                for (int j = 0; j < Array2.length; j++) {
                    transpose[i][j] = Array2[j][i];
                }
            }
        }

        double[][] hasil = new double[Array1.length][transpose[0].length];
        double hasil2 = 0;
//        System.out.println("Hasil Jarak");
        for (int i = 0; i < hasil.length; i++) { //row
            for (int j = 0; j < hasil[0].length; j++) { //col
                for (int l = 0; l < Array1[0].length; l++) {
                    hasil[i][j] += Math.pow(Array1[i][l] - transpose[l][j], 2);
                }
                hasil2 = (Math.sqrt(hasil[i][j]));
//                System.out.print(hasil2+"\t");
            }
//            System.out.println("");
        }
        return hasil2;
    }

    //Fungsi untuk mendapatkan jarak rata-rata penyebaran jari
    public static double JarakSpread(double[][] Array1) {
        double[] HasilJarak = new double[Array1.length - 1];
        double[] Hasil = new double[Array1.length - 1];
        double sum = 0;
        double Rata;
        for (int i = 0; i < Array1[0].length; i++) {
            for (int j = 0; j < Array1.length; j++) {
                if (j <= 3) {
                    HasilJarak[j] += Math.pow((Array1[j][i] - Array1[j + 1][i]), 2);
                    Hasil[j] = Math.sqrt(HasilJarak[j]);
                }
            }
        }

        for (int k = 0; k < 4; k++) {
            sum = sum + Hasil[k];
        }
//        System.out.println("Sum = " + sum);
        Rata = sum / 4;
//        System.out.println("Rata = " + Rata);
        return Rata;
    }

    //Mendapatkan Jarak Maksimal Palm to All point
    public static double ExtendedDistance(double[][] palm, double[][] Dis, double[][] Itm, double[][] Pxm, double[][] Mcp) {
        double HasilED = 0;
        double[] P_Dis = new double[5];
        double[] P_DisJ = new double[5];
        double MakDis = 0;
        double[] P_Itm = new double[5];
        double[] P_ItmJ = new double[5];
        double MakItm = 0;
        double[] P_Pxm = new double[5];
        double[] P_PxmJ = new double[5];
        double MakPxm = 0;
        double[] P_Mcp = new double[5];
        double[] P_McpJ = new double[5];
        double MakMcp = 0;

        //Mendapatkan Jarak Maksimum Palm - Distal
        for (int i = 0; i < Dis[0].length; i++) {
            for (int j = 0; j < Dis.length; j++) {
                P_Dis[j] += Math.pow((palm[0][i] - Dis[j][i]), 2);
                P_DisJ[j] = Math.sqrt(P_Dis[j]);
                if (MakDis < P_DisJ[j]) {
                    MakDis = P_DisJ[j];
                }
            }
        }
//        System.out.println("\nMAX DIS = " + MakDis);

        //Mendapatkan Jarak Maksimum Palm - Intermediate
        for (int i = 0; i < Itm[0].length; i++) {
            for (int j = 0; j < Itm.length; j++) {
                P_Itm[j] += Math.pow((palm[0][i] - Itm[j][i]), 2);
                P_ItmJ[j] = Math.sqrt(P_Itm[j]);
                if (MakItm < P_ItmJ[j]) {
                    MakItm = P_ItmJ[j];
                }
            }
        }
//        System.out.println("\nMAX ITM = " + MakItm);

        //Mendapatkan Jarak Maksimum Palm - Proximal
        for (int i = 0; i < Pxm[0].length; i++) {
            for (int j = 0; j < Pxm.length; j++) {
                P_Pxm[j] += Math.pow((palm[0][i] - Pxm[j][i]), 2);
                P_PxmJ[j] = Math.sqrt(P_Pxm[j]);
                if (MakPxm < P_PxmJ[j]) {
                    MakPxm = P_PxmJ[j];
                }
            }
        }
//        System.out.println("\nMAX PXM = " + MakPxm);

        //Mendapatkan Jarak Maksimum Palm - Metacarpal
        for (int i = 0; i < Mcp[0].length; i++) {
            for (int j = 0; j < Mcp.length; j++) {
                P_Mcp[j] += Math.pow((palm[0][i] - Mcp[j][i]), 2);
                P_McpJ[j] = Math.sqrt(P_Mcp[j]);
                if (MakMcp < P_McpJ[j]) {
                    MakMcp = P_McpJ[j];
                }
            }
        }
//        System.out.println("\nMAX MCP = " + MakMcp);

        double[] HasilMAX = new double[4];
        HasilMAX[0] = MakDis;
        HasilMAX[1] = MakItm;
        HasilMAX[2] = MakPxm;
        HasilMAX[3] = MakMcp;

        for (int i = 0; i < 4; i++) {
            if (HasilED < HasilMAX[i]) {
                HasilED = HasilMAX[i];
            }
        }
//        System.out.println("\nHasil Maks ED = " + HasilED);

        return HasilED;
    }

    //Luas segitiga antara 2 jari
    public static double AverageTriSpread(double[][] Dis, double[][] Mcp) {
        double HasilAT = 0;
        double[][] HasilMidpoint = new double[4][3];
        double[] Jarak1 = new double[4];
        double[] JarakDisMid1 = new double[4];
        double[] Jarak2 = new double[4];
        double[] JarakDisMid2 = new double[4];
        double[] Hasil = new double[4];
        double[] HasilJarak = new double[4];
        double[] Sudut = new double[4];
        double[] Luas = new double[4];
        double[] S = new double[4];
        double sum = 0;

        //Dapatkan Midpoint Metacarpal
        for (int i = 0; i < Mcp[0].length; i++) {
            for (int j = 0; j < Mcp.length; j++) {
                if (j <= Mcp.length - 2) {
                    HasilMidpoint[j][i] = (Mcp[j][i] + Mcp[j + 1][i]) / 2;
//                    System.out.print(HasilMidpoint[j][i]+"\t");
                }
            }
//            System.out.println();
        }

        //mendapatkan jarak antar jari
        for (int i = 0; i < Dis[0].length; i++) {
            for (int j = 0; j < Dis.length; j++) {
                if (j <= 3) {
                    Hasil[j] += Math.pow((Dis[j][i] - Dis[j + 1][i]), 2);
                    HasilJarak[j] = Math.sqrt(Hasil[j]);
                }
            }
        }
        for (int i = 0; i < 4; i++) {
//            System.out.print(HasilJarak[i]+"\t");
        }
//        System.out.println();

        //Mendapatkan Jarak Distal 1 - Midpoint
        for (int i = 0; i < Dis[0].length; i++) {
            for (int j = 0; j < Dis.length; j++) {
                if (j < 4) {
                    Jarak1[j] += Math.pow((Dis[j][i] - HasilMidpoint[j][i]), 2);
                    JarakDisMid1[j] = Math.sqrt(Jarak1[j]);
                }
            }
        }
        for (int i = 0; i < 4; i++) {
//            System.out.print(JarakDisMid1[i]+"\t");
        }
//        System.out.println();

        //Mendapatkan Jarak Distal 2 - Midpoint
        for (int i = 0; i < Dis[0].length; i++) {
            for (int j = 0; j < Dis.length; j++) {
                if (j + 1 < 5) {
                    Jarak2[j] += Math.pow((Dis[j + 1][i] - HasilMidpoint[j][i]), 2);
                    JarakDisMid2[j] = Math.sqrt(Jarak2[j]);
                }
            }
        }
        for (int i = 0; i < 4; i++) {
//            System.out.print(JarakDisMid2[i]+"\t");
        }
//        System.out.println();

        //Mendapatkan Sudut
//        for (int i=0; i<4; i++){
//            Sudut[i] = ((Math.pow(JarakDisMid1[i], 2)) - (Math.pow(JarakDisMid2[i], 2)) - (Math.pow(HasilJarak[i], 2))) / (2 * JarakDisMid1[i] * JarakDisMid2[i]);
//            System.out.print(Math.sin(Sudut[i])+"\t");
//        }
//        System.out.println();
        for (int i = 0; i < 4; i++) {
            S[i] = (JarakDisMid1[i] + JarakDisMid2[i] + HasilJarak[i]) / 2;
            Luas[i] = Math.sqrt(S[i] * (S[i] - JarakDisMid1[i]) * (S[i] - JarakDisMid2[i]) * (S[i] - HasilJarak[i]));
//            System.out.print(S[i]+"\t");
//            System.out.print(Luas[i]+"\t");
            sum = sum + Luas[i];
        }
//        System.out.println();
        HasilAT = sum / 4;
//        System.out.println(HasilAT);

        return HasilAT;
    }
}

//class Main {
//
//    public static void main(String[] args) {
//        // Create a sample listener and controller
//        Fitur listener = new Fitur();
//        Controller controller = new Controller();
//
//        // Have the sample listener receive events from the controller
//        controller.addListener(listener);
//
//        // Keep this process running until Enter is pressed
//        System.out.println("Press Enter to quit...");
//
//        //Coba Message
//        // String message = "Selamat Datang";
//        //JOptionPane.showMessageDialog (null, message);
//        try {
//            System.in.read();
//        } catch (IOException e) {
//        }
//
//        // Remove the sample listener when done
//        controller.removeListener(listener);
//    }
//}
