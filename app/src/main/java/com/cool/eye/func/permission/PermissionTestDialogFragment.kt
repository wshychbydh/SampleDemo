package com.cool.eye.func.permission

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.appcompat.widget.AppCompatButton
import com.cool.eye.func.dialog.toast.ToastHelper
import com.eye.cool.permission.Permission.requestForResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PermissionTestDialogFragment : AppCompatDialogFragment(), View.OnClickListener {
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = super.onCreateDialog(savedInstanceState)
    val btn = AppCompatButton(requireContext())
    btn.text = "点击请求权限"
    btn.setOnClickListener(this)
    dialog.setContentView(btn)
    return dialog
  }

  override fun onClick(v: View?) {
    GlobalScope.launch {
      val result = requestForResult(
          this@PermissionTestDialogFragment,
          arrayOf(
              // android.Manifest.permission.READ_SMS,//manifest中未注册，授权不会成功
              android.Manifest.permission.WRITE_EXTERNAL_STORAGE
          )
      )
      ToastHelper.showToast(requireContext(), "授权${result.isSucceed()}")
      Log.i("Denied permission", "未授权权限-->${result.toDeniedText(requireContext())}")
    }
  }
}