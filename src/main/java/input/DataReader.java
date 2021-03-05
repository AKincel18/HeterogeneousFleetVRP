package input;

import constants.StringConst;
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
import utils.Writer;

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
    @Getter private final List<City> cities = new ArrayList<>();
    @Getter private final List<Vehicle> vehicles = new ArrayList<>();
    @Getter private final Depot depot = new Depot();

    public void readData(){
        try {

            FileInputStream inputStream = new FileInputStream(new File(fileName));

            Workbook workbook = new XSSFWorkbook(inputStream);

            Sheet sheet = workbook.getSheet(StringConst.NAME_OF_SHEET);
            boolean header = true;
            int vehicleId = 0;
            for (Row nextRow : sheet) {
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                City city = City.builder().coords(new Coords()).build();
                Vehicle vehicle = Vehicle.builder().id(vehicleId).build();
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();

                    if (getCellValue(nextCell) == null || header)
                        break;


                        switch (columnIndex) {
                            case 0:
                                city.setName((String) getCellValue(nextCell));
                                break;
                            case 1:
                                city.setAmount((double) getCellValue(nextCell));
                                break;
                            case 2:
                                city.getCoords().setLatitude((double) getCellValue(nextCell));
                                break;
                            case 3:
                                city.getCoords().setLongitude((double) getCellValue(nextCell));
                                break;
                            case 5:
                                vehicle.setName((String) getCellValue(nextCell));
                                vehicleId++;
                                break;
                            case 6:
                                vehicle.setAmount((double) getCellValue(nextCell));
                                break;
                            case 8:
                                depot.setName((String) getCellValue(nextCell));
                                break;
                            case 9:
                                depot.getCoords().setLatitude((double) getCellValue(nextCell));
                                break;
                            case 10:
                                depot.getCoords().setLongitude((double) getCellValue(nextCell));
                                break;
                        }



                }
                try {
                    city.validDataCity();
                    if (!city.isNullCity())
                        cities.add(city);
                } catch (NotValidDataException e) {
                    System.out.println(e.getMessage());
                    return;
                }

                try{
                    vehicle.validData();
                    if (!vehicle.isNull())
                        vehicles.add(vehicle);
                } catch (NotValidDataException e) {
                    System.out.println(e.getMessage());
                    return;
                }

                header = false;
            }

            workbook.close();
            inputStream.close();

            Writer.writeInputData(cities, vehicles, depot);

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
}
