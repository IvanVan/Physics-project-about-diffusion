package sample;

import javafx.beans.binding.DoubleExpression;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class Controller {
    @FXML
    AnchorPane choosenWindow, waterDiffusion, gasDiffusion, addNewSub;

    @FXML
    Button openWaterDiffusion, openGasDiffusion, showWaterDiffusion, backToMainWindow1;

    @FXML
    TextField firstSubstance, dDiffusion, pres, temp, sub1, sub2, gasSub, newMas, newRad, newName;

    @FXML
    ColorPicker colorOfFirst, colorOfSecond;

    @FXML
    Label warning, warningGas, warningNew;

    void clearAll (){
        newMas.setText("");
        newRad.setText("");
        newName.setText("");
        firstSubstance.setText("");
        dDiffusion.setText("");
        pres.setText("");
        temp.setText("");
        sub1.setText("");
        sub2.setText("");
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
        clearAll();
        setVisibleAddNew(false);
        setVisibleWaterDiffusion(false);
        setVisibleChoosenWindow(false);
        setVisibleGasDiffusion(true);
    }

    private double Pressure, Temperature;

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

    private boolean check2 (){
        String s1 = sub1.getText(), s2 = sub2.getText();
        String s3 = temp.getText(), s4 = pres.getText();
        if (s2.equals("") || s1.equals("") || s3.equals("") || s4.equals("") || gasSub.getText().equals("")){
            warningGas.setText("Вы не ввели всю информацию!");
            return false;
        }
        if (InformationAboutSubstance.isInSet(s1) == false || InformationAboutSubstance.isInSet(s2) == false){
            System.out.println("areq");
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
            String s = firstSubstance.getText();
            double k = Double.parseDouble(s) / 100.;
            double koef = Double.parseDouble(dDiffusion.getText());
            WindowWithDiffusions window = new WindowWithDiffusions(koef, k);
            window.start();
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
            Substance s1 = InformationAboutSubstance.getValue(sub1.getText());
            Substance s2 = InformationAboutSubstance.getValue(sub2.getText());
            Pressure = Double.parseDouble(pres.getText());
            Temperature = Double.parseDouble(temp.getText());
            double koef = getKoef(s1.getMass(), s2.getMass(), s1.getRadiius(), s2.getRadiius(), Temperature, Pressure);
            double k = Double.parseDouble(gasSub.getText()) / 100.;
            WindowWithDiffusions window = new WindowWithDiffusions(koef, k);
            window.start();
        }
    }

    boolean check3 (){
        if (newMas.getText().equals("") || newRad.getText().equals("") || newName.getText().equals("")){
            warningNew.setText("Вы не ввели всю информацию!");
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
