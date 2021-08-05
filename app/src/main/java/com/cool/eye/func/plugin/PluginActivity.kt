package com.cool.eye.func.plugin

import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import dalvik.system.DexClassLoader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class PluginActivity : AppCompatActivity() {

  var loader: ClassLoader? = null
  var am: AssetManager? = null

  // var theme: Resources.Theme? = null
  var res: Resources? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_plugin)
  }

  private fun loadFile(path: String) {
    //加载sd卡/data/data/xx包名/xx.apk或xx.jar
    println("==path=>>$path")
    val fis = FileInputStream(path)
    val bytes = ByteArray(fis.available())
    fis.read(bytes)
    fis.close()
    println("==bytes.size=>>${bytes.size}")

    //解压apk
    val dir = File(application.filesDir, "dex")
    dir.mkdirs()
    //拷贝路径
    val file = File(dir, "hot_${Integer.toHexString(path.hashCode())}")
    //解压路径
    val fo = File(dir, "dex_${Integer.toHexString(path.hashCode())}")
    val fos = FileOutputStream(file)
    fos.write(bytes)
    fos.flush()
    fos.close()

    val scl = super.getClassLoader()
    //apk->dex->class
    val dcl = DexClassLoader(
        file.absolutePath, //
        fo.absolutePath,
        null,
        scl
    )

    loader = dcl

    /**
     * 资源拷贝
     */
    val am = AssetManager::class.java.newInstance()
    am.javaClass.getMethod("addAssetPath", String::class.java).invoke(am, fo.absolutePath)

    this.am = am

    //Resource
    val superRes = super.getResources()
    val res = Resources(am, superRes.displayMetrics, superRes.configuration)
    this.res = res

    //theme
//    val thm = res.newTheme()
//    thm.setTo(super.getTheme())
//    this.theme = thm

    //load fragment
    val fragment = classLoader.loadClass("com.example.androidtest.TestPluginFragment")
//    supportFragmentManager.beginTransaction()
//        .add(R.id.containerFl, fragment)
//        .commitNowAllowingStateLoss()
  }

  override fun getClassLoader(): ClassLoader {
    return loader ?: super.getClassLoader()
  }

  override fun getAssets(): AssetManager {
    return am ?: super.getAssets()
  }

  override fun getResources(): Resources {
    return res ?: super.getResources()
  }

//  override fun getTheme(): Resources.Theme {
//    return theme ?: super.getTheme()
//  }

  fun load(view: View) {
    loadFile("/sdcard/app-debug.apk")
  }
}