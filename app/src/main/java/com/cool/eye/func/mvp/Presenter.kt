package com.cool.eye.func.mvp

/**
 *Created by ycb on 2018/09/10
 */
abstract class Presenter : IPresenter {

  @Volatile
  protected var isAlive = false

  override fun onResume() {
    super.onResume()
    isAlive = true
  }

  override fun onPause() {
    super.onPause()
    isAlive = false
  }
}