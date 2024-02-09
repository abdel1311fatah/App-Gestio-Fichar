package com.example.app_gestio_fichar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Button loginBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.editTextTextEmailAddress);
        passwordField = findViewById(R.id.editTextTextPassword);
        loginBtn = findViewById(R.id.loginBtn);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            emailField.setText(currentUser.getEmail());
        }

        loginBtn.setOnClickListener(v -> login());
    }

    public void login() {
        String email = emailField.getText().toString();
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
                            Toast.makeText(Login.this, "El correu o la contrasenya son incorrectes",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void goToMain(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

}