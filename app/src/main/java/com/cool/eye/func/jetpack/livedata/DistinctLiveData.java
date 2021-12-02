package com.cool.eye.func.jetpack.livedata;

import android.os.Looper;

import java.lang.reflect.Field;
import java.util.Objects;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * @author: chuanbo
 * @date: 2021/12/2 10:48
 * @desc: 去重回调，相同值不回调
 */
public class DistinctLiveData<T> extends MutableLiveData<T> {

    //set相同值时是否仍更新旧值，但更新与否均不通知observer
    private final boolean update;

    public DistinctLiveData() {
        this(true);
    }

    public DistinctLiveData(boolean update) {
        this.update = update;
    }

    @Override
    public void setValue(T value) {
        if (Objects.equals(getValue(), value)) {
            if (update) interceptSetValue(value);
            return;
        }
        super.setValue(value);
    }

    private void interceptSetValue(T value) {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Cannot invoke setValue on a background thread");
        }

        //更改mDispatchingValue可能会导致Observer分发不完全
        try {
            Class clazz = LiveData.class;
            Field version = clazz.getDeclaredField("mVersion");
            version.setAccessible(true);
            int v = version.getInt(this);
            version.set(this, v + 1);
            Field data = clazz.getDeclaredField("mData");
            data.setAccessible(true);
            data.set(this, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
