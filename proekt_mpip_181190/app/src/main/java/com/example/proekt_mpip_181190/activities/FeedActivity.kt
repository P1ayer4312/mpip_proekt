package com.example.proekt_mpip_181190.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proekt_mpip_181190.adapters.FeedRecyclerViewAdapter
import com.example.proekt_mpip_181190.R
import com.example.proekt_mpip_181190.models.RecipeCardData

class FeedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO: REMOVE THIS
        val recipes = arrayOf(
            RecipeCardData(
                "Simple cake",
                "Some user",
                "https://www.recipetineats.com/wp-content/uploads/2020/08/My-best-Vanilla-Cake_9-SQ.jpg",
                null
            ),
            RecipeCardData(
                "Simple cake 2",
                "Some user 2",
                "https://www.recipetineats.com/wp-content/uploads/2020/08/My-best-Vanilla-Cake_9-SQ.jpg",
                null
            ),
            RecipeCardData(
                "Simple cake 2",
                "Some user 2",
                "https://www.recipetineats.com/wp-content/uploads/2020/08/My-best-Vanilla-Cake_9-SQ.jpg",
                null
            ),
            RecipeCardData(
                "Simple cake 2",
                "Some user 2",
                "https://www.recipetineats.com/wp-content/uploads/2020/08/My-best-Vanilla-Cake_9-SQ.jpg",
                null
            ),
            RecipeCardData(
                "Simple cake 2",
                "Some user 2",
                "https://www.recipetineats.com/wp-content/uploads/2020/08/My-best-Vanilla-Cake_9-SQ.jpg",
                null
            ),
            RecipeCardData(
                "Simple cake 2",
                "Some user 2",
                "https://www.recipetineats.com/wp-content/uploads/2020/08/My-best-Vanilla-Cake_9-SQ.jpg",
                null
            ),
            RecipeCardData(
                "Simple cake 2",
                "Some user 2",
                "https://www.recipetineats.com/wp-content/uploads/2020/08/My-best-Vanilla-Cake_9-SQ.jpg",
                null
            ),
            RecipeCardData(
                "Simple cake 2",
                "Some user 2",
                "https://www.recipetineats.com/wp-content/uploads/2020/08/My-best-Vanilla-Cake_9-SQ.jpg",
                null
            ),
            RecipeCardData(
                "Simple cake 2",
                "Some user 2",
                "https://www.recipetineats.com/wp-content/uploads/2020/08/My-best-Vanilla-Cake_9-SQ.jpg",
                null
            ),
            RecipeCardData(
                "Simple cake 2",
                "Some user 2",
                "https://www.recipetineats.com/wp-content/uploads/2020/08/My-best-Vanilla-Cake_9-SQ.jpg",
                null
            )
        )

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_feed)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView: RecyclerView = findViewById(R.id.feed_recyclerView)
        recyclerView.adapter = FeedRecyclerViewAdapter(recipes.toList())
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}