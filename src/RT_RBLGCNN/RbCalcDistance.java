/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RT_RBLGCNN;

/**
 *
 * @author Wolez
 */
public class RbCalcDistance {
    public static double[][] calcDistanceTrain(double[][] array1, double[][] array2) {
        double[][] jarak = new double[array2.length][array1.length];
        for (int i = 0; i < array2.length; i++) {        //row = 33
            for (int j = 0; j < array1.length; j++) {    //col = 33
                for (int k = 0; k < array1[0].length; k++) {    //col = 33
                    jarak[i][j] = jarak[i][j] + (Math.pow(Math.abs(array2[i][k] - array1[j][k]), 2));
                }
            }
        }
        return jarak;
    }
}
