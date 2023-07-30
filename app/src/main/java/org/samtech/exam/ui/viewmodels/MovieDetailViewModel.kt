package org.samtech.exam.ui.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.samtech.exam.database.entities.Reviews
import org.samtech.exam.network.pokos.ReviewsPoko
import org.samtech.exam.network.volley.APIController
import org.samtech.exam.network.volley.ServiceListenerVolley
import org.samtech.exam.repositories.ReviewsRepositor
import org.samtech.exam.utils.Constants
import org.samtech.exam.utils.Utils

class MovieDetailViewModel(private val reviewsRepo: ReviewsRepositor) : ViewModel() {

    val reviewsCount: LiveData<List<Int>> = reviewsRepo.getCount().asLiveData()
    fun getReviewsBy(paramMovieid: String): LiveData<List<Reviews>> {
        return reviewsRepo.getReviewBy(paramMovieid).asLiveData()
    }

    fun deleteAllReviews() = viewModelScope.launch {
        reviewsRepo.deleteAll()
    }

    fun insertReviews(reviews: Reviews) = viewModelScope.launch {
        reviewsRepo.insert(reviews)
    }

    fun getApiController(): APIController {
        val service = ServiceListenerVolley()
        return APIController(service)
    }

    fun downloadReviewValues(ctx: Context, idMovieParam: String) {
        val pathReviews = Constants.REVIEWS_PATH_A + idMovieParam + Constants.REVIEWS_PATH_B
        getApiController().getString(pathReviews, Constants.TOKEN) { response ->
            if (!response.isNullOrBlank()) {
                val reviewsResponse = Gson().fromJson(response, ReviewsPoko::class.java)
                for (results in reviewsResponse.results) {
                    insertReviews(
                        Reviews(
                            idMovieParam,
                            Utils.customValidate(ctx, results.authorDetails?.name!!),
                            Utils.customValidate(ctx, results.authorDetails?.username!!),
                            Utils.customValidate(ctx, results.authorDetails?.avatarPath!!),
                            Utils.customValidate(ctx, results.createdAt!!),
                            results.authorDetails?.rating.toString(),
                            results.content
                        )
                    )
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class MoviesDetailViewModelFactory(private val reviewsRepo: ReviewsRepositor) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
                return MovieDetailViewModel(reviewsRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}