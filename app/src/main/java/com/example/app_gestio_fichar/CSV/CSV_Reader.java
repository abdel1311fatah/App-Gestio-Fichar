package com.example.app_gestio_fichar.CSV;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSV_Reader {
    private Info_horari horari;

    public CSV_Reader() {
    }

    public CSV_Reader(Info_horari horari) {
        this.horari = horari;
    }

    public Info_horari getHorari() {
        return horari;
    }

    public void setHorari(Info_horari horari) {
        this.horari = horari;
    }

    public Info_horari get_info_horari(String input, String output) {
        XLSXtoCSVConverter.convertXLSXtoCSV(input + ".xlsx", output + ".csv");
        File csv = new File(output + ".csv");

        // Agrega el encabezado deseado al CSV
        String header = "Hora,X";
        XLSXtoCSVConverter.guardarCSV(leerCSV(csv), csv.getAbsolutePath(), header);

        List<String> csvLines = leerCSV(csv);

        if (csvLines == null || csvLines.isEmpty()) {
            System.out.println("Error: No se pudieron leer las líneas del archivo CSV.");
            return null;
        }

        return parsearCSV(csvLines);
    }

    private List<String> leerCSV(File csv) {
        List<String> lines = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(csv))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                lines.add(String.join(",", line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return lines;
    }

    private Info_horari parsearCSV(List<String> csvLines) {
        if (csvLines.size() < 2) {
            // El CSV debe tener al menos dos líneas (encabezado y al menos una línea de datos)
            return null;
        }

        String[] headers = csvLines.get(0).split(",");
        String[] dies = new String[headers.length - 1];
        String[] hores = new String[headers.length - 1];
        String[] x = new String[headers.length - 1];

        for (int i = 1; i < headers.length; i++) {
            dies[i - 1] = headers[i];
        }

        for (int i = 1; i < csvLines.size(); i++) {
            String[] values = csvLines.get(i).split(",");
            if (values.length >= 2) {
                // Asegurarse de que haya al menos dos valores en cada línea (hora y "X")
                hores[i - 1] = values[0];
                x[i - 1] = values[1];
            } else {
                // Manejar el caso donde no hay suficientes valores en una línea
                // Puedes imprimir un mensaje de error o tomar la acción adecuada
                System.out.println("Error: No hay suficientes valores en la línea " + i);
            }
        }

        return new Info_horari(dies, hores, x);
    }
}