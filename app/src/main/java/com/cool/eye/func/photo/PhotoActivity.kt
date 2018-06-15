package com.cool.eye.func.photo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.cool.eye.func.R
import com.cool.eye.func.R.id.iv
import kotlinx.android.synthetic.main.activity_photo.*

/**
 * Created by cool on 18-3-9
 */
class PhotoActivity : AppCompatActivity() {

    private lateinit var dialog: PhotoDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        dialog = PhotoDialog.Builder(this)
                .setContentView(DefaultView(this))
                .setPhotoListener {
                    iv.setImageBitmap(it)
                }
                .build()
    }

    fun pickerPhoto(view: View) {
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        dialog.onActivityResult(requestCode, resultCode, intent)
    }
}
