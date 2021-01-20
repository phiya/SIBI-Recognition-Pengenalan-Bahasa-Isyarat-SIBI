/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LgcnnManual;

import java.text.DecimalFormat;

/**
 *
 * @author Wolez
 */
public class LgcnnTest {

    static DecimalFormat df = new DecimalFormat("0.00000");

    Normalisasi norm = new Normalisasi();
    Tools tool = new Tools();
    CalcDistance CalcDist = new CalcDistance();
    BacaFile file = new BacaFile();
    BacaFileTest fileTest = new BacaFileTest();
    LgcnnTrain lgcnnTrain = new LgcnnTrain();

    public static int Salah;
    public static int Benar;
    public static double Akurasi;

    //Proses Ujicoba LGCNN
    public void ProsesTestLGCNN(double[][] dataTesting) {
        double[] kelas = new double [dataTesting.length];
        double[][] data = new double[dataTesting.length][dataTesting[0].length-1];
        for(int z=0; z<dataTesting.length; z++){
            kelas[z] = (int) dataTesting[z][12];
            for (int za = 0; za<dataTesting[0].length - 1; za++){
                data[z][za] = dataTesting[z][za];
            }
        }
        tool.printArray(data, "DATATATATATATA :");
        
        
//        System.out.println("Kelassssssssssssss :");
//        tool.printArray(kelas);
//        System.out.println("Varians = " + lgcnnTrain.VarianTest + "\t\tYmax = " + lgcnnTrain.YmaxTest);

        double[][] DataTesting = new double[data.length][data[0].length];
        DataTesting = norm.NormalisasiTest(data);
//        System.out.println("\nHasil Normalisasi data Testing :");
//        tool.printArray(DataTesting);

        double Error = 0;
        Salah = 0;
        Benar = 0;
        Akurasi = 0;

        double[][] JarakTest = new double[DataTesting.length][lgcnnTrain.DataTraining.length];
        JarakTest = CalcDist.calcDistanceTrain(lgcnnTrain.DataTraining, DataTesting);
        System.out.println("Hasil Jarak data Testing");
        tool.printArray(JarakTest,"JARAKKKKKK :");
//        System.out.println();

        double[][] RBF = new double[dataTesting.length][lgcnnTrain.DataTraining.length];
        double[][] DivEffectTest = new double[kelas.length][lgcnnTrain.DataTraining.length];
        double[] DenomTest = new double[lgcnnTrain.DataTraining.length];
        double[] NorLayerTest = new double[kelas.length];
        double HasilMaxTest;
        int IndeksTest;

        for (int i = 0; i < DataTesting.length; i++) {
            System.out.println("\nData Testing Ke- " + i);
            System.out.println("-------------------------------------------------------------------------------------------------------------");

            HasilMaxTest = 0;
            IndeksTest = 0;

            for (int j = 0; j < lgcnnTrain.DataTraining.length; j++) {

                RBF[i][j] = Math.exp(-1 * (JarakTest[i][j] / (2 * (Math.pow(lgcnnTrain.VarianTest, 2)))));

                for (int k = 0; k < kelas.length; k++) { //size kelas training
                    if (k == kelas[j]) { 
                        lgcnnTrain.K[k][j] = 0.9;
                    } else {
                        lgcnnTrain.K[k][j] = 0.1;
                    }
//                    System.out.print("K[" + k + "," + j + "] = " + lgcnnTrain.K[k][j] + "\t");

                    // Divergen Effect ...............................................................................................................
                    if (j == 0) {
                        DivEffectTest[k][j] = 0 + RBF[i][j] * Math.exp(lgcnnTrain.K[k][j] - lgcnnTrain.YmaxTest) * lgcnnTrain.K[k][j];
                    } else if (j > 0) {
                        DivEffectTest[k][j] = DivEffectTest[k][j - 1] + RBF[i][j] * Math.exp(lgcnnTrain.K[k][j] - lgcnnTrain.YmaxTest) * lgcnnTrain.K[k][j];
                    }
//                    System.out.print("DE[" + k + "," + j + "] = " + df.format(DivEffectTest[k][j]) + "\t");
                }
                //Denom .............................................................................................................................
                if (j == 0) {
                    DenomTest[j] = 0 + RBF[i][j];
                } else if (j > 0) {
                    DenomTest[j] = DenomTest[j - 1] + RBF[i][j];
                }
//                System.out.print("Denom [" + j + "] =" + df.format(DenomTest[j]));
//                System.out.println();

                // Hitung Normalisasi Layer dan Derivatif ...........................................................................................
                for (int l = 0; l < kelas.length; l++) {
                    NorLayerTest[l] = DivEffectTest[l][j] / DenomTest[j];
                }
            }
//            System.out.println("\nHasil Normalisasi Layer Testing:");
//            tool.printArray(NorLayerTest);
//            System.out.println();

            //Mencari nilai maksimum untuk menentukan kelas dan indeks sebagai kelas : [Nilai, Kelas]
            for (int m = 0; m < kelas.length; m++) {
                //Nilai kelas
                if (HasilMaxTest < NorLayerTest[m]) {
                    HasilMaxTest = NorLayerTest[m];
                }
                //indeks kelas
                if (NorLayerTest[m] == HasilMaxTest) {
                    IndeksTest = m;
                }
            }
            System.out.println("\nHasil Nilai Layer Maksimum dan Kelas:");
            System.out.println("[" + df.format(HasilMaxTest) + ", " + IndeksTest + "]");

//            System.out.println(fileTest.atributTarget[i]);
            //if (IndeksTest == fileTest.atributTarget[i]) {
            System.out.println("Indek KELAS : "+IndeksTest +"KELAS : "+kelas[i]);
            if (IndeksTest == kelas[i]) {
                Benar = (i + 1) - Salah;
                System.out.println("Benar P = " + i + "\tPemenang = " + IndeksTest
                        + "\tHasil = " + df.format(HasilMaxTest) + "\t\tKelas Asli : " + file.kelas.get(IndeksTest));
            } else {
                Salah = Salah + 1;
                System.out.println("Salah ke-" + Salah + "\tHasil keluar = " + df.format(HasilMaxTest)
                        + "\tHasil Benar = " + LgcnnTrain.K[IndeksTest][i] + "\tSekarang = " + IndeksTest
                        + "\tKelas Asli : " + kelas[IndeksTest] + "\tBenar = " + kelas[i]);
            }
        }

        //Akurasi
        //100 * ((ntest) - yanlisgcnn) / (ntest);
        System.out.println("Benar : " + Benar + "\tBanyak data : " + DataTesting.length);
        Akurasi = 100 * (double) Benar / (double) DataTesting.length;
        System.out.println("Akurasi : " + Akurasi);

//        System.out.println("\nNilai RBF Test : ");
//        tool.printArray(RBF);
    }
}
