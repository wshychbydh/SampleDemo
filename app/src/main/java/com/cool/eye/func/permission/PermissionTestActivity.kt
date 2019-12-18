package com.cool.eye.func.permission

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import com.eye.cool.permission.PermissionHelper

/**
 *Created by ycb on 2019/12/18 0018
 */
class PermissionTestActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_permission_test)
  }

  override fun onResume() {
    super.onResume()
    println("====onResume========>>")
  }

  override fun onStart() {
    super.onStart()
    println("====onStart========>>")
  }

  override fun onPause() {
    super.onPause()
    println("====onPause========>>")
  }

  override fun onStop() {
    super.onStop()
    println("====onStop========>>")
  }

  fun showAlertDialog(v:View){
    AlertDialog.Builder(this)
        .setMessage("test111")
        .setPositiveButton("确定") { dialog, which ->
          anotherAlertDialog()
        }
        .show()
  }

  private fun anotherAlertDialog(){
    AlertDialog.Builder(this)
        .setMessage("test222222222")
        .show()
  }

  fun requestInActivity(v: View) {
    PermissionHelper.Builder(this)
        .permission(android.Manifest.permission.CAMERA)
        .showRationaleWhenRequest(true)
        .deniedPermissionCallback {
          it.forEach { i ->
            println("denied permission--->$i")
          }
        }
        .permissionCallback {
          Toast.makeText(this, "授权$it", Toast.LENGTH_SHORT).show()
        }
        .build()
        .request()
  }

  fun requestInFragment(v: View) {
    PermissionTestDialogFragment().show(supportFragmentManager, "test")
  }

  fun requestInContext(v: View) {
    val intentFilter = IntentFilter()
    intentFilter.addAction("permission")
    registerReceiver(PermissionTestReceiver(), intentFilter)

    val intent = Intent()
    intent.action = "permission"
    sendBroadcast(intent)
  }
}