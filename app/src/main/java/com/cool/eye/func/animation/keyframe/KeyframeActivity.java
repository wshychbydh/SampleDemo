package com.cool.eye.func.animation.keyframe;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.cool.eye.demo.R;

public class KeyframeActivity extends AppCompatActivity {

  private Button mDisplayBtn;
  private ImageView mDisplayIv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_keyframe);
    mDisplayBtn = (Button) findViewById(R.id.btn_display);
    mDisplayIv = (ImageView) findViewById(R.id.iv_display);
    displayAnimation();
  }

  private void displayAnimation() {
    Keyframe kf0 = Keyframe.ofInt(0, 200);
    Keyframe kf1 = Keyframe.ofInt(0.25f, 300);
    Keyframe kf2 = Keyframe.ofInt(0.5f, 400);
    Keyframe kf3 = Keyframe.ofInt(0.75f, 500);
    Keyframe kf4 = Keyframe.ofInt(1f, 600);
    PropertyValuesHolder btnWidthPvh = PropertyValuesHolder
        .ofKeyframe("width", kf0, kf1, kf2, kf3, kf4);
    PropertyValuesHolder btnHeightPvh = PropertyValuesHolder
        .ofKeyframe("height", kf0, kf1, kf2, kf3, kf4);

    ObjectAnimator btnAnim = ObjectAnimator
        .ofPropertyValuesHolder(mDisplayBtn, btnWidthPvh, btnHeightPvh);
    btnAnim.setRepeatCount(ObjectAnimator.INFINITE);
    btnAnim.setRepeatMode(ObjectAnimator.REVERSE);
    btnAnim.setDuration(2000);
    btnAnim.start();

    PropertyValuesHolder ivWidthPvh = PropertyValuesHolder
        .ofKeyframe("width", kf0, kf1, kf2, kf3, kf4);
    PropertyValuesHolder ivHeightPvh = PropertyValuesHolder
        .ofKeyframe("height", kf0, kf1, kf2, kf3, kf4);
    ObjectAnimator ivAnim = ObjectAnimator
        .ofPropertyValuesHolder(new ViewWrapper(mDisplayIv), ivWidthPvh, ivHeightPvh);
    //这种方式在某些机型上有bug，执行一段时间有可能就停止了．
    ivAnim.setRepeatCount(ObjectAnimator.INFINITE);
    ivAnim.setRepeatMode(ObjectAnimator.REVERSE);
    ivAnim.setDuration(2000);
    ivAnim.start();
  }
}
