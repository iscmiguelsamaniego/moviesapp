package org.samtech.exam.ui.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.samtech.exam.database.entities.Movies
import org.samtech.exam.network.pokos.MoviesPoko
import org.samtech.exam.network.volley.APIController
import org.samtech.exam.network.volley.ServiceListenerVolley
import org.samtech.exam.repositories.MoviesRepository
import org.samtech.exam.utils.Constants.POPULAR
import org.samtech.exam.utils.Constants.RATED
import org.samtech.exam.utils.Constants.RECOMMENDED
import org.samtech.exam.utils.Constants.TOKEN
import org.samtech.exam.utils.Utils

class MoviesViewModel(private val resultsRepo: MoviesRepository) : ViewModel() {

    val allPopularMovies: LiveData<List<Movies>> =
        resultsRepo.getMovieByType(POPULAR).asLiveData()

    val allBestRatedMovies: LiveData<List<Movies>> =
        resultsRepo.getMovieByType(RATED).asLiveData()

    val allBestRecommendedMovies: LiveData<List<Movies>> =
        resultsRepo.getMovieByType(RECOMMENDED).asLiveData()


    private fun insertMovies(movies: Movies) = viewModelScope.launch {
        resultsRepo.insert(movies)
    }

    private fun getApiController(): APIController {
        val service = ServiceListenerVolley()
        return APIController(service)
    }

    fun downloadMovieBy(ctx: Context, paramPath: String, paramType: String) {
        getApiController().getString(paramPath, TOKEN) { response ->
            if (!response.isNullOrBlank()) {
                serialize(ctx, response, paramType)
            }
        }
    }

    private fun serialize(ctx: Context, paramResponse: String, paramType: String) {
        var errorMsg = ""
        if (paramResponse.isNotBlank()) {
            val objValues = Gson().fromJson(paramResponse, MoviesPoko::class.java)
            for (results in objValues.results) {
                if (results.id != null) {
                if (paramType.isNotBlank()) {
                if (!results.backdropPath.isNullOrBlank()) {
                    if (results.originalLanguage != null) {
                    if (results.originalTitle != null) {
                    if (results.overview != null) {
                        if (results.popularity != null) {
                        if (results.posterPath != null) {
                        if (results.releaseDate != null) {
                        if (results.title != null) {
                        if (results.video != null) {
                        if (results.voteAverage != null) {
                        if (results.voteAverage != null) {
                        insertMovies(
                            Movies(
                                results.id,
                                paramType,
                                results.adult,
                                Utils.customValidate(ctx, results.backdropPath!!),
                                results.genreIds.toString(),
                                Utils.customValidate(ctx, results.originalLanguage!!),
                                Utils.customValidate(ctx, results.originalTitle!!),
                                Utils.customValidate(ctx, results.overview!!),
                                results.popularity,
                                Utils.customValidate(ctx, results.posterPath!!),
                                results.releaseDate,
                                Utils.customValidate(ctx, results.title!!),
                                results.video,
                                results.voteAverage,
                                results.voteCount
                            )
                        )
                        }else{
                            errorMsg = "voteCount vacio"
                        }
                        }else{
                            errorMsg = "voteAverage vacio"
                        }
                        }else{
                            errorMsg = "video vacio"
                        }
                        }else{
                            errorMsg = "title vacio"
                        }
                        }else{
                            errorMsg = "releaseDate vacio"
                        }
                        }else{
                            errorMsg = "posterPath vacio"
                        }
                        }else{
                            errorMsg = "popularity vacio"
                        }
                    }else{
                        errorMsg = "overview vacio"
                    }
                    }else{
                        errorMsg = "originalTitle vacio"
                    }
                    }else{
                        errorMsg = "genreIds vacio"
                    }
                }else{
                    errorMsg = "backdrop vacio"
                }
                }else{
                    errorMsg = "typo vacio"

                }
                } else {
                    errorMsg = "id vacio"
                }

                if(errorMsg.isNotBlank()) {
                    Log.d("ERROR",errorMsg)
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class MoviesModelFactory(
        private val moviesRepo: MoviesRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
                return MoviesViewModel(moviesRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}