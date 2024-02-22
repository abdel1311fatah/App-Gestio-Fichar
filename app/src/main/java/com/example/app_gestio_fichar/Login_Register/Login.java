package com.example.app_gestio_fichar.Login_Register;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Login extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private String dades;
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

    private void veureRuta(View view) throws IOException {
//        if (selectedFileUri != null) {
        textView5.setText(/*"Ruta del archivo: " + selectedFileUri.getPath() + "\n" + */ llegirCSV());
//        } else {
//            textView5.setText("Ningún archivo seleccionado");
//        }
    }

    private void login() {
        String email = emailField.getText().toString();
        passwordField.setText("123456789");
        String password = passwordField.getText().toString();

        if (!email.isEmpty() && !password.isEmpty()) {
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
        }
    }

    private void checkAndUpdateRutaHorari(String userEmail) {
        DocumentReference userDocRef = db.collection("Empleats").document(userEmail);

        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String rutaHorari = documentSnapshot.getString("ruta_horari");

                if (rutaHorari == null || rutaHorari.isEmpty()) {
                    if (selectedFileUri != null) {
                        String filePath = selectedFileUri.getPath();
                        if (filePath != null) {
                            insertRutaHorariInFirestore(userEmail, filePath);
                            Intent intent = new Intent(this, Contador_Hores.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Login.this, "Error al obtener la ruta del archivo", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this, "Ningún archivo seleccionado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(this, Contador_Hores.class);
                    startActivity(intent);
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(Login.this,
                "Error al verificar la ruta del Excel en Firestore", Toast.LENGTH_SHORT).show());
    }

    private void insertRutaHorariInFirestore(String email, String rutaHorari) {
        Map<String, Object> data = new HashMap<>();
        data.put("ruta_horari", rutaHorari);

        db.collection("Empleats").document(email)
                .set(data)
                .addOnSuccessListener(aVoid -> Log.d("Login", "Ruta del Excel insertada en Firestore"))
                .addOnFailureListener(e -> Log.e("Login", "Error al insertar la ruta del Excel en Firestore", e));
    }

    //    private String llegirCSV() throws IOException {
//        InputStream myInput;
//        AssetManager assetManager = getAssets();
//        myInput = assetManager.open("Horari.xlsx");
//
//        XSSFWorkbook myWorkBook = new XSSFWorkbook(myInput);
//        XSSFSheet mySheet = myWorkBook.getSheetAt(0);
//
////        // Assuming you want to read the first row and first cell of the first sheet
//        Row row = mySheet.getRow(3);
//        String data = row.getCell(2).getStringCellValue();
//
//        myInput.close(); // Close the InputStream when you're done
//
//        return data;
//    }
    private String llegirCSV() throws IOException {
        InputStream myInput = null;
        dades = ""; // Reinicia la variable dades

        try {
            AssetManager assetManager = getAssets();
            myInput = assetManager.open("Horari.xlsx");

            XSSFWorkbook workbook = new XSSFWorkbook(myInput);  // Cambiado a XSSFWorkbook para archivos .xlsx
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();

            while (rowIterator.hasNext()) {
                XSSFRow row = (XSSFRow) rowIterator.next();  // Cambiado a XSSFRow para archivos .xlsx
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    XSSFCell cell = (XSSFCell) cellIterator.next();  // Cambiado a XSSFCell para archivos .xlsx
                    dades += cell.toString() + " ";
                }
                dades += "\n";
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (myInput != null) {
                    myInput.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dades;
    }


    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
