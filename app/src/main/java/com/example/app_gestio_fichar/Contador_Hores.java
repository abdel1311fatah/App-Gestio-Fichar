package com.example.app_gestio_fichar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Contador_Hores extends AppCompatActivity {

    private Button countBtn;
    private TextView nif;
    private TextView email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private Crud crud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador_hores);

        countBtn = findViewById(R.id.countBtn);
        nif = findViewById(R.id.TextViewDNI);
        email = findViewById(R.id.TextViewEmail);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            email.setText(currentUser.getEmail());
        }
    }

    public void contar(View view) {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("Empleats").document(currentUser.getEmail()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            HashMap<String, String> empleat = new HashMap<>();

                            String nif = documentSnapshot.getString("nif");
                            String email = documentSnapshot.getString("email");
                            String password = documentSnapshot.getString("password");
                            String name = documentSnapshot.getString("name");
                            String surname = documentSnapshot.getString("surname");
                            String charge = documentSnapshot.getString("charge");
                            long workedHours = documentSnapshot.getLong("worked_hours");

                            empleat.put("nif", "44444444y");
                            empleat.put("email", email);
                            empleat.put("password", password);
                            empleat.put("name", name);
                            empleat.put("surname", surname);
                            empleat.put("charge", charge);

                            //Contar aqui les hores

                            empleat.put("worked_hours", String.valueOf(workedHours));

                            db.collection("Empleats").document(email).set(empleat)
                                    .addOnSuccessListener(aVoid -> {
                                        // Éxito al guardar en Firestore
                                        Toast.makeText(Contador_Hores.this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Error al guardar en Firestore
                                        Toast.makeText(Contador_Hores.this, "Error al guardar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });


                        } else {
                            Toast.makeText(Contador_Hores.this, "No existeix el usuari",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}