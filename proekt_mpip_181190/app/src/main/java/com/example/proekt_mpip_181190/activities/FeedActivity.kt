package com.example.proekt_mpip_181190.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proekt_mpip_181190.adapters.FeedRecyclerViewAdapter
import com.example.proekt_mpip_181190.R
import com.example.proekt_mpip_181190.models.RecipeCardData
import com.example.proekt_mpip_181190.models.RecipeData
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class FeedActivity : AppCompatActivity(), FeedRecyclerViewAdapter.RecyclerViewEvent {
    private lateinit var db: FirebaseFirestore
    private lateinit var recipeFirestoreItems: ArrayList<RecipeData>
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_feed)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.db = Firebase.firestore
        this.recipeFirestoreItems = arrayListOf()

        this.tabLayout = findViewById(R.id.feed_tabLayout)

        this.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                handleTabSelection(tab.position)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                handleTabSelection(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
        })

        val searchTerm = intent.getStringExtra("SEARCH_TERM")

        // Fetch and map Firebase data
        this.db.collection("recipes").get()
            .addOnSuccessListener { result: QuerySnapshot ->
                for (res in result.toList()) {
                    val title = res.data["title"] as String;

                    if (searchTerm != null &&
                        !title.lowercase().contains(searchTerm.lowercase())
                    ) {
                        // Skip item if search term is provided and the current item title
                        // doesn't contain the search term
                        continue
                    }

                    recipeFirestoreItems.add(
                        RecipeData(
                            res.data["title"] as String,
                            res.data["author"] as String,
                            res.data["imageLink"] as String,
                            res.data["recipeData"] as String,
                            res.data["thumbnail"] as String,
                            res.id
                        )
                    )
                }

                // Map data for cards
                val recipes = arrayListOf<RecipeCardData>()

                for (item in recipeFirestoreItems) {
                    recipes.add(
                        RecipeCardData(
                            item.title,
                            item.author,
                            item.thumbnail,
                            null
                        )
                    )
                }

                val recyclerView: RecyclerView = findViewById(R.id.feed_recyclerView)
                recyclerView.adapter = FeedRecyclerViewAdapter(recipes, this)
                recyclerView.layoutManager = LinearLayoutManager(this)
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreFetch", "Error getting documents.", exception)
            }

    }

    override fun onItemClick(position: Int) {
        val data = this.recipeFirestoreItems[position]
        val displayRecipeActivity = Intent(this, DisplayRecipeActivity::class.java)
        displayRecipeActivity.putExtra("DOCUMENT_ID", data.documentId)
        startActivity(displayRecipeActivity)
    }

    private fun handleTabSelection(tabPosition: Int) {
        when (tabPosition) {
            0 -> {
                val createRecipeTitleImageActivity =
                    Intent(this, CreateRecipeTitleImageActivity::class.java)

                startActivity(createRecipeTitleImageActivity)
            }

            1 -> {
                val searchFieldActivity = Intent(this, SearchFieldActivity::class.java)
                startActivity(searchFieldActivity)
            }
        }
    }
}