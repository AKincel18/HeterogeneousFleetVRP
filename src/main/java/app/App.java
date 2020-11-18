package app;

import constants.StringConst;
import input.DataReader;

public class App {

    public static void main(String[] args) {
        DataReader dataReader = new DataReader(StringConst.FILE_NAME);
        dataReader.readData();

        dataReader.getCities().forEach( c -> System.out.println(c.getName() + " = " + c.getAmount()));
        dataReader.getVehicles().forEach( v -> System.out.println(v.getName() + " = " + v.getAmount()));
    }
}
