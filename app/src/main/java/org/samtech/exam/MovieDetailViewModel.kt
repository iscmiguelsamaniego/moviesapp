package org.samtech.exam

import android.util.Log
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

class MovieDetailViewModel(private val reviewsRepo: ReviewsRepositor) : ViewModel() {

    val allReviews: LiveData<List<Reviews>> = reviewsRepo.getReviews().asLiveData()
    val reviewsCount: LiveData<List<Int>> = reviewsRepo.getCount().asLiveData()

    fun deleteAllReviews() = viewModelScope.launch {
        reviewsRepo.deleteAll()
    }

    fun insertReviews(reviews: Reviews) = viewModelScope.launch {
        reviewsRepo.insert(reviews)
    }


    fun downloadReviewValues(idMovieParam: String) {
        val ctx = Singleton.instance?.applicationContext
        if (ctx != null) {
            val service = ServiceListenerVolley()
            val apiController = APIController(service)

            val pathReviews = Constants.REVIEWS_PATH_A + idMovieParam + Constants.REVIEWS_PATH_B
            apiController.getString(pathReviews, Constants.TOKEN) { response ->
                if (!response.isNullOrBlank()) {
                    val reviewsResponse = Gson().fromJson(response, ReviewsPoko::class.java)
                    for (results in reviewsResponse.results) {
                        insertReviews(
                            Reviews(
                                reviewsResponse.id,
                                results.authorDetails?.name,
                                results.authorDetails?.username,
                                results.authorDetails?.avatarPath,
                                results.authorDetails?.rating.toString(),
                                results.createdAt,
                                results.content
                            )
                        )
                    }
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