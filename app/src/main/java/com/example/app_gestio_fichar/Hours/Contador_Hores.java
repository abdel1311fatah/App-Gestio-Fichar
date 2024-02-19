package com.example.app_gestio_fichar.Hours;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_gestio_fichar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private long workedhours = 0;

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
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void contar(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            textView4.setText("Pille el usuari");
            db.collection("Empleats").document(currentUser.getEmail()).get().addOnSuccessListener(documentSnapshot -> {
                try {
                    if (documentSnapshot.exists()) {
                        textView3.setText("Pille el usuari de la collecio");
                        HashMap<String, String> empleat = new HashMap<>();

                        String nif = documentSnapshot.getString("nif");
                        String email = documentSnapshot.getString("email");
                        String password = documentSnapshot.getString("password");
                        String name = documentSnapshot.getString("name");
                        String surname = documentSnapshot.getString("surname");
                        String charge = documentSnapshot.getString("charge");
                        workedhours = documentSnapshot.getLong("worked_hours"); // Sin declarar nuevamente

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
                    } else {
                        textView3.setText("No hi ha usuari a la coleccio");
                    }
                } catch (Exception e) {
                    Log.e("Contador_Hores", "Error al obtener datos de Firebase", e);
                }

                // Realizar operaciones con la base de datos, si es necesario

                // Iniciar el temporizador si no está en ejecución
                if (timer == null) {
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            // Código que se ejecutará repetidamente cada 1 minuto
                            updateUI(1); // Aumentamos en 1 minuto
                        }
                    }, 0, TimeUnit.MINUTES.toMillis(1));

                    // Desactivar el botón para evitar múltiples inicios
                    countBtn.setEnabled(false);
                }
            });
        } else {
            textView4.setText("No pille el usuari");
        }
    }

    private void updateUI(long minutesToAdd) {
        // Actualiza la interfaz de usuario en el hilo principal
        workedhours += minutesToAdd;
        handler.post(() -> worked_hours.setText(String.valueOf(workedhours)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener el temporizador si está en ejecución
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}