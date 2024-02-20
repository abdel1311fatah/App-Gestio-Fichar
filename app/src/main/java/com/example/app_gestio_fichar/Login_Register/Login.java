package com.example.app_gestio_fichar.Login_Register;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class Login extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 1;

    private EditText emailField;
    private EditText passwordField;
    private Button loginBtn;
    private Button selectFileButton;
    private Button uploadExcelBtn;
    private Uri selectedFileUri;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.editTextTextEmailAddress);
        passwordField = findViewById(R.id.editTextTextPassword);
        loginBtn = findViewById(R.id.loginBtn);
        selectFileButton = findViewById(R.id.selectFileButton);
        uploadExcelBtn = findViewById(R.id.uploadExcelBtn);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            emailField.setText(currentUser.getEmail());
        }

        loginBtn.setOnClickListener(v -> login());
        selectFileButton.setOnClickListener(v -> openFilePicker());
        uploadExcelBtn.setOnClickListener(v -> uploadExcel());
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");  // MIME type para archivos Excel (.xlsx)
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

    private void saveSelectedFilePath(String filePath) {
        // Puedes implementar lógica para guardar la ruta del archivo según tus necesidades
        // Por ejemplo, almacenarlo en SharedPreferences, base de datos, etc.
    }

    public void login() {
        String email = emailField.getText().toString();
        passwordField.setText("123456789");
        String password = passwordField.getText().toString();

        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
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

    public void uploadExcel() {
        if (selectedFileUri != null) {
            // Acciones específicas al subir el archivo, por ejemplo, subirlo a Firebase Storage
            Toast.makeText(this, "Subir Excel: " + selectedFileUri.getPath(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Selecciona un archivo Excel primero", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}