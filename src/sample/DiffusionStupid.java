package sample;

import java.awt.*;

/**
 * Created by Vadim on 04.12.16.
 */
public class DiffusionStupid extends Diffusion {
    private MathStatistics statistics = new MathStatistics();
    private double XRed = super.X0;
    private double XBlue = super.X0;
    private double startTime = 0.0, lastTime = 0.0;

    public DiffusionStupid() throws InterruptedException {
        super();
        startTime = System.currentTimeMillis();
    }

    @Override
    public void start() {
        super.start();
        startTime = System.currentTimeMillis();
        lastTime = startTime;
        XRed = super.X0;
        XBlue = super.X0;
    }

    @Override
    public void update() {
        super.update();
        double t = 0.001 * (System.currentTimeMillis() - lastTime);
        double deltaX = statistics.getXBySqrX(2.0 * super.D * t);
        XRed += deltaX;
        XRed = Math.min(XRed, (double)super.Width);
        XBlue -= deltaX;
        XBlue = Math.max(0.0, XBlue);
        lastTime = System.currentTimeMillis();
    }

    public double getExpansionexpansionRed() {
        return XRed / super.X0;
    }

    public double getExpansionexpansionBlue() {return (super.Width - XBlue) / (super.Width - super.X0);}

    @Override
    public Color getColor(int x) {
        return new Color((x > XRed ? 0 : (int)(255.0 / getExpansionexpansionRed())), 0, (x < XBlue ? 0 : (int)(255.0 / getExpansionexpansionBlue())));
    }

    @Override
    public double getNRed(int x) {
        return (x > XRed ? 0.0 : 1.0 / getExpansionexpansionRed());
    }

    @Override
    public double getNBlue(int x) {
        return (x < XBlue ? 0.0 : 1.0 / getExpansionexpansionBlue());
    }

    public double getTimeSeconds() {
        return 0.001 * (System.currentTimeMillis() - startTime);
    }

}