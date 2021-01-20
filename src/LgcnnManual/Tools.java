/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LgcnnManual;

import java.util.Arrays;
import java.util.Comparator;
import static LgcnnManual.BacaFile.df;

/**
 *
 * @author Wolez
 */
public class Tools {

    public void sortingArray(double[][] array, int col) {
        Arrays.sort(array, new Comparator<double[]>() {
            @Override
            public int compare(double[] s1, double[] s2) {
                return Double.compare(s1[col], s2[col]);
            }
        });
    }

    public static void printArray(double[][][] a,String judul) {
        for (int i = 0; i < a.length; i++) {
            System.out.println(judul+" "+(i+1));
            for (int j = 0; j < a[0].length; j++) {
                for (int k = 0; k < a[0][0].length; k++) {
                    System.out.print(df.format(a[i][j][k]) + "\t");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static void printArray(double[][] a,String judul) {
        System.out.println(judul);
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                System.out.print(df.format(a[i][j]) + "\t");
            }
            System.out.println();
        }
    }

    public static void printArray(double[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.println(df.format(a[i]));
        }
    }
}
