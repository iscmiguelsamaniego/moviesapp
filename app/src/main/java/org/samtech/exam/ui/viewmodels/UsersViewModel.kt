package org.samtech.exam.ui.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.samtech.exam.database.entities.Movies
import org.samtech.exam.database.entities.Reviews
import org.samtech.exam.database.entities.Users
import org.samtech.exam.network.pokos.MoviesPoko
import org.samtech.exam.network.pokos.UserPoko
import org.samtech.exam.network.volley.APIController
import org.samtech.exam.network.volley.ServiceListenerVolley
import org.samtech.exam.repositories.MoviesRepository
import org.samtech.exam.repositories.UsersRepository
import org.samtech.exam.utils.Constants.PROFILE_PATH
import org.samtech.exam.utils.Constants.RATED_BY_ME
import org.samtech.exam.utils.Constants.RATED_PATH
import org.samtech.exam.utils.Constants.TOKEN
import org.samtech.exam.utils.Utils.customValidate

class UsersViewModel(
    private val usersRepo: UsersRepository,
    private val resultsRepo: MoviesRepository
) : ViewModel() {

    val allRatedByMeMovies: LiveData<List<Movies>> =
        resultsRepo.getMovieByType(RATED_BY_ME).asLiveData()

    val allUsers: LiveData<List<Users>> = usersRepo.getUsers().asLiveData()

    private fun insertUsers(users: Users) = viewModelScope.launch {
        usersRepo.insert(users)
    }

    private fun insertMovies(movies: Movies) = viewModelScope.launch {
        resultsRepo.insert(movies)
    }

    private fun getApiController(): APIController {
        val service = ServiceListenerVolley()
        return APIController(service)
    }

    fun downloadValuesBy(ctx: Context, paramPath: String, paramType: String) {
        getApiController().getString(paramPath, TOKEN) { response ->
            if (!response.isNullOrBlank()) {
                if (paramPath.equals(PROFILE_PATH)) {
                    serializeUsers(ctx, response)
                } else if (paramPath.equals(RATED_PATH)) {
                    serializeMovies(ctx, response, paramType)
                }
            }
        }
    }

    fun serializeUsers(ctx: Context, paramResponse: String) {
        if (paramResponse.isNotBlank()) {
            val userResponse = Gson().fromJson(paramResponse, UserPoko::class.java)

            insertUsers(
                Users(
                    userResponse.id,
                    userResponse.avatar?.tmdb!!.avatarPath,
                    customValidate(ctx, userResponse.iso6391!!),
                    customValidate(ctx, userResponse.iso31661!!),
                    customValidate(ctx, userResponse.name!!),
                    userResponse.includeAdult,
                    customValidate(ctx, userResponse.username!!),
                )
            )
        }
    }

    private fun serializeMovies(ctx: Context, paramResponse: String, paramType: String) {
        if (paramResponse.isNotBlank()) {
            val objValues = Gson().fromJson(paramResponse, MoviesPoko::class.java)
            for (results in objValues.results) {
                insertMovies(
                    Movies(
                        results.id,
                        paramType,
                        results.adult,
                        customValidate(ctx, results.backdropPath!!),
                        results.genreIds.toString(),
                        customValidate(ctx, results.originalLanguage!!),
                        customValidate(ctx, results.originalTitle!!),
                        customValidate(ctx, results.overview!!),
                        results.popularity,
                        customValidate(ctx, results.posterPath!!),
                        results.releaseDate,
                        customValidate(ctx, results.title!!),
                        results.video,
                        results.voteAverage,
                        results.voteCount
                    )
                )
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class UserViewModelFactory(
        private val usersRepo: UsersRepository,
        private val moviesRepo: MoviesRepository
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
                return UsersViewModel(usersRepo, moviesRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}