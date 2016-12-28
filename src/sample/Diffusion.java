package sample;
import java.awt.*;


/**
 * Created by Vadim on 04.12.16.
 */
public class Diffusion {
    protected double D = 0.5;
    protected int Width = 1000;
    protected double X0 = 100;
    protected double XFirst = X0, XSecond = X0;
    protected Color colorFirst = Color.RED, colorSecond = Color.BLUE;
    protected double speedConst = 1.0;
    protected double defaultRelativeSpeed = 5.0 / 100.0;

    Diffusion() throws InterruptedException {

    }

    protected Color getColorSuperposition(double nFirst, double nSecond) {
        double nSum = nFirst + nSecond;
        nFirst /= nSum;
        nSecond /= nSum;
        return new Color((int)(nFirst * colorFirst.getRed() + nSecond * colorSecond.getRed()),
                (int)(nFirst * colorFirst.getGreen() + nSecond * colorSecond.getGreen()),
                (int)(nFirst * colorFirst.getBlue() + nSecond * colorSecond.getBlue()));
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

    public void setColorFirst(Color color1) {
        colorFirst = color1;
    }
    public void setColorSecond(Color color2) {
        colorSecond = color2;
    }

    public void start() {
        XFirst = X0;
        XSecond = X0;
    }

    public void update() {

    }

    public void multipleUpdate(int iterations, double relativeSpeed) {
        speedConst = relativeSpeed / defaultRelativeSpeed;
    }

    public double getX0() {
        return X0;
    }

    public Color getColor(int x) {
        return Color.GREEN;
    }

    public double getNFirst(int x) {
        return 1.0;
    }

    public double getNSecond(int x) {
        return 1.0;
    }
}