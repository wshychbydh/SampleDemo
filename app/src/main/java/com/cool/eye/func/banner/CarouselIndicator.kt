package com.cool.eye.func.banner

import android.animation.Animator
import android.animation.AnimatorInflater
import android.annotation.TargetApi
import android.content.Context
import android.database.DataSetObserver
import android.os.Build
import android.support.annotation.AnimatorRes
import android.support.annotation.DrawableRes
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.Interpolator
import android.widget.LinearLayout
import com.cool.eye.func.R
import com.enternityfintech.gold.app.view.indicator.ICarousel

/**
 * Created by cool on 2018/4/18.
 */
class CarouselIndicator : LinearLayout {
    private var viewPager: ViewPager? = null
    private var indicatorMargin = -1
    private var indicatorWidth = -1
    private var indicatorHeight = -1
    private var animatorResId = R.animator.scale_with_alpha
    private var animatorReverseResId = 0
    private var indicatorBackgroundResId = R.drawable.white_radius
    private var indicatorUnselectedBackgroundResId = R.drawable.white_radius
    private var animatorOut: Animator? = null
    private var animatorIn: Animator? = null
    private var immediateAnimatorOut: Animator? = null
    private var immediateAnimatorIn: Animator? = null
    var replaceView: View? = null

    private var lastPosition = -1

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    @JvmOverloads
    fun configureIndicator(indicatorWidth: Int, indicatorHeight: Int, indicatorMargin: Int,
                           @AnimatorRes animatorId: Int = R.animator.scale_with_alpha, @AnimatorRes animatorReverseId: Int = 0,
                           @DrawableRes indicatorBackgroundId: Int = R.drawable.white_radius,
                           @DrawableRes indicatorUnselectedBackgroundId: Int = R.drawable.white_radius) {

        this.indicatorWidth = indicatorWidth
        this.indicatorHeight = indicatorHeight
        this.indicatorMargin = indicatorMargin

        animatorResId = animatorId
        animatorReverseResId = animatorReverseId
        indicatorBackgroundResId = indicatorBackgroundId
        indicatorUnselectedBackgroundResId = indicatorUnselectedBackgroundId

        checkIndicatorConfig(context)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        handleTypedArray(context, attrs)
        checkIndicatorConfig(context)
    }

    private fun handleTypedArray(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CarouselIndicator)
        indicatorWidth = typedArray.getDimensionPixelSize(R.styleable.CarouselIndicator_width, -1)
        indicatorHeight = typedArray.getDimensionPixelSize(R.styleable.CarouselIndicator_height, -1)
        indicatorMargin = typedArray.getDimensionPixelSize(R.styleable.CarouselIndicator_margin, -1)

        animatorResId = typedArray.getResourceId(R.styleable.CarouselIndicator_animator,
                R.animator.scale_with_alpha)
        animatorReverseResId = typedArray.getResourceId(R.styleable.CarouselIndicator_animator_reverse, 0)
        indicatorBackgroundResId = typedArray.getResourceId(R.styleable.CarouselIndicator_drawable,
                R.drawable.white_radius)
        indicatorUnselectedBackgroundResId = typedArray.getResourceId(R.styleable.CarouselIndicator_drawable_unselected,
                indicatorBackgroundResId)

        val orientation = typedArray.getInt(R.styleable.CarouselIndicator_orientation, -1)
        setOrientation(if (orientation == LinearLayout.VERTICAL) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL)

        val gravity = typedArray.getInt(R.styleable.CarouselIndicator_ci_gravity, -1)
        setGravity(if (gravity >= 0) gravity else Gravity.CENTER)

        typedArray.recycle()
    }

    private fun checkIndicatorConfig(context: Context) {
        indicatorWidth = if (indicatorWidth < 0) dip2px(DEFAULT_INDICATOR_WIDTH.toFloat()) else indicatorWidth
        indicatorHeight = if (indicatorHeight < 0) dip2px(DEFAULT_INDICATOR_WIDTH.toFloat()) else indicatorHeight
        indicatorMargin = if (indicatorMargin < 0) dip2px(DEFAULT_INDICATOR_WIDTH.toFloat()) else indicatorMargin

        animatorResId = if (animatorResId == 0) R.animator.scale_with_alpha else animatorResId

        animatorOut = createAnimatorOut(context)
        immediateAnimatorOut = createAnimatorOut(context)
        immediateAnimatorOut!!.duration = 0

        animatorIn = createAnimatorIn(context)
        immediateAnimatorIn = createAnimatorIn(context)
        immediateAnimatorIn!!.duration = 0

        indicatorBackgroundResId = if (indicatorBackgroundResId == 0)
            R.drawable.white_radius
        else
            indicatorBackgroundResId
        indicatorUnselectedBackgroundResId = if (indicatorUnselectedBackgroundResId == 0)
            indicatorBackgroundResId
        else
            indicatorUnselectedBackgroundResId
    }

    private fun createAnimatorOut(context: Context): Animator {
        return AnimatorInflater.loadAnimator(context, animatorResId)
    }

    private fun createAnimatorIn(context: Context): Animator {
        val animatorIn: Animator
        if (animatorReverseResId == 0) {
            animatorIn = AnimatorInflater.loadAnimator(context, animatorResId)
            animatorIn.interpolator = ReverseInterpolator()
        } else {
            animatorIn = AnimatorInflater.loadAnimator(context, animatorReverseResId)
        }
        return animatorIn
    }

    fun setViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        if (viewPager.adapter != null && viewPager.adapter!!.count > 1) {
            val currentItem = getCurrentItem()
            lastPosition = -1
            createIndicators(currentItem, getDataCount())
            viewPager.addOnPageChangeListener(internalPageChangeListener)
            internalPageChangeListener.onPageSelected(currentItem)
            viewPager.adapter!!.registerDataSetObserver(dataSetObserver)
        }

    }

    private fun createIndicators(currentItem: Int, indicatorCount: Int) {
        removeAllViews()
        if (indicatorCount > 1) {
            val orientation = orientation
            for (i in 0 until indicatorCount) {
                if (currentItem == i) {
                    addIndicator(orientation, indicatorBackgroundResId, immediateAnimatorOut)
                } else {
                    addIndicator(orientation, indicatorUnselectedBackgroundResId,
                            immediateAnimatorIn)
                }
            }
        }
    }

    private fun addIndicator(orientation: Int, @DrawableRes backgroundDrawableId: Int,
                             animator: Animator?) {
        if (animator!!.isRunning) {
            animator.end()
            animator.cancel()
        }

        val indicatorView = View(context)
        indicatorView.setBackgroundResource(backgroundDrawableId)
        addView(indicatorView, indicatorWidth, indicatorHeight)
        val lp = indicatorView.layoutParams as LinearLayout.LayoutParams

        if (orientation == LinearLayout.HORIZONTAL) {
            lp.leftMargin = indicatorMargin
            lp.rightMargin = indicatorMargin
        } else {
            lp.topMargin = indicatorMargin
            lp.bottomMargin = indicatorMargin
        }

        indicatorView.layoutParams = lp

        animator.setTarget(indicatorView)
        animator.start()
    }

    private inner class ReverseInterpolator : Interpolator {
        override fun getInterpolation(value: Float): Float {
            return Math.abs(1.0f - value)
        }
    }

    private fun dip2px(dpValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    companion object {

        private const val DEFAULT_INDICATOR_WIDTH = 5
    }

    private val internalPageChangeListener = object : OnPageChangeListener {

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {

            val indicatorCount = getDataCount()

            if (indicatorCount <= 0) return

            val indicatorPosition = position % indicatorCount

            if (replaceView != null && indicatorPosition == indicatorCount - 1) {
                replaceView!!.visibility = View.VISIBLE
                visibility = View.GONE
            } else {
                if (replaceView != null) {
                    replaceView!!.visibility = View.GONE
                }
                visibility = View.VISIBLE
            }

            if (animatorIn!!.isRunning) {
                animatorIn!!.end()
                animatorIn!!.cancel()
            }

            if (animatorOut!!.isRunning) {
                animatorOut!!.end()
                animatorOut!!.cancel()
            }

            if (lastPosition >= 0 && getChildAt(lastPosition) != null) {
                val currentIndicator = getChildAt(lastPosition)
                currentIndicator.setBackgroundResource(indicatorUnselectedBackgroundResId)
                animatorIn!!.setTarget(currentIndicator)
                animatorIn!!.start()
            }

            val selectedIndicator = getChildAt(indicatorPosition)
            if (selectedIndicator != null) {
                selectedIndicator.setBackgroundResource(indicatorBackgroundResId)
                animatorOut!!.setTarget(selectedIndicator)
                animatorOut!!.start()
            }
            lastPosition = indicatorPosition
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    private val dataSetObserver: DataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            super.onChanged()
            val newCount = getDataCount()
            val currentCount = childCount
            val currentItem = viewPager!!.currentItem % newCount

            lastPosition = when {
                newCount == currentCount -> return
                lastPosition < newCount -> currentItem
                else -> -1
            }

            createIndicators(currentItem, newCount)
        }
    }

    private fun getCurrentItem(): Int {
        val dataCount = getDataCount()
        return if (dataCount > 0) {
            viewPager!!.currentItem % dataCount
        } else 0
    }

    private fun getDataCount(): Int {
        return if (viewPager!!.adapter is ICarousel) {
            val iCarousel = viewPager!!.adapter as ICarousel
            iCarousel.getDataSize()
        } else {
            viewPager!!.adapter?.count ?: 0
        }
    }
}