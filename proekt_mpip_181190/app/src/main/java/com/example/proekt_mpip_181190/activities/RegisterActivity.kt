package com.example.proekt_mpip_181190.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proekt_mpip_181190.R

class RegisterActivity : AppCompatActivity() {
    private lateinit var usernameField: EditText;
    private lateinit var emailField: EditText;
    private lateinit var passwordField: EditText;
    private lateinit var backButton: Button;
    private lateinit var registerButton: Button;
    private lateinit var invalidCredentialsText: TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find elements
        this.usernameField = findViewById(R.id.register_usernameField)
        this.emailField = findViewById(R.id.register_emailField)
        this.passwordField = findViewById(R.id.register_passwordField)
        this.backButton = findViewById(R.id.register_backButton)
        this.registerButton = findViewById(R.id.register_registerButton)
        this.invalidCredentialsText = findViewById(R.id.register_invalidCredentialsErrorText)

        // Events
        this.backButton.setOnClickListener {
            finish()
        }

    }
}