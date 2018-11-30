package ngrammProject;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DataModel {
    private StringProperty ngrammName;
    private DoubleProperty firsTextAverage;
    private DoubleProperty secondTextAverage;
    private DoubleProperty difference;

    public DataModel(){
        this(null,null,null,null);
    }

    public DataModel(String ngrammName, Double firsTextAverage, Double secondTextAverage, Double difference) {
        this.ngrammName = new SimpleStringProperty(ngrammName);
        this.firsTextAverage =  new SimpleDoubleProperty(firsTextAverage);
        this.secondTextAverage = new SimpleDoubleProperty(secondTextAverage) ;
        this.difference = new SimpleDoubleProperty(difference);
    }

    public DataModel(String ngrammName) {
        this.ngrammName =  new SimpleStringProperty(ngrammName);
    }

    public String getNgrammName() {
        return ngrammName.get();
    }

    public StringProperty ngrammNameProperty() {
        return ngrammName;
    }

    public void setNgrammName(String ngrammName) {
        this.ngrammName.set(ngrammName);
    }

    public double getFirsTextAverage() {
        return firsTextAverage.get();
    }

    public DoubleProperty firsTextAverageProperty() {
        return firsTextAverage;
    }

    public void setFirsTextAverage(double firsTextAverage) {
        this.firsTextAverage.set(firsTextAverage);
    }

    public double getSecondTextAverage() {
        return secondTextAverage.get();
    }

    public DoubleProperty secondTextAverageProperty() {
        return secondTextAverage;
    }

    public void setSecondTextAverage(double secondTextAverage) {
        this.secondTextAverage.set(secondTextAverage);
    }

    public double getDifference() {
        return difference.get();
    }

    public DoubleProperty differenceProperty() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference.set(difference);
    }
}