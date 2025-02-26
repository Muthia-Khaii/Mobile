package com.example.myapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class loginn : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginn)

        dbHelper = DatabaseHelper(this)

        // Ambil elemen dari layout
        val usernameEditText = findViewById<TextInputEditText>(R.id.username)
        val passwordEditText = findViewById<TextInputEditText>(R.id.password)
        val usernameLayout = findViewById<TextInputLayout>(R.id.username_layout)
        val passwordLayout = findViewById<TextInputLayout>(R.id.password_layout)
        val loginButton = findViewById<MaterialButton>(R.id.login_button)
        val signupLink = findViewById<TextView>(R.id.signup_link)

        loginButton.setOnClickListener {
            val username = usernameEditText.text?.toString()?.trim() ?: ""
            val password = passwordEditText.text?.toString()?.trim() ?: ""

            // Reset error
            usernameLayout.error = null
            passwordLayout.error = null

            if (!isValidUsername(username)) {
                usernameLayout.error = "Username harus minimal 6 karakter!"
                return@setOnClickListener
            }

            if (!isValidPassword(password)) {
                passwordLayout.error = "Password harus minimal 6 karakter!"
                return@setOnClickListener
            }

            if (dbHelper.readUser(username, password)) {
                Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                usernameLayout.error = "Username atau Password salah!"
                passwordLayout.error = "Silakan coba lagi."
            }
        }

        // Pindah ke halaman Signup
        signupLink.setOnClickListener {
            startActivity(Intent(this, signup::class.java))
        }
    }

    // Validasi username minimal 6 karakter
    private fun isValidUsername(username: String): Boolean {
        return username.length >= 6
    }

    // Validasi password minimal 6 karakter
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}
