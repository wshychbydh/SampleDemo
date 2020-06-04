package com.cool.eye.func.mvp

import android.content.Intent
import android.os.Bundle

/**
 *Created by ycb on 2018/09/06
 */
interface IPresenter {

  fun onAttach() {}

  fun onCreate(bundle: Bundle? = null) {}

  fun onPostCreate(bundle: Bundle? = null) {}

  fun onActivityCreated(bundle: Bundle? = null) {}

  fun onViewCreated(bundle: Bundle? = null) {}

  fun onStart() {}

  fun onRestart() {}

  fun onResume() {}

  fun onPause() {}

  fun onStop() {}

  fun onDestroy() {}

  fun onNewIntent(intent: Intent?) {}

  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

  fun onBackPressed() {}
}