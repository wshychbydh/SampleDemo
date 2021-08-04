package com.cool.eye.func.animation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.cool.eye.demo.R;
import com.cool.eye.func.animation.bezier.BezierActivity;
import com.cool.eye.func.animation.keyframe.KeyframeActivity;

public class AnimationActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_animation);
  }

  public void toBezier(View view) {
    startActivity(new Intent(this, BezierActivity.class));
  }

  public void toKeyframe(View view) {
    startActivity(new Intent(this, KeyframeActivity.class));
  }
}
