package me.samen.d2.view.obsnote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import me.samen.d2.R
import me.samen.d2.data.BUNDLE_THING_CONTENT
import me.samen.d2.data.BUNDLE_THING_ID
import me.samen.d2.databinding.ActivityONoteDetailBinding

class ONoteDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityONoteDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filename = intent.getStringExtra(BUNDLE_THING_ID)
        val content = intent.getStringExtra(BUNDLE_THING_CONTENT)
        if (filename == null || content == null) { // mandatory for this screen
            finish()
            return
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_o_note_detail)
        binding.notecontent = content
    }
}