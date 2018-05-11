package com.cool.eye.func.address

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.Dimension
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.SparseArray
import android.util.TypedValue
import android.view.View

/**
 * Created by cool on 2017/4/19.
 * Only support LinearLayoutManager.Vertical
 */

class StickyItemDecoration : RecyclerView.ItemDecoration {
    private val dividerWidth: Int
    private var divider: Drawable? = null
    private var dividerHeight: Int = 0
    private var titleHeight: Int = 0
    private var textPaint: Paint? = null
    private var backgroundPaint: Paint? = null
    private var textHeight: Float = 0f
    private var textBaselineOffset: Float = 0f
    private lateinit var context: Context

    var isShowFirstGroup = true

    var keys = SparseArray<String>()

    /**
     * 是否显示悬浮头部
     */
    var showStickyHeader = true

    constructor(context: Context) {
        val typeArray = context.obtainStyledAttributes(ATTRS)
        divider = typeArray.getDrawable(0)
        typeArray.recycle()
        this.dividerHeight = divider!!.intrinsicHeight
        this.dividerWidth = divider!!.intrinsicWidth
        init(context)
    }

    /**
     *
     * @param context
     * @param drawableId
     */
    constructor(context: Context, @DrawableRes drawableId: Int) {
        divider = ContextCompat.getDrawable(context, drawableId)
        this.dividerHeight = divider!!.intrinsicHeight
        this.dividerWidth = divider!!.intrinsicWidth
        init(context)
    }

    /**
     * 自定义分割线
     * 也可以使用[Canvas.drawRect]或者[Canvas.drawText]等等
     * 结合[Paint]去绘制各式各样的分割线
     *
     * @param context
     * @param color         整型颜色值，非资源id
     * @param dividerWidth  单位为dp
     * @param dividerHeight 单位为dp
     */
    constructor(context: Context, @ColorInt color: Int, @Dimension dividerWidth: Float, @Dimension dividerHeight: Float) {
        divider = ColorDrawable(color)
        this.dividerWidth = getDipValue(context, dividerWidth).toInt()
        this.dividerHeight = getDipValue(context, dividerHeight).toInt()
        init(context)
    }

    private fun init(mContext: Context) {
        this.context = mContext
        textPaint = Paint()
        textPaint!!.isAntiAlias = true
        textPaint!!.textSize = getSpValue(context, 16f)
        textPaint!!.color = Color.WHITE
        val fm = textPaint!!.fontMetrics
        textHeight = fm.bottom - fm.top //计算文字高度
        textBaselineOffset = fm.bottom

        backgroundPaint = Paint()
        backgroundPaint!!.isAntiAlias = true
        backgroundPaint!!.color = Color.BLUE
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDraw(c, parent, state)
        drawVertical(c, parent)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)

        if (!showStickyHeader) {
            return
        }

        if (keys.size() == 0) return

        val firstVisiblePos = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (!isShowFirstGroup && firstVisiblePos < keys.valueAt(0).length) return

        if (firstVisiblePos == RecyclerView.NO_POSITION) return

        val title = getTitle(firstVisiblePos)
        if (title.isNullOrEmpty()) return

        var flag = false
        if (getTitle(firstVisiblePos + 1) != null && title != getTitle(firstVisiblePos + 1)) {
            //说明是当前组最后一个元素，但不一定碰撞了
            val child = parent.findViewHolderForAdapterPosition(firstVisiblePos).itemView
            if (child.top + child.measuredHeight < titleHeight) {
                //进一步检测碰撞
                c.save() //保存画布当前的状态
                flag = true
                c.translate(0f, (child.top + child.measuredHeight - titleHeight).toFloat()) //负的代表向上
            }
        }

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val top = parent.paddingTop
        val bottom = top + titleHeight
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), backgroundPaint!!)
        val x = getDipValue(context,10f)
        val y = bottom.toFloat() - (titleHeight - textHeight) / 2 - textBaselineOffset //计算文字baseLine
        c.drawText(title!!, x, y, textPaint!!)

        if (flag) {
            //还原画布为初始状态
            c.restore()
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        val pos = parent.getChildAdapterPosition(view)
        if (!isShowFirstGroup && pos == 0) {
            outRect.set(0, 0, 0, 0)
        } else {
            if (keys.indexOfKey(pos) > -1) { //留出头部偏移
                outRect.set(0, titleHeight, 0, 0)
            } else {
                outRect.set(0, dividerHeight, 0, 0)
            }
        }
    }

    /**
     * *如果该位置没有，则往前循环去查找标题，找到说明该位置属于该分组
     *
     * @param position
     * @return
     */
    private fun getTitle(position: Int): String? {
        var tempPos = position
        while (findPosition(tempPos)) {
            if (keys.indexOfKey(tempPos) > -1) {
                return keys.get(tempPos)
            }
            tempPos--
        }
        return null
    }

    private fun findPosition(position: Int): Boolean {
        return if (isShowFirstGroup) {
            position >= 0
        } else {
            position > 0
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        var top: Int
        var bottom: Int
        for (i in 1 until parent.childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val position = params.viewLayoutPosition
            if (keys.indexOfKey(position) > -1) {
                //画头部
                top = child.top - params.topMargin - titleHeight
                bottom = top + titleHeight
                c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), backgroundPaint!!)
                //                float x=child.getPaddingLeft()+params.leftMargin;
                val x = getDipValue(context,10f)
                val y = bottom.toFloat() - (titleHeight - textHeight) / 2 - textBaselineOffset
                //计算文字baseLine
                c.drawText(keys.get(position), x, y, textPaint!!)
            } else {
                //画普通分割线
                top = child.top - params.topMargin - dividerHeight
                bottom = top + dividerHeight
                divider!!.setBounds(left, top, right, bottom)
                divider!!.draw(c)
            }
        }
    }

    fun setTitleHeight(titleHeight: Float) {
        this.titleHeight = getDipValue(context, titleHeight).toInt()
    }

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)

        private fun getDipValue(context: Context, value: Float): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    value, context.resources.displayMetrics)
        }

        private fun getSpValue(context: Context, value: Float): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    value, context.resources.displayMetrics)
        }
    }
}
