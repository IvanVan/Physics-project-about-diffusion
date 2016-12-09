package sample;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vadim on 04.12.16.
 */
public class DiffusionDifferencial extends Diffusion {
    private double dt = 0.000001;
    private double dx = 0.011;
    private double nRED[];
    private double n2RED[];
    private double nBLUE[];
    private double n2BLUE[];
    private double alpha = -super.D * dt/ (dx * dx);

    DiffusionDifferencial() throws InterruptedException  {
        super();
    }

    @Override
    public void start() {
        super.start();
        nRED = new double[super.Width];
        n2RED = new double[super.Width];
        nBLUE = new double[super.Width];
        n2BLUE = new double[super.Width];
        alpha = -super.D * dt/ (dx * dx);
        for (int x = 0; x < super.Width; ++x) {
            nRED[x] = (x < super.getX0() ? 1.0 : 0.0);
            nBLUE[x] = 1.0 - nRED[x];
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
            if (x == 0) {n2RED[x] = nRED[x] - alpha * (nRED[x + 1] - nRED[x]); continue;}
            if (x == super.Width - 1) {n2RED[x] = nRED[x] + alpha * (nRED[x] - nRED[x - 1]); continue;}
            n2RED[x] = nRED[x] - alpha * (nRED[x + 1] - nRED[x]) + alpha * (nRED[x] - nRED[x - 1]);
        }
        for (int x = super.Width - 1; x >= 0; --x) {
            if (x == 0) {n2BLUE[x] = nBLUE[x] + alpha * (nBLUE[x] - nBLUE[x + 1]); continue;}
            if (x == super.Width - 1) {n2BLUE[x] = nBLUE[x] - alpha * (nBLUE[x - 1] - nBLUE[x]); continue;}
            n2BLUE[x] = nBLUE[x] - alpha * (nBLUE[x - 1] - nBLUE[x]) + alpha * (nBLUE[x] - nBLUE[x + 1]);
        }
        for (int x = 0; x < super.Width; ++x) {
            nRED[x] = n2RED[x];
            nBLUE[x] = n2BLUE[x];
            //nRED[x] = Math.max(0.0, Math.min(1.0, nRED[x]));
            //nBLUE[x] = Math.max(0.0, Math.min(1.0, nBLUE[x]));
            n2RED[x] = n2BLUE[x] = 0;
        }
    }

    @Override
    public Color getColor(int x) {
        return new Color((int)(255 * nRED[x]), 0, (int)(255 * nBLUE[x]));
    }

    @Override
    public double getNRed(int x) {
        return nRED[x];
    }

    @Override
    public double getNBlue(int x) {
        return nBLUE[x];
    }

}