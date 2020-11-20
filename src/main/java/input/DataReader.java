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

            Sheet sheet = workbook.getSheet(StringConst.NAME_OF_SHEET);

            for (Row nextRow : sheet) {
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                City city = new City();
                Vehicle vehicle = new Vehicle();
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
                        case 3:
                            vehicle.setName((String) getCellValue(nextCell));
                            break;
                        case 4:
                            vehicle.setAmount((double) getCellValue(nextCell));
                            break;
                    }


                }
                if (city.isNotNull()) {
                    cities.add(city);
                }
                if (vehicle.isNotNull()) {
                    vehicles.add(vehicle);
                }


            }

            workbook.close();
            inputStream.close();

            //writeData();

        }
        catch (IOException e) {
            //log.error("not loaded data");
            System.out.println("not loaded data");
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

    private void writeData(){
        System.out.println("Cities:");
        cities.forEach( c -> System.out.println(c.getName() + " = " + c.getAmount()));
        System.out.println();
        System.out.println();
        System.out.println("Vehicles");
        vehicles.forEach( v -> System.out.println(v.getName() + " = " + v.getAmount()));
    }
}
