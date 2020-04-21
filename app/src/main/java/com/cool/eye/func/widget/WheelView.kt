package com.cool.eye.func.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader.TileMode
import android.os.Handler
import android.os.Message
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.cool.eye.demo.R
import java.util.*

/**
 * WheelView滚轮
 */
class WheelView : View {
  /**
   * 控件宽度
   */
  private var controlWidth: Float = 0.toFloat()

  /**
   * 控件高度
   */
  private var controlHeight: Float = 0.toFloat()
  /**
   * 是否滑动中
   */
  /**
   * 是否正在滑动
   *
   * @return
   */
  var isScrolling = false
    private set

  /**
   * 选择的内容
   */
  private val itemList = ArrayList<ItemObject>()

  /**
   * 设置数据
   */
  private var dataList: List<String> = ArrayList()

  /**
   * 按下的坐标
   */
  private var downY: Int = 0

  /**
   * 按下的时间
   */
  private var downTime: Long = 0

  /**
   * 短促移动
   */
  private val goonTime: Long = 200

  /**
   * 短促移动距离
   */
  private val goonDistance = 100

  /**
   * 画线画笔
   */
  private var linePaint: Paint? = null

  /**
   * 线的默认颜色
   */
  private var lineColor = -0x1000000

  /**
   * 线的默认宽度
   */
  private var lineHeight = 2f

  /**
   * 默认字体
   */
  private var normalFont = 14.0f

  /**
   * 选中的时候字体
   */
  private var selectedFont = 22.0f

  /**
   * 单元格高度
   */
  private var unitHeight = 50

  /**
   * 显示多少个内容
   */
  private var itemNumber = 7

  /**
   * 默认字体颜色
   */
  private var normalColor = -0x1000000

  /**
   * 选中时候的字体颜色
   */
  private var selectedColor = -0x10000

  /**
   * 蒙板高度
   */
  private var maskHeight = 48.0f

  /**
   * 选择监听
   */
  private var onSelectListener: OnSelectListener? = null
  /**
   * 是否可用
   */
  /**
   * 是否可用
   *
   * @return
   */
  /**
   * 设置是否可用
   *
   * @param isEnable
   */
  var isEnable = true

  /**
   * 是否允许选空
   */
  private var noEmpty = true

  /**
   * 正在修改数据，避免ConcurrentModificationException异常
   */
  private var isClearing = false

  /**
   * 获取返回项 id
   *
   * @return
   */
  val selected: Int
    get() {
      for (item in itemList) {
        if (item.isSelected)
          return item.id
      }
      return -1
    }

  /**
   * 获取返回的内容
   *
   * @return
   */
  val selectedText: String
    get() {
      for (item in itemList) {
        if (item.isSelected)
          return item.itemText
      }
      return ""
    }

  /**
   * 获取列表大小
   *
   * @return
   */
  val listSize: Int
    get() = itemList.size

  @SuppressLint("HandlerLeak")
  internal var handler: Handler = object : Handler() {

    override fun handleMessage(msg: Message) {
      super.handleMessage(msg)
      when (msg.what) {
        REFRESH_VIEW -> invalidate()
        else -> {
        }
      }
    }

  }

  constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
    init(context, attrs)
    initData()
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    init(context, attrs)
    initData()
  }

  constructor(context: Context) : super(context) {
    initData()
  }

  /**
   * 初始化，获取设置的属性
   *
   * @param context
   * @param attrs
   */
  private fun init(context: Context, attrs: AttributeSet) {

    val attribute = context.obtainStyledAttributes(attrs, R.styleable.WheelView)
    unitHeight = attribute.getDimension(R.styleable.WheelView_wv_unitHeight, unitHeight.toFloat()).toInt()
    itemNumber = attribute.getInt(R.styleable.WheelView_wv_itemNumber, itemNumber)

    normalFont = attribute.getDimension(R.styleable.WheelView_wv_normalTextSize, normalFont)
    selectedFont = attribute.getDimension(R.styleable.WheelView_wv_selectedTextSize, selectedFont)
    normalColor = attribute.getColor(R.styleable.WheelView_wv_normalTextColor, normalColor)
    selectedColor = attribute.getColor(R.styleable.WheelView_wv_selectedTextColor, selectedColor)

    lineColor = attribute.getColor(R.styleable.WheelView_wv_lineColor, lineColor)
    lineHeight = attribute.getDimension(R.styleable.WheelView_wv_lineHeight, lineHeight)

    maskHeight = attribute.getDimension(R.styleable.WheelView_wv_maskHeight, maskHeight)
    noEmpty = attribute.getBoolean(R.styleable.WheelView_wv_noEmpty, true)
    isEnable = attribute.getBoolean(R.styleable.WheelView_wv_isEnable, true)

    attribute.recycle()

    controlHeight = (itemNumber * unitHeight).toFloat()
  }

  /**
   * 初始化数据
   */
  private fun initData() {
    isClearing = true
    itemList.clear()
    for (i in dataList.indices) {
      val itemObject = ItemObject()
      itemObject.id = i
      itemObject.itemText = dataList[i]
      itemObject.x = 0
      itemObject.y = i * unitHeight
      itemList.add(itemObject)
    }
    isClearing = false
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    controlWidth = measuredWidth.toFloat()
    if (controlWidth != 0f) {
      setMeasuredDimension(measuredWidth, itemNumber * unitHeight)
    }
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    drawLine(canvas)
    drawList(canvas)
    drawMask(canvas)
  }

  /**
   * 绘制线条
   *
   * @param canvas
   */
  private fun drawLine(canvas: Canvas) {

    if (linePaint == null) {
      linePaint = Paint()
      linePaint!!.color = lineColor
      linePaint!!.isAntiAlias = true
      linePaint!!.strokeWidth = lineHeight
    }

    canvas.drawLine(0f, controlHeight / 2 - unitHeight / 2 + lineHeight,
        controlWidth, controlHeight / 2 - unitHeight / 2 + lineHeight, linePaint!!)
    canvas.drawLine(0f, controlHeight / 2 + unitHeight / 2 - lineHeight,
        controlWidth, controlHeight / 2 + unitHeight / 2 - lineHeight, linePaint!!)
  }

  @Synchronized
  private fun drawList(canvas: Canvas) {
    if (isClearing)
      return
    try {
      for (itemObject in itemList) {
        itemObject.drawSelf(canvas, measuredWidth)
      }
    } catch (e: Exception) {
    }

  }

  /**
   * 绘制遮盖板
   *
   * @param canvas
   */
  private fun drawMask(canvas: Canvas) {
    val lg = LinearGradient(0f, 0f, 0f, maskHeight, 0x00f2f2f2,
        0x00f2f2f2, TileMode.MIRROR)
    val paint = Paint()
    paint.shader = lg
    canvas.drawRect(0f, 0f, controlWidth, maskHeight, paint)

    val lg2 = LinearGradient(0f, controlHeight - maskHeight,
        0f, controlHeight, 0x00f2f2f2, 0x00f2f2f2, TileMode.MIRROR)
    val paint2 = Paint()
    paint2.shader = lg2
    canvas.drawRect(0f, controlHeight - maskHeight, controlWidth,
        controlHeight, paint2)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (!isEnable)
      return true
    val y = event.y.toInt()
    when (event.action) {
      MotionEvent.ACTION_DOWN -> {
        isScrolling = true
        downY = event.y.toInt()
        downTime = System.currentTimeMillis()
      }
      MotionEvent.ACTION_MOVE -> {
        actionMove(y - downY)
        onSelectListener()
      }
      MotionEvent.ACTION_UP -> {
        val move = Math.abs(y - downY)
        // 判断这段时间移动的距离
        if (System.currentTimeMillis() - downTime < goonTime && move > goonDistance) {
          goonMove(y - downY)
        } else {
          actionUp(y - downY)
          noEmpty()
          isScrolling = false
        }
      }
      else -> {
      }
    }
    return true
  }

  /**
   * 继续移动一定距离
   */
  @Synchronized
  private fun goonMove(move: Int) {
    Thread(Runnable {
      var distance = 0
      while (distance < unitHeight * MOVE_NUMBER) {
        try {
          Thread.sleep(5)
        } catch (e: InterruptedException) {
          e.printStackTrace()
        }

        actionThreadMove(if (move > 0) distance else distance * -1)
        distance += 10

      }
      actionUp(if (move > 0) distance - 10 else distance * -1 + 10)
      noEmpty()
    }).start()
  }

  /**
   * 不能为空，必须有选项
   */
  private fun noEmpty() {
    if (!noEmpty)
      return
    for (item in itemList) {
      if (item.isSelected)
        return
    }
    val move = itemList[0].moveToSelected().toInt()
    if (move < 0) {
      defaultMove(move)
    } else {
      defaultMove(itemList[itemList.size - 1]
          .moveToSelected().toInt())
    }
    for (item in itemList) {
      if (item.isSelected) {
        if (onSelectListener != null)
          onSelectListener!!.endSelect(item.id, item.itemText)
        break
      }
    }
  }

  /**
   * 移动的时候
   *
   * @param move
   */
  private fun actionMove(move: Int) {
    for (item in itemList) {
      item.move(move)
    }
    invalidate()
  }

  /**
   * 移动，线程中调用
   *
   * @param move
   */
  private fun actionThreadMove(move: Int) {
    for (item in itemList) {
      item.move(move)
    }
    val rMessage = Message()
    rMessage.what = REFRESH_VIEW
    handler.sendMessage(rMessage)
  }

  /**
   * 松开的时候
   *
   * @param move
   */
  private fun actionUp(move: Int) {
    var newMove = 0
    if (move > 0) {
      for (i in itemList.indices) {
        if (itemList[i].isSelected) {
          newMove = itemList[i].moveToSelected().toInt()
          if (onSelectListener != null)
            onSelectListener!!.endSelect(itemList[i].id,
                itemList[i].itemText)
          break
        }
      }
    } else {
      for (i in itemList.indices.reversed()) {
        if (itemList[i].isSelected) {
          newMove = itemList[i].moveToSelected().toInt()
          if (onSelectListener != null)
            onSelectListener!!.endSelect(itemList[i].id,
                itemList[i].itemText)
          break
        }
      }
    }
    for (item in itemList) {
      item.newY(move + 0)
    }
    slowMove(newMove)
    val rMessage = Message()
    rMessage.what = REFRESH_VIEW
    handler.sendMessage(rMessage)

  }

  /**
   * 缓慢移动
   *
   * @param move
   */
  @Synchronized
  private fun slowMove(move: Int) {
    Thread(Runnable {
      // 判断正负
      var m = if (move > 0) move else move * -1
      val i = if (move > 0) 1 else -1
      // 移动速度
      val speed = 1
      while (true) {
        m = m - speed
        if (m <= 0) {
          for (item in itemList) {
            item.newY(m * i)
          }
          val rMessage = Message()
          rMessage.what = REFRESH_VIEW
          handler.sendMessage(rMessage)
          try {
            Thread.sleep(2)
          } catch (e: InterruptedException) {
            e.printStackTrace()
          }

          break
        }
        for (item in itemList) {
          item.newY(speed * i)
        }
        val rMessage = Message()
        rMessage.what = REFRESH_VIEW
        handler.sendMessage(rMessage)
        try {
          Thread.sleep(2)
        } catch (e: InterruptedException) {
          e.printStackTrace()
        }

      }
      for (item in itemList) {
        if (item.isSelected) {
          if (onSelectListener != null)
            onSelectListener!!.endSelect(item.id, item.itemText)
          break
        }
      }
    }).start()
  }

  /**
   * 移动到默认位置
   *
   * @param move
   */
  private fun defaultMove(move: Int) {
    for (item in itemList) {
      item.newY(move)
    }
    val rMessage = Message()
    rMessage.what = REFRESH_VIEW
    handler.sendMessage(rMessage)
  }

  /**
   * 滑动监听
   */
  private fun onSelectListener() {
    if (onSelectListener == null)
      return
    for (item in itemList) {
      if (item.isSelected) {
        onSelectListener!!.selecting(item.id, item.itemText)
      }
    }
  }

  /**
   * 设置数据 （第一次）
   *
   * @param data
   */
  fun setData(data: List<String>) {
    this.dataList = data
    initData()
  }

  /**
   * 重置数据
   *
   * @param data
   */
  fun refreshData(data: List<String>) {
    setData(data)
    invalidate()
  }

  /**
   * 设置默认选项
   *
   * @param index
   */
  fun setDefault(index: Int) {
    if (index > itemList.size - 1)
      return
    val move = itemList[index].moveToSelected()
    defaultMove(move.toInt())
  }

  /**
   * 获取某项的内容
   *
   * @param index
   * @return
   */
  fun getItemText(index: Int): String {
    return itemList[index].itemText
  }

  /**
   * 监听
   *
   * @param onSelectListener
   */
  fun setOnSelectListener(onSelectListener: OnSelectListener) {
    this.onSelectListener = onSelectListener
  }

  /**
   * 单条内容
   *
   * @author JiangPing
   */
  private inner class ItemObject {
    /**
     * id
     */
    var id = 0

    /**
     * 内容
     */
    var itemText = ""

    /**
     * x坐标
     */
    var x = 0

    /**
     * y坐标
     */
    var y = 0

    /**
     * 移动距离
     */
    var move = 0

    /**
     * 字体画笔
     */
    private var textPaint: TextPaint? = null

    /**
     * 字体范围矩形
     */
    private var textRect: Rect? = null

    /**
     * 是否在可视界面内
     *
     * @return
     */
    val isInView: Boolean
      get() = y + move <= controlHeight && y + move + unitHeight / 2 + textRect!!.height() / 2 >= 0

    /**
     * 判断是否在选择区域内
     *
     * @return
     */
    val isSelected: Boolean
      get() {
        if (y + move >= controlHeight / 2 - unitHeight / 2 + lineHeight && y + move <= controlHeight / 2 + unitHeight / 2 - lineHeight) {
          return true
        }
        return if (y + move + unitHeight >= controlHeight / 2 - unitHeight / 2 + lineHeight && y + move + unitHeight <= controlHeight / 2 + unitHeight / 2 - lineHeight) {
          true
        } else y + move <= controlHeight / 2 - unitHeight / 2 + lineHeight && y + move + unitHeight >= controlHeight / 2 + unitHeight / 2 - lineHeight
      }

    /**
     * 绘制自身
     *
     * @param canvas         画板
     * @param containerWidth 容器宽度
     */
    fun drawSelf(canvas: Canvas, containerWidth: Int) {

      if (textPaint == null) {
        textPaint = TextPaint()
        textPaint!!.isAntiAlias = true
      }

      if (textRect == null)
        textRect = Rect()

      // 判断是否被选择
      if (isSelected) {
        textPaint!!.color = selectedColor
        // 获取距离标准位置的距离
        var moveToSelect = moveToSelected()
        moveToSelect = if (moveToSelect > 0) moveToSelect else moveToSelect * -1
        // 计算当前字体大小
        val textSize = normalFont + (selectedFont - normalFont) * (1.0f - moveToSelect / unitHeight.toFloat())
        textPaint!!.textSize = textSize
      } else {
        textPaint!!.color = normalColor
        textPaint!!.textSize = normalFont
      }

      // 返回包围整个字符串的最小的一个Rect区域
      itemText = TextUtils.ellipsize(itemText, textPaint, containerWidth.toFloat(),
          TextUtils.TruncateAt.END) as String
      textPaint!!.getTextBounds(itemText, 0, itemText.length, textRect)
      // 判断是否可视
      if (!isInView)
        return

      // 绘制内容
      canvas.drawText(itemText, x + controlWidth / 2 - textRect!!.width() / 2,
          (y + move + unitHeight / 2 + textRect!!.height() / 2).toFloat(), textPaint!!)

    }

    /**
     * 移动距离
     *
     * @param _move
     */
    fun move(_move: Int) {
      this.move = _move
    }

    /**
     * 设置新的坐标
     *
     * @param _move
     */
    fun newY(_move: Int) {
      this.move = 0
      this.y = y + _move
    }

    /**
     * 获取移动到标准位置需要的距离
     */
    fun moveToSelected(): Float {
      return controlHeight / 2 - unitHeight / 2 - (y + move)
    }
  }

  /**
   * 选择监听
   *
   * @author JiangPing
   */
  interface OnSelectListener {
    /**
     * 结束选择
     *
     * @param id
     * @param text
     */
    fun endSelect(id: Int, text: String)

    /**
     * 选中的内容
     *
     * @param id
     * @param text
     */
    fun selecting(id: Int, text: String)

  }

  companion object {
    /**
     * 刷新界面
     */
    private val REFRESH_VIEW = 0x001

    /**
     * 移动距离
     */
    private val MOVE_NUMBER = 5
  }
}
