package sample;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vadim on 04.12.16.
 */
public class DiffusionDifferencial extends Diffusion {
    private double dt = 0.000001;
    private double dx = 0.011;
    private double nFirst[];
    private double n2First[];
    private double nSecond[];
    private double n2Second[];
    private double alpha = -super.D * dt/ (dx * dx);

    DiffusionDifferencial() throws InterruptedException  {
        super();
    }

    @Override
    public void start() {
        super.start();
        nFirst = new double[super.Width];
        n2First = new double[super.Width];
        nSecond = new double[super.Width];
        n2Second = new double[super.Width];
        alpha = -super.D * dt / (dx * dx);
        for (int x = 0; x < super.Width; ++x) {
            nFirst[x] = (x < super.getX0() ? 1.0 : 0.0);
            nSecond[x] = 1.0 - nFirst[x];
        }
    }

    @Override
    public void setBorder(double alpha) {
        super.setBorder(alpha);
    }

    @Override
    public void update() {
        super.update();
        for (int x = 0; x < super.Width; ++x) {
            if (x == 0) {n2First[x] = nFirst[x] - alpha * (nFirst[x + 1] - nFirst[x]); continue;}
            if (x == super.Width - 1) {n2First[x] = nFirst[x] + alpha * (nFirst[x] - nFirst[x - 1]); continue;}
            n2First[x] = nFirst[x] - alpha * (nFirst[x + 1] - nFirst[x]) + alpha * (nFirst[x] - nFirst[x - 1]);
        }
        for (int x = super.Width - 1; x >= 0; --x) {
            if (x == 0) {n2Second[x] = nSecond[x] + alpha * (nSecond[x] - nSecond[x + 1]); continue;}
            if (x == super.Width - 1) {n2Second[x] = nSecond[x] - alpha * (nSecond[x - 1] - nSecond[x]); continue;}
            n2Second[x] = nSecond[x] - alpha * (nSecond[x - 1] - nSecond[x]) + alpha * (nSecond[x] - nSecond[x + 1]);
        }
        for (int x = 0; x < super.Width; ++x) {
            nFirst[x] = n2First[x];
            nSecond[x] = n2Second[x];
            //nFirst[x] = Math.max(0.0, Math.min(1.0, nFirst[x]));
            //nSecond[x] = Math.max(0.0, Math.min(1.0, nSecond[x]));
            n2First[x] = n2Second[x] = 0;
        }
    }

    @Override
    public Color getColor(int x) {
        return getColorSuperposition(nFirst[x], nSecond[x]);
    }

    @Override
    public double getNFirst(int x) {
        return nFirst[x];
    }

    @Override
    public double getNSecond(int x) {
        return nSecond[x];
    }

}