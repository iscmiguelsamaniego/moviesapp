package org.samtech.exam.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkUtils {

    @Suppress("DEPRECATION")
    fun isOnline(context: Context?): Boolean {

        var result = false
        if (context != null) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            result = true
                        }

                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            result = true
                        }

                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            result = true
                        }

                    }
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    }

                    if (activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }

        return result
    }
}