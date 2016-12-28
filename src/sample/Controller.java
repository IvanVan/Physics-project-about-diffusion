package sample;

import javafx.beans.binding.DoubleExpression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.omg.CORBA.Object;

import java.util.Observable;

import static java.awt.SystemColor.window;

/**
 * Created by Vanya on 13.12.2016.
 */

public class Controller {
    @FXML
    Slider howPerc, howPercent;

    @FXML
    AnchorPane choosenWindow, waterDiffusion, gasDiffusion, addNewSub;

    @FXML
    Button openWaterDiffusion, openGasDiffusion, showWaterDiffusion, backToMainWindow1;

    @FXML
    TextField firstSubstance, dDiffusion, pres, temp, sub1, sub2, gasSub, newMas, newRad, newName;

    @FXML
    ColorPicker colorOfFirst, colorOfSecond, colorSecondGas, colorFirstGas;

    @FXML
    Label warning, warningGas, warningNew;

    @FXML
    ChoiceBox howProcess, subs1, subs2, box;

    @FXML
    void getPer (){
        double newVal = howPerc.getValue();
        String s = Double.toString(newVal);
        newVal = howPercent.getValue();
        firstSubstance.setText(s);
        s = Double.toString(newVal);
        gasSub.setText(s);
    }



    void clearAll (){
        newMas.setText("");
        newRad.setText("");
        newName.setText("");
        firstSubstance.setText("");
        dDiffusion.setText("");
        pres.setText("");
        temp.setText("");
        gasSub.setText("");
        warning.setText("");
        warningGas.setText("");
        warningNew.setText("");
    }

    private void setVisibleChoosenWindow (boolean type){
        choosenWindow.setVisible(type);
    }

    private void setVisibleWaterDiffusion (boolean type){
        waterDiffusion.setVisible(type);
    }

    private void setVisibleGasDiffusion (boolean type){
        gasDiffusion.setVisible(type);
    }

    private void setVisibleAddNew (boolean type) {
        addNewSub.setVisible(type);
    }

    @FXML
    void setBackToMainWindow(){
        clearAll();
        addNewSub.setVisible(false);
        waterDiffusion.setVisible(false);
        gasDiffusion.setVisible(false);
        choosenWindow.setVisible(true);
    }

    @FXML
    void setOpenWaterDiffusion (){
        ObservableList <String> q = FXCollections.observableArrayList();
        q.add("Сравнение моделей");
        q.add("График энтропии");
        howProcess.setItems(q);
        howProcess.setMaxSize(150., 25.);
        howProcess.setMinSize(150., 25.);
        clearAll();
        setVisibleAddNew(false);
        setVisibleChoosenWindow(false);
        setVisibleGasDiffusion(false);
        setVisibleWaterDiffusion(true);
    }

    @FXML
    void setBackToMainWindow2(){
        clearAll();
        setBackToMainWindow();
    }

    @FXML
    void setOpenGasDiffusion () {
        ObservableList <String> q = FXCollections.observableArrayList();
        q.add("Сравнение моделей");
        q.add("График энтропии");
        box.setItems(q);
        box.setMaxSize(140., 22.);
        box.setMinSize(140., 22.);
        clearAll();
        subs1.setItems(InformationAboutSubstance.getGas());
        subs2.setItems(InformationAboutSubstance.getGas());
        subs1.setMaxSize(140., 21.);
        subs1.setMinSize(140., 21.);
        subs2.setMaxSize(140., 21.);
        subs2.setMinSize(140., 21.);
        setVisibleAddNew(false);
        setVisibleWaterDiffusion(false);
        setVisibleChoosenWindow(false);
        setVisibleGasDiffusion(true);
    }

    private double Pressure, Temperature;

    private boolean check (){
        String s = firstSubstance.getText();
        if (s.equals("")){
            //warning.setText("Вы не ввели пропорцию!");
            return false;
        }
        double val = Double.parseDouble(s);
        if (val > 100. || val < 0){
            //warning.setText("Вы ввели некорректное значени пропорции!");
            return false;
        }
        if (dDiffusion.getText().equals("")){
            //warning.setText("Вы не ввели коэффициент диффузии!");
            return false;
        }
        val = Double.parseDouble(dDiffusion.getText());
        if (val < 0.){
            //warning.setText("Вы ввели некорректный коэффициент диффузии");
            return false;
        }
        return true;
    }

    private boolean check2 (){
        String s1 = subs1.getValue().toString(), s2 = subs2.getValue().toString();
        String s3 = temp.getText(), s4 = pres.getText();
        if (s2.equals("") || s1.equals("") || s3.equals("") || s4.equals("") || gasSub.getText().equals("")){
            warningGas.setText("Вы не ввели всю информацию!");
            return false;
        }
        if (InformationAboutSubstance.isInSet(s1) == false || InformationAboutSubstance.isInSet(s2) == false){
            warningGas.setText("Данных веществ нет в нашей базе, вы можете добавить их");
            return false;
        }
        double q = Double.parseDouble(gasSub.getText());
        if (q > 100. || q < 0.){
            warningGas.setText("Вы ввели некорректные данные");
            return false;
        }
        return true;
    }

    @FXML
    void setShowWaterDiffusion() throws InterruptedException {
        if (check()){
            Color first = colorOfFirst.getValue();
            Color second = colorOfSecond.getValue();
            double r1 = first.getRed();
            double r2 = second.getRed();
            double b1 = first.getBlue();
            double b2 = second.getBlue();
            double g1 = first.getGreen();
            double g2 = second.getGreen();
            String s = firstSubstance.getText();
            double k = Double.parseDouble(s) / 100.;
            double koef = Double.parseDouble(dDiffusion.getText());
            java.lang.Object s1 = howProcess.getValue();
            if (s1.toString().equals("Сравнение моделей")) {
                WindowWithDiffusions window = new WindowWithDiffusions(koef, k);
                window.setColorFirstByRGB((int) (r1 * 255), (int) (g1 * 255), (int) (b1 * 255));
                window.setColorSecondByRGB((int) (r2 * 255), (int) (g2 * 255), (int) (b2 * 255));
                window.start();
            } else if (s1.toString().equals("График энтропии")){
                WindowWithDiffusionAndEntrophy window = new WindowWithDiffusionAndEntrophy(koef, k);
                window.setColorSecondByRGB((int) (r2 * 255), (int) (g2 * 255), (int) (b2 * 255));
                window.setColorFirstByRGB((int) (r1 * 255), (int) (g1 * 255), (int) (b1 * 255));
                window.start();
            }
        }
    }

    double getKoef (double mas1, double mas2, double rad1, double rad2, double t, double p){
        t += 273.;
        double chis = (1.858 / 1000.) * Math.sqrt(t * t * t) * Math.sqrt((1./mas1 + 1./mas2)/1000.);
        double zn = p * (1./2.) * ((rad1 + rad2) / 1000_000_000.);
        return chis/zn;
    }

    @FXML
    void setShowGasDiffusion () throws InterruptedException {
        if (check2()){
            Color first = colorFirstGas.getValue();
            Color second = colorSecondGas.getValue();
            double r1 = first.getRed();
            double r2 = second.getRed();
            double b1 = first.getBlue();
            double b2 = second.getBlue();
            double g1 = first.getGreen();
            double g2 = second.getGreen();
            java.lang.Object t1 = subs1.getValue();
            Substance s1 = InformationAboutSubstance.getValue(t1.toString());
            t1 = subs2.getValue();
            Substance s2 = InformationAboutSubstance.getValue(t1.toString());
            Pressure = Double.parseDouble(pres.getText());
            Temperature = Double.parseDouble(temp.getText());
            double koef = getKoef(s1.getMass(), s2.getMass(), s1.getRadiius(), s2.getRadiius(), Temperature, Pressure);
            double k = Double.parseDouble(gasSub.getText()) / 100.;
            java.lang.Object what = box.getValue();
            if (what.toString().equals("Сравнение моделей")) {
                WindowWithDiffusions window = new WindowWithDiffusions(koef, k);
                window.start();
                window.setColorFirstByRGB((int) (r1 * 255), (int) (g1 * 255), (int) (b1 * 255));
                window.setColorSecondByRGB((int) (r2 * 255), (int) (g2 * 255), (int) (b2 * 255));
            } else if (what.toString().equals("График энтропии")){
                WindowWithDiffusionAndEntrophy window = new WindowWithDiffusionAndEntrophy(koef, k);
                window.start();
                window.setColorFirstByRGB((int) (r1 * 255), (int) (g1 * 255), (int) (b1 * 255));
                window.setColorSecondByRGB((int) (r2 * 255), (int) (g2 * 255), (int) (b2 * 255));
            }
        }
    }

    boolean check3 (){
        if (newMas.getText().equals("") || newRad.getText().equals("") || newName.getText().equals("")){
            //warningNew.setText("Вы не ввели всю информацию!");
            return false;
        }
        return true;
    }

    @FXML
    void setShowWindowAddNew (){
        setVisibleChoosenWindow(false);
        setVisibleWaterDiffusion(false);
        setVisibleGasDiffusion(false);
        setVisibleAddNew(true);
    }

    @FXML
    void setAddNew (){
        if (check3()){
            String name = newName.getText();
            double mas = Double.parseDouble(newMas.getText());
            double rad = Double.parseDouble(newRad.getText());
            Substance sub = new Substance(name, newMas.getText(), newRad.getText());
            if (InformationAboutSubstance.isInSet(name) == false){
                InformationAboutSubstance.q.add(name);
                InformationAboutSubstance.addNew(sub);
                FileWorker.addNew(sub);
                clearAll();
                warningNew.setText("Вещество успешно добавлено!");
            } else {
                clearAll();
                warningNew.setText("Это вещество было добавлено ранее!");
            }
        }
    }
}
