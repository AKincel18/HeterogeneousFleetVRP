package app;

import algorithms.genetic.GeneticAlgorithm;
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
                10);
        geneticAlgorithm.initRoutes();

    }
}
