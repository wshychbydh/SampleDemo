package com.cool.eye.func.address;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by cool on 2017/4/19.
 * Only support LinearLayoutManager.Vertical
 */

public class StickyItemDecoration2 extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private final int dividerWidth;
    private Drawable divider;
    private int dividerHeight;
    private SparseArray<String> keys = new SparseArray<>();
    private int titleHeight;
    private Paint textPaint;
    private Paint backgroundPaint;
    private float textHeight;
    private float textBaselineOffset;
    private Context context;

    private boolean showFirstGroup = true;


    public void setShowFirstGroup(boolean showFirstGroup) {
        this.showFirstGroup = showFirstGroup;
    }

    public boolean isShowFirstGroup() {
        return showFirstGroup;
    }

    /**
     * 是否显示悬浮头部
     */
    private boolean showStickyHeader = true;

    public StickyItemDecoration2(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        divider = a.getDrawable(0);
        a.recycle();
        this.dividerHeight = divider.getIntrinsicHeight();
        this.dividerWidth = divider.getIntrinsicWidth();
        init(context);
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param drawableId 分割线图片
     */
    public StickyItemDecoration2(Context context, @DrawableRes int drawableId) {
        divider = ContextCompat.getDrawable(context, drawableId);
        this.dividerHeight = divider.getIntrinsicHeight();
        this.dividerWidth = divider.getIntrinsicWidth();
        init(context);
    }

    /**
     * 自定义分割线
     * 也可以使用{@link Canvas#drawRect(float, float, float, float, Paint)}或者{@link Canvas#drawText(String, float, float, Paint)}等等
     * 结合{@link Paint}去绘制各式各样的分割线
     *
     * @param context
     * @param color         整型颜色值，非资源id
     * @param dividerWidth  单位为dp
     * @param dividerHeight 单位为dp
     */
    public StickyItemDecoration2(Context context, @ColorInt int color, @Dimension float
            dividerWidth, @Dimension float dividerHeight) {
        divider = new ColorDrawable(color);
        this.dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dividerWidth, context.getResources().getDisplayMetrics());
        this.dividerHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dividerHeight, context.getResources().getDisplayMetrics());
        init(context);
    }

    private void init(Context mContext) {
        this.context = mContext;
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, mContext
                .getResources().getDisplayMetrics()));
        textPaint.setColor(Color.WHITE);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        textHeight = fm.bottom - fm.top;//计算文字高度
        textBaselineOffset = fm.bottom;

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(Color.MAGENTA);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawVertical(c, parent);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        if (!showStickyHeader) {
            return;
        }

        if (keys.size() == 0) return;

        int firstVisiblePos = ((LinearLayoutManager) parent.getLayoutManager())
                .findFirstVisibleItemPosition();
        if (!showFirstGroup && firstVisiblePos < keys.valueAt(0).length()) {
            return;
        }

        if (firstVisiblePos == RecyclerView.NO_POSITION) {
            return;
        }

        String title = getTitle(firstVisiblePos);
        if (TextUtils.isEmpty(title)) {
            return;
        }

        boolean flag = false;
        if (getTitle(firstVisiblePos + 1) != null && !title.equals(getTitle(firstVisiblePos + 1))) {
            //说明是当前组最后一个元素，但不一定碰撞了
            View child = parent.findViewHolderForAdapterPosition(firstVisiblePos).itemView;
            if (child.getTop() + child.getMeasuredHeight() < titleHeight) {
                //进一步检测碰撞
                c.save();//保存画布当前的状态
                flag = true;
                c.translate(0, child.getTop() + child.getMeasuredHeight() - titleHeight);//负的代表向上
            }
        }

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int top = parent.getPaddingTop();
        int bottom = top + titleHeight;
        c.drawRect(left, top, right, bottom, backgroundPaint);
        float x = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context
                .getResources().getDisplayMetrics());
        float y = bottom - (titleHeight - textHeight) / 2 - textBaselineOffset;//计算文字baseLine
        c.drawText(title, x, y, textPaint);

        if (flag) {
            //还原画布为初始状态
            c.restore();
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        if (!showFirstGroup && pos == 0) {
            outRect.set(0, 0, 0, 0);
        } else {
            if (keys.indexOfKey(pos) > -1) {//留出头部偏移
                outRect.set(0, titleHeight, 0, 0);
            } else {
                outRect.set(0, dividerHeight, 0, 0);
            }
        }
    }

    /**
     * *如果该位置没有，则往前循环去查找标题，找到说明该位置属于该分组
     *
     * @param position
     * @return
     */
    private String getTitle(int position) {
        while (findPosition(position)) {
            if (keys.indexOfKey(position) > -1) {
                return keys.get(position);
            }
            position--;
        }
        return null;
    }

    private boolean findPosition(int position) {
        if (showFirstGroup) {
            return position >= 0;
        } else {
            return position > 0;
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int top;
        int bottom;
        for (int i = 1; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = params.getViewLayoutPosition();
            if (keys.indexOfKey(position) > -1) {
                //画头部
                top = child.getTop() - params.topMargin - titleHeight;
                bottom = top + titleHeight;
                c.drawRect(left, top, right, bottom, backgroundPaint);
//                float x=child.getPaddingLeft()+params.leftMargin;
                float x = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                        context.getResources().getDisplayMetrics());
                float y = bottom - (titleHeight - textHeight) / 2 - textBaselineOffset;
                //计算文字baseLine
                c.drawText(keys.get(position), x, y, textPaint);
            } else {
                //画普通分割线
                top = child.getTop() - params.topMargin - dividerHeight;
                bottom = top + dividerHeight;
                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }

    public void showStickyHeader(boolean showStickyHeader) {
        this.showStickyHeader = showStickyHeader;
    }

    public void setKeys(SparseArray<String> keys) {
        this.keys = keys;
    }

    public SparseArray<String> getKeys() {
        return keys;
    }

    public void setTitleHeight(int titleHeight) {
        this.titleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                titleHeight, context.getResources().getDisplayMetrics());
    }
}
