package input;

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
    private final String sheetName;
    @Getter private final List<City> cities = new ArrayList<>();
    @Getter private final List<Vehicle> vehicles = new ArrayList<>();
    @Getter private final Depot depot = new Depot();

    public void readData(){
        try {

            FileInputStream inputStream = new FileInputStream(new File(fileName));

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = sheetName != null ? workbook.getSheet(sheetName) : workbook.getSheetAt(0);
            boolean header = true;
            int vehicleId = 0;
            int cityId = 1;
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
