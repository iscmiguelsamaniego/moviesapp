package org.samtech.exam.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

object LocationUtils {
    fun Context.hasPermission(permission: String): Boolean {
        if (permission == Manifest.permission.ACCESS_BACKGROUND_LOCATION &&
            android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
            return true
        }

        return ActivityCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_GRANTED
    }

    fun Fragment.requestPermissionWithRationale(
        permission: String,
        requestCode: Int,
        snackbar: Snackbar
    ) {
        val provideRationale = shouldShowRequestPermissionRationale(permission)

        if (provideRationale) {
            snackbar.show()
        } else {
            requestPermissions(arrayOf(permission), requestCode)
        }
    }

}