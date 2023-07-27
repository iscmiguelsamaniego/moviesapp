package org.samtech.exam.ui.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import org.samtech.exam.Singleton
import org.samtech.exam.firebase.models.FSUser
import org.samtech.exam.firebase.repositories.FirebaseUserRepository
import org.samtech.exam.interfaces.UserRepository
import org.samtech.exam.interfaces.UserRepository.UserListener
import org.samtech.exam.network.pokos.MyRatedPoko
import org.samtech.exam.network.pokos.UserPoko
import org.samtech.exam.network.volley.APIController
import org.samtech.exam.network.volley.ServiceListenerVolley
import org.samtech.exam.utils.Constants.BEST_RATED_PATH
import org.samtech.exam.utils.Constants.BEST_RECOMMENDED_PATH
import org.samtech.exam.utils.Constants.CATEGORIES_PATH
import org.samtech.exam.utils.Constants.POPULAR_PATH
import org.samtech.exam.utils.Constants.TOKEN
import org.samtech.exam.utils.Constants.PROFILE_PATH
import org.samtech.exam.utils.Constants.RATED_PATH
import org.samtech.exam.utils.NetworkUtils.isOnline

class UserViewModel(
     val userRepo: FirebaseUserRepository
) : ViewModel() {
    private val userRepository: UserRepository = userRepo

   fun getUserFSValues() : MutableLiveData<FSUser>{
       val userMLD  = MutableLiveData<FSUser>()

       userRepository.getUserValues(object : UserListener{
           override fun onUserResult(user: FSUser) {
                   userMLD.postValue(user)
           }
       })

       return userMLD
   }

    fun checkroutes(){
        Log.d("yyyyy", RATED_PATH)
        Log.d("yyyyy", CATEGORIES_PATH)
        Log.d("yyyyy",POPULAR_PATH)
        Log.d("yyyyy", BEST_RATED_PATH)
        Log.d("yyyyy", BEST_RECOMMENDED_PATH)
    }

    fun downloadRated(){
        val ctx = Singleton.instance?.applicationContext
        if (ctx != null) {
            if (isOnline(ctx)) {
                val service = ServiceListenerVolley()
                val apiController = APIController(service)

                apiController.getString(RATED_PATH, TOKEN) { response ->
                    if (!response.isNullOrBlank()) {
                        val myRatedResponse = Gson().fromJson(response, MyRatedPoko::class.java)
                        Log.d("+++++++>>>>", ""+myRatedResponse)
                        //userRepository.storeUserValues(myRatedResponse)
                        //userRepo.storeUserData(myRatedResponse)
                    }
                }
            }
        }
    }

    fun downloadAndStoreOrUpdateUser(documentId : String) {
        val ctx = Singleton.instance?.applicationContext
        if (ctx != null) {
            if (isOnline(ctx)) {
                val service = ServiceListenerVolley()
                val apiController = APIController(service)

                apiController.getString(PROFILE_PATH, TOKEN) { response ->
                    if (!response.isNullOrBlank()) {
                        val userPokoResponse = Gson().fromJson(response, UserPoko::class.java)
                        if(documentId.isBlank()) {
                            userRepository.storeUserValues(userPokoResponse)
                        }else{
                            userRepository.updateUserValues(documentId, userPokoResponse)
                        }
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class UserViewModelFactory(private val userRepo: FirebaseUserRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return UserViewModel(userRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}