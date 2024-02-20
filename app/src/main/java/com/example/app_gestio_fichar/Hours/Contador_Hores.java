package com.example.app_gestio_fichar.Hours;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_gestio_fichar.CSV.CSV_Reader;
import com.example.app_gestio_fichar.CSV.Info_horari;
import com.example.app_gestio_fichar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Contador_Hores extends AppCompatActivity {

    private Button countBtn;
    private TextView textViewNif;
    private TextView textViewEmail;
    private TextView worked_hours;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private Timer timer;
    private Handler handler = new Handler();
    private CSV_Reader csvReader;
    private Validador validador;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador_hores);

        countBtn = findViewById(R.id.countBtn);
        textViewNif = findViewById(R.id.TextViewDNI);
        textViewEmail = findViewById(R.id.TextViewEmail);
        worked_hours = findViewById(R.id.worked_hours);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        mAuth = FirebaseAuth.getInstance();
        csvReader = new CSV_Reader();
        validador = new Validador();
        context = this;

        copyAssetFileToInternalStorage("Horari.xlsx");
    }

    public void contar(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            textView4.setText("Pille el usuario");
            db.collection("Empleats").document(currentUser.getEmail()).get().addOnSuccessListener(documentSnapshot -> {
                try {
                    if (documentSnapshot.exists()) {
                        textView3.setText("Pille el usuario de la colección");
                        HashMap<String, String> empleat = new HashMap<>();

                        String nif = documentSnapshot.getString("nif");
                        String email = documentSnapshot.getString("email");
                        String password = documentSnapshot.getString("password");
                        String name = documentSnapshot.getString("name");
                        String surname = documentSnapshot.getString("surname");
                        String charge = documentSnapshot.getString("charge");

                        textViewEmail.setText(email);
                        textViewNif.setText(nif);
                        empleat.put("nif", nif);
                        empleat.put("email", email);
                        empleat.put("password", password);
                        empleat.put("name", name);
                        empleat.put("surname", surname);
                        empleat.put("charge", charge);

                        if (empleat.isEmpty()) {
                            textView2.setText("empleat esta buit");
                        } else if (empleat == null) {
                            textView2.setText("empleat es null");
                        } else {
                            textView2.setText(empleat.toString());
                        }

                        String inputFileName = "Horari";
                        File file = new File(context.getFilesDir(), inputFileName);
                        try {
                            if (!file.exists()) {
                                throw new FileNotFoundException("El archivo no existe: " + file.getAbsolutePath());
                            }else{
                                textViewEmail.setText("El arxiu existeix");
                            }

                            // Rest of the code to open and process the file
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Log.e("Contador_Hores", "Error: El archivo no existe");
                            textView3.setText("Error: El archivo no existe");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Contador_Hores", "Error al abrir y procesar el archivo: " + e.getMessage());
                            textView3.setText("Error al abrir y procesar el archivo");
                        }
                        String outputFileName = getFilesDir().getPath() + "/HorariOutput";
                        Info_horari horari = csvReader.get_info_horari(file.getAbsolutePath(), outputFileName);

                        if (horari != null) {
                            if (timer == null) {
                                worked_hours.setText("0");  // Changed to String
                                timer = new Timer();
                                timer.scheduleAtFixedRate(new TimerTask() {
                                    @Override
                                    public void run() {
                                        updateUI(documentSnapshot, 1);
                                    }
                                }, 0, TimeUnit.HOURS.toMillis(1));

                                countBtn.setEnabled(false);
                            }
                        } else {
                            textView3.setText("Error al obtener el horario");
                        }
                    } else {
                        textView3.setText("No hay usuario en la colección");
                    }
                } catch (Exception e) {
                    Log.e("Contador_Hores", "Error al obtener datos de Firebase", e);
                }
            });
        } else {
            textView4.setText("No pille el usuario");
        }
    }

    private void updateUI(DocumentSnapshot documentSnapshot, long hoursToAdd) {
        try {
            String workedHoursString = documentSnapshot.getString("worked_hours");
            long workedhours = Long.parseLong(workedHoursString);
            workedhours += hoursToAdd;
            final String finalWorkedHours = String.valueOf(workedhours);  // Changed to String
            handler.post(() -> worked_hours.setText(finalWorkedHours));
        } catch (NumberFormatException e) {
            Log.e("Contador_Hores", "Error al convertir 'worked_hours' a número", e);
        }
    }

    private void copyAssetFileToInternalStorage(String fileName) {
        try {
            File file = new File(context.getFilesDir(), fileName);

            if (!file.exists()) {
                InputStream inputStream = context.getAssets().open(fileName);
                FileOutputStream outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Contador_Hores", "Error al copiar el archivo desde assets: " + e.getMessage());
            textView3.setText("Error al copiar el archivo desde assets");

            // Si ocurre un error al copiar desde assets, intenta mover desde la ubicación predeterminada
            try {
                File destFile = new File(context.getFilesDir(), fileName);
                File sourceFile = new File("app/src/main/assets/" + fileName);

                if (sourceFile.exists() && sourceFile.isFile()) {
                    InputStream inputStream = new FileInputStream(sourceFile);
                    FileOutputStream outputStream = new FileOutputStream(destFile);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }

                    outputStream.close();
                    inputStream.close();

                    Log.i("Contador_Hores", "Archivo copiado desde la ubicación predeterminada.");
                } else {
                    Log.e("Contador_Hores", "Error: El archivo no existe en la ubicación predeterminada.");
                    textView3.setText("Error: El archivo no existe en la ubicación predeterminada.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                Log.e("Contador_Hores", "Error al copiar el archivo desde la ubicación predeterminada: " + ex.getMessage());
                textView3.setText("Error al copiar el archivo desde la ubicación predeterminada.");
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
