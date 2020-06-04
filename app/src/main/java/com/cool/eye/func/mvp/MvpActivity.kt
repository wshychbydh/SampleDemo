package com.cool.eye.func.mvp

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.ParameterizedType
import java.util.*

/**
 *Created by ycb on 2018/09/06
 */
abstract class MvpActivity<T : IPresenter> : AppCompatActivity() {

  private val presenters: LinkedHashSet<IPresenter> by lazy { LinkedHashSet<IPresenter>() }

  protected val presenter = initPresenter()

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    addPresenter(presenter)
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    presenters.forEach {
      it.onPostCreate(intent?.extras)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    presenters.forEach {
      it.onCreate(intent?.extras)
    }
  }

  override fun onStart() {
    super.onStart()
    presenters.forEach {
      it.onStart()
    }
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    presenters.forEach {
      it.onNewIntent(intent)
    }
  }

  override fun onRestart() {
    super.onRestart()
    presenters.forEach {
      it.onRestart()
    }
  }

  override fun onPause() {
    super.onPause()
    presenters.forEach {
      it.onPause()
    }
  }

  override fun onResume() {
    super.onResume()
    presenters.forEach {
      it.onResume()
    }
  }

  override fun onStop() {
    super.onStop()
    presenters.forEach {
      it.onStop()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    presenters.forEach {
      it.onDestroy()
    }
    presenters.clear()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    presenters.forEach {
      it.onActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onBackPressed() {
    super.onBackPressed()
    presenters.forEach {
      it.onBackPressed()
    }
  }

  /**
   * Keep presenter consistent with the lifecycle of Activity
   */
  protected fun addPresenter(presenter: IPresenter) {
    this.presenters.add(presenter)
  }

  private fun initPresenter(): T {
    val params = arrayListOf<Class<*>>()
    findIViewClass(this.javaClass, params)
    val clazz = Class.forName(getTClass().name)
    //当一个界面对应多个Presenter时需要区分,这里默认只找泛型Presenter对应的View
    for (i in (0 until params.size)) {
      try {
        val constructor = clazz.getConstructor(params[i]) ?: continue
        return constructor.newInstance(this) as T
      } catch (ignore: Exception) {
      }
    }
    throw IllegalStateException("Your activity must be implement IView")
  }

  private fun findIViewClass(clazz: Class<*>, list: ArrayList<Class<*>>) {
    clazz.interfaces.forEach {
      if (it.name == IView::class.java.name || it.name == ILoadView::class.java.name) {
        list.add(clazz)
      }
      if (it.interfaces.isNotEmpty()) {
        val temp = arrayListOf<Class<*>>()
        findIViewClass(it, temp)
        if (temp.isNotEmpty()) {
          list.add(it)
        }
      }
    }
  }

  private fun getTClass(): Class<IPresenter> {
    return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<IPresenter>
  }

  open fun withContext(): Context {
    return this
  }
}