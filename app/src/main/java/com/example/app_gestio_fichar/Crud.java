package com.example.app_gestio_fichar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    private Button deleteBtn;
    private FirebaseAuth mAuth;
    private Context mContext;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Crud() {
    }

    public Crud(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);

        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);
        deleteBtn = findViewById(R.id.deleteBtn);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            emailField.setText(currentUser.getEmail());
        }

        deleteBtn.setOnClickListener(v -> delete(emailField.getText().toString()));

    }

//    public Task<HashMap<String, String>> get(String gmail) {
//        TaskCompletionSource<HashMap<String, String>> taskCompletionSource = new TaskCompletionSource<>();
//
//        db.collection("Empleats").document(gmail).get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        HashMap<String, String> empleat = new HashMap<>();
//
//                        String nif = documentSnapshot.getString("nif");
//                        String email = documentSnapshot.getString("email");
//                        String password = documentSnapshot.getString("password");
//                        String name = documentSnapshot.getString("name");
//                        String surname = documentSnapshot.getString("surname");
//                        String charge = documentSnapshot.getString("charge");
//                        long workedHours = documentSnapshot.getLong("worked_hours");
//                        String ruta_horari = documentSnapshot.getString("ruta_horari");
//
//                        empleat.put("nif", nif);
//                        empleat.put("email", email);
//                        empleat.put("password", password);
//                        empleat.put("name", name);
//                        empleat.put("surname", surname);
//                        empleat.put("charge", charge);
//                        empleat.put("worked_hours", String.valueOf(workedHours));
//                        empleat.put("ruta_horari", ruta_horari);
//
//                        taskCompletionSource.setResult(empleat);
//
//                    } else {
//                        taskCompletionSource.setException(new RuntimeException("No se encontraron datos para el email proporcionado"));
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    taskCompletionSource.setException(e);
//                });
//
//        return taskCompletionSource.getTask();
//    }


    public void save(String nif, String email, String password, String name, String surname, String charge, long workedHours, String ruta_horari) {

        // Els objectes de firebase es poden guardar amb hashmap
        Map<String, Object> empleado = new HashMap<>();
        empleado.put("nif", nif);
        empleado.put("email", email);
        empleado.put("password", password);
        empleado.put("name", name);
        empleado.put("surname", surname);
        empleado.put("charge", charge);
        empleado.put("worked_hours", workedHours);
        empleado.put("ruta_horari", ruta_horari);

        db.collection("Empleats").document(email).set(empleado)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(mContext, "Has guardat be les dades", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(mContext, "No has pogut guardar les dades: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void delete(String email) {
        if (!email.isEmpty()) {
            String password = passwordField.getText().toString();

            if (password.isEmpty()) {
                Toast.makeText(this, "Has de ficar la contrasenya", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                FirebaseFirestore.getInstance().collection("Empleats").document(email).delete() // per el document
                                        .addOnSuccessListener(aVoidFirestore -> {
                                            Toast.makeText(this, "Has borrat l'empleat de firestore", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(eFirestore -> {
                                            Toast.makeText(this, "No s'ha pogut eliminar l'empleat de Firestore: " + eFirestore.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                                user.delete() // per a l authentication
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(this, "Has borrat l'empleat de l' authentication", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "No s'ha pogut eliminar l'usuari d'autenticació: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            Toast.makeText(this, "Error d'autenticació: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Has de ficar el correu", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}