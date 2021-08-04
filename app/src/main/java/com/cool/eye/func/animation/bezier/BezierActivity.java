package com.cool.eye.func.animation.bezier;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.cool.eye.demo.R;

/**
 * Created by cool on 17-5-10.
 */
public class BezierActivity extends Activity {

  private BezierLayout mBezierLayout;
  private TextView mCountTv;
  private volatile int mCount;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bezier);
    mBezierLayout = (BezierLayout) findViewById(R.id.bezier_container);
    mCountTv = (TextView) findViewById(R.id.tv_count);
    mBezierLayout.getProperty().setAnimationListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animation) {
        mCountTv.setText("共执行了" + ++mCount + "次动画．");
      }

      @Override
      public void onAnimationEnd(Animator animation) {
      }

      @Override
      public void onAnimationCancel(Animator animation) {

      }

      @Override
      public void onAnimationRepeat(Animator animation) {

      }
    });
  }

  public void start(View v) {
    mBezierLayout.start();
  }

  public void stop(View v) {
    mBezierLayout.stop();
  }

  public void speedUp(View view) {
    mBezierLayout.getProperty().addInterval();
  }

  public void speedDown(View view) {
    mBezierLayout.getProperty().decInterval();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mBezierLayout.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mBezierLayout.onPause();
  }
}
