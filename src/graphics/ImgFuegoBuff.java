package graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;

/**
 *
 * @author pau
 */
public class ImgFuegoBuff extends BufferedImage {

    private byte[] baRaster;
    private Integer[] iaRaster;

    //256 colores --> 0.B  1.G  2.R
    private color[] paCo = new color[256];
    //private int[] arrIntensidades = new int[256];
    private int[][] arrIntensidades;

    public ImgFuegoBuff() {
        super(500, 500, TYPE_3BYTE_BGR);

        //Height -> altura //width -> anchura
        this.arrIntensidades = new int[this.getHeight()][this.getWidth()];
        this.baRaster = this.convertDataRasterToByteArray(this.getRaster());
        this.iaRaster = this.copyBaRasterToIntArray(this.baRaster);
        this.rellenarPaletaColores();
    }

    public void startFuego() {
        this.calcularIntensidades();
        this.dibujarFuego();

    }

    private void dibujarFuego() {
        for (int fil = 0; fil < this.arrIntensidades.length; fil++) {
            for (int col = 0; col < this.arrIntensidades[fil].length; col++) {

                //int intColor = this.paCo[this.arrIntensidades[fil][col]];
                int rejillaWidth = this.arrIntensidades.length;
                int intensidad = this.arrIntensidades[fil][col];

                int colorB = this.paCo[intensidad].getB();
                int colorG = this.paCo[intensidad].getG();
                int colorR = this.paCo[intensidad].getR();

                this.iaRaster[((fil * rejillaWidth) * 3 + col * 3)] = colorB;
                this.iaRaster[((fil * rejillaWidth) * 3 + col * 3) + 1] = colorG;
                this.iaRaster[((fil * rejillaWidth) * 3 + col * 3) + 2] = colorR;

            }

        }

        this.updateIaRaster(iaRaster);

    }

    private void calcularIntensidades() {
        this.generateSparks();
        this.computeFlameIntensities();
    }

    private void rellenarPaletaColores() {
        //
        for (int i = 0; i < 120; i++) {
            this.paCo[i] = new color(0, 0, 0);
        }
        for (int i = 120; i < 160; i++) {
            this.paCo[i] = new color(0, 30, 199);
        }
        for (int i = 160; i < 192; i++) {
            this.paCo[i] = new color(10, 120, 251);
        }
        for (int i = 192; i < 256; i++) {
            this.paCo[i] = new color(10, 255, 251);
        }
    }

    private void computeFlameIntensities() {
        for (int fil = 0; fil < this.arrIntensidades.length - 1; fil++) {
            for (int col = 1; col < this.arrIntensidades[fil].length - 1; col++) {
                //calcula la itensidad media de cada casilla
                this.arrIntensidades[fil][col] = ((this.arrIntensidades[fil + 1][col - 1]
                        + this.arrIntensidades[fil + 1][col]
                        + this.arrIntensidades[fil + 1][col + 1]) / 3);

                if (this.arrIntensidades[fil][col] < 0) {
                    this.arrIntensidades[fil][col] = 0;
                }
            }

        }

    }

    private void generateSparks() {
        //Generate random sparks
        for (int col = 0; col < this.arrIntensidades[0].length; col++) {
            int sorteo = (int) (Math.random() * 2);

            if (sorteo == 1) {
                this.arrIntensidades[this.getHeight() - 1][col] = 255;
            } else {
                this.arrIntensidades[this.getHeight() - 1][col] = 0;
            }

        }
    }

    /*
    //por defecto todos la lista está a 0 (color negro)
    public void crearChispas() {

        //colorea pixles random a lo largo de la imagen
        for (int i = 0; i < this.rejillaIntensidades[0].length; i++) {

            //numeros aleatorios entre 0 y 1
            int b = (int) (Math.random() * 2);

            //si 1 pintar, negro
            if (b == 1) {
                //la ultima fila, posicion "i"
                this.rejillaIntensidades[this.getHeight() - 1][i] = 0;

                //si no, pintar blanco
            } else {
                //la ultima fila, posicion "i"
                this.rejillaIntensidades[this.getHeight() - 1][i] = 255;
            }
        }

        //pasa de la rejilla bidimensional a la lista unidimensional 
        for (int fil = 0; fil < this.rejillaIntensidades.length; fil++) {
            for (int col = 0; col < this.rejillaIntensidades[fil].length; col++) {

                int intColor = paletaColores[this.rejillaIntensidades[fil][col]];
                int rejillaWidth = this.rejillaIntensidades.length;

                this.iaRaster[((fil * rejillaWidth) * 3 + col * 3)] = intColor;
                this.iaRaster[((fil * rejillaWidth) * 3 + col * 3) + 1] = intColor;
                this.iaRaster[((fil * rejillaWidth) * 3 + col * 3) + 2] = intColor;

            }

        }

        this.updateIaRaster(iaRaster);
    }

    private void calcularIntensidades() {
        for (int fil = 0; fil < this.rejillaIntensidades.length - 3; fil++) {
            for (int col = 0; col < this.rejillaIntensidades[0].length - 3; col++) {
                this.rejillaIntensidades[fil][col] = (this.rejillaIntensidades[fil + 1][col]
                        + this.rejillaIntensidades[fil + 1][col + 1]
                        + this.rejillaIntensidades[fil + 1][col - 1]);
            }

        }

    }

    private void calcularBase() {

        //colorea pixles random a lo largo de la imagen
        for (int i = 0; i < this.rejillaIntensidades[0].length; i++) {

            //numeros aleatorios entre 0 y 1
            int b = (int) (Math.random() * 2);

            //si 1 pintar, negro
            if (b == 1) {
                //la ultima fila, posicion "i"
                this.rejillaIntensidades[this.getHeight() - 1][i] = 0;

                //si no, pintar blanco
            } else {
                //la ultima fila, posicion "i"
                this.rejillaIntensidades[this.getHeight() - 1][i] = 255;
            }
        }
    }
    
    private void rellenarPaleta() {

        for (int i = 0; i < this.paletaColores.length; i += 3) {

            this.paletaColores[i] = i;
            this.paletaColores[i + 1] = i;
            this.paletaColores[i + 2] = i;

        }
    }
     */
    private void updateIaRaster(Integer[] iaRaster) {

        for (int i = 0; i < iaRaster.length; i++) {
            this.baRaster[i] = iaRaster[i].byteValue();

        }
    }

    private byte[] convertDataRasterToByteArray(Raster ras) {

        byte[] baDataRasterSource;

        baDataRasterSource = ((DataBufferByte) ras.getDataBuffer()).getData();
        return baDataRasterSource;
    }

    private Integer[] copyBaRasterToIntArray(byte[] baRaster) {
        Integer[] iaRasterCopy = new Integer[baRaster.length];

        for (int i = 0; i < baRaster.length; i++) {
            iaRasterCopy[i] = Byte.toUnsignedInt(baRaster[i]);
        }

        return iaRasterCopy;
    }

}

class color {

    int b, g, r;

    public color(int b, int g, int r) {
        this.b = b;
        this.g = g;
        this.r = r;
    }

    public int getB() {
        return b;
    }

    public int getG() {
        return g;
    }

    public int getR() {
        return r;
    }

}
