package app;

import algorithms.genetic.GeneticAlgorithm;
import algorithms.genetic.model.ParametersGA;
import algorithms.localsearch.LocalSearchAlgorithm;
import algorithms.localsearch.model.LocalSearchMethod;
import algorithms.simulatedannealing.SimulatedAnnealingAlgorithm;
import algorithms.simulatedannealing.model.ParametersSA;
import constants.StringConst;
import input.DataReader;


public class App {

    public static void main(String[] args) {
        DataReader dataReader = new DataReader(StringConst.FILE_NAME);
        dataReader.readData();

//        new GeneticAlgorithm(
//                dataReader.getCities(),
//                dataReader.getVehicles(),
//                dataReader.getDepot(),
//                new ParametersGA(10, 10, 0.9, 0.05, 10)
//        ).start();

//        new LocalSearchAlgorithm(
//                dataReader.getCities(),
//                dataReader.getVehicles(),
//                dataReader.getDepot(),
//                LocalSearchMethod.GREEDY
//        ).start();

        new SimulatedAnnealingAlgorithm(
                dataReader.getCities(),
                dataReader.getVehicles(),
                dataReader.getDepot(),
                new ParametersSA(0.8, 10,  0.9, 5
                )
        ).start();


    }


}
