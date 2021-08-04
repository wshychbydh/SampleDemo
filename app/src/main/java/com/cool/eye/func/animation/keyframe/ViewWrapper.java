package com.cool.eye.func.animation.keyframe;

import android.view.View;

/**
 * View的某个属性要实现属性动画,那么该属性就必须要有set和get方法.如果没有,可以用以下方式包装一下:
 */
public class ViewWrapper {

  private View mTarget;

  public ViewWrapper(View target) {
    this.mTarget = target;
  }

  public int getWidth() {
    return mTarget.getLayoutParams().width;
  }

  public void setWidth(int width) {
    mTarget.getLayoutParams().width = width;
    mTarget.requestLayout();
  }

  public int getHeight() {
    return mTarget.getLayoutParams().height;
  }

  public void setHeight(int height) {
    mTarget.getLayoutParams().height = height;
    mTarget.requestLayout();
  }
}