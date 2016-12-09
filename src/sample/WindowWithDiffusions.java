package sample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.TimeUnit;

import static javax.swing.UIManager.getColor;

/**
 * Created by Vadim on 04.12.16.
 */

// Application of git is topchic!!!


public class WindowWithDiffusions extends Thread {
    private double D;
    private double borderAlpha;
    private Diffusion[] diffusionModels = new Diffusion[2];
    private MyJFrame window;
    private int Width, Height;
    private KeyListener keyListener;

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
            diffusionModels[i] = (i == 0 ? new DiffusionDifferencial() : new DiffusionStupid());
            diffusionModels[i].setWidth(Width);
            diffusionModels[i].setBorder(borderAlpha);
            diffusionModels[i].setD(D);
            diffusionModels[i].start();
        }

        JLabel label = new JLabel("Press 'space' to start/pause   and   'R' to restart");
        //label.setBounds(50, Height - 100, Width - 50, 100);
        label.setBackground(Color.green);
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

    public void setColorFirst(Color colorFirst) {

    }

    public void setColorSecond(Color colorSecond) {

    }

    @Override
    public void run() {
        int iteration = 0;
        boolean delayedPause = true;
        window.dontWannaPaused();
        window.dontWannaOneMore();
        while (true) {
            if (window.isWannaOneMore()) {
                window.dontWannaOneMore();
                window.dontWannaPaused();
                delayedPause = true;
                for (int i = 0; i < 2; ++i) {
                    try {
                        diffusionModels[i] = (i == 0 ? new DiffusionDifferencial() : new DiffusionStupid());
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    }
                    diffusionModels[i].setWidth(Width);
                    diffusionModels[i].setBorder(borderAlpha);
                    diffusionModels[i].setD(D);
                    diffusionModels[i].start();
                }
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
            if (window.isPaused()) {
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
                continue;
            }
            for (int i = 0; i < 2; ++i) {
                for (int x = 0; x < Width; ++x) {
                    window.drawLine(i, x, diffusionModels[i].getColor(x));
                }
                if (iteration % 2 == 0) {
                    window.clearGraph(i);
                    window.setStartPlotPoint(i, 0, diffusionModels[i].getNRed(0), Color.red);
                    for (int x = 0; x < Width; x += 10) {
                        window.drawPlotPoint(i, x, diffusionModels[i].getNRed(x));
                    }
                    window.drawPlotPoint(i, Width - 1, diffusionModels[i].getNRed(Width - 1));
                    window.setStartPlotPoint(i, Width - 1, diffusionModels[i].getNBlue(Width - 1), Color.blue);
                    for (int x = Width - 1; x >= 0; x -= 10) {
                        window.drawPlotPoint(i, x, diffusionModels[i].getNBlue(x));
                    }
                    window.drawPlotPoint(i, 0, diffusionModels[i].getNBlue(0));
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
                    //e.printStackTrace();
                }
            }
            if (delayedPause) {
                window.wannaPaused();
                delayedPause = false;
            }
            ++iteration;
        }
    }
}
