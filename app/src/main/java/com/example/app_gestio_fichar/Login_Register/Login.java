package com.example.app_gestio_fichar.Login_Register;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_gestio_fichar.CSV.Info_horari;
import com.example.app_gestio_fichar.Crud;
import com.example.app_gestio_fichar.Hours.Contador_Hores;
import com.example.app_gestio_fichar.MainActivity;
import com.example.app_gestio_fichar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.time.LocalDateTime;

public class Login extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private EditText emailField;
    private EditText passwordField;
    private TextView textView5;
    private Uri selectedFileUri;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.editTextTextEmailAddress);
        passwordField = findViewById(R.id.editTextTextPassword);
        textView5 = findViewById(R.id.textView5);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            emailField.setText(currentUser.getEmail());
        }

        findViewById(R.id.loginBtn).setOnClickListener(v -> login());
        findViewById(R.id.selectFileButton).setOnClickListener(v -> openFilePicker());
        findViewById(R.id.mainBtn).setOnClickListener(v -> goToMain());
        findViewById(R.id.button).setOnClickListener(v -> {
            try {
                veureRuta(v);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
            selectedFileUri = data.getData();
            String selectedFilePath = selectedFileUri.getPath();
            Toast.makeText(this, "Archivo seleccionado: " + selectedFilePath, Toast.LENGTH_SHORT).show();
        }
    }

    private void login() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (!email.isEmpty() && !password.isEmpty()) {
            if (selectedFileUri == null) {
                Toast.makeText(Login.this, "Debes seleccionar un archivo antes de iniciar sesión", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                checkAndUpdateRutaHorari(user.getEmail());
                            }
                        } else {
                            Toast.makeText(Login.this, "El correo o la contraseña son incorrectos",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(Login.this, "El correo y la contraseña no pueden estar vacíos", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAndUpdateRutaHorari(String userEmail) {

        DocumentReference userDocRef = db.collection("Empleats").document(userEmail);
        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String rutaHorari = documentSnapshot.getString("ruta_horari"); // ruta d excel guardada a la db
                if (rutaHorari == null || rutaHorari.isEmpty()) { // si la ruta guardada a la db esta buida o es null li asignem la nova ruta
                    if (selectedFileUri != null) {
                        String filePath = selectedFileUri.getPath();
                        if (filePath != null) {

                            String nif = documentSnapshot.getString("nif");
                            String email = documentSnapshot.getString("email");
                            String password = documentSnapshot.getString("password");
                            String name = documentSnapshot.getString("name");
                            String surname = documentSnapshot.getString("surname");
                            String charge = documentSnapshot.getString("charge");
                            long workedHours = documentSnapshot.getLong("worked_hours");

                           Crud crud = new Crud(this);
                           crud.save(nif,email,password,name,surname,charge,workedHours,selectedFileUri.getPath());

                            if (selectedFileUri != null) {

                                Intent intent = new Intent(this, Contador_Hores.class);

                                intent.putExtra("ruta_horari", filePath); // Poner la ruta del archivo en el intent
                                intent.putExtra("file_uri", selectedFileUri.toString()); // Agregar la URI como dato extra

                                startActivity(intent);

                            } else {
                                Toast.makeText(Login.this, "No has agafat cap archiu", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(Login.this, "Error al obtener la ruta del archivo", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this, "Ningún archivo seleccionado", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Intent intent = new Intent(this, Contador_Hores.class);

                    if (selectedFileUri != null) {
                        rutaHorari = selectedFileUri.getPath();
                        intent.putExtra("ruta_horari", rutaHorari);
                        intent.putExtra("file_uri", selectedFileUri.toString()); // Agregar la URI como dato extra
                    }

                    startActivity(intent);

                }
            }
        }).addOnFailureListener(e -> Toast.makeText(Login.this,
                "Error al verificar la ruta del Excel en Firestore", Toast.LENGTH_SHORT).show());
    }

    public void veureRuta(View view) throws IOException {
        if (selectedFileUri != null) { // que hagui seleccionat un arxiu
            Info_horari infoHorari = new Info_horari();
            Info_horari horari = infoHorari.llegirCSV(selectedFileUri, getContentResolver(), LocalDateTime.now());
            textView5.setText("Ruta: " + selectedFileUri.getPath() + "\n" + "Contingut: " + horari.toString() + horari.getX().size() + " X");
        } else {
            textView5.setText("Has de seleccionar un arxiu");
        }
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}