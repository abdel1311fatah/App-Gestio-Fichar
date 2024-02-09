package com.example.app_gestio_fichar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Crud extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Button getBtn;
    private Button deleteBtn;
    private Button saveBtn;
    private FirebaseAuth mAuth;

    String nif, email, password, name, surname, charge;
    int workedHours;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);

        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextContrasena);
        getBtn = findViewById(R.id.getBtn);
        saveBtn = findViewById(R.id.saveBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            emailField.setText(currentUser.getEmail());
        }

        getBtn.setOnClickListener(v -> get());
        saveBtn.setOnClickListener(v -> save(nif,  email, password, name, surname, charge, workedHours));
        deleteBtn.setOnClickListener(v -> delete(emailField.getText().toString()));
    }
    public void get() {

    }
    public void save(String nif, String email, String password, String name, String surname, String charge, int workedHours) {

        // Crear un mapa con los datos que deseas almacenar
        Map<String, Object> empleado = new HashMap<>();
        empleado.put("nif", nif);
        empleado.put("email", email);
        empleado.put("password", password);
        empleado.put("name", name);
        empleado.put("surname", surname);
        empleado.put("charge", charge);
        empleado.put("worked_hours", workedHours);

        // Agregar el documento a la colección "Empleats" con el correo electrónico como identificador único
        db.collection("Empleats").document(email).set(empleado)
                .addOnSuccessListener(aVoid -> {
                    // Éxito al guardar en Firestore
                    Toast.makeText(Crud.this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Error al guardar en Firestore
                    Toast.makeText(Crud.this, "Error al guardar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void delete(String email) {
        db.collection("Empleats").document(email).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Crud.this, "Has borrat l' empleat", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Crud.this, "No s' ha pogut eliminar l' empleat" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}