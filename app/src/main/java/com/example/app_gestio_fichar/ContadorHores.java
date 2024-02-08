package com.example.app_gestio_fichar;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ContadorHores extends AppCompatActivity {

    private EditText idForm;
    private TextView resultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador_hores);
    }

    public void search(View view) {
        String texte = idForm.getText().toString();

        if (!texte.isEmpty()) {

        } else {
            Toast.makeText(this, "Por favor, ingresa un ID válido", Toast.LENGTH_SHORT).show();
        }
    }
}
