package com.example.app_gestio_fichar.CSV;

import android.util.Log;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<String> leerCSV(File csv) {
        List<String> lines = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(csv))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                lines.add(String.join(",", line));
            }
        } catch (IOException | CsvValidationException e) {
            Log.e("Contador_Hores", "Error al leer el archivo CSV", e);
        }

        return lines;
    }
    private Info_horari parsearCSV(List<String> csvLines) {
        System.out.println("Tamaño de csvLines: " + csvLines.size());

        if (csvLines.size() < 2) {
            System.out.println("Error: No hay suficientes líneas en el archivo CSV.");
            return new Info_horari(); // Devolver un objeto Info_horari vacío indicando el error
        }

        String[] headers = csvLines.get(0).split(",");
        System.out.println("Headers: " + Arrays.toString(headers));

        String[] dies = new String[headers.length - 1];
        String[] hores = new String[headers.length - 1];
        String[] x = new String[headers.length - 1];

        for (int i = 1; i < headers.length; i++) {
            dies[i - 1] = headers[i];
        }

        for (int i = 1; i < csvLines.size(); i++) {
            String[] values = csvLines.get(i).split(",");
            if (values.length >= 2) {
                hores[i - 1] = values[0];
                x[i - 1] = values[1];
            } else {
                System.out.println("Error: No hay suficientes valores en la línea " + i);
                System.out.println("Valores encontrados: " + Arrays.toString(values));
            }
        }

        System.out.println("dies: " + Arrays.toString(dies));
        System.out.println("hores: " + Arrays.toString(hores));
        System.out.println("x: " + Arrays.toString(x));

        return new Info_horari(dies, hores, x);
    }
}
