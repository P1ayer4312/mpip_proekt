package com.example.proekt_mpip_181190.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proekt_mpip_181190.R
import com.example.proekt_mpip_181190.adapters.FeedRecyclerViewAdapter
import com.example.proekt_mpip_181190.models.RecipeCardData
import com.example.proekt_mpip_181190.models.RecipeData
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore

class DisplayRecipeActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_display_recipe)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.db = Firebase.firestore
        this.webView = findViewById(R.id.displayRecipe_webView)
        this.webView.settings.javaScriptEnabled = true
        this.webView.settings.loadWithOverviewMode = true
        this.webView.settings.useWideViewPort = true
        this.webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW


        this.webView.loadData(
            """
            <div>Loading recipe, please wait</div>
            """.trimIndent(),
            "text/html; charset=UTF-8",
            null
        )

        // Fetch document recipe by id sent from the list cart
        val documentId = intent.getStringExtra("DOCUMENT_ID")!!
        this.db.collection("recipes")
            .document(documentId)
            .get()
            .addOnSuccessListener { res: DocumentSnapshot ->
                val data = RecipeData(
                    res.data!!["title"] as String,
                    res.data!!["author"] as String,
                    res.data!!["imageLink"] as String,
                    res.data!!["recipeData"] as String,
                    res.data!!["thumbnail"] as String,
                    res.id
                )

                this.webView.loadDataWithBaseURL(
                    null,
                    """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    </head>
                    <body>
                        <h1>${data.title}</h1>
                        <h3>Author: ${data.author}</h3>
                        <br />
                        <div>${data.recipeData}</div>
                    </body>
                    </html>
                    """.trimIndent(),
                    "text/html",
                    "UTF-8",
                    null
                )
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreFetch", "Error getting documents.", exception)
            }
    }
}