package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vanya on 13.12.2016.
 */
public class InformationAboutSubstance {
    public static ObservableList<String> q = FXCollections.observableArrayList();

    public static ObservableList <String> getGas (){
        return q;
    }

    public static HashMap <String, Integer> hp = new HashMap<>();

    public static ArrayList <Substance> a = new ArrayList<>();

    public static void addNew (Substance substance){
        if (isInSet(substance.getName()) == false){
            int q = a.size();
            hp.put(substance.getName(), Integer.valueOf(q));
            a.add(substance);
        }
    }

    public static Substance getValue(String s){
        Integer q = hp.get(s);
        return a.get(q);
    }

    public static boolean isInSet (String s){
        return hp.containsKey(s);
    }
}
