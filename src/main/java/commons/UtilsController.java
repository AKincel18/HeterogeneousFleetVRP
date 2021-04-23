package commons;

import constants.StringConst;
import exceptions.InputException;
import exceptions.InputFilesNotGivenException;
import input.DataReader;
import utils.Pair;

import static constants.StringConst.INPUT_NOT_GIVEN_CONTENT_ERROR;
import static constants.StringConst.INPUT_NOT_GIVEN_HEADER_ERROR;

abstract public class UtilsController {

    private PathsHolder holder;

    protected DataReader getInputData() {
        Pair<String, String> inputs = getInputsName();
        DataReader dataReader = new DataReader(inputs.getObj1(), inputs.getObj2());
        dataReader.readData();
        return dataReader;
    }

    private Pair<String, String> getInputsName() {
        holder = PathsHolder.getInstance();
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

    protected void validateInput() throws InputException {
        holder = PathsHolder.getInstance();
        if (holder.getInputPath() == null && holder.getExampleInputName() == null)
            throw new InputFilesNotGivenException(INPUT_NOT_GIVEN_HEADER_ERROR, INPUT_NOT_GIVEN_CONTENT_ERROR);
    }
}
