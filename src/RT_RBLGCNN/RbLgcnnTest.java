/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose RbTools | Templates
 * and open the template in the editor.
 */
package RT_RBLGCNN;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 *
 * @author Wolez
 */
public class RbLgcnnTest {

    static DecimalFormat df = new DecimalFormat("0.00000");

    RbNormalisasi norm = new RbNormalisasi();
    RbTools tool = new RbTools();
    RbCalcDistance CalcDist = new RbCalcDistance();
    RbBacaFileTest fileTest = new RbBacaFileTest();
    RbWriteRead WR = new RbWriteRead();
    GUI_RT_RBLGCNN real = new GUI_RT_RBLGCNN();

    RbFitur RbF = new RbFitur();

    public static String HASILRB;
    public static int Salah;
    public static int Benar;
    public static double Akurasi;

//    public static double Varian;
//    public static double Ymax;
    //Proses Ujicoba LGCNN
    public void ProsesTestLGCNN(double[][] dataTesting) throws IOException {

        double[][] DataTesting = new double[dataTesting.length][dataTesting[0].length];
        DataTesting = norm.NormalisasiTest(dataTesting);
//        System.out.println("Hasil RbNormalisasi data Testing :");
//        tool.printArray(DataTesting);

        double Error = 0;
        Salah = 0;
        Benar = 0;
        Akurasi = 0;
//        HASILRB = null;

        int SizeDataTraining = WR.ReadCSVint(RbF.PathSizeDatatraining);
        double[][] DataTraining = new double[SizeDataTraining][DataTesting[0].length];
        DataTraining = WR.ReadCSVRT(RbF.PathDataTraining);
//        System.out.println("\nData Training :");
//        tool.printArray(DataTraining);

        double[][] JarakTest = new double[DataTesting.length][SizeDataTraining];
        JarakTest = CalcDist.calcDistanceTrain(DataTraining, DataTesting);
//        System.out.println("\nHasil Jarak data Testing");
//        tool.printArray(JarakTest);
//        System.out.println();

        int SizeKelas = WR.ReadCSVint(RbF.PathSizeKelas);
        double[][] RBF = new double[DataTesting.length][SizeDataTraining];
        double[][] DivEffectTest = new double[SizeKelas][SizeDataTraining];
        double[] DenomTest = new double[SizeDataTraining];
        double[] NorLayerTest = new double[SizeKelas];
        double HasilMaxTest;
        int IndeksTest;

        double[] AtributTarget = new double[SizeDataTraining];
        AtributTarget = WR.ReadCSV1D(RbF.PathAtributTarget);
//        for (int i = 0; i < SizeDataTraining; i++) {
//            System.out.println(AtributTarget[i]);
//        }
        double[][] K = new double[SizeKelas][DataTraining.length];
//        Varian = 0;
//        Ymax = 0;
        double Varian;
        double Ymax;
        Varian = WR.ReadCSVdouble(RbF.PathVarianRT);
        Ymax = WR.ReadCSVdouble(RbF.PathYmaxRT);
//        System.out.println("VARIANNNN : " + Varian);
//        System.out.println("YMAXXXXX : " + Ymax);

        for (int i = 0; i < DataTesting.length; i++) {
//            System.out.println("\nData Testing Ke- " + i);
//            System.out.println("-------------------------------------------------------------------------------------------------------------");

            HasilMaxTest = 0;
            IndeksTest = 0;

            for (int j = 0; j < SizeDataTraining; j++) {

                RBF[i][j] = Math.exp(-1 * (JarakTest[i][j] / (2 * (Math.pow(Varian, 2)))));

                for (int k = 0; k < SizeKelas; k++) {
                    if (k == AtributTarget[j]) {
                        K[k][j] = 0.9;
                    } else {
                        K[k][j] = 0.1;
                    }
//                    System.out.print("K[" + k + "," + j + "] = " + K[k][j] + "\t");

                    // Divergen Effect ...............................................................................................................
                    if (j == 0) {
                        DivEffectTest[k][j] = 0 + RBF[i][j] * Math.exp(K[k][j] - Ymax) * K[k][j];
                    } else if (j > 0) {
                        DivEffectTest[k][j] = DivEffectTest[k][j - 1] + RBF[i][j] * Math.exp(K[k][j] - Ymax) * K[k][j];
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

                // Hitung RbNormalisasi Layer dan Derivatif ...........................................................................................
                for (int l = 0; l < SizeKelas; l++) {
                    NorLayerTest[l] = DivEffectTest[l][j] / DenomTest[j];
                }
            }
//            System.out.println("\nHasil RbNormalisasi Layer Testing:");
//            tool.printArray(NorLayerTest);
//            System.out.println();

            //Mencari nilai maksimum untuk menentukan kelas dan indeks sebagai kelas : [Nilai, Kelas]
            for (int m = 0; m < SizeKelas; m++) {
                //Nilai kelas
                if (HasilMaxTest < NorLayerTest[m]) {
                    HasilMaxTest = NorLayerTest[m];
                }
                //indeks kelas
                if (NorLayerTest[m] == HasilMaxTest) {
                    IndeksTest = m;
                }
            }
//            System.out.println("\nHasil Nilai Layer Maksimum dan Kelas:");
//            System.out.println("[" + df.format(HasilMaxTest) + ", " + IndeksTest + "]");

//            System.out.println(fileTest.atributTarget[i]);
            String[] kelas = new String[SizeKelas];
            //kelas = null;
//            System.out.println(RbF.PathKelas);
            kelas = WR.ReadCSVList(RbF.PathKelas);
//            for (int s=0; s<kelas.length; s++){
//                System.out.print(kelas[s]+"\t");
//            }
//            System.out.println();

            HASILRB = kelas[IndeksTest];
        }
    }
}
