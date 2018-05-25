package com.cool.eye.func

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.cool.eye.func.address.mvp.view.AddressActivity
import com.cool.eye.func.notify.NotifyActivity
import com.cool.eye.func.permission.DefaultRationale
import com.cool.eye.func.permission.Permission
import com.cool.eye.func.permission.PermissionHelper
import com.cool.eye.func.permission.SettingRationale
import com.cool.eye.func.view.trend.TrendActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun toPermission(view: View) {
        PermissionHelper.Builder(this)
                .permission(Permission.Group.STORAGE)
                .rationale(DefaultRationale())
                .rationaleSetting(SettingRationale())
                .build()
                .request()
    }

    fun toAddress(view: View) {
        startActivity(Intent(this, AddressActivity::class.java))
    }

    fun toNotify(view: View) {
        startActivity(Intent(this, NotifyActivity::class.java))
    }

    fun toTrendView(v: View) {
        startActivity(Intent(this, TrendActivity::class.java))
    }
}
