package sample;

/**
 * Created by Vanya on 13.12.2016.
 */
public class Substance {
    private String name;

    private double radiius, mass;

    public Substance (String name, String mass, String radius){
        this.name = name;
        this.mass = Double.parseDouble(mass);
        this.radiius = Double.parseDouble(radius);
    }

    public String getName (){
        return name;
    }

    public double getRadiius(){
        return radiius;
    }

    public double getMass(){
        return mass;
    }
}
