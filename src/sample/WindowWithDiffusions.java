package sample;
import javafx.scene.paint.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.TimeUnit;

import static javax.swing.UIManager.getColor;

/**
 * Created by Vadim on 04.12.16.
 */

public class WindowWithDiffusions extends Thread {
    private double D;
    private double borderAlpha;
    private Diffusion[] diffusionModels = new Diffusion[2];
    private MyJFrame window;
    private int Width, Height;
    private Color colorLeft = Color.RED, colorRight = Color.BLUE;
    private double correctionSpeed = 1.0;
    //private SpeedJFrame speedWindow;

    WindowWithDiffusions(double d, double borderalpha) throws InterruptedException {
        D = d;
        if (Math.abs(DiffusionDifferencial.getAlphaBy(D)) > 0.5) {
            //x(t) = sqrt(2Dt)
            // D' -> D / a => x'(t) -> x(t) / sqrt(a)
            // cD' = 0.5 => D' = 0.5 / c
            // correctionSpeed = 1/sqrt(0.5 / c   /    last / c) = sqrt(last / 0.5)
            correctionSpeed = Math.sqrt(Math.abs(DiffusionDifferencial.getAlphaBy(D)) / 0.5);
            D /= correctionSpeed * correctionSpeed;
        }
        borderAlpha = borderalpha;
        Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
        window = new MyJFrame(screenSize.width, screenSize.height);
        Width = (screenSize.width - 250) / 2;
        Height = (screenSize.height - 250) / 2;

        for (int i = 0; i < 2; ++i) {
            window.setBoxPlace(i, 50 + (Width + 50) * i, 50, Width, Height);
            window.setGraphPlace(i, 50 + (Width + 50) * i, 100 + Height, Width, Height);
            diffusionModels[i] = (i == 0 ? new DiffusionDifferencial() : new DiffusionStatistical());
            diffusionModels[i].setWidth(Width);
            diffusionModels[i].setBorder(borderAlpha);
            diffusionModels[i].setD(D);
            diffusionModels[i].start();
        }
        window.startGui();
        window.wannaPaused();
        //speedWindow = new SpeedJFrame((int) screenSize.getWidth() - 200, (int) screenSize.getHeight() - 60,200, 60);
    }

    public void setBorderAlpha(double alpha) {
        borderAlpha = alpha;
    }

    public void setD(double d) {
        D = d;
    }

    private Color getColorSuperposition(double nFirst, double nSecond) {
        double nSum = nFirst + nSecond;
        nFirst /= nSum;
        nSecond /= nSum;
        return new Color((int)(nFirst * colorLeft.getRed() + nSecond * colorRight.getRed()),
                (int)(nFirst * colorLeft.getGreen() + nSecond * colorRight.getGreen()),
                (int)(nFirst * colorLeft.getBlue() + nSecond * colorRight.getBlue()));
    }

    private void updateModels(int iteration, boolean delayedPause) {
        for (int i = 0; i < 2; ++i) {
            for (int x = 0; x < Width; ++x) {
                window.drawLine(i, x, diffusionModels[i].getColor(x));
            }
            if (iteration % 2 == 0 || delayedPause) {
                window.clearGraph(i);
                window.setStartPlotPoint(i, 0, diffusionModels[i].getNFirst(0), colorLeft);
                for (int x = 0; x < Width; x += 2) {
                    window.drawPlotPoint(i, x, diffusionModels[i].getNFirst(x));
                }
                window.drawPlotPoint(i, Width - 1, diffusionModels[i].getNFirst(Width - 1));
                window.setStartPlotPoint(i, Width - 1, diffusionModels[i].getNSecond(Width - 1), colorRight);
                for (int x = Width - 1; x >= 0; x -= 2) {
                    window.drawPlotPoint(i, x, diffusionModels[i].getNSecond(x));
                }
                window.drawPlotPoint(i, 0, diffusionModels[i].getNSecond(0));
            }
            diffusionModels[i].multipleUpdate(5000, window.getSpeed());
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        int iteration = 0;
        for (int i = 0; i < 2; ++i) {
            diffusionModels[i].setColorFirst(colorLeft);
            diffusionModels[i].setColorSecond(colorRight);
        }
        updateModels(-1, true);
        boolean delayedPause = false;
        window.dontWannaPaused();
        window.wannaOneMore();
        int x = 0;
        while (true) {
            if (!delayedPause && window.isWannaOneMore()) {
                window.dontWannaOneMore();
                window.dontWannaPaused();
                delayedPause = true;
                for (int i = 0; i < 2; ++i) {
                    try {
                        diffusionModels[i] = (i == 0 ? new DiffusionDifferencial() : new DiffusionStatistical());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    diffusionModels[i].setWidth(Width);
                    diffusionModels[i].setBorder(borderAlpha);
                    diffusionModels[i].setD(D);
                    diffusionModels[i].setColorFirst(colorLeft);
                    diffusionModels[i].setColorSecond(colorRight);
                    diffusionModels[i].start();
                }
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!delayedPause && window.isPaused()) {
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            updateModels(iteration, delayedPause);
            if (delayedPause) {
                window.wannaPaused();
                window.dontWannaOneMore();
                delayedPause = false;
            }
            ++iteration;
        }
    }
    public void setColorFirst(Color colorFirst) {
        colorLeft = colorFirst;
    }

    public void setColorFirstByRGB(int R, int G, int B) {colorLeft = new Color(R, G, B);}

    public void setColorSecond(Color colorSecond) {
        colorRight = colorSecond;
    }

    public void setColorSecondByRGB(int R, int G, int B) {colorRight = new Color(R, G, B);}
}