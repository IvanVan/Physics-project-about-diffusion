package  sample;

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
}