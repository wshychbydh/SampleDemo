package com.cool.eye.func.banner

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.cool.eye.demo.R
import com.leochuan.*
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlin.random.Random


/**
 * Created by ycb on 2019/11/14 0014
 * https://github.com/angcyo/ViewPagerLayoutManager
 *
 * AutoPlayRecyclerView 包含AutoPlaySnapHelper，若需要其他定制可按需重写。
 */
class GalleryActivity : AppCompatActivity() {

  private val pages = mutableListOf<Any>(1, 2, 3)
  private val adapter1 = Banner4Adapter(pages, "CarouselLayoutManager：")
  private val adapter2 = Banner4Adapter(pages, "CircleLayoutManager：")
  private val adapter3 = Banner4Adapter(pages, "CircleScaleLayoutManager：")
  private val adapter4 = Banner4Adapter(pages, "GalleryLayoutManager：")
  private val adapter5 = Banner4Adapter(pages, "RotateLayoutManager：")
  private val adapter6 = Banner4Adapter(pages, "ScaleLayoutManager：")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_gallery)

    CenterSnapHelper().attachToRecyclerView(autoRv1)
    CenterSnapHelper().attachToRecyclerView(autoRv3)

    val carouselLm = CarouselLayoutManager(
        CarouselLayoutManager
            .Builder(this, 0)
            .setOrientation(RecyclerView.VERTICAL)
            .setMoveSpeed(0.001f)  //设置为0不能滑动,也不能自动轮播
            .setMinScale(1f)   //小于1缩小，大于1放大
    )
    val circleLm = CircleLayoutManager(
        CircleLayoutManager
            .Builder(this)
        //  .setAngleInterval(90)
        //  .setDistanceToBottom(20)
        //  .setMaxRemoveAngle()
        //  .setMinRemoveAngle()
        //  .setFlipRotate()
        //  .setMaxVisibleItemCount(5)
        //  .setMoveSpeed(1)
        //  .setGravity()
        //  .setZAlignment()
        //  .setReverseLayout()
    )
    val circleScaleLm = CircleScaleLayoutManager(
        CircleScaleLayoutManager
            .Builder(this)
            .setMaxVisibleItemCount(1)
    )
    val galleryLm = GalleryLayoutManager(
        GalleryLayoutManager.Builder(this, 0)
            //  .setAngle(30f)
            //  .setDistanceToBottom(100)
            // .setFlipRotate(true)
            //   .setItemSpace(100)
            //   .setMinAlpha(0.2f)
            //   .setMoveSpeed(0.5f)
            //  .setOrientation(RecyclerView.HORIZONTAL)
            // .setRotateFromEdge(true)
            //  .setReverseLayout(true)
            .setMaxVisibleItemCount(5)
    )
    val rotateLm = RotateLayoutManager(
        RotateLayoutManager.Builder(this, 20)
            .setMaxVisibleItemCount(1)
    )
    val scaleLm = ScaleLayoutManager(
        ScaleLayoutManager.Builder(this, 0)
            .setMaxVisibleItemCount(2)
    )

    autoRv1.layoutManager = carouselLm
    autoRv2.layoutManager = circleLm
    autoRv3.layoutManager = circleScaleLm
    autoRv4.layoutManager = galleryLm
    autoRv5.layoutManager = rotateLm
    autoRv6.layoutManager = scaleLm

    autoRv1.adapter = adapter1
    autoRv2.adapter = adapter2
    autoRv3.adapter = adapter3
    autoRv4.adapter = adapter4
    autoRv5.adapter = adapter5
    autoRv6.adapter = adapter6
  }

  fun changePagerNum(v: View) {
    pages.clear()
    val size = Random.nextInt(20) + 1
    (0..size).forEach {
      pages.add(it)
    }
    adapter1.notifyDataSetChanged()
    adapter2.notifyDataSetChanged()
    adapter3.notifyDataSetChanged()
    adapter4.notifyDataSetChanged()
    adapter5.notifyDataSetChanged()
    adapter6.notifyDataSetChanged()
  }
}