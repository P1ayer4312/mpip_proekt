package com.example.proekt_mpip_181190.activities

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
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
import com.google.firebase.storage.storage
import jp.wasabeef.richeditor.RichEditor
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

class CreateRecipeActivity : AppCompatActivity() {
    private lateinit var mEditor: RichEditor
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    private lateinit var db: FirebaseFirestore
    private lateinit var dbStorage: FirebaseStorage

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


        val file = Uri.parse(this.thumbnailUriString)
        val riversRef = this.dbStorage.reference.child("images/${file.lastPathSegment}")
        val uploadTask = riversRef.putFile(file)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            taskSnapshot.storage.path
            Log.w("URL", taskSnapshot.metadata?.path.toString())
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
                    val img = convertImageUriToBase64DataUrl(uri);
                    mEditor.html += "<img src=\"${img}\" width=300>"
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
        val thumbnailToBase64 = convertImageUriToBase64DataUrl(Uri.parse(this.thumbnailUriString))
        val recipe = RecipeData(
            title = this.recipeTitle,
            author = "Blazho",
            imageLink = "link?",
            recipeData = mEditor.html,
            thumbnail = thumbnailToBase64,
            documentId = ""
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

    private fun convertImageUriToBase64DataUrl(uri: Uri): String {
        val contentResolver: ContentResolver = contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        val base64String: String = Base64.encodeToString(byteArray, Base64.DEFAULT)

        // Get MIME type from URI
        val mimeType: String? = contentResolver.getType(uri)

        return if (mimeType != null) {
            "data:$mimeType;base64,$base64String"
        } else {
            ""
        }
    }
}
