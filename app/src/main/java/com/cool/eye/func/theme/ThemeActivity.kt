package com.cool.eye.func.theme

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.SkinAppCompatDelegateImpl
import com.cool.eye.demo.R
import com.eye.cool.permission.PermissionChecker
import com.eye.cool.permission.checker.Request
import com.eye.cool.permission.support.PermissionGroup
import kotlinx.android.synthetic.main.activity_theme.*
import skin.support.SkinCompatManager
import skin.support.content.res.SkinCompatUserThemeManager
import kotlin.random.Random

/**
 * https://github.com/ximsfei/Android-skin-support
 */
class ThemeActivity : AppCompatActivity() {

  val bgs = intArrayOf(R.drawable.theme_bg1, R.drawable.theme_bg2, R.drawable.theme_bg3)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_theme)

    PermissionChecker(
        Request.build(this){
          permissions(PermissionGroup.STORAGE)
        }
    ).check {

    }
  }

  override fun getDelegate(): AppCompatDelegate {
    return SkinAppCompatDelegateImpl.get(this, this)
  }

  fun changeTheme(view: View) {
    themeBgLayout.setBackgroundResource(bgs[Random.nextInt(bgs.size)])
    // loadTheme(CustomSDCardLoader.SKIN_LOADER_STRATEGY_SDCARD)
    // loadTheme(ZipSDCardLoader.SKIN_LOADER_STRATEGY_ZIP)
    loadTheme(SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS)
  }

  private fun loadTheme(strategy: Int) {
    SkinCompatManager.getInstance().loadSkin("theme.skin", object : SkinCompatManager.SkinLoaderListener {
      override fun onSuccess() {
        Log.d("skin", "onSuccess--->")
      }

      override fun onFailed(errMsg: String?) {
        Log.d("skin", "onFailed--->$errMsg")
      }

      override fun onStart() {
        Log.d("skin", "onStart--->")
      }
    }, strategy)
  }

  fun restoreTheme(view: View) {
    // 恢复应用默认皮肤
    SkinCompatManager.getInstance().restoreDefaultTheme()
  }


  fun changeImage(view: View) {
    SkinCompatUserThemeManager.get().addDrawablePath(R.drawable.ic_theme_char, "sdcard/skins/ic_placeholder.png")
    SkinCompatUserThemeManager.get().apply()
  }

  fun clearColor(view: View) {
    // 清除所有已有颜色值。
    SkinCompatUserThemeManager.get().clearColors()
  }

  fun changeColor(view: View) {
    SkinCompatUserThemeManager.get().addColorState(R.color.bg_color, "#5e702F")
    SkinCompatUserThemeManager.get().addColorState(R.color.text_color, "#123456")
    SkinCompatUserThemeManager.get().apply()
  }

  fun clearImage(view: View) {
    // 清除所有已有图片路径。
    SkinCompatUserThemeManager.get().clearDrawables()
  }
}
