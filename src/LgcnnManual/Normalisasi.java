/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LgcnnManual;

import java.util.Arrays;

/**
 *
 * @author Wolez
 */
public class Normalisasi {
    static double[] Min;
    static double[] Max;
    
    //Normalisasi Data Training
    public static double[][] NormalisasiTrain(double[][] Data) {

        double[][] Transpose = new double[Data[0].length][Data.length];
        double[][] Transpose2 = new double[Data[0].length][Data.length];
        if (Data.length > 0) {
            for (int i = 0; i < Data[0].length; i++) {
                for (int j = 0; j < Data.length; j++) {
                    Transpose[i][j] = Data[j][i];
                    Transpose2[i][j] = Data[j][i];
                }
            }
        }
//        System.out.println("\nHasil Transpose :");
//        printArray(Transpose);
        // mendapatkan Min dan Max
        Max = new double[Data[0].length];
        Min = new double[Data[0].length];
        for (int i = 0; i < Transpose2.length; i++) {
            Arrays.sort(Transpose2[i]); //Sorting per-baris array dari nilai terkecil ke besar
            Min[i] = Transpose2[i][0]; //mengambil nilai terkecil / nilai ke- 0 
            Max[i] = Transpose2[i][Transpose2[i].length - 1]; // mengambil nilai terbesat / nilai yg terakhir
        }
//        System.out.println("\nHasil Max :");
//        printArray(Max);
//        System.out.println("\nHasil Min :");
//        printArray(Min);

        //Menormalisasi setiap data
        double[][] Normalisasi = new double[Data[0].length][Data.length];
        for (int i = 0; i < Transpose.length; i++) {
            for (int j = 0; j < Transpose[0].length; j++) {
                if (Transpose[i][j] == 0){
                    Normalisasi[i][j] = 0;
                }else{
                    Normalisasi[i][j] = (Transpose[i][j] - Min[i]) / (Max[i] - Min[i]);
                }
                //System.out.print(Data[j][i]+"");
            }
            //System.out.println();
        }
//        System.out.println("\nHasil Normalisasi :");
//        printArray(Normalisasi);

        //Kembalikan data dari transpose
        double[][] HasilNormalisasi = new double[Data.length][Data[0].length];
        if (Data.length > 0) {
            for (int i = 0; i < Data.length; i++) {
                for (int j = 0; j < Data[0].length; j++) {
                    HasilNormalisasi[i][j] = Normalisasi[j][i];
                }
            }
        }
//        System.out.println("\nHasil Data normalisasi kembali dari Transpose :");
//        printArray(HasilNormalisasi);

        return HasilNormalisasi;
    }
    
    
     //Normalisasi Data Testing
    public static double[][] NormalisasiTest(double[][] Data) {

        double[][] Transpose = new double[Data[0].length][Data.length];
        double[][] Transpose2 = new double[Data[0].length][Data.length];
        if (Data.length > 0) {
            for (int i = 0; i < Data[0].length; i++) {
                for (int j = 0; j < Data.length; j++) {
                    Transpose[i][j] = Data[j][i];
                    Transpose2[i][j] = Data[j][i];
                }
            }
        }
//        System.out.println("\nHasil Transpose :");
//        printArray(Transpose);
        // mendapatkan Min dan Max
//        double[] Max = new double[Data[0].length];
//        double[] Min = new double[Data[0].length];
//        for (int i = 0; i < Transpose2.length; i++) {
//            Arrays.sort(Transpose2[i]); //Sorting per-baris array dari nilai terkecil ke besar
//            Min[i] = Transpose2[i][0]; //mengambil nilai terkecil / nilai ke- 0 
//            Max[i] = Transpose2[i][Transpose2[i].length - 1]; // mengambil nilai terbesat / nilai yg terakhir
//        }
//        System.out.println("\nHasil Max :");
//        printArray(Max);
//        System.out.println("\nHasil Min :");
//        printArray(Min);

        //Menormalisasi setiap data test dengan menggunakan Min dan Max dari data Training
        double[][] Normalisasi = new double[Data[0].length][Data.length];
        for (int i = 0; i < Transpose.length; i++) {
            for (int j = 0; j < Transpose[0].length; j++) {
                if (Transpose[i][j] == 0){
                    Normalisasi[i][j] = 0;
                }else if (Transpose[i][j] > 0){
                    Normalisasi[i][j] = (Transpose[i][j] - Min[i]) / (Max[i] - Min[i]);
                }
//                System.out.print(Data[j][i]+"");
            }
//            System.out.println();
        }
//        System.out.println("\nHasil Normalisasi :");
//        printArray(Normalisasi);

        //Kembalikan data dari transpose
        double[][] HasilNormalisasi = new double[Data.length][Data[0].length];
        if (Data.length > 0) {
            for (int i = 0; i < Data.length; i++) {
                for (int j = 0; j < Data[0].length; j++) {
                    HasilNormalisasi[i][j] = Normalisasi[j][i];
                }
            }
        }
//        System.out.println("\nHasil Data normalisasi kembali dari Transpose :");
//        printArray(HasilNormalisasi);

        return HasilNormalisasi;
    }
    
}
