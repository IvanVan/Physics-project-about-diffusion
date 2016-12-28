package sample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.TimeUnit;

import static java.awt.Color.black;
// change
public class MyJFrame extends JFrame {

    private int Width = 1000;
    private int Height = 1000;
    private int[] X0Box, Y0Box, WidthBox, HeightBox;
    private int[] X0Graph, Y0Graph, WidthGraph, HeightGraph;
    private int[] currentPlotPosX, currentPlotPosY;
    private Color[] currentColor = {Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK};
    private boolean paused = false;
    private boolean oneMore = false;
    private KeyListener keyListener;
    private JSlider slider;
    private JPanel panel;
    private int minSpeed = 0;
    private int maxSpeed = 20000;
    private int defaultSpeed = 500;

    public void setWidth(int W) {
        Width = W;
    }

    public void setHeight(int H) {
        Height = H;
    }

    public void setBoxPlace(int i, int x0, int y0, int sizex, int sizey) {
        X0Box[i] = x0;
        Y0Box[i] = y0;
        WidthBox[i] = sizex;
        HeightBox[i] = sizey;
    }

    public void setGraphPlace(int i, int x0, int y0, int sizex, int sizey) {
        X0Graph[i] = x0;
        Y0Graph[i] = y0;
        WidthGraph[i] = sizex;
        HeightGraph[i] = sizey;
    }

    @Override
    public void paint(Graphics g) {
        //super.paint(g);
        Color color = g.getColor();
        g.setColor(Color.BLACK);
        g.setFont(new Font("Courier New", Font.ITALIC, 15));
        g.drawString("Нажмите ПРОБЕЛ для паузы/старта и R для рестарта ; также можете нажать D для установки скорости по умолчанию", 100, 35);
        g.setColor(color);
    }

    void drawLine(int i, int x, Color color) {
        Graphics g = panel.getGraphics();
        g.setColor(color);
        g.drawLine(X0Box[i] + x, Y0Box[i], X0Box[i] + x, Y0Box[i] + HeightBox[i]);
    }

    void setStartPlotPoint(int i, int x, double n) {
        int realX, realY;
        if (WidthBox[i] != 0 && HeightBox[i] != 0) {
            realX = X0Graph[i] +  x * WidthGraph[i] / WidthBox[i];
            realY = Y0Graph[i] + (int)((1.0 - n) * HeightGraph[i]);
        } else {
            realX = X0Graph[i] + x;
            realY = Y0Graph[i] + (int)((1.0 - n) * HeightGraph[i]);
        }
        currentPlotPosX[i] = realX;
        currentPlotPosY[i] = realY;
    }

    void setStartPlotPoint(int i, int x, double n, Color color) {
        int realX, realY;
        if (WidthBox[i] != 0 && HeightBox[i] != 0) {
            realX = X0Graph[i] +  x * WidthGraph[i] / WidthBox[i];
            realY = Y0Graph[i] + (int)((1.0 - n) * HeightGraph[i]);
        } else {
            realX = X0Graph[i] + x;
            realY = Y0Graph[i] + (int)((1.0 - n) * HeightGraph[i]);
        }
        currentPlotPosX[i] = realX;
        currentPlotPosY[i] = realY;
        currentColor[i] = color;
    }

    void drawPlotPoint(int i, int x, double n) {
        int realX, realY;
        if (WidthBox[i] != 0 && HeightBox[i] != 0) {
            realX = X0Graph[i] +  x * WidthGraph[i] / WidthBox[i];
            realY = Y0Graph[i] + (int)((1.0 - n) * HeightGraph[i]);
        } else {
            realX = X0Graph[i] + x;
            realY = Y0Graph[i] + (int)((1.0 - n) * HeightGraph[i]);
        }
        Graphics g = this.panel.getGraphics();
        g.setColor(currentColor[i]);
        g.drawLine(currentPlotPosX[i], currentPlotPosY[i], realX, realY);
        currentPlotPosX[i] = realX;
        currentPlotPosY[i] = realY;
    }

    void clearAt(int x0, int y0, int W, int H) {
        Graphics g = panel.getGraphics();
        g.clearRect(x0, y0, W, H);
    }

    void clearAll() {
        clearAt(0, 0, Width, Height);
    }

    void clearBox(int i) {
        clearAt(X0Box[i], Y0Box[i], WidthBox[i], HeightBox[i]);
    }

    void clearGraph(int i) {
        clearAt(X0Graph[i], Y0Graph[i], WidthGraph[i], HeightGraph[i]);
        drawRect(X0Graph[i], Y0Graph[i], WidthGraph[i], HeightGraph[i], Color.BLACK);
        Graphics g = this.panel.getGraphics();
        g.setColor(black);
        g.drawLine(X0Graph[i], Y0Graph[i], X0Graph[i] + WidthGraph[i], Y0Graph[i]);
        g.drawLine(X0Graph[i] + WidthGraph[i], Y0Graph[i], X0Graph[i] + WidthGraph[i], Y0Graph[i] + HeightGraph[i]);
        g.drawLine(X0Graph[i] + WidthGraph[i], Y0Graph[i] + HeightGraph[i], X0Graph[i], Y0Graph[i] + HeightGraph[i]);
        g.drawLine(X0Graph[i], Y0Graph[i] + HeightGraph[i], X0Graph[i], Y0Graph[i]);
    }

    void drawRect(int x0, int y0, int W, int H, Color color) {
        Graphics g = panel.getGraphics();
        g.setColor(color);
        g.fillRect(x0, y0, W, H);
    }

    void delay(int t) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(t);
    }

    void startGui() {
        setVisible(true);
    }

    public MyJFrame(int width, int height) throws InterruptedException {
        setBackground(Color.WHITE);
        setTitle("Diffusion models");
        setWidth(width);
        setHeight(height);
        setBounds(0, 0, width, height);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        X0Box = new int[4];
        Y0Box = new int[4];
        WidthBox = new int[4];
        HeightBox = new int[4];
        X0Graph = new int[4];
        Y0Graph = new int[4];
        WidthGraph = new int[4];
        HeightGraph = new int[4];
        currentPlotPosX = new int[4];
        currentPlotPosY = new int[4];
        currentPlotPosY = new int[4];
        keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    paused = !paused;
                    //System.out.println("space");
                }
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    oneMore = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    slider.setValue(defaultSpeed);
                }
            }
        };
        setBackground(Color.WHITE);
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.DARK_GRAY);
        slider = new JSlider(minSpeed, maxSpeed, defaultSpeed);
        Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
        slider.setBounds(50, (int) screenSize.getHeight() - 140, (int) screenSize.getWidth() - 200, 20);
        slider.addKeyListener(keyListener);
        panel.addKeyListener(keyListener);
        addKeyListener(keyListener);
        panel.add(slider);
        add(panel);
    }

    public double getSpeed() {
        return (double) slider.getValue() / (maxSpeed - minSpeed);
    }

    public void myRepaint() {
        paintComponents(this.panel.getGraphics());
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isWannaOneMore() {return oneMore;}

    public void dontWannaOneMore() {
        oneMore = false;
    }

    public void dontWannaPaused() {
        paused = false;
    }

    public void wannaPaused() {
        paused = true;
    }

    public void wannaOneMore() {oneMore = true;}
}