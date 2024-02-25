package com.example.app_gestio_fichar.Hours;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_gestio_fichar.CSV.Info_horari;
import com.example.app_gestio_fichar.MainActivity;
import com.example.app_gestio_fichar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Contador_Hores extends AppCompatActivity {

    private Button countBtn;
    private TextView textViewEmail;
    private TextView worked_hours;
    private TextView textView4;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private Timer timer;
    private Handler handler = new Handler();
    private Validador validador;
    private Context context;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador_hores);

        countBtn = findViewById(R.id.countBtn);
        textViewEmail = findViewById(R.id.TextViewEmail);
        worked_hours = findViewById(R.id.worked_hours);
        textView4 = findViewById(R.id.textView4);
        mAuth = FirebaseAuth.getInstance();
        validador = new Validador();
        String uriString = getIntent().getStringExtra("file_uri");
        uri = Uri.parse(uriString);

        countBtn.setOnClickListener(v -> contar(v));
    }

    public void contar(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            textViewEmail.setText("Usuari " + currentUser.getEmail());
            db.collection("Empleats").document(currentUser.getEmail()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot != null) {
                            try {
                                Info_horari horari = new Info_horari();
                                horari = horari.llegirCSV(uri, getContentResolver(), LocalDateTime.now());

                                textView4.setText(horari.toString() + horari.getX().size());

                                if (horari != null && horari.getHores() != null && horari.getHores().size() > 0) {
                                    int dia = horari.getDia();
                                    String[] hores = horari.getHores().toArray(new String[0]);

                                    int diaActual = LocalDateTime.now().getDayOfWeek().getValue();
                                    if (dia == diaActual) {

                                        if (validador.isHomeTime(hores) || validador.isWeekend(diaActual)){
                                            worked_hours.setText("No estas en una hora de les del horari o estas a cap de setmana");
                                        } else {
                                            try {
                                                if (timer == null) {
                                                    worked_hours.setText("0");
                                                    timer = new Timer();
                                                    timer.scheduleAtFixedRate(new TimerTask() {
                                                        @Override
                                                        public void run() {
                                                            runOnUiThread(() -> updateUI(documentSnapshot, 1));
                                                        }
                                                        // aqui fa el contador d' hora en hora
                                                        // una hora de delay per no spamejar les hores, el delay es el TimeUnits.Hours.toMillis(1), que es una hora i nomes es fa al entrar
                                                    }, TimeUnit.HOURS.toMillis(1), TimeUnit.HOURS.toMillis(1));

                                                    countBtn.setEnabled(false);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Log.e("Contador_Hores", "Error al contar", e);
                                            }
                                        }
                                    }

                                } else {
                                    textViewEmail.setText("No s ha pogut obtenir l horario");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("Contador_Hores", "Error al obtener datos de Firebase", e);
                            }
                        }
                    });
        } else {
            textView4.setText("No s' ha obtingut al usuari");
        }
    }

    private void updateUI(DocumentSnapshot documentSnapshot, long hoursToAdd) {
        try {
            long workedHours = documentSnapshot.getLong("worked_hours");

            workedHours += hoursToAdd;

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String userEmail = currentUser.getEmail();
                db.collection("Empleats").document(userEmail)
                        .update("worked_hours", workedHours)
                        .addOnSuccessListener(aVoid -> Log.d("Contador_Hores", "Campo worked_hours actualizado correctamente"))
                        .addOnFailureListener(e -> Log.e("Contador_Hores", "Error al actualizar worked_hours", e));
            }

            String workedHoursString = String.valueOf(workedHours);
            handler.post(() -> worked_hours.setText(workedHoursString + " horas"));
            worked_hours.setText(workedHoursString);
        } catch (NumberFormatException e) {
            Log.e("Contador_Hores", "worked_hours no s ha recollit be", e);
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
    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
