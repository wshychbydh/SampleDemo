package com.cool.eye.func.photo

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import com.cool.eye.func.R
import kotlinx.android.synthetic.main.photo_add.view.*

/**
 * Created by cool on 2018/6/12
 */
class DefaultView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        LinearLayout(context, attrs, defStyle), OnClickListener {

    private var listener: IPhotoListener? = null

    fun setPhotoListener(listener: IPhotoListener) {
        this.listener = listener
    }

    init {
        setBackgroundColor(Color.WHITE)
        orientation = VERTICAL
        val padding = (context.resources.displayMetrics.density * 12).toInt()
        setPadding(padding, padding, padding, padding)

        val view = LayoutInflater.from(context).inflate(R.layout.photo_add, this, true)
        view.take_photo.setOnClickListener(this)
        view.select_album.setOnClickListener(this)
        view.cancel.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when {
            v.id == R.id.take_photo -> {
                listener?.onTakePhoto()
            }
            v.id == R.id.select_album -> {
                listener?.onSelectAlbum()
            }
            v.id == R.id.cancel -> {
                listener?.onCancel()
            }
        }
    }
}
