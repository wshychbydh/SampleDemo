package com.cool.eye.func.jetpack.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

/**
 *
 *  val vm: GlobalViewModel by lazy {
 *      Global.getViewModelProvider().get(GlobalViewModel.class);
 *  }
 *
 * @author: chuanbo
 * @date: 2021/11/10 10:46
 * @desc: 全局持有类
 */
public final class Global {

    private Global() {
    }

    @NonNull
    public static ViewModelStore getViewModelStore() {
        return Holder.store;
    }

    @NonNull
    public static ViewModelProvider.NewInstanceFactory getFactory() {
        return Holder.factory;
    }

    @NonNull
    public static ViewModelProvider getViewModelProvider() {
        return Holder.provider;
    }

    private static final class Holder {

        public static final ViewModelStore store = new ViewModelStore();
        public static final ViewModelProvider.NewInstanceFactory factory
                = new ViewModelProvider.NewInstanceFactory();

        public static final ViewModelProvider provider = new ViewModelProvider(store, factory);
    }
}
