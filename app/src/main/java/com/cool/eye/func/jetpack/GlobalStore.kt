package com.cool.eye.func.jetpack

import androidx.lifecycle.ViewModelStore

/**
 * val vm: GlobalViewModel by lazy {
 *  ViewModelProvider(
 *  GlobalStore,
 *  ViewModelProvider.AndroidViewModelFactory(application)).get(GlobalViewModel::class.java)
 * }
 */
object GlobalStore : ViewModelStore()