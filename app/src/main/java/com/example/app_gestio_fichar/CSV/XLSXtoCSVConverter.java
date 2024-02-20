package com.example.app_gestio_fichar.CSV;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XLSXtoCSVConverter {

    public static List<String> convertXLSXtoCSV(String xlsxFilePath, String csvFilePath) {
        List<String> csvLines = new ArrayList<>();

        try (FileInputStream excelFile = new FileInputStream(new File(xlsxFilePath));
             Workbook workbook = new XSSFWorkbook(excelFile)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                StringBuilder line = new StringBuilder();
                Iterator<Cell> cellIterator = row.iterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    line.append(getCellValueAsString(cell));

                    if (cellIterator.hasNext()) {
                        line.append(",");
                    }
                }

                csvLines.add(line.toString());
            }

            // No es necesario cerrar explícitamente el Workbook, se hace automáticamente con el try-with-resources
            guardarCSV(csvLines, csvFilePath, "Hora,X");  // Agrega el encabezado

        } catch (IOException e) {
            e.printStackTrace();
        }

        return csvLines;
    }

    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
    public static void guardarCSV(List<String> csvLines, String csvFilePath, String header) {
        try (PrintWriter writer = new PrintWriter(new File(csvFilePath))) {
            writer.println(header);  // Agrega el encabezado
            for (String line : csvLines) {
                writer.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
