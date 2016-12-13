package sample;
import java.awt.*;
import java.util.Random;

/**
 * Created by Vadim on 04.12.16.
 */
public class MathStatistics {
    public int sampleSize = 100;
    private Random rnd = new Random();

    public double nextRandDuble(double maxMod) {
        return maxMod * 2.0 * (rnd.nextDouble() - 0.5);
    }

    public double nextRandDouble(double min, double max) {
        double mid = (min + max) / 2.0;
        double len = (max - min);
        return mid + nextRandDuble(len);
    }

    public double getXBySqrX(double sqrX) {
        double ans = 0.0;
        for (int i = 0; i < sampleSize; ++i)
            ans += Math.sqrt(sqrX + nextRandDuble(sqrX)) / (double)sampleSize;
        return ans;
    }

    Dimension getDisplacement(double squareDistribution, double dimentionX, double dimentionY) {
        double x, y;
        y = nextRandDuble(dimentionY);
        x = nextRandDouble(0, 2 * squareDistribution);
        return new Dimension((int)x, (int)y);
    }

    public double[] getDistribution(double mathExpectation, double dispersion, int cnt) {
        double[] sample = new double[cnt];
        int innerIterations = 10;
        for (int i = 0; i < cnt; ++i) {
            double delta = .0;
            for (int it = 0; it < innerIterations; ++it) {
                delta += rnd.nextDouble();
            }
            delta -= (double)innerIterations / 2.0;
            delta /= (double)innerIterations / 2.0;
            sample[i] = mathExpectation + dispersion * delta;
        }
        return sample;
    }

    public double getProbability(double x, double mathExpectation, double dispersion) {
        return Math.exp(- (x - mathExpectation) * (x - mathExpectation) / (2.0 * dispersion)) / Math.sqrt(2.0 * Math.PI * dispersion);
    }

}