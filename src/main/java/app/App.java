package app;

import algorithms.genetic.GeneticAlgorithm;
import algorithms.genetic.model.Parameters;
import algorithms.localsearch.LocalSearchAlgorithm;
import algorithms.localsearch.model.LocalSearchMethod;
import constants.StringConst;
import input.DataReader;


public class App {

    public static void main(String[] args) {
        DataReader dataReader = new DataReader(StringConst.FILE_NAME);
        dataReader.readData();
        new GeneticAlgorithm(
                dataReader.getCities(),
                dataReader.getVehicles(),   
                dataReader.getDepot(),
                new Parameters(10, 10, 0.9, 0.05, 10)
        ).start();

//        new LocalSearchAlgorithm(
//                dataReader.getCities(),
//                dataReader.getVehicles(),
//                dataReader.getDepot(),
//                LocalSearchMethod.GREEDY
//        ).start();


    }


}
