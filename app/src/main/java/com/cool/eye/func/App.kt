package com.cool.eye.func

import android.app.Application
import com.cool.eye.func.recyclerview.TestEmptyViewHolder
import com.cool.eye.func.theme.CustomSDCardLoader
import com.cool.eye.func.theme.ZipSDCardLoader
import com.eye.cool.adapter.loadmore.DefaultLoadMoreViewHolder
import com.eye.cool.adapter.loadmore.DefaultNoMoreDataViewHolder
import com.eye.cool.adapter.loadmore.LoadMore
import com.eye.cool.adapter.loadmore.NoMoreData
import com.eye.cool.adapter.support.DefaultLoadingViewHolder
import com.eye.cool.adapter.support.Empty
import com.eye.cool.adapter.support.GlobalConfig
import com.eye.cool.adapter.support.Loading
import skin.support.SkinCompatManager
import skin.support.app.SkinAppCompatViewInflater
import skin.support.constraint.app.SkinConstraintViewInflater


/**
 *Created by ycb on 2019/11/15 0015
 */
class App : Application() {
  override fun onCreate() {
    super.onCreate()
    SkinCompatManager.withoutActivity(this)
        .addInflater(SkinAppCompatViewInflater())           // 基础控件换肤初始化
        //   .addInflater(SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
        .addInflater(SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
        //  .addInflater(SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
        .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
        .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
        .addStrategy(CustomSDCardLoader())
        .addStrategy(ZipSDCardLoader())
        .loadSkin()

    GlobalConfig
        .setDefaultCount(5)
        .showLoadMore(true)
        .showNoMoreData(true)
        .showNoMoreStatusAlways(true)
        .setDefaultEmpty(Empty(), TestEmptyViewHolder::class.java)
        .setDefaultLoading(Loading(), DefaultLoadingViewHolder::class.java)
        .setDefaultNoMoreData(NoMoreData(), DefaultNoMoreDataViewHolder::class.java)
        .setDefaultLoadMore(LoadMore(), DefaultLoadMoreViewHolder::class.java)
  }
}