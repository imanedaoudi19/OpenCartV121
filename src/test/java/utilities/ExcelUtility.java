package utilities;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {

    String path;
    FileInputStream fis;
    XSSFWorkbook workbook;
    Sheet sheet;

    public ExcelUtility(String path) {
        this.path = path;
    }

    // Get total rows (excluding header)
    public int getRowCount(String sheetName) throws IOException {
        fis = new FileInputStream(path);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet(sheetName);

        int rowCount = sheet.getLastRowNum();

        workbook.close();
        fis.close();

        return rowCount;
    }

    // Get total columns (VERY IMPORTANT)
    public int getCellCount(String sheetName, int rowNum) throws IOException {
        fis = new FileInputStream(path);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet(sheetName);

        Row row = sheet.getRow(rowNum);
        int colCount = row.getLastCellNum();  // ✅ counts all columns

        workbook.close();
        fis.close();

        return colCount;
    }

    // Get cell data
    public String getCellData(String sheetName, int rowNum, int colNum) throws IOException {
        fis = new FileInputStream(path);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet(sheetName);

        Row row = sheet.getRow(rowNum);

        DataFormatter formatter = new DataFormatter();
        String data = formatter.formatCellValue(row.getCell(colNum));

        workbook.close();
        fis.close();

        return data.trim();   // ✅ remove hidden spaces
    }
}