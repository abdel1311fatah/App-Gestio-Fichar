package com.example.app_gestio_fichar.Login_Register;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_gestio_fichar.Crud;
import com.example.app_gestio_fichar.MainActivity;
import com.example.app_gestio_fichar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextContrasena = findViewById(R.id.editTextPassword);
        EditText editTextDNI = findViewById(R.id.editTextDni);
        EditText editTextNom = findViewById(R.id.editTextNombre);
        EditText editTextCognom = findViewById(R.id.editTextApellido);
        EditText editTextCarreg = findViewById(R.id.editTextCharge);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextEmail.getText().toString();
                String password = editTextContrasena.getText().toString();
                String name = editTextNom.getText().toString();
                String surname = editTextCognom.getText().toString();
                String nif = editTextDNI.getText().toString();
                String carreg = editTextCarreg.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname) && !TextUtils.isEmpty(nif)) {
                    register(email, password, name, surname, nif, carreg);
                } else {
                    Toast.makeText(Register.this, "Has d' omplir tots els camps", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void register(String email, String password, String name, String surname, String nif, String carreg) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");

                            String filePath = ""; // aqui el deixem buit per que despres al login l acabem d omplir
                            Crud crud = new Crud(Register.this);
                            crud.save(nif, email, password, name, surname, carreg, 0, filePath);
                            Toast.makeText(Register.this, "T' has registrat correctament", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);

                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "No has pogut registrarte",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void goToMain(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

}