package app;

import algorithms.genetic.GeneticAlgorithm;
import algorithms.genetic.model.ParametersGenetic;
import algorithms.genetic.model.SelectionMethods;
import algorithms.localsearch.LocalSearchAlgorithm;
import algorithms.localsearch.model.LocalSearchMethod;
import algorithms.simulatedannealing.SimulatedAnnealingAlgorithm;
import algorithms.simulatedannealing.model.ParametersSimulatedAnnealing;
import algorithms.tabusearch.TabuSearchAlgorithm;
import algorithms.tabusearch.model.ParametersTabuSearch;
import constants.StringConst;
import input.DataReader;


public class App {

    public static void main(String[] args) {
        DataReader dataReader = new DataReader(StringConst.FILE_NAME, StringConst.NAME_OF_SHEET_STATIC);
        dataReader.readData();

        long start = System.currentTimeMillis();

//        new GeneticAlgorithm(
//                dataReader.getCities(),
//                dataReader.getVehicles(),
//                dataReader.getDepot(),
//                new ParametersGenetic(100, 1000, 0.95,
//                        0.05, 10, SelectionMethods.TOURNAMENT, 60, 2)
//        ).start();

//        new LocalSearchAlgorithm(
//                dataReader.getCities(),
//                dataReader.getVehicles(),
//                dataReader.getDepot(),
//                LocalSearchMethod.STEEPEST
//        ).start();

//        new SimulatedAnnealingAlgorithm(
//                dataReader.getCities(),
//                dataReader.getVehicles(),
//                dataReader.getDepot(),
//                new ParametersSimulatedAnnealing(0.90, 10,  0.95, 100
//                )
//        ).start();

        new TabuSearchAlgorithm(
                dataReader.getCities(),
                dataReader.getVehicles(),
                dataReader.getDepot(),
                new ParametersTabuSearch(200, 20) //2000, 200
        ).start();

        float elapsedTime = (System.currentTimeMillis() - start) / 1000.0f;
        System.out.println("Elapsed time = " + elapsedTime + " sec");
    }


}
