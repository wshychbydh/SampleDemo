package com.cool.eye.func.animation.reveal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.cool.eye.demo.R
import kotlinx.android.synthetic.main.activity_reveal.*

class RevealActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_reveal)
    viewPager.adapter = RevealAdapter()

    viewPager.addOnPageChangeListener(this)

    editIv.setImageDrawable(RevealDrawable(
        ContextCompat.getDrawable(this, R.drawable.edit_colour_ic)!!,
        ContextCompat.getDrawable(this, R.drawable.edit_grey_ic)!!,
    ))

    filmIv.setImageDrawable(RevealDrawable(
        ContextCompat.getDrawable(this, R.drawable.film_colour_ic)!!,
        ContextCompat.getDrawable(this, R.drawable.film_grey_ic)!!,
    ))
  }

  override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    val level = (positionOffset * 5000).toInt()

    println("=onPageScrolled=>>positionOffset: $positionOffset ; positionOffsetPixels: $positionOffsetPixels ; level: $level")

    editIv.setImageLevel(level)
    filmIv.setImageLevel(10000 - level)
  }

  override fun onPageSelected(position: Int) {
  }

  override fun onPageScrollStateChanged(state: Int) {
  }
}