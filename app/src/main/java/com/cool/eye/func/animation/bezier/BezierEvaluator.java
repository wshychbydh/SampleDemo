package com.cool.eye.func.animation.bezier;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by cool on 17-5-10.
 *
 * 套用公式计算坐标值，不同的公式得到的结果不一致，展现的动画也会不同． 因此这里可以套用多个不同的公式来产出多种动画．
 */
public final class BezierEvaluator implements TypeEvaluator<PointF> {

  private PointF mPointF1, mPointF2;

  public BezierEvaluator(PointF pointF1, PointF pointF2) {
    this.mPointF1 = pointF1;
    this.mPointF2 = pointF2;
  }

  @Override
  public PointF evaluate(float fraction, PointF startValue, PointF endValue) {

    // 贝塞尔曲线的公式
    // fraction取值0~1
    float temp = 1 - fraction;
    PointF pointF = new PointF();
    pointF.x = startValue.x * temp * temp * temp + 3 * mPointF1.x * fraction
        * temp * temp + 3 * mPointF2.x * fraction * fraction
        * temp + endValue.x * fraction * fraction * fraction;

    pointF.y = startValue.y * temp * temp * temp + 3 * mPointF1.y * fraction
        * temp * temp + 3 * mPointF2.y * fraction * fraction
        * temp + endValue.y * fraction * fraction * fraction;
    return pointF;
  }
}
