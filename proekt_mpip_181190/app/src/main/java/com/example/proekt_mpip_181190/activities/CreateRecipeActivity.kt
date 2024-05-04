package com.example.proekt_mpip_181190.activities

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proekt_mpip_181190.R
import jp.wasabeef.richeditor.RichEditor


class CreateRecipeActivity : AppCompatActivity() {
    private lateinit var mEditor: RichEditor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_recipe)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get elements
        this.mEditor = findViewById(R.id.editor)

        // Configure editor
        configureEditorAndEvents(mEditor)
    }

    private fun configureEditorAndEvents(mEditor: RichEditor) {
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Insert text here...");

        mEditor.setOnTextChangeListener { text ->
            run {
                Log.d("DEBUG", text)
//                mPreview.text = text
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
            mEditor.insertImage(
                "https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg",
                "dachshund", 320
            )
        }
    }
}
