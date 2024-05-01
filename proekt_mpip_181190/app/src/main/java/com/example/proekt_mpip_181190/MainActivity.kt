package com.example.proekt_mpip_181190

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var loginButton: Button;
    private lateinit var registerButton: Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Intents
        val loginIntent = Intent(this, LoginActivity::class.java)
        val registerIntent = Intent(this, RegisterActivity::class.java)
        val feedActivity = Intent(this, FeedActivity::class.java) //TODO: REMOVE

        // Find elements
        this.loginButton = findViewById(R.id.main_loginButton);
        this.registerButton = findViewById(R.id.main_registerButton)

        // Events
        this.loginButton.setOnClickListener {
            startActivity(feedActivity)
        }
        this.registerButton.setOnClickListener {
            startActivity(registerIntent)
        }
    }
}