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
    private KeyListener keyListener;
    private Color colorLeft = Color.RED, colorRight = Color.BLUE;

    WindowWithDiffusions(double d, double borderalpha) throws InterruptedException {
        D = d;
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

        //JLabel label = new JLabel("Press 'space' to start/pause   and   'R' to restart");
        JButton label = new JButton("BUTTOOOON");
        label.setBounds(50, screenSize.height - 110, 100, 10);
        //label.setBackground(Color.green);
        window.addComponent(label);
        window.startGui();
        window.wannaPaused();
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

    private void updateModels(int iteration) {
        for (int i = 0; i < 2; ++i) {
            for (int x = 0; x < Width; ++x) {
                window.drawLine(i, x, diffusionModels[i].getColor(x));
            }
            if (iteration % 2 == 0) {
                window.clearGraph(i);
                window.setStartPlotPoint(i, 0, diffusionModels[i].getNFirst(0), colorLeft);
                for (int x = 0; x < Width; x += 5) {
                    window.drawPlotPoint(i, x, diffusionModels[i].getNFirst(x));
                }
                window.drawPlotPoint(i, Width - 1, diffusionModels[i].getNFirst(Width - 1));
                window.setStartPlotPoint(i, Width - 1, diffusionModels[i].getNSecond(Width - 1), colorRight);
                for (int x = Width - 1; x >= 0; x -= 5) {
                    window.drawPlotPoint(i, x, diffusionModels[i].getNSecond(x));
                }
                window.drawPlotPoint(i, 0, diffusionModels[i].getNSecond(0));
            }
            if (i == 0) {
                for (int j = 0; j < 5000; ++j) {
                    diffusionModels[i].update();
                }
            } else {
                diffusionModels[i].update();
            }
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
        for (iteration = 0; iteration < 2; ++iteration)
            updateModels(0);
        boolean delayedPause = false;
        window.dontWannaPaused();
        window.wannaOneMore();
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
            updateModels(iteration);
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