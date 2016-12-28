package sample;

import javafx.collections.FXCollections;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Vanya on 13.12.2016.
 */
public class FileWorker {
    public static void addNew (Substance substance){
        String s = substance.getName();
        FileWorker.write(s);
        double q = substance.getMass();
        s = Double.toString(q);
        FileWorker.write(s);
        q = substance.getRadiius();
        s = Double.toString(q);
        FileWorker.write(s);
    }

    private static void write(String text) {
        File myFile = new File("input.txt");
        try{
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(myFile, true)));
            writer.println(text);
            writer.flush();
            writer.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void read() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"), StandardCharsets.UTF_8);
        int kol = 0;
        String name = "", mass = "", radius = "";
        for(String line: lines){
            if (kol == 0){
                name = line;
            } else if (kol == 1){
                mass = line;
            } else {
                radius = line;
                Substance substance = new Substance(name, mass, radius);
                InformationAboutSubstance.addNew(substance);
                InformationAboutSubstance.q.add(name);
                kol = -1;
            }
            kol++;
        }
    }
}