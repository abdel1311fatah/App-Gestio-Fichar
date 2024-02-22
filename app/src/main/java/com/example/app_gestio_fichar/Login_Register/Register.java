package com.example.app_gestio_fichar.Login_Register;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button selectFileButton;
    private Uri selectedFileUri;

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private Button d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextContrasena = findViewById(R.id.editTextContrasena);
        EditText editTextDNI = findViewById(R.id.editTextDni);
        EditText editTextNom = findViewById(R.id.editTextNombre);
        EditText editTextCognom = findViewById(R.id.editTextApellido);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextEmail.getText().toString();
                String password = editTextContrasena.getText().toString();
                String name = editTextNom.getText().toString();
                String surname = editTextCognom.getText().toString();
                String nif = editTextDNI.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname) && !TextUtils.isEmpty(nif)) {
                    register(email, password, name, surname, nif);
                } else {
                    Toast.makeText(Register.this, "Has d' omplir tots els camps", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void register(String email, String password, String name, String surname, String nif) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Insertar aqui tots els camps del formulari a firestore i fer lo de contar hores

                            Crud crud = new Crud();
//                            crud.save(nif,email,password,name,surname,"Professor",0); // Anyadir careregs a la logica

                            Toast.makeText(Register.this, "T' has registrat correctament",
                                    Toast.LENGTH_SHORT).show();
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