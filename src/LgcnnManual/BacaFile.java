/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LgcnnManual;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Wolez
 */
public class BacaFile {

    static DecimalFormat df = new DecimalFormat("0.00000");
    Tools tool = new Tools();

    String[][] indat;
    String s, sp1;
    StringTokenizer st;
    ArrayList x = new ArrayList();
    public static List kelas = new ArrayList();
    public static double[][] dataTraining;
    public static double[] atributTarget;
    public static double[][] dataLkp;

    public void bacaFileToArray(String fname) throws FileNotFoundException, IOException {
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
//        System.out.println("Data Sample :");
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < (words / lines); j++) {
                indat[i][j] = ct[inval];
                inval++;
//                System.out.print((indat[i][j]) + "\t");
            }
//            System.out.println();
        }
//        System.out.println("Jumlah Baris : " + indat.length);
//        System.out.println("Jumlah Kolom : " + indat[0].length);
    }

    public void defenisiClass() {
        // Mendefenisikan klass ke number
        String temp = null;
        int idx = -1;
        List x = new ArrayList();
        System.out.println();
//        System.out.println("\nDefenisi Class ke Number :");
        for (int i = 0; i < indat[0].length; i++) {
            String cek = null;
            for (int j = 0; j < indat.length; j++) {
                if (i == indat[0].length - 1) {
                    cek = indat[j][i];
                    if (temp != cek && !kelas.contains(cek)) {
                        idx++;
                        x.add(idx);
                        kelas.add(cek);
                    }
                    if (kelas.contains(cek)) {
                        indat[j][i] = String.valueOf(kelas.indexOf(cek));
                    } else {
                        indat[j][i] = String.valueOf(idx);
                    }
                    temp = cek;
                }
            }
        }
//        System.out.println("Hasil :");
        for (int i = 0; i < indat.length; i++) {
            for (int j = 0; j < (indat[0].length); j++) {
//                System.out.print((indat[i][j]) + "\t");
            }
//            System.out.println();
        }
//        System.out.println("Atribut Kelas : " + kelas);
//        System.out.println("Defenisi Atribut : " + x);
//        System.out.println("Jumlah Kelas = " + kelas.size());
//
//        System.out.println("Panjang indat = " + indat.length);
    }

    public void ambilAtributTarget() {
        System.out.println("\nAtribut Target");
        atributTarget = new double[indat.length];
        for (int i = 0; i < indat.length; i++) {
            for (int j = 0; j < indat[0].length; j++) {
                if (j == (indat[0].length - 1)) {
                    atributTarget[i] = Double.parseDouble(indat[i][j]);
                }
//                System.out.print(atributTarget[i] + "\t");
            }
//            System.out.print("\n");
        }
//        tool.printArray(atributTarget);
        //System.out.println(atributTarget[0]);
    }

    public void ambilDataTraining() {
//        System.out.println("\nData Training");
        dataTraining = new double[indat.length][indat[0].length - 1];
        for (int i = 0; i < indat.length; i++) {
            for (int j = 0; j < indat[0].length - 1; j++) {
                dataTraining[i][j] = Double.parseDouble(indat[i][j]);
//                System.out.print(df.format(dataTraining[i][j]) + "\t");
            }
//            System.out.print("\n");
        }

    }

    public void ambilDataLengkap() {
//        System.out.println("\nData Training");
        dataLkp = new double[indat.length][indat[0].length];
        for (int i = 0; i < indat.length; i++) {
            for (int j = 0; j < indat[0].length; j++) {
                dataLkp[i][j] = Double.parseDouble(indat[i][j]);
//                System.out.print(df.format(dataTraining[i][j]) + "\t");
            }
//            System.out.print("\n");
        }

    }
}
