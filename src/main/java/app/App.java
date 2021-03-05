package app;

import algorithms.genetic.GeneticAlgorithm;
import algorithms.genetic.model.Parameters;
import constants.StringConst;
import input.DataReader;

public class App {

    public static void main(String[] args) {
        DataReader dataReader = new DataReader(StringConst.FILE_NAME);
        dataReader.readData();

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(
                dataReader.getCities(),
                dataReader.getVehicles(),
                dataReader.getDepot(),
                new Parameters(10, 10, 0.9, 0.05));
        geneticAlgorithm.start();



    }


}
