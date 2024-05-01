package com.example.proekt_mpip_181190.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proekt_mpip_181190.R

class LoginActivity : AppCompatActivity() {
    private lateinit var loginButton: Button;
    private lateinit var backButton: Button;
    private lateinit var emailField: EditText;
    private lateinit var passwordField: EditText;
    private lateinit var invalidCredentialsText: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Intent
        val feedActivity = Intent(this, FeedActivity::class.java)

        // Find elements
        this.loginButton = findViewById(R.id.login_loginButton)
        this.backButton = findViewById(R.id.login_backButton)
        this.emailField = findViewById(R.id.login_emailField)
        this.passwordField = findViewById(R.id.login_passwordField)
        this.invalidCredentialsText = findViewById(R.id.login_invalidCredentialsErrorText)

        // Events
        this.backButton.setOnClickListener {
            finish()
        }
        this.loginButton.setOnClickListener {
            startActivity(feedActivity)
        }
    }


}