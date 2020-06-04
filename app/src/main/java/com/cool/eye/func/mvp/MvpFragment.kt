package com.cool.eye.func.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import java.lang.reflect.ParameterizedType
import java.util.*

/**
 *Created by ycb on 2018/09/06
 */
abstract class MvpFragment<T : IPresenter> : androidx.fragment.app.Fragment() {

  private val presenters: LinkedHashSet<IPresenter> by lazy { LinkedHashSet<IPresenter>() }

  protected val presenter = initPresenter()

  override fun onAttach(context: Context) {
    super.onAttach(context)
    attachPresenter(presenter)
    presenters.forEach {
      it.onAttach()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    presenters.forEach {
      it.onCreate(arguments)
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenters.forEach {
      it.onViewCreated(arguments)
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    presenters.forEach {
      it.onActivityCreated(arguments)
    }
  }

  override fun onStart() {
    super.onStart()
    presenters.forEach {
      it.onStart()
    }
  }

  override fun onPause() {
    super.onPause()
    presenters.forEach {
      it.onPause()
    }
  }

  override fun setUserVisibleHint(isVisibleToUser: Boolean) {
    super.setUserVisibleHint(isVisibleToUser)
    presenters.forEach {
      if (isVisibleToUser) {
        it.onResume()
      } else {
        it.onPause()
      }
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

  override fun onDestroyView() {
    super.onDestroyView()
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

  protected fun attachPresenter(presenter: IPresenter) {
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
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
    throw IllegalStateException("Your fragment must be implement IView")
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
    return this.requireContext()
  }
}