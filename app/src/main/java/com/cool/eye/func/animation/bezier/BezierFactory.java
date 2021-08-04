package com.cool.eye.func.animation.bezier;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import com.cool.eye.demo.R;
import java.util.Random;

/**
 * Created by cool on 17-5-10.
 */

public final class BezierFactory {

  private final static int[] BEZIER_RES = new int[]{R.drawable.heart_red_ic, R.drawable.heart_green_ic,
      R.drawable.heart_gray_ic, R.drawable.heart_blue_ic, R.drawable.heart_yellow_ic};

  private static Random mRandom = new Random();

  private BezierFactory() {
  }

  /**
   * 创建View只需要传Context即可，但是考虑到连续调用会用到其他属性值． 如若用一个包装类来传递又多此一举，故将父容器作为参数．
   */
  public static BezierView addBezierView(@NonNull final BezierLayout container) {
    final ImageView iv = new ImageView(container.getContext());
    Drawable drawable = container.getResources()
        .getDrawable(BEZIER_RES[mRandom.nextInt(BEZIER_RES.length)]);
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
        drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
    iv.setLayoutParams(params);
    iv.setImageDrawable(drawable);
    BezierView bezierView = new BezierView();
    bezierView.setView(iv);
    bezierView
        .setActiveRect(new Rect(0, container.getMeasuredWidth(), 0, container.getMeasuredHeight()));
    bezierView.setPointF(
        getBezierPoints(container.getMeasuredWidth(), container.getMeasuredHeight(),
            iv.getMeasuredHeight()));
    bezierView.setDuration(container.getProperty().getDuration());
    AnimatorSet set = BezierAnimationHelper.getAnimatorSet(bezierView, mRandom);
    set.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        // 动画结束后，移除iv
        container.removeView(iv);
      }
    });
    Animator.AnimatorListener listener = container.getProperty().getAnimationListener();
    if (listener != null) {
      set.addListener(listener);
    }
    container.addView(iv);
    bezierView.setAnimatorSet(set);
    set.start();
    return bezierView;
  }

  /**
   * @param viewHeight 子view的高度
   */
  private static PointF[] getBezierPoints(int activeWidth, int activeHeight, int viewHeight) {
    // 贝塞尔曲线的4个点
    PointF pointF0 = new PointF(mRandom.nextInt(activeWidth), activeHeight - viewHeight);
    PointF pointF1 = getBezierPointF(activeWidth, activeHeight, 1);
    PointF pointF2 = getBezierPointF(activeWidth, activeHeight, 2);
    PointF pointF3 = new PointF(mRandom.nextInt(activeWidth), 0);
    return new PointF[]{pointF0, pointF1, pointF2, pointF3};
  }

  private static PointF getBezierPointF(int activeWidth, int activeHeight, int index) {
    PointF pointF = new PointF();
    // 波动的范围在0~activeWidth之间，也可以波动出去
    pointF.x = mRandom.nextInt(activeWidth / 2);
    if (index == 2) {
      //point2在右半屏
      pointF.x += activeWidth / 2;
    }
    // 为了向上波动，故 point2.y < point1.y
    int height = mRandom.nextInt(activeHeight / 2);
    if (index == 1) {
      height += activeHeight / 2;
    }
    pointF.y = height;
    return pointF;
  }
}
