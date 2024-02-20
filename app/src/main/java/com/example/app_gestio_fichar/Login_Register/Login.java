package com.example.app_gestio_fichar.Login_Register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_gestio_fichar.Hours.Contador_Hores;
import com.example.app_gestio_fichar.MainActivity;
import com.example.app_gestio_fichar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 1;

    private EditText emailField;
    private EditText passwordField;
    private Button loginBtn;
    private Button d;
    private Button selectFileButton;
    private String selectedFilePath;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.editTextTextEmailAddress);
        passwordField = findViewById(R.id.editTextTextPassword);
        loginBtn = findViewById(R.id.loginBtn);
        selectFileButton = findViewById(R.id.selectFileButton);
        d = findViewById(R.id.button);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            emailField.setText(currentUser.getEmail());
        }

        loginBtn.setOnClickListener(v -> login());
        selectFileButton.setOnClickListener(v -> openFilePicker());
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedFilePath = data.getData().getPath();
            Toast.makeText(this, "Archivo seleccionado: " + selectedFilePath, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveSelectedFilePath(String userEmail, String filePath) {
        DocumentReference userDocRef = db.collection("Empleats").document(userEmail);

        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String rutaHorari = documentSnapshot.getString("ruta_horari");

                if (rutaHorari == null || rutaHorari.isEmpty()) {
                    // La ruta no existe en Firestore, la insertamos
                    insertRutaHorariInFirestore(userEmail, filePath);
                } else {
                    // La ruta ya existe, la actualizamos
                    updateRutaHorariInFirestore(userEmail, filePath);
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(Login.this,
                "Error al verificar la ruta del Excel en Firestore", Toast.LENGTH_SHORT).show());
    }

    private void updateRutaHorariInFirestore(String email, String rutaHorari) {
        db.collection("Empleats").document(email)
                .update("ruta_horari", rutaHorari)
                .addOnSuccessListener(aVoid -> Log.d("Login", "Ruta del Excel actualizada en Firestore"))
                .addOnFailureListener(e -> Log.e("Login", "Error al actualizar la ruta del Excel en Firestore", e));
    }

    private void insertRutaHorariInFirestore(String email, String rutaHorari) {
        Map<String, Object> data = new HashMap<>();
        data.put("ruta_horari", rutaHorari);

        db.collection("Empleats").document(email)
                .set(data)
                .addOnSuccessListener(aVoid -> Log.d("Login", "Ruta del Excel insertada en Firestore"))
                .addOnFailureListener(e -> Log.e("Login", "Error al insertar la ruta del Excel en Firestore", e));
    }

    public void login() {
        String email = emailField.getText().toString();
        passwordField.setText("123456789"); // Use a more secure placeholder password
        String password = passwordField.getText().toString();

        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Verify if the Excel path already exists in Firestore
                                saveSelectedFilePath(user.getEmail(), selectedFilePath); // Pass filePath instead of URI

                                Intent intent = new Intent(this, Contador_Hores.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(Login.this, "El correo o la contraseña son incorrectos",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void goToMain(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void veureRuta(View view){
        d.setText(selectedFilePath);
    }

}