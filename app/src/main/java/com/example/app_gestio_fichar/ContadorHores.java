package com.example.app_gestio_fichar;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_gestio_fichar.Model.Empleat;


@RequiresApi(api = Build.VERSION_CODES.O)
public class ContadorHores extends AppCompatActivity {

//    private ActivityResultLauncher<Intent> filePickerLauncher;
//    private Calculator calculator;
//    ZoneId zonaHoraria = ZoneId.systemDefault(); // Agafe la zona horaria del mobil
//    LocalDateTime horaActual = LocalDateTime.now(zonaHoraria); // Agafe la hora actual de la zona horaria, al ser LocalDateTime ens donarie algo amb aquest format: 2019-01-31T12:30:45

    private EditText idForm;
    private TextView resultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador_hores);

//        calculator = new Calculator();
        idForm = (EditText) findViewById(R.id.idForm);
        resultat = (TextView) findViewById(R.id.textViewId);

//        filePickerLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                            Uri selectedFileUri = result.getData().getData();
//                            if (selectedFileUri != null) {
//                                String filePath = selectedFileUri.getPath();
//                                String extension = filePath.substring(filePath.lastIndexOf(".") + 1);
//                                if (!extension.equals("csv") || extension.equals("xlsx")) {
//                                    Toast.makeText(ContadorHores.this, "El archivo seleccionado no es un CSV", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                try {
//                                    // Lee el archivo CSV y extrae información
//                                    readCSV(filePath);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                    Toast.makeText(ContadorHores.this, "Error al leer el archivo CSV", Toast.LENGTH_SHORT).show();
//                                } catch (CsvValidationException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }
//                        }
//                    }
//                }
//        );
//
//        openFilePicker();
    }

    public void search(View view) {
        String texte = idForm.getText().toString();
        int idText = Integer.parseInt(texte);

        if (!texte.isEmpty()) {
            Empleat empleat = new Empleat();
            empleat = empleat.filterById(idText);

            if (empleat != null) {
                resultat.setText(empleat.toString());
            } else {
                Toast.makeText(this, "Empleado no encontrado con el ID proporcionado", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Por favor, ingresa un ID válido", Toast.LENGTH_SHORT).show();
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void readCSV(String filePath) throws IOException, CsvValidationException {
//        CSVReader reader = new CSVReader(new FileReader(filePath));
//        String[] headers = reader.readNext(); // Te els noms de les columnes, els dies
//        String[] nextLine;
//        while ((nextLine = reader.readNext()) != null) {
//            // Acceso a los datos de cada columna en la fila actual
//            String dia = nextLine[0];
//            String horari = nextLine[1];
//            int contadorHores = calculator.contadorHores(horari);
//
//            if (!calculator.isWeekend(horaActual.getDayOfWeek().getValue())) { // Si no es dissabte o diumenge
//
//                if (dia.equals(horaActual.getDayOfWeek().toString())) { // Si el dia del CSV coincideix amb el dia actual i es entre setmana
//                    int horaCSV = Integer.parseInt(horari.substring(0, 2)); // Agafe les hores del CSV // by copilot
//                    int minutsCSV = Integer.parseInt(horari.substring(3, 5)); // Agafe els minuts del CSV // by copilot
//                    if (calculator.itsNow(horaCSV, minutsCSV)){
//
//                    }
//                }
//
//            }else{
//                Toast.makeText(this, "Avui no es treballa", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//        reader.close();
//    }

//    private void openFilePicker() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("text/csv"); // Puedes ajustar el tipo de archivo según tus necesidades
//        filePickerLauncher.launch(Intent.createChooser(intent, "Selecciona un archivo CSV"));
//    }
}
