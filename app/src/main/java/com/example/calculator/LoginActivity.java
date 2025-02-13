package com.example.calculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnDeleteAll;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi UI
        etEmail = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);

        // Inisialisasi DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Cek apakah ada pengguna terdaftar
        if (!dbHelper.hasRegisteredUser()) {
            // Jika belum ada pengguna terdaftar, arahkan ke halaman pendaftaran
            Toast.makeText(this, "Belum ada pengguna terdaftar, silakan daftar terlebih dahulu.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, SignUp.class));
            finish();
        }

        // Event listener untuk tombol login
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Harap isi semua bidang!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.readUser(email, password)) {
                Toast.makeText(LoginActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, Laman_Utama.class));
                finish();
            } else {
                showLoginErrorDialog();
            }
        });

        // Event listener untuk tombol hapus semua pengguna
        btnDeleteAll.setOnClickListener(v -> {
            dbHelper.deleteAllUsers();
            Toast.makeText(LoginActivity.this, "Semua data pengguna telah dihapus!", Toast.LENGTH_SHORT).show();

            // Setelah menghapus data, arahkan pengguna ke halaman registrasi
            startActivity(new Intent(LoginActivity.this, SignUp.class));
            finish();
        });
    }


    // Menampilkan dialog kesalahan login
    private void showLoginErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Login Gagal")
                .setMessage("Email atau Password salah. Silakan coba lagi.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}
