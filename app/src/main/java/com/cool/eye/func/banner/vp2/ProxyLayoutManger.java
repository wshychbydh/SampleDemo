package com.cool.eye.func.banner.vp2;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

class ProxyLayoutManger extends LinearLayoutManager {

  private ViewPager2 viewPager2;
  private RecyclerView.LayoutManager linearLayoutManager;
  private long pageScrollDuration;

  ProxyLayoutManger(ViewPager2 viewPager2, RecyclerView.LayoutManager layoutManager,
      long pageScrollDuration) {
    super(viewPager2.getContext());
    this.viewPager2 = viewPager2;
    this.linearLayoutManager = layoutManager;
    this.pageScrollDuration = pageScrollDuration;
  }

  @Override
  public boolean performAccessibilityAction(@NonNull RecyclerView.Recycler recycler,
      @NonNull RecyclerView.State state, int action, @Nullable Bundle args) {
    return linearLayoutManager.performAccessibilityAction(recycler, state, action, args);
  }

  @Override
  public void onInitializeAccessibilityNodeInfo(@NonNull RecyclerView.Recycler recycler,
      @NonNull RecyclerView.State state, @NonNull AccessibilityNodeInfoCompat info) {
    linearLayoutManager.onInitializeAccessibilityNodeInfo(recycler, state, info);
  }

  @Override
  public boolean requestChildRectangleOnScreen(@NonNull RecyclerView parent,
      @NonNull View child, @NonNull Rect rect, boolean immediate,
      boolean focusedChildVisible) {
    return linearLayoutManager.requestChildRectangleOnScreen(parent, child, rect, immediate);
  }

  //核心处理页面切换速度的方法
  @Override
  public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
      int position) {
    LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(
        recyclerView.getContext()) {
      @Override
      protected int calculateTimeForDeceleration(int dx) {
        return (int) (pageScrollDuration * (1 - .3356));
      }
    };
    linearSmoothScroller.setTargetPosition(position);
    startSmoothScroll(linearSmoothScroller);
  }

  @Override
  protected void calculateExtraLayoutSpace(@NonNull RecyclerView.State state,
      @NonNull int[] extraLayoutSpace) {
    int pageLimit = viewPager2.getOffscreenPageLimit();
    if (pageLimit == ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT) {
      super.calculateExtraLayoutSpace(state, extraLayoutSpace);
      return;
    }
    final int offscreenSpace = getPageSize() * pageLimit;
    extraLayoutSpace[0] = offscreenSpace;
    extraLayoutSpace[1] = offscreenSpace;
  }

  private int getPageSize() {
    final RecyclerView rv = (RecyclerView) viewPager2.getChildAt(0);
    return getOrientation() == RecyclerView.HORIZONTAL
        ? rv.getWidth() - rv.getPaddingLeft() - rv.getPaddingRight()
        : rv.getHeight() - rv.getPaddingTop() - rv.getPaddingBottom();
  }
}
