package org.samtech.exam.utils

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.Window
import android.widget.Toast
import org.samtech.exam.R

object Utils {
    fun toast(ctx: Context?, message: String?) {
        if (ctx != null) {
            val toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show()
        }
    }

    fun showAlertWithAction(
        paramContext: Context,
        paramScreenShootUrl: String
    ) {
        val customDialog = Dialog(paramContext)
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog.setCancelable(false)
        customDialog.setContentView(R.layout.custom_dialog)
        /*val screenShoot =
            customDialog.findViewById(R.id.custom_dialog_app_screen_shoot) as ImageView*/



        /*btnClose.setOnClickListener {
            customDialog.dismiss()
        }*/


        customDialog.setCancelable(false)
        customDialog.show()
    }
}