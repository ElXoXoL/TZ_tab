package com.test.tz_tab.utils

import android.util.Log
import com.test.tz_tab.BuildConfig
import java.lang.Exception


object Logger {

    private val isLogsEnabled = BuildConfig.DEBUG

    fun log(obj: Any?, logTag: String = "TZ_TAB"){
        if (isLogsEnabled) Log.d(logTag, obj.toString())
    }

}