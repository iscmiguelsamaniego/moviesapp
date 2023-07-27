package org.samtech.exam.network.volley

import android.graphics.Bitmap
import org.json.JSONObject

interface ServiceListener {

    fun postWithToken(path: String,
                      token: String,
                      params: JSONObject,
                      completionHandler: (response: String?) -> Unit)

    fun getImage(path: String, token: String, completionHandler: (response: Bitmap?) -> Unit)

    fun getString(path: String, token: String, completionHandler: (response: String?) -> Unit)
}