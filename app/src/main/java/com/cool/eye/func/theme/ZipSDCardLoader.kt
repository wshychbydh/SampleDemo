package com.cool.eye.func.theme

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import skin.support.load.SkinSDCardLoader
import skin.support.utils.SkinFileUtils
import java.io.File

class ZipSDCardLoader : SkinSDCardLoader() {

  override fun loadSkinInBackground(context: Context, skinName: String): String {
    Log.d("theme", "loadSkinInBackground , skinName---->$skinName")
    //TODO 解压zip包中的资源，同时可以根据skinName安装皮肤包(.skin)。
    //FIXME 这里猜测是支持将多个apk打包到一个zip包同时下发多个主题，需要解压主题，根据一定条件返回可加载的某个apk路径
    return super.loadSkinInBackground(context, skinName)
  }

  override fun getSkinPath(context: Context, skinName: String): String {
    Log.d("theme", "getSkinPath , skinName---->$skinName")
    // TODO 返回皮肤包路径，如果只需要使用zip包，则返回""
    return File(SkinFileUtils.getSkinDir(context), skinName).absolutePath
  }

  override fun getDrawable(context: Context?, skinName: String?, resId: Int): Drawable {
    Log.d("theme", "getDrawable , skinName---->$skinName , resId-->$resId")
    // TODO 根据resId来判断是否使用zip包中的资源。
    return super.getDrawable(context, skinName, resId)
  }

  override fun getType(): Int {
    return SKIN_LOADER_STRATEGY_ZIP
  }

  companion object {

    val SKIN_LOADER_STRATEGY_ZIP = Integer.MAX_VALUE - 1
  }
}