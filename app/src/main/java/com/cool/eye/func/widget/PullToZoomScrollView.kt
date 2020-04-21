package com.cool.eye.func.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import com.cool.eye.demo.R

/**
 * Modify from https://github.com/jiyiren/PullToZoomScrollView
 */

class PullToZoomScrollView(context: Context, attrs: AttributeSet) : NestedScrollView(context, attrs) {

  var onScrollChangedListener: ((l: Int, t: Int, oldl: Int, oldt: Int) -> Unit)? = null

  private var isFirstLoad: Boolean = false //加载该View的布局时是否是第一次加载，是第一次就让其实现OnMeasure里的代码

  private var parentView: LinearLayout? = null //布局的父布局，ScrollView内部只能有一个根ViewGroup，就是此View
  private var topView: View? = null //这个是带背景的上半部分的View，下半部分的View用不到的

  private val screenHeight: Int //整个手机屏幕的高度，这是为了初始化该View时设置mTopView用的
  private val topViewHeight: Int //这个就是mTopView的高度

  private var currentOffset = 0 //当前右侧滚条顶点的偏移量。ScrollView右侧是有滚动条的，当下拉时，
  //滚动条向上滑，当向下滑动时，滚动条向下滑动。

  private var animator: ObjectAnimator? = null //这个是对象动画，这个在本View里很简单，也很独立，就在这里申明一下，后面有两个方法

  private var startY = 0f //向下拉动要放大，手指向下滑时，点击的第一个点的Y坐标
  private var isZoom: Boolean = false //是否正在向下拉放大上半部分View
  //在MoVe时，如果发现滑动标签位移量为0，则获取此时的Y坐标，作为起始坐标，然后置为true,为了在连续的Move中只获取一次起始坐标
  //当Up弹起时，一次触摸移动完成，将isTouchOne置为false
  private var distance = 0f //向下滑动到释放的高度差

  var parallaxAble = false  //动画差
  var zoomAble = true   //缩放
  var scrollAble = true

  init {
    this.overScrollMode = View.OVER_SCROLL_NEVER
    val wm = getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val metrics = DisplayMetrics()
    wm.defaultDisplay.getMetrics(metrics)
    screenHeight = metrics.heightPixels
    topViewHeight = screenHeight / 2 - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90f, context.resources.displayMetrics).toInt()

    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PullToZoomScrollView)
    parallaxAble = typedArray.getBoolean(R.styleable.PullToZoomScrollView_parallaxAble, false)
    zoomAble = typedArray.getBoolean(R.styleable.PullToZoomScrollView_zoomAble, true)
    typedArray.recycle()
  }

  /**
   * 将记录的值设置到控件上，并只让控件设置一次
   * @param widthMeasureSpec
   * @param heightMeasureSpec
   */
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    if (!isFirstLoad) {
      parentView = this.getChildAt(0) as LinearLayout
      topView = parentView!!.getChildAt(0)
      topView!!.layoutParams.height = topViewHeight
      isFirstLoad = true
    }
  }

  val touchSlop = ViewConfiguration.get(context).scaledTouchSlop

  override fun onTouchEvent(ev: MotionEvent): Boolean {
    if (!scrollAble) return false
    if (zoomAble) {
      val action = ev.action
      when (action) {
        MotionEvent.ACTION_DOWN -> {
          startY = ev.y
        }
        MotionEvent.ACTION_MOVE -> if (currentOffset <= 0) {
          distance = ev.y - startY
          if (distance > 0) {
            isZoom = true
            setT((-distance / 4f).toInt())
          }
        }
        MotionEvent.ACTION_UP -> {
          if (isZoom) {
            reset()
            isZoom = false
          }
        }
      }
    }
    return super.onTouchEvent(ev)
  }

  /**
   * 对象动画要有的设置方法
   * @param t
   */
  private fun setT(t: Int) {
    scrollTo(0, 0)
    if (t < 0) {
      topView!!.layoutParams.height = topViewHeight - t
      topView!!.requestLayout()
    }
  }

  /**
   * 主要用于释放手指后的回弹效果
   */
  private fun reset() {
    if (animator != null && animator!!.isRunning) {
      return
    }
    animator = ObjectAnimator.ofInt(this, "t", (-distance / 4f).toInt(), 0)
    animator!!.duration = 150
    animator!!.start()
  }

  /**
   * 这个是设置向上滑动时，上半部分View滑动速度让其小于下半部分
   * @param l
   * @param t
   * @param oldl
   * @param oldt
   */
  override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
    super.onScrollChanged(l, t, oldl, oldt)
    onScrollChangedListener?.invoke(l, t, oldl, oldt)
    currentOffset = t //右边滑动标签相对于顶端的偏移量
    //当手势上滑，则右侧滚动条下滑，下滑的高度小于TopView的高度，则让TopView的上滑速度小于DownView的上滑速度
    //DownView的上滑速度是滚动条的速度，也就是滚动的距离是右侧滚动条的距离
    //则TopView的速度要小，只需要将右侧滚动条的偏移量也就是t缩小一定倍数就行了。我这里除以2速度减小1倍
    if (parallaxAble) {
      if (t in 0..topViewHeight && !isZoom) {
        topView!!.translationY = (t / 2).toFloat() //使得TopView滑动的速度小于滚轮滚动的速度
      }
    }
    if (zoomAble) {
      if (isZoom) {
        scrollTo(0, 0)
      }
    }
  }
}