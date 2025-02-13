package com.example.calculator

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val layoutCalculator = findViewById<LinearLayout>(R.id.layoutCalculator)
        val layoutNotes = findViewById<LinearLayout>(R.id.layoutNotes)

        layoutCalculator.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java)) // Menuju Kalkulator
        }

        layoutNotes.setOnClickListener {
            startActivity(Intent(this, HNote::class.java)) // Menuju Notes
        }
    }
}
