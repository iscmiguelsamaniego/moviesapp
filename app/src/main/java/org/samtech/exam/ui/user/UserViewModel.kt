package org.samtech.exam.ui.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.samtech.exam.Singleton
import org.samtech.exam.database.entities.Results
import org.samtech.exam.firebase.models.FSUser
import org.samtech.exam.firebase.repositories.FireStoreUserRepository
import org.samtech.exam.interfaces.UserRepository
import org.samtech.exam.interfaces.UserRepository.UserListener
import org.samtech.exam.network.pokos.MyRatedPoko
import org.samtech.exam.network.pokos.UserPoko
import org.samtech.exam.repositories.ResultsRepository
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
    private val userRepo: FireStoreUserRepository,
    private val resultsRepo: ResultsRepository,
) : ViewModel() {
    private val userRepository: UserRepository = userRepo

    val allResults: LiveData<List<Results>> = resultsRepo.getResults().asLiveData()

    fun getUserFSValues(): MutableLiveData<FSUser> {
        val userMLD = MutableLiveData<FSUser>()

        userRepository.getUserValues(object : UserListener {
            override fun onUserResult(user: FSUser) {
                userMLD.postValue(user)
            }
        })

        return userMLD
    }

    fun checkroutes() {
        Log.d("yyyyy", RATED_PATH)
        Log.d("yyyyy", CATEGORIES_PATH)
        Log.d("yyyyy", POPULAR_PATH)
        Log.d("yyyyy", BEST_RATED_PATH)
        Log.d("yyyyy", BEST_RECOMMENDED_PATH)
    }

    fun insertResults(results: Results) = viewModelScope.launch {
        resultsRepo.insert(results)
    }

     fun getRatedValues() {
        val ctx = Singleton.instance?.applicationContext
        if (ctx != null) {
            if (isOnline(ctx)) {
                val service = ServiceListenerVolley()
                val apiController = APIController(service)

                apiController.getString(RATED_PATH, TOKEN) { response ->
                    if (!response.isNullOrBlank()) {
                        val ratedVal = Gson().fromJson(response, MyRatedPoko::class.java)

                        for(results in ratedVal.results){
                            insertResults(
                                Results(
                                    results.id,
                                    results.adult,
                                    results.backdropPath,
                                    results.originalLanguage,
                                    results.originalTitle,
                                    results.overview,
                                    results.popularity,
                                    results.posterPath,
                                    results.releaseDate,
                                    results.title,
                                    results.video,
                                    results.voteAverage,
                                    results.voteCount,
                                    results.rating
                                )
                            )
                        }

                    }
                }
            }
        }
    }

    fun downloadAndStoreOrUpdateUser(documentId: String) {
        val ctx = Singleton.instance?.applicationContext
        if (ctx != null) {
            if (isOnline(ctx)) {
                val service = ServiceListenerVolley()
                val apiController = APIController(service)

                apiController.getString(PROFILE_PATH, TOKEN) { response ->
                    if (!response.isNullOrBlank()) {
                        val userPokoResponse = Gson().fromJson(response, UserPoko::class.java)
                        if (documentId.isBlank()) {
                            userRepository.storeUserValues(userPokoResponse)
                        } else {
                            userRepository.updateUserValues(documentId, userPokoResponse)
                        }
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class UserViewModelFactory(
        private val userRepo: FireStoreUserRepository,
        private val resultsRepo: ResultsRepository
        ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return UserViewModel(userRepo, resultsRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}