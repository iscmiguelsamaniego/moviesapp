package org.samtech.exam.utils

import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.text.Html
import android.text.Spanned
import android.view.Gravity
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import org.samtech.exam.R

object Utils {
    private var mLastClickTime: Long = 0

    fun setGlideImage(paramContext: Context, paramUrl: String, paramImageView: ImageView) {
        Glide
            .with(paramContext)
            .load(paramUrl)
            .placeholder(R.drawable.ic_no_image)
            .into(paramImageView)
    }

    fun getSpannedText(text: String) : Spanned{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(text);
        }
    }

    fun mayTapButton(time: Long): Boolean {
        return if (SystemClock.elapsedRealtime() - mLastClickTime < time) {
            false
        } else {
            mLastClickTime = SystemClock.elapsedRealtime()
            true
        }
    }

    fun customToast(ctx: Context, message: String?) {
        val toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun customValidate(ctx: Context, paramValue: String): String {
        var value = ""
        if (paramValue.isBlank()) {
            value = ctx.getString(R.string.no_registered)
        } else {
            value = paramValue
        }
        return value
    }

}