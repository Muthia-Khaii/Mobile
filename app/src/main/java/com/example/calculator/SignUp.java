package com.example.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {
    private EditText etEmail, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Inisialisasi UI
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Inisialisasi DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Event listener untuk tombol daftar
        btnSignUp.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Validasi input
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignUp.this, "Harap isi semua bidang!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(SignUp.this, "Format email tidak valid!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(SignUp.this, "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignUp.this, "Konfirmasi password tidak cocok!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Proses registrasi
            if (dbHelper.registerUser(email, password)) {
                Toast.makeText(SignUp.this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUp.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(SignUp.this, "Email sudah terdaftar!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
