package sample;

import java.awt.*;

/**
 * Created by Vadim on 06.12.16.
 */
public class DiffusionStatistical extends Diffusion {
    private MathStatistics statistics = new MathStatistics();
    private double startTime = 0.0, lastTime = 0.0;
    private Dimension[] particlesRed, particlesBlue;
    private void createStartParticles() {
        particlesRed = new Dimension[(int) (super.X0 * (super.Width / 2))];
        particlesBlue = new Dimension[(int) ((super.Width - super.X0) * (super.Width / 2))];
        for (int w = 0; w < super.X0; ++w) {
            for (int h = 0; h < super.Width / 2; ++h) {
                System.out.println("w = " + w + " ; h = " + h);
                particlesRed[w * (super.Width / 2) + h] = new Dimension(w, h);
            }
        }
        for (int w = 0; w < super.Width - super.X0; ++w) {
            for (int h = 0; h < super.Width / 2; ++h) {
                particlesBlue[w * (super.Width / 2) + h] = new Dimension(w, h);
            }
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

    @Override
    public void update() {
        super.update();
        double t = 0.001 * (System.currentTimeMillis() - lastTime);
        double squareDeltaX = 2.0 * super.D * t;
        for (int i = 0; i < particlesRed.length; ++i) {
            Dimension displacement = statistics.getDisplacement(squareDeltaX, super.Width, super.Width / 2);
            particlesRed[i].setSize(particlesRed[i].getWidth() + displacement.getWidth(), particlesRed[i].getHeight() + displacement.getHeight());
        }
        for (int i = 0; i < particlesRed.length; ++i) {
            Dimension displacement = statistics.getDisplacement(squareDeltaX, super.Width, super.Width / 2);
            particlesBlue[i].setSize(particlesBlue[i].getWidth() - displacement.getWidth(), particlesBlue[i].getHeight() + displacement.getHeight());
        }
        lastTime = System.currentTimeMillis();
    }


    @Override
    public Color getColor(int x) {
        int cntRed = 0, cntBlue = 0;
        for (int i = 0; i < particlesRed.length; ++i) {
            if (particlesRed[i].getWidth() == x) ++cntRed;
        }
        for (int i = 0; i < particlesBlue.length; ++i) {
            if (particlesBlue[i].getWidth() == x) ++cntBlue;
        }
        return new Color(255 * cntRed / (super.Width / 2),0,255 * cntBlue / (super.Width / 2));
    }

    @Override
    public double getNRed(int x) {
        int cntRed = 0;
        for (int i = 0; i < particlesRed.length; ++i) {
            if (particlesRed[i].getWidth() == x) ++cntRed;
        }
        return (double)cntRed / ((double)super.Width / 2);
    }

    @Override
    public double getNBlue(int x) {
        int cntBlue = 0;
        for (int i = 0; i < particlesBlue.length; ++i) {
            if (particlesBlue[i].getWidth() == x) ++cntBlue;
        }
        return (double)cntBlue / ((double)super.Width / 2);
    }

    public double getTimeSeconds() {
        return 0.001 * (System.currentTimeMillis() - startTime);
    }
}
