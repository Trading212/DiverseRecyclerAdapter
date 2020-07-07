package com.trading212.diverserecycleradapter.util

import android.util.Log
import com.trading212.diverserecycleradapter.BuildConfig

private const val TAG = "DiverseRecyclerAdapter"

internal inline fun logInfo(crossinline message: () -> String) {
    if (BuildConfig.DEBUG) {
        Log.i(TAG, message())
    }
}

internal inline fun logError(crossinline message: () -> String) {
    if (BuildConfig.DEBUG) {
        Log.e(TAG, message())
    }
}