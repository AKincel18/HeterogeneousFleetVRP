package input;

import constants.StringConst;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.City;
import model.Vehicle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//@Slf4j
@RequiredArgsConstructor
public class DataReader {

    private final String fileName;
    @Getter private List<City> cities = new ArrayList<>();
    @Getter private List<Vehicle> vehicles = new ArrayList<>();


    public void readData(){
        try {

            FileInputStream inputStream = new FileInputStream(new File(fileName));

            Workbook workbook = new XSSFWorkbook(inputStream);

            readCities(workbook);
            readVehicles(workbook);

            workbook.close();
            inputStream.close();
        }
        catch (IOException e) {
            //log.error("not loaded data");
            System.out.println("not loaded data");
        }


    }

    //todo Duplicates
    @SuppressWarnings("Duplicates")
    private void readVehicles(Workbook workbook) {
        Sheet sheet = workbook.getSheet(StringConst.NAME_OF_SHEET_VEHICLE);

        for (Row nextRow : sheet) {
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            Vehicle vehicle = new Vehicle();
            while (cellIterator.hasNext()) {
                Cell nextCell = cellIterator.next();
                int columnIndex = nextCell.getColumnIndex();

                switch (columnIndex) {
                    case 0:
                        vehicle.setName((String) getCellValue(nextCell));
                        break;
                    case 1:
                        vehicle.setAmount((double) getCellValue(nextCell));
                        break;
                }


            }
            vehicles.add(vehicle);


        }
    }

    //todo Duplicates
    @SuppressWarnings("Duplicates")
    private void readCities(Workbook workbook) {
        Sheet sheet = workbook.getSheet(StringConst.NAME_OF_SHEET_CITY);

        for (Row nextRow : sheet) {
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            City city = new City();
            while (cellIterator.hasNext()) {
                Cell nextCell = cellIterator.next();
                int columnIndex = nextCell.getColumnIndex();

                switch (columnIndex) {
                    case 0:
                        city.setName((String) getCellValue(nextCell));
                        break;
                    case 1:
                        city.setAmount((double) getCellValue(nextCell));
                        break;
                }


            }
            cities.add(city);


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
