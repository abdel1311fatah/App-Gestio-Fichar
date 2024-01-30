package com.example.app_gestio_fichar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;

public class ContadorHores extends AppCompatActivity {

    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador_hores);

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Uri selectedFileUri = result.getData().getData();
                            if (selectedFileUri != null) {
                                String filePath = selectedFileUri.getPath();
                                String extension = filePath.substring(filePath.lastIndexOf(".") + 1);
                                if (!extension.equals("csv")) {
                                    Toast.makeText(ContadorHores.this, "El archivo seleccionado no es un CSV", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                try {
                                    // Lee el archivo CSV y extrae información
                                    readCSV(filePath);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(ContadorHores.this, "Error al leer el archivo CSV", Toast.LENGTH_SHORT).show();
                                } catch (CsvValidationException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
        );

        openFilePicker();
    }

    private void readCSV(String filePath) throws IOException, CsvValidationException {
        CSVReader reader = new CSVReader(new FileReader(filePath));
        String[] headers = reader.readNext(); // Obtener nombres de columnas (días)
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            // nextLine contiene los datos de cada fila del CSV
            // Puedes acceder a los elementos utilizando nextLine[i]
            // Por ejemplo, para obtener el día y el horario de la fila actual
            String dia = nextLine[0];
            String horario = nextLine[1];

            // Puedes realizar acciones con el día y el horario aquí
            // Por ejemplo, mostrarlos en un Toast
            Toast.makeText(this, "Día: " + dia + ", Horario: " + horario, Toast.LENGTH_SHORT).show();
        }
        reader.close();
    }
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/csv"); // Puedes ajustar el tipo de archivo según tus necesidades
        filePickerLauncher.launch(Intent.createChooser(intent, "Selecciona un archivo CSV"));
    }
}
