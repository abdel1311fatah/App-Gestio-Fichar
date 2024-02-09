package com.example.app_gestio_fichar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Crud extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private TextView result;
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
        result = findViewById(R.id.result);
        getBtn = findViewById(R.id.getBtn);
        saveBtn = findViewById(R.id.saveBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            emailField.setText(currentUser.getEmail());
        }

        getBtn.setOnClickListener(v -> get(emailField.getText().toString()));
        saveBtn.setOnClickListener(v -> save(nif,  email, password, name, surname, charge, workedHours));
        deleteBtn.setOnClickListener(v -> delete(emailField.getText().toString()));
    }
    public Task<HashMap<String, String>> get(String gmail) { // No deixa fer un metode hashmap directament per una cosa de FireStore
        TaskCompletionSource<HashMap<String, String>> taskCompletionSource = new TaskCompletionSource<>();

        db.collection("Empleats").document(gmail).get()
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

                        empleat.put("nif", nif);
                        empleat.put("email", email);
                        empleat.put("password", password);
                        empleat.put("name", name);
                        empleat.put("surname", surname);
                        empleat.put("charge", charge);
                        empleat.put("worked_hours", String.valueOf(workedHours));

                        taskCompletionSource.setResult(empleat);

                        result.setText(empleat.toString());

                    } else {
                        // El documento no existe
                        Toast.makeText(Crud.this, "No se encontraron datos para el email proporcionado", Toast.LENGTH_SHORT).show();
                        taskCompletionSource.setException(new RuntimeException("No se encontraron datos"));
                    }
                })
                .addOnFailureListener(e -> {
                    // Error al intentar recuperar el documento
                    Toast.makeText(Crud.this, "Error al obtener datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    taskCompletionSource.setException(e);
                });

        return taskCompletionSource.getTask();
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