/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RT_LGCNN;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Wolez
 */
public class Tools {
    static DecimalFormat df = new DecimalFormat("#.##");
    
        public void sortingArray(double[][] array, int col) {
        Arrays.sort(array, new Comparator<double[]>() {
            @Override
            public int compare(double[] s1, double[] s2) {
                return Double.compare(s1[col], s2[col]);
            }
        });
    }

    public static void printArray(double[][] a) {
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
