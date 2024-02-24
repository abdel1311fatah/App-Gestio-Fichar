package com.example.app_gestio_fichar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_gestio_fichar.Login_Register.Login;
import com.example.app_gestio_fichar.Login_Register.Register;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void goToCrud(View view) {

        Intent intent = new Intent(this, Crud.class);
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