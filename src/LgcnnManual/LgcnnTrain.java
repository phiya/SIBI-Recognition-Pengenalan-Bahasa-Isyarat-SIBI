/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LgcnnManual;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Wolez
 */
public class LgcnnTrain {

    static DecimalFormat df = new DecimalFormat("0.00000");

    Normalisasi norm = new Normalisasi();
    Tools tool = new Tools();
    BacaFile file = new BacaFile();
    CalcDistance CalcDist = new CalcDistance();

    public static double[][] DataTraining;
    public static double[][] K;
    public static double GVarians;
    public static int Epochs;
    public static int Iterasi;

    public static double VarianTest;
    public static double YmaxTest;

    //Proses Training Data
    public void ProsesLatihLGCNN(double[][] dataTraining) {
        
        double[] kelasTR = new double [dataTraining.length];
        double[][] dataTR = new double[dataTraining.length][dataTraining[0].length-1];
        for(int z=0; z<dataTraining.length; z++){
            kelasTR[z] = (int) dataTraining[z][12];
            for (int za = 0; za<dataTraining[0].length - 1; za++){
                dataTR[z][za] = dataTraining[z][za];
            }
        }
        tool.printArray(dataTR, "DATATARRRRRR");

        //Normalisasi data training
        DataTraining = new double[dataTR.length][dataTR[0].length];
        DataTraining = norm.NormalisasiTrain(dataTR);
//        System.out.println("\nHasil Normalisasi :");
//        tool.printArray(DataTraining);

        Iterasi = 0;
//        int Epoch = 10;
        int Epoch;
        double Varian = 0;

        //Inisialisai Varian
        if (Varian != 0) {
            Varian = GVarians;
        } else {
            Varian = 0.4;
        }

        //Inisialisasi Epoch
        if (Epochs != 0) {
            Epoch = Epochs;
        } else {
            Epoch = 1;
        }

        double[] Varians = new double[DataTraining.length];
        double[][] RBF = new double[DataTraining.length][DataTraining.length];
        double Ymax = 0.9;
        double LR = 12;

        double[][] Y = new double[DataTraining.length][DataTraining.length];

        K = new double[kelasTR.length][DataTraining.length];
        double[][] DivEffect = new double[kelasTR.length][DataTraining.length];
        double[][] V = new double[kelasTR.length][DataTraining.length];
        double[][] W = new double[kelasTR.length][DataTraining.length];
        double[] Denom = new double[DataTraining.length];

        double[] NorLayer = new double[kelasTR.length];
        double[] Derivatif = new double[kelasTR.length];

        double HasilMax;
        int Indeks;
        double[][] HasilKelas = new double[DataTraining.length][2];
        ArrayList<Double> MaxL = new ArrayList<>();

        double Error = 0;
        double Total = 0.1;
        double ErrorLama = 0;
        double ErrorMAX = 1;

        double Error_Derivatif_A = 0;
        double Error_Derivatif_B = 0;
        double Error_Derivatif = 0;
        ArrayList<Double> VarianIterasi = new ArrayList<>();

        //Mengukur Jarak Data Training
        double[][] Jarak = new double[DataTraining.length][DataTraining.length];
        Jarak = CalcDist.calcDistanceTrain(DataTraining, DataTraining);
//        System.out.println("\nNilai Jarak : ");
//        tool.printArray(Jarak);

        while (Iterasi < Epoch && Math.abs(ErrorMAX) > 0.00001) {
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("\nIterasi Ke-" + Iterasi);
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
            Total = 0;

            //Update Varian dari iterasi sebelumnya
            if (Iterasi > 0) {
                VarianIterasi.add(Iterasi, VarianIterasi.get(Iterasi - 1) + LR * Error_Derivatif);
            } else {
                VarianIterasi.add(Varian);
            }

            Varians[0] = VarianIterasi.get(Iterasi);
            for (int i = 0; i < DataTraining.length; i++) {//data yang dilatih   //row = 33   
                HasilMax = 0;
                Indeks = 0;

                //Update Nilai varian per-Data
                if (i > 0 && (Varians[i - 1] + LR * Error_Derivatif) > 0) {
                    Varians[i] = Varians[i - 1] + LR * Error_Derivatif;
                } else if (i > 0 && Varians[i - 1] + LR * (Error_Derivatif) < 0) {
                    Varians[i] = Varians[i - 1];
                }
//                System.out.println("\nVarians " + i + " = " + df.format(Varians[i]));
                for (int j = 0; j < DataTraining.length; j++) {//data latih         //col = 33  

                    // Fungsi Aktifasi RBF ..............................................................................................................
                    RBF[i][j] = Math.exp(-1 * (Jarak[i][j] / (2 * (Math.pow(Varians[i], 2)))));
//                    System.out.println("KLASSSSSS SIZEEE : "+file.kelas.size());
                    for (int k = 0; k < kelasTR.length; k++) { //panjang kelas //3
                        //Berikan Nilai 0.9 pada kelas dan 0.1 pada Kelas yang lain
                        if (k == kelasTR[j]) {
                            K[k][j] = 0.9;
                        } else {
                            K[k][j] = 0.1;
                        }
//                        System.out.print("K[" + k + "," + j + "] = " + result[k][j] + "\t");

                        // Divergen Effect ...............................................................................................................
                        if (j == 0) {
                            DivEffect[k][j] = 0 + RBF[i][j] * Math.exp(K[k][j] - Ymax) * K[k][j];
                        } else if (j > 0) {
                            DivEffect[k][j] = DivEffect[k][j - 1] + RBF[i][j] * Math.exp(K[k][j] - Ymax) * K[k][j];
                        }
//                        System.out.print("DE[" + k + "," + j + "] = " + df.format(DivEffect[k][j]) + "\t");

                        // Hitung V .....................................................................................................................
                        if (j == 0) {
                            V[k][j] = 0 + 2 * ((Math.exp(K[k][j] - Ymax) * K[k][j]) * RBF[i][j] * (Jarak[i][j] / (Math.pow(Varians[i], 3))));
                        } else if (j > 0) {
                            V[k][j] = V[k][j - 1] + (2 * ((Math.exp(K[k][j] - Ymax) * K[k][j]) * RBF[i][j] * (Jarak[i][j] / (Math.pow(Varians[i], 3)))));
                        }
//                        System.out.print("V[" + k + "," + j + "] = " + df.format(V[k][j]) + "\t");

                        //Hitung W ......................................................................................................................
                        if (j == 0) {
                            W[k][j] = 0 + 2 * (RBF[i][j] * (Jarak[i][j] / (Math.pow(Varians[i], 3))));
                        } else if (j > 0) {
                            W[k][j] = W[k][j - 1] + (2 * (RBF[i][j] * (Jarak[i][j] / (Math.pow(Varians[i], 3)))));
                        }
//                        System.out.print("W[" + k + "," + j + "] = " + df.format(W[k][j]) + "\t");
                    }
                    //Denom .............................................................................................................................
                    if (j == 0) {
                        Denom[j] = 0 + RBF[i][j];
                    } else if (j > 0) {
                        Denom[j] = Denom[j - 1] + RBF[i][j];
                    }
//                    System.out.print("Denom [" + j + "] =" + df.format(Denom[j]));
//                    System.out.println();

                    // Hitung Normalisasi Layer dan Derivatif ...........................................................................................
                    for (int l = 0; l < kelasTR.length; l++) {
                        NorLayer[l] = DivEffect[l][j] / Denom[j];
                        Derivatif[l] = (V[l][j] - (W[l][j] * NorLayer[l])) / Denom[j];
                    }
                }
//                System.out.println("\nHasil Normalisasi Layer :");
//                tool.printArray(NorLayer);
//                System.out.println("\nHasil Derivatif :");
//                tool.printArray(Derivatif);

                //Mencari nilai maksimum untuk menentukan kelas dan indeks sebagai kelas : [Nilai, Kelas]
                for (int m = 0; m < kelasTR.length; m++) {
                    //Nilai kelas
                    if (HasilMax < NorLayer[m]) {
                        HasilMax = NorLayer[m];
                    }
                    //indeks kelas
                    if (NorLayer[m] == HasilMax) {
                        Indeks = m;
                    }
                }
//                System.out.println("\nHasil Nilai Layer Maksimum dan Kelas:");
//                System.out.println("[" + df.format(HasilMax) + ", " + Indeks + "]");

                //Hasil Array [Nilai, Kelas]
                HasilKelas[i][0] = HasilMax;
                HasilKelas[i][1] = Indeks;

                //Error
//                System.out.println("RESULT (" + Indeks + ", " + i + ") : " + result[Indeks][i]);
//                System.out.println("HASIL  : " + HasilKelas[i][0]);
                Error = K[Indeks][i] - HasilKelas[i][0];
//                System.out.println("Error = " + Error);

                //Total
                Total = Total + Math.abs(Error);
//                System.out.println("Total : " + Total);

                //Error Maksimal
                ErrorMAX = ErrorLama - Error;
//                System.out.println("Error Maksimum = " + ErrorMAX);

                //Error Lama
                ErrorLama = Error;
//                System.out.println("Error Lama = " + ErrorLama);

                //Error Derivatif/Turunan
//                System.out.println("\nResult = " + df.format(result[Indeks][i]));
//                System.out.println("Derivatif = " + df.format(Derivatif[Indeks]));
//                System.out.println("Norlayer = " + df.format(NorLayer[Indeks]));
                Error_Derivatif_A = K[Indeks][i] * (Derivatif[Indeks] / NorLayer[Indeks]);
                Error_Derivatif_B = (1 - K[Indeks][i]) * (Derivatif[Indeks] / (NorLayer[Indeks] - 1));
                Error_Derivatif = Error_Derivatif_A + Error_Derivatif_B;
//                System.out.println("\nError Derivatif A = " + df.format(Error_Derivatif_A));
//                System.out.println("Error Derivatif B = " + df.format(Error_Derivatif_B));
//                System.out.println("Error Derivatif = " + df.format(Error_Derivatif));

                //Update Varians Iterasi 
                VarianIterasi.add(Iterasi, Varians[DataTraining.length - 1]);
                VarianTest = Varians[DataTraining.length - 1];
//                System.out.println("Varian Iterasi = " + VarianIterasi.get(Iterasi));

                //Ymax
                MaxL.add(Iterasi, HasilKelas[DataTraining.length - 1][0]);
//                System.out.println("MaxL = " + MaxL.get(Iterasi));
            }

//            System.out.println("\nJarak : ");
//            tool.printArray(Jarak);
////
//            System.out.println("\nNilai RBF : ");
//            tool.printArray(RBF);
            System.out.println("\nHasil Nilai dan Kelas :");
            tool.printArray(HasilKelas,"Hasil Nilai dan Kelas");

            System.out.println("-------------------------------------------------------------------------------------------------");

            Ymax = Collections.max(MaxL);
            YmaxTest = Ymax;
            System.out.println("Ymax = " + Ymax);
            Iterasi++;
        }
//        System.out.println("Ymax = " + MaxL);
    }
}
