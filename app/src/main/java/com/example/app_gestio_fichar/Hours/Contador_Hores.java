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
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;
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

        countBtn.setOnClickListener(v -> contar(v)); // Cleaner click handling
    }

    public void contar(View view) {

        int actualDay = LocalDateTime.now().getDayOfWeek().getValue(); // el getValue es per que pase de dayOfWeek a int
        int actualHour = LocalDateTime.now().getHour();
        int actualMinute = LocalDateTime.now().getMinute();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            textView4.setText("Usuario obtenido");
            db.collection("Empleats").document(currentUser.getEmail()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        try {
                            if (documentSnapshot.exists()) {
                                textView3.setText("Usuario en la colección");
                                HashMap<String, String> empleat = new HashMap<>();

                                String nif = documentSnapshot.getString("nif");
                                String email = documentSnapshot.getString("email");
                                String password = documentSnapshot.getString("password");
                                String name = documentSnapshot.getString("name");
                                String surname = documentSnapshot.getString("surname");
                                String charge = documentSnapshot.getString("charge");
                                String filePath = documentSnapshot.getString("ruta_horari"); // Get route from Firestore

                                textViewEmail.setText(email);
                                textViewNif.setText(nif);
                                empleat.put("nif", nif);
                                empleat.put("email", email);
                                empleat.put("password", password);
                                empleat.put("name", name);
                                empleat.put("surname", surname);
                                empleat.put("charge", charge);
                                empleat.put("ruta_horari", filePath);

                                if (empleat.isEmpty()) {
                                    textView2.setText("Empleado vacío");
                                } else if (empleat == null) {
                                    textView2.setText("Empleado nulo");
                                } else {
                                    textView2.setText(empleat.toString());

                                    File file = new File(Objects.requireNonNull(empleat.get("ruta_horari")));
                                    if (!file.exists()) {
                                        throw new FileNotFoundException("El archivo no existe: " + file.getAbsolutePath());
                                    } else {
                                        textViewEmail.setText("El archivo existe");
                                    }


                                    //quiero recojer aqui los datos del csv

                                    try {
                                        String outputFileName = getFilesDir().getPath() + "/HorariOutput";
                                        Info_horari horari = csvReader.get_info_horari(file.getAbsolutePath(), outputFileName);

                                        if (horari != null) {
                                            String[] dies = horari.getDies();
                                            String[] hores = horari.getHores();
                                            String[] x = horari.getX();

                                            // Use the collected data to determine if it's working time
                                            boolean isWorkingTime = validador.isWorkingTime(dies, hores, x, actualDay, actualHour, actualMinute);

                                            if (isWorkingTime) {
                                                // Start the timer to count hours
                                                if (timer == null) {
                                                    worked_hours.setText("0");
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
                                                textView3.setText("No es hora de trabajo según el horario");
                                            }
                                        } else {
                                            textView3.setText("Error al obtener el horario");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.e("Contador_Hores", "Error al abrir y procesar el archivo: " + e.getMessage());
                                        textView3.setText("Error al abrir y procesar el archivo");
                                    }
                                }
                            } else {
                                textView3.setText("No hay usuario en la colección");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Contador_Hores", "Error al obtener datos de Firebase", e);
                        }
                    });
        } else {
            textView4.setText("No se ha obtenido el usuario");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
