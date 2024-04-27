package com.example.proekt_mpip_181190

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var loginButton: Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginIntent = Intent(this, LoginActivity::class.java);

        // Find elements
        this.loginButton = findViewById(R.id.main_loginButton);

        // Events
        this.loginButton.setOnClickListener {
            startActivity(loginIntent)
        }
    }
}