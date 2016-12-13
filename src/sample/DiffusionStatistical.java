package sample;
import java.awt.*;

/**
 * Created by Vadim on 06.12.16.
 */
public class DiffusionStatistical extends Diffusion {
    private MathStatistics statistics = new MathStatistics();
    private double startTime = 0.0, lastTime = 0.0;
    private double[] nFirst, nSecond;
    private double[] nFirst2, nSecond2;
    private double modelDispersion = 1;
    private double[] displacements;
    private void createStartParticles() {
        nFirst = new double[super.Width];
        nSecond = new double[super.Width];
        nFirst2 = new double[super.Width];
        nSecond2 = new double[super.Width];
        displacements = new double[super.Width];
        for (int x = 0; x < super.Width; ++x) {
            nFirst[x] = (x < super.X0 ?  1.0 : 0.0);
            nSecond[x] = (x > super.X0 ? 1.0 : 0.0);
        }
    }
    public DiffusionStatistical() throws InterruptedException {
        super();
        createStartParticles();
    }

    @Override
    public void start() {
        super.start();
        startTime = System.currentTimeMillis();
        lastTime = startTime;
        createStartParticles();
    }

    public void setModelDispersion(double dispersion) {
        modelDispersion = dispersion;
    }

    private void clearN2() {
        for (int i = 0; i < super.Width; ++i) {
            nFirst2[i] = 0;
            nSecond2[i] = 0;
        }
    }

    private double getBrightnessFirst() {
        return super.X0 / super.XFirst;
    }

    private double getBrightnessSecond() {
        return (super.Width - super.X0) / (super.Width - super.XSecond);
    }

    @Override
    public void update() {
        super.update();
        double t = 0.001 * (System.currentTimeMillis() - lastTime);
        double mathExpectationOfDisplacement = statistics.getXBySqrX(2.0 * super.D * t);
        XFirst += mathExpectationOfDisplacement;
        XSecond -= mathExpectationOfDisplacement;
        XFirst = Math.min(super.Width, XFirst);
        XSecond = Math.max(0.0, XSecond);
        clearN2();
        double sumPFirst = 0.0, sumPSecond = 0.0;
        for (int x = 0; x < super.Width; ++x) {
            sumPFirst += statistics.getProbability((double) x, super.XFirst, modelDispersion);
            sumPSecond += statistics.getProbability((double) x, super.XSecond, modelDispersion);
        }
        for (int x = 0; x < super.Width; ++x) {
            double pxFirst = statistics.getProbability((double) x, super.XFirst, modelDispersion);
            double pxSecond = statistics.getProbability((double) x, super.XSecond, modelDispersion);
            pxFirst *= 1.0 / sumPFirst;
            pxSecond *= 1.0 / sumPSecond;
            for (int x2 = 0; x2 < x; ++x2) {
                nFirst2[x2] += pxFirst * getBrightnessFirst();
            }
            for (int x2 = super.Width - 1; x2 > x; --x2) {
                nSecond2[x2] += pxSecond * getBrightnessSecond();
            }
        }
        for (int x = 0; x < super.Width; ++x) {
            nFirst[x] = nFirst2[x];
            nSecond[x] = nSecond2[x];
        }
        modelDispersion += 10;
        lastTime = System.currentTimeMillis();
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

    public double getTimeSeconds() {
        return 0.001 * (System.currentTimeMillis() - startTime);
    }
}