package com.cool.eye.func.animation.bezier;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;

/**
 * Created by cool on 17-5-10.
 */
public class BezierLayout extends RelativeLayout {

  private Property mProperty;

  public BezierLayout(Context context) {
    this(context, null);
  }

  public BezierLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    //FIXME 通过xml来设置相关属性，此处省略．
    mProperty = new Property();
  }

  public Property getProperty() {
    return mProperty;
  }

  private void addBezierView() {
    BezierFactory.addBezierView(this);
    if (mProperty.mRecycle) {
      mHandler.sendEmptyMessageDelayed(0, mProperty.mInterval);
    }
  }

  private Handler mHandler = new Handler(new Callback() {
    @Override
    public boolean handleMessage(Message msg) {
      addBezierView();
      return true;
    }
  });

  public void start() {
    if (mProperty.mRunning) {
      return;
    }
    mHandler.removeMessages(0);
    mProperty.mRunning = true;
    addBezierView();
  }

  public void stop() {
    mProperty.mRunning = false;
    mHandler.removeMessages(0);
  }

  /**
   * 恢复暂停的动画
   */
  public void onResume() {
    if (mProperty.mRunning) {
      start();
    }
  }

  /**
   * 暂停动画
   */
  public void onPause() {
    mHandler.removeMessages(0);
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    if (mProperty.mAutoRunning) {
      start();
    }
  }

  @Override
  protected void onDetachedFromWindow() {
    stop();
    super.onDetachedFromWindow();
  }

  public class Property {

    private final static int MAX_INTERVAL = 1000;
    private final static int MIN_INTERVAL = 100;

    private final static int MAX_DURATION = 5000;
    private final static int MIN_DURATION = 1000;

    private boolean mAutoRunning = true; // 是否自动开启动画
    private boolean mRecycle = true; // 是否循环播放子动画
    private int mInterval = 500; // 每间隔多少毫秒执行一次子动画
    private int mDuration = 3000; // 每个子动画执行的时间

    private boolean mRunning = false; //是否正在执行

    @Nullable
    private Animator.AnimatorListener mAnimationListener;

    public Animator.AnimatorListener getAnimationListener() {
      return mAnimationListener;
    }

    public void setAnimationListener(@Nullable Animator.AnimatorListener animationListener) {
      mAnimationListener = animationListener;
    }

    public int getDuration() {
      return mDuration;
    }

    public int getInterval() {
      return mInterval;
    }

    public void addDuration() {
      if (mRunning) {
        mDuration -= 1000;
        mDuration = mDuration < MIN_DURATION ? MIN_DURATION : mDuration;
      }
    }

    public void decDuration() {
      if (mRunning) {
        mDuration += 1000;
        mDuration = mDuration > MAX_DURATION ? MAX_DURATION : mDuration;
      }
    }

    public void addInterval() {
      if (mRunning) {
        mInterval -= 100;
        mInterval = mInterval < MIN_INTERVAL ? MIN_INTERVAL : mInterval;
      }
    }

    public void decInterval() {
      if (mRunning) {
        mInterval += 100;
        mInterval = mInterval > MAX_INTERVAL ? MAX_INTERVAL : mInterval;
      }
    }

    public Property setInterval(int interval) {
      this.mInterval = interval;
      return this;
    }

    public Property setDuration(int duration) {
      this.mDuration = duration;
      return this;
    }

    public Property setAutoRunning(boolean autoRunning) {
      mAutoRunning = autoRunning;
      return this;
    }

    public Property setRecycle(boolean recycle) {
      this.mRecycle = recycle;
      return this;
    }

    public boolean isAutoRunning() {
      return mAutoRunning;
    }

    public boolean isRecycle() {
      return mRecycle;
    }
  }
}
