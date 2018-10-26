package graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class MyBufferedImg extends BufferedImage {

    private byte[] baRaster;
    private Graphics gr;

    public MyBufferedImg(BufferedImage img, Graphics gr) {
        super(img.getColorModel(), img.getRaster(), img.getColorModel().isAlphaPremultiplied(), null);
        this.baRaster = this.convertDataRasterToByteArray(this.getRaster());
        //this.gr = gr;
    }
    
    //piblic methods
    
    public byte[] getBaRaster(){
        return this.baRaster;
    }
    
    public Integer[] getIaRaster(){
       return this.copyBaRasterToIntArray(this.baRaster);
    }

    public void doFade(String imgSrc, int frames) throws InterruptedException {

        //Obtener el raster de la segunda imagen
        Raster rasImg = this.getRasterImgToFade(imgSrc);

        //Obtener la diferencia de las dos iaRasters
        float[] iaDiferencia = this.calcularDiferenciaIaRasters(rasImg, frames);

        //Update raster img
        for (int i = 0; i < frames; i++) {
            Thread.sleep(100);
            this.updateFadeOutImg(iaDiferencia);
            //this.paint();
        }

    }

    public void paint(Graphics grr) {
        
        grr.drawImage(this, 800, 0, 500, 500, null);
       
    }
    
    //private methods

    private byte[] convertDataRasterToByteArray(Raster ras) {

        byte[] baDataRasterSource;

        baDataRasterSource = ((DataBufferByte) ras.getDataBuffer()).getData();
        return baDataRasterSource;
    }

    private byte[] copyIntArrayToByteArray(Integer[] biSource) {

        byte[] isCopy = new byte[biSource.length];

        for (int i = 0; i < biSource.length; i++) {
            isCopy[i] = biSource[i].byteValue();
        }

        return isCopy;
    }

    private byte[] copyFloatArrayToByteArray(float[] fiSource) {

        byte[] isCopy = new byte[fiSource.length];

        for (int i = 0; i < fiSource.length; i++) {
            isCopy[i] = (byte) fiSource[i];
        }

        return isCopy;
    }

    private float[] calcularDiferenciaIaRasters(Raster rasImg, int numFrames) {

        Integer[] iaRasterOriginal = this.copyDataRasterToIntArray(rasImg);
        Integer[] iaRasterToFade = this.copyDataRasterToIntArray(this.getRaster());

        float[] iaDiferencia = new float[iaRasterToFade.length];

        for (int pos = 0; pos < iaDiferencia.length; pos++) {
            iaDiferencia[pos] = (iaRasterOriginal[pos] - iaRasterToFade[pos]) / numFrames;
        }

        return iaDiferencia;
    }
    
    private Integer[] copyBaRasterToIntArray(byte[] baRaster){
        Integer[] iaRasterCopy = new Integer[baRaster.length];
        
        for (int i = 0; i < baRaster.length; i++) {
            iaRasterCopy[i]  = Byte.toUnsignedInt(baRaster[i]);
        }
        
        return iaRasterCopy;
    }

    private Integer[] copyDataRasterToIntArray(Raster ras) {

        byte[] baDataRasterSource;
        Integer[] iaRaster;

        baDataRasterSource = ((DataBufferByte) ras.getDataBuffer()).getData();
        iaRaster = new Integer[baDataRasterSource.length];

        for (int i = 0; i < baDataRasterSource.length; i++) {
            iaRaster[i] = Byte.toUnsignedInt(baDataRasterSource[i]);
        }

        return iaRaster;
    }

    private Raster getRasterImgToFade(String imgSrc) {

        Raster ras;
        try {
            ras = ImageIO.read(new File(imgSrc)).getRaster();

        } catch (IOException ex) {
            Logger.getLogger(MyBufferedImg.class.getName()).log(Level.SEVERE, null, ex);
            return null;
            //lanzar excepción
        }

        return ras;
    }

    private void updateFadeOutImg(float[] faDiferencia) {
        byte[] baRasterDiferencia = this.copyFloatArrayToByteArray(faDiferencia);

        for (int i = 0; i < baRaster.length; i++) {
            this.baRaster[i] += baRasterDiferencia[i];
        }

    }

}
