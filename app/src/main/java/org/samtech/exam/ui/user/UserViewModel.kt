package org.samtech.exam.ui.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.samtech.exam.Singleton
import org.samtech.exam.database.entities.Results
import org.samtech.exam.database.entities.Users
import org.samtech.exam.network.pokos.MyRatedPoko
import org.samtech.exam.network.pokos.UserPoko
import org.samtech.exam.network.volley.APIController
import org.samtech.exam.network.volley.ServiceListenerVolley
import org.samtech.exam.repositories.ResultsRepository
import org.samtech.exam.repositories.UsersRepository
import org.samtech.exam.utils.Constants
import org.samtech.exam.utils.Constants.BEST_RATED_PATH
import org.samtech.exam.utils.Constants.BEST_RECOMMENDED_PATH
import org.samtech.exam.utils.Constants.CATEGORIES_PATH
import org.samtech.exam.utils.Constants.POPULAR_PATH
import org.samtech.exam.utils.Constants.RATED_PATH
import org.samtech.exam.utils.Constants.TOKEN

class UserViewModel(
    private val usersRepo: UsersRepository,
    private val resultsRepo: ResultsRepository
) : ViewModel() {

    val allResults: LiveData<List<Results>> = resultsRepo.getResults().asLiveData()
    val allUsers: LiveData<List<Users>> = usersRepo.getUsers().asLiveData()

    fun checkroutes() {
        Log.d("yyyyy", RATED_PATH)
        Log.d("yyyyy", CATEGORIES_PATH)
        Log.d("yyyyy", POPULAR_PATH)
        Log.d("yyyyy", BEST_RATED_PATH)
        Log.d("yyyyy", BEST_RECOMMENDED_PATH)
    }

    fun insertUsers(users: Users) = viewModelScope.launch {
        usersRepo.insert(users)
    }

    fun insertResults(results: Results) = viewModelScope.launch {
        resultsRepo.insert(results)
    }

    fun downloadUserValues() {
        val ctx = Singleton.instance?.applicationContext
        if (ctx != null) {
            val service = ServiceListenerVolley()
            val apiController = APIController(service)

            apiController.getString(Constants.PROFILE_PATH, Constants.TOKEN) { response ->
                if (!response.isNullOrBlank()) {

                    val userResponse = Gson().fromJson(response, UserPoko::class.java)

                    insertUsers(
                        Users(
                            userResponse.id,
                            userResponse.avatar?.tmdb!!.avatarPath,
                            userResponse.iso6391,
                            userResponse.iso31661,
                            userResponse.name,
                            userResponse.includeAdult,
                            userResponse.username
                        )
                    )
                }
            }
        }
    }

    fun downloadRatedValues() {
        val ctx = Singleton.instance?.applicationContext
        if (ctx != null) {
            val service = ServiceListenerVolley()
            val apiController = APIController(service)

            apiController.getString(RATED_PATH, TOKEN) { response ->
                if (!response.isNullOrBlank()) {
                    val ratedVal = Gson().fromJson(response, MyRatedPoko::class.java)

                    for (results in ratedVal.results) {

                        insertResults(
                            Results(
                                results.id,
                                results.adult,
                                results.backdropPath,
                                results.genreIds.toString(),
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

    @Suppress("UNCHECKED_CAST")
    class UserViewModelFactory(
        private val usersRepo: UsersRepository,
        private val resultsRepo: ResultsRepository
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return UserViewModel(usersRepo, resultsRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}