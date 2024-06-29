package com.example.proekt_mpip_181190.activities

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proekt_mpip_181190.R
import java.io.ByteArrayOutputStream
import java.io.InputStream

class CreateRecipeTitleImageActivity : AppCompatActivity() {
    private lateinit var imageButton: ImageButton
    private lateinit var thumbnailUri: Uri
    private lateinit var nextButton: Button
    private lateinit var backButton: Button
    private lateinit var title: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_recipe_title_image)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.imageButton = findViewById(R.id.createRecipeTitleImage_imageButton)
        this.nextButton = findViewById(R.id.createRecipeTitleImage_nextButton)
        this.backButton = findViewById(R.id.createRecipeTitleImage_backButton)
        this.title = findViewById(R.id.createRecipeTitleImage_titleField)

        this.backButton.setOnClickListener {
            finish()
        }

        val createRecipeActivity = Intent(this, CreateRecipeActivity::class.java)
        this.nextButton.setOnClickListener {
            val thumbnailUrl = this.thumbnailUri.toString();
            val title = this.title.text.toString();
            createRecipeActivity.putExtra("THUMBNAIL_URI", thumbnailUrl)
            createRecipeActivity.putExtra("RECIPE_TITLE", title)
            startActivity(createRecipeActivity)
        }

        val getPreviewImage =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    this.imageButton.setImageURI(uri)
                    this.thumbnailUri = uri
                }
            }
        this.imageButton.setOnClickListener {
            getPreviewImage.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
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