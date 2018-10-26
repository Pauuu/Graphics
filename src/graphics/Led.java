/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

/**
 *
 * @author pau
 */
public class Led {

    private Integer[] iaColores;

    public Led(Integer[] iaColores) {
        this.iaColores = iaColores;
    }

    public Integer getColorMedio(int bgr) {

        int auxB, auxG, auxR;
        int totalPxls;
        int iColorMedioB;
        int iColorMedioG;
        int iColorMedioR;
        Integer[] bgrMedio = new Integer[3];

        bgrMedio[0] = 0; //variable might not have been initialized
        bgrMedio[1] = 0;
        bgrMedio[2] = 0;

        totalPxls = this.iaColores.length;

        for (int pos = 0; pos < totalPxls; pos += 3) {

            bgrMedio[0] += this.iaColores[pos]; //suma el total para luego dividir y hacer la media
            bgrMedio[1] += this.iaColores[pos + 1];
            bgrMedio[2] += this.iaColores[pos + 2];
        }

        bgrMedio[0] /= (totalPxls / 3);    //calcula el valor medio
        bgrMedio[1] = bgrMedio[1] / (totalPxls / 3);
        bgrMedio[2] = bgrMedio[2] / (totalPxls / 3);

        return bgrMedio[bgr];
    }

}
