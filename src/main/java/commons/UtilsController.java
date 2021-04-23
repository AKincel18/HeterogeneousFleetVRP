package commons;

import constants.StringConst;
import input.DataReader;
import utils.Pair;

abstract public class UtilsController {

    protected DataReader getInputData() {
        Pair<String, String> inputs = getInputsName();
        DataReader dataReader = new DataReader(inputs.getObj1(), inputs.getObj2());
        dataReader.readData();
        return dataReader;
    }

    private Pair<String, String> getInputsName() {
        PathsHolder holder = PathsHolder.getInstance();
        Pair<String, String> inputs = new Pair<>();
        if (holder.getInputPath() != null) {
            inputs.setObj1(holder.getInputPath());
        }
        else {
            inputs.setObj1(StringConst.FILE_NAME);
            inputs.setObj2(holder.getExampleInputName().toString());
        }
        return inputs;
    }
}
