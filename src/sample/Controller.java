package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class Controller {
    @FXML
    AnchorPane choosenWindow, waterDiffusion, gasDiffusion;

    @FXML
    Button openWaterDiffusion, openGasDiffusion, showWaterDiffusion, backToMainWindow1;

    @FXML
    TextField firstSubstance, dDiffusion;

    @FXML
    ColorPicker colorOfFirst, colorOfSecond;

    @FXML
    Label warning;

    private void setVisibleChoosenWindow (boolean type){
        choosenWindow.setVisible(type);
    }

    private void setVisibleWaterDiffusion (boolean type){
        waterDiffusion.setVisible(type);
    }

    private void setVisibleGasDiffusion (boolean type){
        gasDiffusion.setVisible(type);
    }

    @FXML
    void setBackToMainWindow(){
        waterDiffusion.setVisible(false);
        gasDiffusion.setVisible(false);
        choosenWindow.setVisible(true);
    }

    @FXML
    void setOpenWaterDiffusion (){
        setVisibleChoosenWindow(false);
        setVisibleGasDiffusion(false);
        setVisibleWaterDiffusion(true);
    }

    @FXML
    void setBackToMainWindow2(){
        setBackToMainWindow();
    }

    @FXML
    void setOpenGasDiffusion () {
        setVisibleWaterDiffusion(false);
        setVisibleChoosenWindow(false);
        setVisibleGasDiffusion(true);
    }

    private boolean check (){
        String s = firstSubstance.getText();
        if (s.equals("")){
            warning.setText("Вы не ввели пропорцию!");
            return false;
        }
        double val = Double.parseDouble(s);
        if (val > 100. || val < 0){
            warning.setText("Вы ввели некорректное значени пропорции!");
            return false;
        }
        if (dDiffusion.getText().equals("")){
            warning.setText("Вы не ввели коэффициент диффузии!");
            return false;
        }
        val = Double.parseDouble(dDiffusion.getText());
        if (val < 0.){
            warning.setText("Вы ввели некорректный коэффициент диффузии");
            return false;
        }
        return true;
    }

    @FXML
    void setShowWaterDiffusion() throws InterruptedException {
        if (check()){
            Color first = colorOfFirst.getValue();
            Color second = colorOfSecond.getValue();
            String s = firstSubstance.getText();
            double k = Double.parseDouble(s) / 100.;
            double koef = Double.parseDouble(dDiffusion.getText());
            WindowWithDiffusions window = new WindowWithDiffusions(koef, k);
            window.start();
        }
    }
}
