package RT_RBLGCNN;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class RbWriteRead {

    public static void writeToCSV(double[][] aData, String aDatafile) {
        try {
//            String[][] aData = new String[aaData.length][aaData[0].length];
//            for (int i=0; i<aaData.length; i++){
//                for (int j=0; j<aaData[0].length; j++){
//                    aData[i][j] = Double.toString(aaData[i][j]);;
//                }
//            }

            FileOutputStream fout = new FileOutputStream(aDatafile, false);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fout));

            for (int row = 0; row < aData.length; row++) {
                for (int column = 0; column < aData[row].length; column++) {
//                    if (aData[row][column] == 0) {
//                        bw.append("null");
//                        // The comma separated value
//                        bw.append(',');
//                    } else {
                    bw.append(Double.toString(aData[row][column]));
                    bw.append(',');
//                    }
                }//end column loop (inner loop)
                bw.append('\n');
            }//end row loop (outer loop)
            bw.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public static void writeToCSV(double[] aData, String aDatafile) {
        try {
            FileOutputStream fout = new FileOutputStream(aDatafile, false);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fout));

            for (int row = 0; row < aData.length; row++) {
                bw.append(Double.toString(aData[row]));
                bw.append('\n');
            }//end row loop (outer loop)
            bw.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public static void writeToCSV(List aData, String aDatafile) {
        try {
            FileOutputStream fout = new FileOutputStream(aDatafile, false);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fout));

            for (int row = 0; row < aData.size(); row++) {
                //b.append(list.get(i).toString());
                bw.append(aData.get(row).toString());
                bw.append('\n');
            }//end row loop (outer loop)
            bw.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    
    public static void writeToCSV(double aData, String aDatafile) {
        try {
            FileOutputStream fout = new FileOutputStream(aDatafile, false);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fout));
            bw.append(Double.toString(aData));
            bw.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    
    public static void writeToCSV(int aData, String aDatafile) {
        try {
            FileOutputStream fout = new FileOutputStream(aDatafile, false);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fout));
            bw.append(Integer.toString(aData));
            bw.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

//    static String[][] indat;
//    static String s, sp1;
//    static StringTokenizer st;
//    static ArrayList x ;
//    public static List kelas = new ArrayList();
//    public static double[][] DataHasil;

    public static double[][] ReadCSV(String fname) throws FileNotFoundException, IOException {
//        x = new ArrayList();
        
        String[][] indat;
    String s, sp1;
    StringTokenizer st;
    ArrayList x = new ArrayList();;
    List kelas = new ArrayList();
    double[][] DataHasil;
        
        FileReader fr = new FileReader(fname);
        BufferedReader buf = new BufferedReader(fr);

        int words = 0;
        int chars = 0;
        int lines = 0;

        while ((s = buf.readLine()) != null) {
            lines++;
            st = new StringTokenizer(s, ",");
            while (st.hasMoreTokens()) {
                words++;
                s = st.nextToken();
                chars += s.length();
                String y = new String(s);
                x.add(y);
            }
        }

        String ct[] = new String[0];
        ct = (String[]) x.toArray(ct);

        indat = new String[lines][words / lines];
        int inval = 0;

        // Mencetak array dari pembacaan file
//        System.out.println("Dataset :");
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < (words / lines); j++) {
                indat[i][j] = ct[inval];
                inval++;
//                System.out.print((indat[i][j]) + "\t");
            }
//            System.out.println();
        }
//        fr.close();
//        System.out.println("Jumlah Baris : " + indat.length);
//        System.out.println("Jumlah Kolom : " + indat[0].length);

//        System.out.println("\nHasil Parsing ke double :");
        DataHasil = new double[indat.length][indat[0].length];
        for (int i = 0; i < indat.length; i++) {
            for (int j = 0; j < indat[i].length; j++) {
                DataHasil[i][j] = Double.parseDouble(indat[i][j]);
//                System.out.print(indat[i][j] + "\t");
            }
//            System.out.println();
        }
        return DataHasil;
    }
    

//    static String[] indat2;
//    static String s2;
//    static StringTokenizer st2;
//    static ArrayList x2 ;
//    public static List kelas2 = new ArrayList();
//    public static double[] DataHasil2;

    public static double[] ReadCSV1D(String fname) throws FileNotFoundException, IOException {
//        x2 = new ArrayList();
        
        String[] indat2;
    String s2;
    StringTokenizer st2;
    ArrayList x2 = new ArrayList();
    List kelas2 = new ArrayList();
    double[] DataHasil2;
    
        FileReader fr = new FileReader(fname);
        BufferedReader buf = new BufferedReader(fr);

        int words = 0;
        int chars = 0;
        int lines = 0;

        while ((s2 = buf.readLine()) != null) {
            lines++;
            st2 = new StringTokenizer(s2, ",");
            while (st2.hasMoreTokens()) {
                words++;
                s2 = st2.nextToken();
                chars += s2.length();
                String y = new String(s2);
                x2.add(y);
            }
        }

        String ct[] = new String[0];
        ct = (String[]) x2.toArray(ct);

        indat2 = new String[lines];
        int inval2 = 0;

        // Mencetak array dari pembacaan file
//        System.out.println("Dataset :");
        for (int i = 0; i < lines; i++) {
            indat2[i] = ct[inval2];
            inval2++;
//            System.out.println((indat2[i]));
        }
//        System.out.println("Jumlah Baris : " + indat.length);
//        System.out.println("Jumlah Kolom : " + indat[0].length);

//        System.out.println("\nHasil Parsing ke doubleeee :");
        DataHasil2 = new double[indat2.length];
        for (int i = 0; i < indat2.length; i++) {
            DataHasil2[i] = Double.parseDouble(indat2[i]);
//            System.out.println(indat2[i]);
        }
        return DataHasil2;
    }
    
//    static String[] indat3;
//    static String s3;
//    static StringTokenizer st3;
//    static ArrayList x3;
//    public static List kelas3 = new ArrayList();
//    public static double[] DataHasil3;

    public static String [] ReadCSVList(String fname) throws FileNotFoundException, IOException {
//        x3 = new ArrayList();
        
    String[] indat3;
    String s3;
    StringTokenizer st3;
    ArrayList x3 = new ArrayList();;
    List kelas3 = new ArrayList();
    double[] DataHasil3;
        
        FileReader fr = new FileReader(fname);
        BufferedReader buf = new BufferedReader(fr);

        int words = 0;
        int chars = 0;
        int lines = 0;

        while ((s3 = buf.readLine()) != null) {
            lines++;
            st3 = new StringTokenizer(s3, ",");
            while (st3.hasMoreTokens()) {
                words++;
                s3 = st3.nextToken();
                chars += s3.length();
                String y = new String(s3);
                x3.add(y);
            }
        }

        String ct[] = new String[0];
        ct = (String[]) x3.toArray(ct);

        indat3 = new String[lines];
        int inval2 = 0;

        // Mencetak array dari pembacaan file
//        System.out.println("Dataset :");
        for (int i = 0; i < lines; i++) {
            indat3[i] = ct[inval2];
            inval2++;
//            System.out.println((indat3[i]));
        }

        return indat3;
    }

    public static int ReadCSVint(String fname) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(fname);
        BufferedReader buf = new BufferedReader(fr);
        int Size = Integer.parseInt(buf.readLine());
        return Size;
    }
    
    public static double ReadCSVdouble(String fname) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(fname);
        BufferedReader buf = new BufferedReader(fr);
        double Data = Double.parseDouble(buf.readLine());
        return Data;
    }
    
    
    
    
//    static String[][] indat7;
//    static String s7, sp17;
//    static StringTokenizer st7;
//    static ArrayList x7 ;
//    public static List kelas7 = new ArrayList();
//    public static double[][] DataHasil7;

    public static double[][] ReadCSVRT(String fname) throws FileNotFoundException, IOException {
//        x7 = new ArrayList();
        
        String[][] indat7;
    String s7, sp17;
    StringTokenizer st7;
    ArrayList x7 = new ArrayList();
    List kelas7 = new ArrayList();
    double[][] DataHasil7;
        
        FileReader fr = new FileReader(fname);
        BufferedReader buf = new BufferedReader(fr);

        int words = 0;
        int chars = 0;
        int lines = 0;

        while ((s7 = buf.readLine()) != null) {
            lines++;
            st7 = new StringTokenizer(s7, ",");
            while (st7.hasMoreTokens()) {
                words++;
                s7 = st7.nextToken();
                chars += s7.length();
                String y = new String(s7);
                x7.add(y);
            }
        }

        String ct7[] = new String[0];
        ct7 = (String[]) x7.toArray(ct7);

        indat7 = new String[lines][words / lines];
        int inval7 = 0;

        // Mencetak array dari pembacaan file
//        System.out.println("Dataset :");
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < (words / lines); j++) {
                indat7[i][j] = ct7[inval7];
                inval7++;
//                System.out.print((indat[i][j]) + "\t");
            }
//            System.out.println();
        }
//        fr.close();
//        System.out.println("Jumlah Baris : " + indat.length);
//        System.out.println("Jumlah Kolom : " + indat[0].length);

//        System.out.println("\nHasil Parsing ke double :");
        DataHasil7 = new double[indat7.length][indat7[0].length];
        for (int i = 0; i < indat7.length; i++) {
            for (int j = 0; j < indat7[i].length; j++) {
                DataHasil7[i][j] = Double.parseDouble(indat7[i][j]);
//                System.out.print(indat[i][j] + "\t");
            }
//            System.out.println();
        }
        return DataHasil7;
    }
    

}




