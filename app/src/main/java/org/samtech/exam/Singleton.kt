package org.samtech.exam

import android.app.Application
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.samtech.exam.database.MoviesRoomDatabase
import org.samtech.exam.firebase.repositories.FireStoreUserRepository
import org.samtech.exam.repositories.ResultsRepository

class Singleton : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { MoviesRoomDatabase.getDataBase(this, applicationScope) }
    val resultsRepository by lazy { ResultsRepository(database.resultsDao()) }
    val fireStoreUserRepository by lazy { FireStoreUserRepository() }


    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    val requestQueue: RequestQueue? = null
        get() {
            if (field == null) {
                return Volley.newRequestQueue(applicationContext)
            }
            return field
        }

    fun <T> addToRequestQueue(request: Request<T>, tag: String) {
        request.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue?.add(request)
    }

    fun <T> addToRequestQueue(request: Request<T>) {
        request.tag = TAG
        requestQueue?.add(request)
    }

    fun cancelPendingRequests(tag: Any) {
        if (requestQueue != null) {
            requestQueue!!.cancelAll(tag)
        }
    }

    companion object {
        private val TAG = Singleton::class.java.simpleName
        @get:Synchronized
        var instance: Singleton? = null
            private set
    }
}