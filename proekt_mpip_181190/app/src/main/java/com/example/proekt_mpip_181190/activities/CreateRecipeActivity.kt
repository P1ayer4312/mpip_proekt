package com.example.proekt_mpip_181190.activities

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proekt_mpip_181190.R
import com.example.proekt_mpip_181190.models.RecipeData
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask.TaskSnapshot
import com.google.firebase.storage.storage
import jp.wasabeef.richeditor.RichEditor
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.UUID

class CreateRecipeActivity : AppCompatActivity() {
    private lateinit var mEditor: RichEditor
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    private lateinit var db: FirebaseFirestore
    private lateinit var dbStorage: FirebaseStorage
    private lateinit var thumbnailUrlFirebase: String
    private lateinit var authorName: String

    private lateinit var thumbnailUriString: String
    private lateinit var recipeTitle: String
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
        this.dbStorage = Firebase.storage

        this.recipeTitle = intent.getStringExtra("RECIPE_TITLE")!!
        this.thumbnailUriString = intent.getStringExtra("THUMBNAIL_URI")!!
        this.authorName = intent.getStringExtra("AUTHOR_NAME")!!

        val file = Uri.parse(this.thumbnailUriString)
        val thumbnailImgUUID = UUID.randomUUID().toString()
        val fileRef = this.dbStorage.reference.child("images/${thumbnailImgUUID}")
        val uploadTask = fileRef.putFile(file)

        uploadTask.addOnFailureListener {
            Log.w("ERROR", "Unable to upload file")
        }.addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener {
                this.thumbnailUrlFirebase = it.toString()
            }
        }

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
        val imagePicker =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    val thumbnailImgUUID = UUID.randomUUID().toString()
                    val fileRef = this.dbStorage.reference.child("images/${thumbnailImgUUID}")
                    val uploadTask = fileRef.putFile(uri)

                    uploadTask.addOnFailureListener {
                        Log.w("ERROR", "Unable to upload file")
                    }.addOnSuccessListener {
                        fileRef.downloadUrl.addOnSuccessListener {

                            mEditor.insertImage(
                                it.toString(),
                                "", 320
                            )
                        }
                    }
                }

            }

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
            imagePicker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
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
                webView.layoutParams.height = (screenHeight / 1.5).toInt()
            }

            webView.requestLayout()
        }
    }

    private fun saveRecipeToFirestore(mEditor: RichEditor) {
        val recipe = RecipeData(
            title = this.recipeTitle,
            author = this.authorName,
            imageLink = "",
            recipeData = mEditor.html,
            thumbnail = this.thumbnailUrlFirebase,
            documentId = ""
        )

        Toast.makeText(
            this,
            "Saving recipe...",
            Toast.LENGTH_LONG
        ).show()

        this.db.collection("recipes")
            .add(recipe)
            .addOnSuccessListener { docReference ->
                Log.d("STORE", "Save id: ${docReference.id}")
                val feedActivity = Intent(this, FeedActivity::class.java)
                startActivity(feedActivity)
            }
            .addOnFailureListener { e ->
                Log.w("ERROR", e)
            }
    }
}
