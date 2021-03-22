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
        DataReader dataReader = new DataReader(StringConst.FILE_NAME, StringConst.NAME_OF_SHEET_2);
        dataReader.readData();

        new GeneticAlgorithm(
                dataReader.getCities(),
                dataReader.getVehicles(),
                dataReader.getDepot(),
                new ParametersGenetic(10, 1000, 0.9,
                        0.05, 10, SelectionMethods.RANK, 4, 1.5)
        ).start();

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
//                new ParametersSimulatedAnnealing(0.9, 10,  0.8, 1000
//                )
//        ).start();

//        new TabuSearchAlgorithm(
//                dataReader.getCities(),
//                dataReader.getVehicles(),
//                dataReader.getDepot(),
//                new ParametersTabuSearch(1000, 10)
//        ).start();


    }


}
