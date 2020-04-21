package com.cool.eye.func.util

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import android.util.DisplayMetrics
import java.util.*


object LocaleHelper {

  private const val LANGUAGE = "language"

  fun setLocale(context: Context) {
    setLocale(context, getPreferenceLanguage(context))
  }

  fun onAttach(context: Context) {
    val lang = getPreferenceLanguage(context)
    setLocale(context, lang)
  }

  fun onAttach(context: Context, defaultLanguage: String) {
    val lang = getPreferenceLanguage(context, defaultLanguage)
    setLocale(context, lang)
  }

  fun getLanguage(context: Context): String {
    return getPreferenceLanguage(context)
  }

  fun currentLanguageIsEn(context: Context): Boolean {
    val lan = getLanguage(context)
    return lan == Locale.ENGLISH.language
  }

  fun getLocale(context: Context): Locale {
    return getDefaultLocale(context)
  }

  private fun setLocale(context: Context, language: String) {
    setLocale(context, Locale(language))
  }

  private fun setLocale(context: Context, locale: Locale) {
    preferences(context, locale)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      updateResources(context, locale)
      //适配8.0以上，必须applicationContext调用一次
      updateResources(context.applicationContext, locale)
    } else updateResourcesLegacy(context, locale)
  }

  fun setLocaleForResult(context: Context, locale: Locale): Boolean {
    if (locale.language == getLanguage(context)) return false
    setLocale(context, locale)
    return true
  }

  private fun getPreferenceLanguage(context: Context, defaultLanguage: String? = null): String {
    val preferences = context.getSharedPreferences(LANGUAGE, Context.MODE_PRIVATE)
    return preferences.getString(LANGUAGE, defaultLanguage) ?: getDefaultLocale(context).language
  }

  private fun getPreferenceLanguage(context: Context, defaultLocale: Locale): Locale {
    val preferences = context.getSharedPreferences(LANGUAGE, Context.MODE_PRIVATE)
    return when (preferences.getString(LANGUAGE, null)) {
      Locale.SIMPLIFIED_CHINESE.language -> Locale.SIMPLIFIED_CHINESE
      Locale.ENGLISH.language -> Locale.ENGLISH
      else -> defaultLocale
    }
  }

  //ignored local type
  private fun preferences(context: Context, language: String) {
    val preferences = context.getSharedPreferences(LANGUAGE, Context.MODE_PRIVATE)
    val editor = preferences.edit()
    editor.putString(LANGUAGE, language)
    editor.apply()
  }

  //ignored local type
  private fun preferences(context: Context, locale: Locale) {
    val preferences = context.getSharedPreferences(LANGUAGE, Context.MODE_PRIVATE)
    val editor = preferences.edit()
    editor.putString(LANGUAGE, locale.language)
    editor.apply()
  }

  @TargetApi(Build.VERSION_CODES.N)
  private fun updateResources(context: Context, language: String?): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val configuration = context.resources.configuration
    configuration.setLocale(locale)
    return context.createConfigurationContext(configuration)
  }

  @TargetApi(Build.VERSION_CODES.N)
  private fun updateResources(context: Context, locale: Locale) {
//    Locale.setDefault(locale)
//    val configuration = context.resources.configuration
//    configuration.setLocale(locale)
//    return context.createConfigurationContext(configuration)

    val resources = context.resources
    val dm: DisplayMetrics = resources.displayMetrics
    val config: Configuration = resources.configuration
    config.setLocale(locale)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      val localeList = LocaleList(locale)
      LocaleList.setDefault(localeList)
      config.setLocales(localeList)
      context.createConfigurationContext(config)
    }
    Locale.setDefault(locale)
    resources.updateConfiguration(config, dm)
  }

  private fun updateResourcesLegacy(context: Context, language: String?): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val resources = context.resources
    val configuration = resources.configuration
    configuration.locale = locale
    resources.updateConfiguration(configuration, resources.displayMetrics)
    return context
  }

  private fun updateResourcesLegacy(context: Context, locale: Locale) {
    Locale.setDefault(locale)
    val resources = context.resources
    val configuration = resources.configuration
    configuration.locale = locale
    resources.updateConfiguration(configuration, resources.displayMetrics)
  }


  private fun getDefaultLocale(context: Context): Locale {
    //由于API仅支持7.0，需要判断，否则程序会crash(解决7.0以上系统不能跟随系统语言问题)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      val preferences = context.getSharedPreferences(LANGUAGE, Context.MODE_PRIVATE)
      val checkedLanguage = preferences.getString(LANGUAGE, null)
      val localeList = LocaleList.getDefault()
      // 如果app已选择不跟随系统语言，则取第二个数据为系统默认语言
      if (!checkedLanguage.isNullOrEmpty() && localeList.size() > 1) {
        localeList[1]
      } else {
        localeList[0]
      }
    } else {
      Locale.getDefault()
    }
  }
}