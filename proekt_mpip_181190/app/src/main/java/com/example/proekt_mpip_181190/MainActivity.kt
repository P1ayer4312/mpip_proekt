package com.example.proekt_mpip_181190

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast
import com.example.proekt_mpip_181190.activities.CreateRecipeActivity
import com.example.proekt_mpip_181190.activities.CreateRecipeTitleImageActivity
import com.example.proekt_mpip_181190.activities.DisplayRecipeActivity
import com.example.proekt_mpip_181190.activities.FeedActivity
import com.example.proekt_mpip_181190.activities.LoginActivity
import com.example.proekt_mpip_181190.activities.RegisterActivity
import com.example.proekt_mpip_181190.models.RecipeData
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

//    private lateinit var loginButton: Button;
//    private lateinit var registerButton: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val feedActivity = Intent(this, FeedActivity::class.java)
        startActivity(feedActivity)

//        // Intents
//        val loginIntent = Intent(this, LoginActivity::class.java)
//        val registerIntent = Intent(this, RegisterActivity::class.java)
//        // Find elements
//        this.loginButton = findViewById(R.id.main_loginButton)
//        this.registerButton = findViewById(R.id.main_registerButton)
//
//        // Events
//        this.loginButton.setOnClickListener {
//            startActivity(loginIntent)
//        }
//        this.registerButton.setOnClickListener {
//            startActivity(registerIntent)
//        }

    }
}