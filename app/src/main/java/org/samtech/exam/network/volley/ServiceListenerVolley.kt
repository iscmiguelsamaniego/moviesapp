package org.samtech.exam.network.volley

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import org.samtech.exam.Singleton
import org.samtech.exam.utils.Constants.AUTHORIZATION
import org.samtech.exam.utils.Constants.BASE_PATH
import org.samtech.exam.utils.Constants.BEARER

class ServiceListenerVolley : ServiceListener {

    val TAG = ServiceListenerVolley::class.java.simpleName

    override fun postWithToken(
        path: String,
        token: String,
        params: JSONObject,
        completionHandler: (response: String?) -> Unit
    ) {
        val jsonObjReq = object : JsonObjectRequest(
            Method.POST, BASE_PATH + path, params,
            Response.Listener { response ->
                Log.d(TAG, "/post request OK! Response: $response")
                completionHandler(response.toString())
            },
            Response.ErrorListener { error ->
                VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
                completionHandler(null)
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                headers.put(AUTHORIZATION, BEARER + token)
                return headers
            }
        }
        Singleton.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

    override fun getImage(
        path: String,
        token: String,
        completionHandler: (response: Bitmap?) -> Unit
    ) {

        val imageRequest: ImageRequest = object : ImageRequest(
            BASE_PATH + path,
            { response ->
                Log.d(TAG, "/post request OK! Response: $response")
                completionHandler(response)
            },
            0,
            0,
            ImageView.ScaleType.CENTER_CROP,
            Bitmap.Config.ARGB_8888,
            { error ->
                VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put(AUTHORIZATION, BEARER + token)
                return headers
            }
        }

        Singleton.instance?.addToRequestQueue(imageRequest, TAG)

    }

    override fun getString(
        path: String,
        token: String,
        completionHandler: (response: String?) -> Unit
    ) {
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET,
            BASE_PATH + path,
            Response.Listener<String> { response ->
                Log.d(TAG, "/post request OK! Response: $response")
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
                completionHandler(null)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put(AUTHORIZATION, BEARER + token)
                return headers
            }
        }

        Singleton.instance?.addToRequestQueue(stringRequest, TAG)
    }
}