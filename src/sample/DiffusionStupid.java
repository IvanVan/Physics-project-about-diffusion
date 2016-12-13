package sample;
import java.awt.*;

/**
 * Created by Vadim on 04.12.16.
 */
public class DiffusionStupid extends Diffusion {
    private MathStatistics statistics = new MathStatistics();
    private double XFirst = super.X0;
    private double XSecond = super.X0;
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
        XFirst = super.X0;
        XSecond = super.X0;
    }

    @Override
    public void update() {
        super.update();
        double t = 0.001 * (System.currentTimeMillis() - lastTime);
        double deltaX = statistics.getXBySqrX(2.0 * super.D * t);
        XFirst += deltaX;
        XFirst = Math.min(XFirst, (double)super.Width);
        XSecond -= deltaX;
        XSecond = Math.max(0.0, XSecond);
        lastTime = System.currentTimeMillis();
    }

    public double getExpansionexpansionFirst() {
        return XFirst / super.X0;
    }

    public double getExpansionexpansionSecond() {return (super.Width - XSecond) / (super.Width - super.X0);}

    @Override
    public Color getColor(int x) {
        double nFirst = (x > XFirst ? 0.0 : 1.0 / getExpansionexpansionFirst());
        double nSecond = (x < XSecond ? 0.0 : 1.0 / getExpansionexpansionSecond());
        return getColorSuperposition(nFirst, nSecond);
    }

    @Override
    public double getNFirst(int x) {
        return (x > XFirst ? 0.0 : 1.0 / getExpansionexpansionFirst());
    }

    @Override
    public double getNSecond(int x) {
        return (x < XSecond ? 0.0 : 1.0 / getExpansionexpansionSecond());
    }

    public double getTimeSeconds() {
        return 0.001 * (System.currentTimeMillis() - startTime);
    }

}