package app;

import algorithms.genetic.GeneticAlgorithm;
import algorithms.genetic.model.ParametersGenetic;
import algorithms.genetic.model.SelectionMethods;
import constants.StringConst;
import input.DataReader;


public class App {

    public static void main(String[] args) {
        DataReader dataReader = new DataReader(StringConst.FILE_NAME, StringConst.NAME_OF_SHEET_2);
        dataReader.readData();

        long start = System.currentTimeMillis();

        new GeneticAlgorithm(
                dataReader.getCities(),
                dataReader.getVehicles(),
                dataReader.getDepot(),
                new ParametersGenetic(100, 1000, 0.95,
                        0.05, 10, SelectionMethods.RANK, 60, 2)
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
//                new ParametersSimulatedAnnealing(0.90, 20,  0.95, 200
//                )
//        ).start();

//        new TabuSearchAlgorithm(
//                dataReader.getCities(),
//                dataReader.getVehicles(),
//                dataReader.getDepot(),
//                new ParametersTabuSearch(2000, 200) //2000, 200
//        ).start();

        float elapsedTime = (System.currentTimeMillis() - start) / 1000.0f;
        System.out.println("Elapsed time = " + elapsedTime + " sec");
    }


}
