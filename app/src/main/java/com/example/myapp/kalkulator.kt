package com.example.myapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer
import android.view.View

class kalkulator : AppCompatActivity() {

    private lateinit var solution_tv: TextView
    private lateinit var result_tv: TextView
    private lateinit var btnExit: Button

    private var firstNumber = 0.0
    private var currentOperation = ""

    private lateinit var efek1: MediaPlayer
    private lateinit var efek2: MediaPlayer
    private lateinit var efek3: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kalkulator)

        // Initialize TextViews
        solution_tv = findViewById(R.id.solution_tv)
        result_tv = findViewById(R.id.result_tv)
        btnExit = findViewById(R.id.btnExit)

        // Initialize sounds
        efek1 = MediaPlayer.create(this, R.raw.efek_1)
        efek2 = MediaPlayer.create(this, R.raw.efek_2)
        efek3 = MediaPlayer.create(this, R.raw.efek_3)

        // Number buttons
        findViewById<Button>(R.id.button_0).setOnClickListener { appendNumber("0", efek1) }
        findViewById<Button>(R.id.button_1).setOnClickListener { appendNumber("1", efek1) }
        findViewById<Button>(R.id.button_2).setOnClickListener { appendNumber("2", efek2) }
        findViewById<Button>(R.id.button_3).setOnClickListener { appendNumber("3", efek1) }
        findViewById<Button>(R.id.button_4).setOnClickListener { appendNumber("4", efek2) }
        findViewById<Button>(R.id.button_5).setOnClickListener { appendNumber("5", efek1) }
        findViewById<Button>(R.id.button_6).setOnClickListener { appendNumber("6", efek2) }
        findViewById<Button>(R.id.button_7).setOnClickListener { appendNumber("7", efek1) }
        findViewById<Button>(R.id.button_8).setOnClickListener { appendNumber("8", efek2) }
        findViewById<Button>(R.id.button_9).setOnClickListener { appendNumber("9", efek1) }
        findViewById<Button>(R.id.button_dot).setOnClickListener { appendNumber(".", efek1) }

        // Operators
        findViewById<Button>(R.id.button_plus).setOnClickListener { setOperation("+", efek3) }
        findViewById<Button>(R.id.button_minus).setOnClickListener { setOperation("-", efek3) }
        findViewById<Button>(R.id.button_multiply).setOnClickListener { setOperation("*", efek3) }
        findViewById<Button>(R.id.button_divide).setOnClickListener { setOperation("/", efek3) }

        // Clear buttons
        findViewById<Button>(R.id.button_c).setOnClickListener {
            clearCalculator(efek3)
        }

        findViewById<Button>(R.id.button_ac).setOnClickListener {
            clearCalculator(efek3)
        }

        // Equals button
        findViewById<Button>(R.id.button_equals).setOnClickListener {
            calculateResult(efek3)
        }

        // Exit button
        btnExit.setOnClickListener {
            finish()
        }

        // We'll hide the bracket buttons since we're not using them in this simpler version
        findViewById<Button>(R.id.button_open_bracket).visibility = View.GONE
        findViewById<Button>(R.id.button_close_bracket).visibility = View.GONE
    }

    private fun appendNumber(number: String, efek: MediaPlayer) {
        efek.start() // Play sound
        if (result_tv.text == "0" || result_tv.text == "Error") {
            result_tv.text = number
        } else {
            result_tv.append(number)
        }
        updateSolutionTv()
    }

    private fun setOperation(operator: String, efek: MediaPlayer) {
        efek.start() // Play sound
        try {
            firstNumber = result_tv.text.toString().toDouble()
            currentOperation = operator
            result_tv.text = "0"
            updateSolutionTv()
        } catch (e: Exception) {
            result_tv.text = "Error"
            solution_tv.text = ""
            firstNumber = 0.0
            currentOperation = ""
        }
    }

    private fun calculateResult(efek: MediaPlayer) {
        efek.start() // Play sound
        try {
            val secondNumber = result_tv.text.toString().toDouble()
            val result = when (currentOperation) {
                "+" -> firstNumber + secondNumber
                "-" -> firstNumber - secondNumber
                "*" -> firstNumber * secondNumber
                "/" -> {
                    if (secondNumber == 0.0) throw ArithmeticException("Division by zero")
                    firstNumber / secondNumber
                }
                else -> secondNumber
            }

            // Format result to remove unnecessary decimal places
            val formattedResult = if (result % 1 == 0.0) {
                result.toLong().toString()
            } else {
                result.toString()
            }

            result_tv.text = formattedResult
            solution_tv.text = formattedResult
            firstNumber = result
            currentOperation = ""

        } catch (e: Exception) {
            result_tv.text = "Error"
            solution_tv.text = ""
            firstNumber = 0.0
            currentOperation = ""
        }
    }

    private fun clearCalculator(efek: MediaPlayer) {
        efek.start() // Play sound
        result_tv.text = "0"
        solution_tv.text = ""
        firstNumber = 0.0
        currentOperation = ""
    }

    private fun updateSolutionTv() {
        val currentText = when {
            currentOperation.isEmpty() -> result_tv.text
            else -> "$firstNumber $currentOperation ${result_tv.text}"
        }
        solution_tv.text = currentText
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the media players when the activity is destroyed
        efek1.release()
        efek2.release()
        efek3.release()
    }
}
