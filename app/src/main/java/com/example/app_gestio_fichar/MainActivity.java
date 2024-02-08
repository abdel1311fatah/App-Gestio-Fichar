package com.example.app_gestio_fichar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void goToFilterById(View view) {

        Intent intent = new Intent(this, ContadorHores.class);
        startActivity(intent);

    }
    public void goToRegister(View view) {

        Intent intent = new Intent(this, Register.class);
        startActivity(intent);

    }
    public void goToLogin(View view) {

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

    }

}