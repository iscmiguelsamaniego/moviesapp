package org.samtech.exam.network.volley

import android.graphics.Bitmap
import org.json.JSONObject


class APIController constructor(serviceListenerInjection: ServiceListener) : ServiceListener{
private val serviceListener: ServiceListener = serviceListenerInjection

    override fun postWithToken(
        path: String,
        token: String,
        params: JSONObject,
        completionHandler: (response: String?) -> Unit
    ) {
        serviceListener.postWithToken(path, token, params, completionHandler)
    }

    override fun getImage(
        path: String,
        token: String,
        completionHandler: (response: Bitmap?) -> Unit
    ) {
        serviceListener.getImage(path, token, completionHandler)
    }

    override fun getString(
        path: String,
        token: String,
        completionHandler: (response: String?) -> Unit
    ) {
        serviceListener.getString(path, token, completionHandler)
    }


}