package commons;

import constants.StringConst;
import exceptions.InputException;
import exceptions.InputFilesNotGivenException;
import exceptions.OutputFileNotGivenException;
import exceptions.SaveLocationNotGivenException;
import input.DataReader;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import output.DataWriter;
import utils.Pair;

import static constants.StringConst.*;

abstract public class UtilsController {

    private PathsHolder holder;
    protected Task<Void> currentTask;

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
        } else {
            inputs.setObj1(StringConst.FILE_NAME);
            inputs.setObj2(holder.getExampleInputName().toString());
        }
        return inputs;
    }

    private void validateInputFile() throws InputException {
        holder = PathsHolder.getInstance();
        if (holder.getInputPath() == null && holder.getExampleInputName() == null)
            throw new InputFilesNotGivenException(INPUT_NOT_GIVEN_HEADER_ERROR, INPUT_NOT_GIVEN_CONTENT_ERROR);

    }

    private void validateSaveLocation() throws InputException {
        holder = PathsHolder.getInstance();
        if (holder.getOutputPath() == null)
            throw new SaveLocationNotGivenException(SAVE_LOCATION_NOT_GIVEN_HEADER_ERROR, SAVE_LOCATION_NOT_GIVEN_CONTENT_ERROR);
    }

    private void validateOutputFile() throws InputException {
        holder = PathsHolder.getInstance();
        if (holder.getOutputFile() == null || holder.getOutputFile().isEmpty())
            throw new OutputFileNotGivenException(OUTPUT_FILE_NOT_GIVEN_HEADER_ERROR, OUTPUT_FILE_NOT_GIVEN_CONTENT_ERROR);
    }

    protected String getSaveLocation() {
        return PathsHolder.getInstance().getOutputPath();
    }

    protected String getOutputFile() {
        return PathsHolder.getInstance().getOutputFile();
    }

    protected void showConfirmMessage() {
        holder = PathsHolder.getInstance();
        new CustomAlert(
                Alert.AlertType.INFORMATION,
                CONFIRM_HEADER_INFO,
                CONFIRM_CONTENT_INFO + holder.getOutputFile() + ".xls",
                ButtonType.OK)
                .showAndWait();
    }

    protected void validateCommon() throws InputException {
        validateInputFile();
        validateSaveLocation();
        validateOutputFile();
    }

    protected void startAlgorithm(Algorithm algorithm) {
        currentTask = createTask(algorithm);
        new Thread(currentTask).start();
    }

    private Task<Void> createTask(Algorithm algorithm) {
        return new Task<>() {
            @Override
            protected Void call() {
                algorithm.start();
                new DataWriter().writeData(algorithm.getResult(), getSaveLocation(), getOutputFile());
                return null;
            }
        };
    }
}
