package com.example.proekt_mpip_181190.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proekt_mpip_181190.R

class SearchFieldActivity : AppCompatActivity() {
    private lateinit var inputField: EditText
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_field)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.inputField = findViewById(R.id.searchField_inputField)
        this.searchButton = findViewById(R.id.searchField_searchButton)

        val feedActivity = Intent(this, FeedActivity::class.java)
        this.searchButton.setOnClickListener {
            val searchTerm = this.inputField.text.toString()
            feedActivity.putExtra("SEARCH_TERM", searchTerm)
            startActivity(feedActivity)
        }
    }
}