package com.example.proekt_mpip_181190.activities

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proekt_mpip_181190.R
import com.example.proekt_mpip_181190.models.RecipeData
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import jp.wasabeef.richeditor.RichEditor

class CreateRecipeActivity : AppCompatActivity() {
    private lateinit var mEditor: RichEditor
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_recipe)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.db = Firebase.firestore

        // Get elements
        this.mEditor = findViewById(R.id.editor)
        this.cancelButton = findViewById(R.id.createRecipe_cancel)
        this.saveButton = findViewById(R.id.createRecipe_save)

        // Events
        this.cancelButton.setOnClickListener {
            finish()
        }
        this.saveButton.setOnClickListener {
            saveRecipeToFirestore(mEditor)
        }

        // Configure editor
        configureEditorAndEvents(mEditor)
        configureKeyboardResizeHandler()

    }

    private fun configureEditorAndEvents(mEditor: RichEditor) {
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Insert text here...");

        findViewById<View>(R.id.action_undo).setOnClickListener { mEditor.undo() }
        findViewById<View>(R.id.action_redo).setOnClickListener { mEditor.redo() }
        findViewById<View>(R.id.action_bold).setOnClickListener { mEditor.setBold() }
        findViewById<View>(R.id.action_clear_format).setOnClickListener { mEditor.removeFormat() }
        findViewById<View>(R.id.action_italic).setOnClickListener { mEditor.setItalic() }
        findViewById<View>(R.id.action_strikethrough).setOnClickListener { mEditor.setStrikeThrough() }
        findViewById<View>(R.id.action_underline).setOnClickListener { mEditor.setUnderline() }
        findViewById<View>(R.id.action_heading1).setOnClickListener { mEditor.setHeading(1) }
        findViewById<View>(R.id.action_heading2).setOnClickListener { mEditor.setHeading(2) }
        findViewById<View>(R.id.action_heading3).setOnClickListener { mEditor.setHeading(3) }
        findViewById<View>(R.id.action_align_left).setOnClickListener { mEditor.setAlignLeft() }
        findViewById<View>(R.id.action_align_center).setOnClickListener { mEditor.setAlignCenter() }
        findViewById<View>(R.id.action_align_right).setOnClickListener { mEditor.setAlignRight() }
        findViewById<View>(R.id.action_insert_bullets).setOnClickListener { mEditor.setBullets() }
        findViewById<View>(R.id.action_insert_numbers).setOnClickListener { mEditor.setNumbers() }
        findViewById<View>(R.id.action_insert_image).setOnClickListener {
            //TODO: Implement image picker
            mEditor.insertImage(
                "https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg",
                "dachshund", 320
            )
        }
    }

    private fun configureKeyboardResizeHandler() {
        val contentView = findViewById<View>(android.R.id.content)
        contentView.getViewTreeObserver().addOnGlobalLayoutListener {
            val r = Rect()
            contentView.getWindowVisibleDisplayFrame(r)
            val screenHeight = contentView.getRootView().height
            val keypadHeight = screenHeight - r.bottom
            val webView: RichEditor = findViewById(R.id.editor)
            val cancelSaveContainer: LinearLayout =
                findViewById(R.id.createRecipe_cancelSaveContainer)
            if (keypadHeight > screenHeight * 0.15) { // Keyboard is shown
                // Reduce the WebView's height or adjust layout parameters here
                webView.layoutParams.height =
                    (screenHeight - keypadHeight - Math.round(keypadHeight / 3.0)).toInt()
            } else {
                // Reset WebView's height or layout parameters when keyboard is hidden
//                webView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                webView.layoutParams.height = (screenHeight / 1.5).toInt()
            }

            webView.requestLayout()
        }
    }

    private fun saveRecipeToFirestore(mEditor: RichEditor) {

        val recipe = RecipeData(
            title = "Submit from da phone?? :OO",
            author = "Blazho",
            imageLink = "link?",
            recipeData = mEditor.html
        )

        this.db.collection("recipes")
            .add(recipe)
            .addOnSuccessListener { docReference ->
                Log.d("STORE", "Save id: ${docReference.id}")
                finish()
            }
            .addOnFailureListener { e ->
                Log.w("ERROR", e)
            }
    }
}
