package com.example.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Laman_Utama extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laman_utama);  // Pastikan layout kamu adalah laman_utama

        // Menemukan tombol berdasarkan ID
        Button btnClose = findViewById(R.id.btn_keluar);

        // Mengatur OnClickListener untuk tombol
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuat Intent untuk berpindah ke MainActivity (yang berisi kalkulator)
                Intent intent = new Intent(Laman_Utama.this, MainActivity.class);
                startActivity(intent);  // Menjalankan intent untuk pindah ke MainActivity
                finish();  // Menutup aktivitas saat ini (Laman_Utama)
            }
        });
    }
}
