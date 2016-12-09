package sample;

import java.awt.*;

/**
 * Created by Vadim on 04.12.16.
 */
public class Diffusion {
    protected double X0 = 100;
    protected double X = X0;
    protected double D = 0.5;
    protected int Width = 1000;

    Diffusion() throws InterruptedException {

    }

    public int getWidth() {
        return Width;
    }

    public void setD(double d) {
        D = d;
    }

    public void setBorder(double alpha) {
        X0 = alpha * Width;
    }

    public void setWidth(int width) { Width = width; }

    public void start() {

    }

    public void update() {

    }

    public double getX0() {
        return X0;
    }

    public Color getColor(int x) {
        return Color.GREEN;
    }

    public double getNRed(int x) {
        return 1.0;
    }

    public double getNBlue(int x) {
        return 1.0;
    }
}
