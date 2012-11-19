package gui;


import java.util.Random;

/**
 *  Feeds Test Data
 * 
 * @author mattvertescher
 */
public class DataTest {
    
    Random rng;
    int increment; 
    
    public DataTest() {
        rng = new Random();
    }
    
    public float[] getTimes() {
        float[] f = new float[100];
        for (int i = 0; i < 100; i++) {
            f[i] = i + increment;
        }
        return f;
    }
    
    public float[] getStockPrice() {
        float[] f = new float[100];
        for (int i = 0; i < 100; i++) {
            f[i] = 10 * rng.nextFloat();
        }
        return f;
    }
    
    public float[] getFast() {
        float[] f = new float[100];
        for (int i = 0; i < 100; i++) {
            f[i] = 11 *rng.nextFloat();
        }
        return f;
    }
    
    public float[] getSlow() {
        float[] f = new float[100];
        for (int i = 0; i < 100; i++) {
            f[i] = 9 * rng.nextFloat();
        }
        return f;
    }
}
