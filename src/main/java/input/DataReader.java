package input;

import commons.client.ValidationResult;
import exceptions.NotValidDataException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Coords;
import model.Depot;
import model.Vehicle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import validators.CityValidator;
import validators.DepotValidator;
import validators.AmountValidator;
import validators.VehicleValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static constants.StringConst.INPUT_DATA_IS_NOT_VALID_CONTENT_ERROR;
import static constants.StringConst.INPUT_DATA_IS_NOT_VALID_HEADER_ERROR;

@RequiredArgsConstructor
public class DataReader {

    private final String fileName;
    private final String sheetName;
    @Getter private final List<City> cities = new ArrayList<>();
    @Getter private final List<Vehicle> vehicles = new ArrayList<>();
    @Getter private final Depot depot = new Depot();
    @Getter private ValidationResult validationResult;

    public void readData() {
        try {

            FileInputStream inputStream = new FileInputStream(new File(fileName));
            Workbook workbook = new XSSFWorkbook(inputStream);

            Sheet sheet = workbook.getSheet(sheetName);
            boolean header = true;
            int vehicleId = 0;
            int cityId = 1;
            int rowCounter = 1;
            CityValidator cityValidator = new CityValidator();
            VehicleValidator vehicleValidator = new VehicleValidator();
            DepotValidator depotValidator = new DepotValidator();

            for (Row nextRow : sheet) {
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                City city = City.builder().coords(new Coords()).id(cityId).build();
                Vehicle vehicle = Vehicle.builder().id(vehicleId).build();
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();
                    Object cellValue = getCellValue(nextCell);
                    if (columnIndex == 4 || columnIndex == 7)
                        continue;
                    if (cellValue == null || header)
                        break;

                    switch (columnIndex) {
                        case 0:
                            city.setName((String) cellValue);
                            cityId++;
                            break;
                        case 1:
                            city.setAmount((double) cellValue);
                            break;
                        case 2:
                            city.getCoords().setLatitude((double) cellValue);
                            break;
                        case 3:
                            city.getCoords().setLongitude((double) cellValue);
                            break;
                        case 5:
                            vehicle.setName((String) cellValue);
                            vehicleId++;
                            break;
                        case 6:
                            vehicle.setAmount((double) cellValue);
                            break;
                        case 8:
                            depot.setName((String) cellValue);
                            break;
                        case 9:
                            depot.getCoords().setLatitude((double) cellValue);
                            break;
                        case 10:
                            depot.getCoords().setLongitude((double) cellValue);
                            break;
                    }


                }

                if (!header) {
                    try {
                        cityValidator.validateCity(city, rowCounter);
                        vehicleValidator.validateVehicle(vehicle, rowCounter);
                        cities.add(city);
                        if (vehicleValidator.isVehicle(vehicle))
                            vehicles.add(vehicle);
                        if (rowCounter == 2)
                            depotValidator.validateDepot(depot, rowCounter);
                    } catch (NotValidDataException exception) {
                        validationResult = new ValidationResult(false, exception);
                        return;
                    }
                }
                header = false;
                rowCounter++;
            }
            workbook.close();
            inputStream.close();

            try {
                new AmountValidator().validateAmount(vehicles, cities);
            } catch (NotValidDataException exception) {
                validationResult = new ValidationResult(false, exception);
                return;
            }
            validationResult = new ValidationResult(true, null);

        } catch (IOException | ClassCastException ignored) {
            validationResult = new ValidationResult(false,
                    new NotValidDataException(INPUT_DATA_IS_NOT_VALID_HEADER_ERROR, INPUT_DATA_IS_NOT_VALID_CONTENT_ERROR));
        }

    }

    public List<String> getSheets() {
        try {
            FileInputStream inputStream = new FileInputStream(new File(fileName));
            Workbook workbook = new XSSFWorkbook(inputStream);

            List<String> sheets = new ArrayList<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheets.add(workbook.getSheetName(i));
            }
            return sheets;
        } catch (IOException ignore) {
            return null;
        }
    }

    private Object getCellValue(Cell cell) {

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();

            case Cell.CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
        }

        return null;
    }
}
