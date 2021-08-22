package output;

import commons.algorithms.Result;
import model.City;
import model.Vehicle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static constants.StringConst.SUM_OF_DISTANCES;

public class DataWriter {

    public void writeData(Result result, String saveLocation, String outputFile) {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        printDistance(sheet, result.getSum().toString());

        int rowCount = 2;
        for (Map.Entry<Vehicle, List<City>> entry : result.getRoutes().entrySet()) {
            Row row = sheet.createRow(rowCount++);
            writeRoute(entry, row);
        }

        try (FileOutputStream outputStream = new FileOutputStream(saveLocation + "/" + outputFile + ".xls")) {
            workbook.write(outputStream);
        } catch (IOException ignore) {}
    }

    private void writeRoute(Map.Entry<Vehicle, List<City>> route, Row row) {
        Cell cell = row.createCell(0);
        cell.setCellValue(route.getKey().getName());

        for (int i = 0; i < route.getValue().size(); i++) {
            cell = row.createCell(i + 1);
            cell.setCellValue(route.getValue().get(i).getName());
        }
    }

    private void printDistance(Sheet sheet, String distance) {
        Row firstRow = sheet.createRow(0);
        Cell cell = firstRow.createCell(0);
        cell.setCellValue(SUM_OF_DISTANCES);
        cell = firstRow.createCell(1);
        cell.setCellValue(distance);
    }
}
