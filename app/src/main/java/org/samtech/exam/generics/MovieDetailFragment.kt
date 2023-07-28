package org.samtech.exam.generics

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import org.samtech.exam.MovieDetailViewModel
import org.samtech.exam.R
import org.samtech.exam.utils.Constants.BASE_IMAGE_PATH
import org.samtech.exam.utils.Utils
import org.samtech.exam.utils.Utils.setGlideImage

class MovieDetailFragment : Fragment() {

    //private lateinit var backdropIView: ImageView
    private lateinit var backdropIView: ConstraintLayout
    private lateinit var posterIView: ImageView
    private lateinit var movieOverviewTView: TextView
    private lateinit var descriptionTView: TextView

    private lateinit var userReviewIView: ImageView
    private lateinit var titleReviewTView: TextView
    private lateinit var scoreReviewTView: TextView
    private lateinit var subtitleReviewTView: TextView
    private lateinit var detailReviewTView: TextView
    private lateinit var viewAllReviewsBtn: Button

    companion object {
        fun newInstance() = MovieDetailFragment()
    }

    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movie_detail, container, false)
        backdropIView = root.findViewById(R.id.fr_movie_detail_backdrop)
        posterIView = root.findViewById(R.id.rated_item_poster)
        descriptionTView = root.findViewById(R.id.fr_movie_in_details)
        movieOverviewTView = root.findViewById(R.id.fr_movie_detail_overview)

        userReviewIView = root.findViewById(R.id.fr_movie_detail_icon_review)
        titleReviewTView = root.findViewById(R.id.fr_movie_detail_title_review)
        scoreReviewTView = root.findViewById(R.id.fr_movie_detail_review_score)
        subtitleReviewTView = root.findViewById(R.id.fr_movie_detail_review_subtitlte_details)
        detailReviewTView = root.findViewById(R.id.fr_movie_detail_review)
        viewAllReviewsBtn = root.findViewById(R.id.fr_movie_detail_view_all_reviews)

        setupValues(inflater.context)

        return root
    }

    fun setupValues(ctx : Context){

        val idArg = arguments?.getString("id").toString()
        val adultArg = arguments?.getString("adult").toString()
        val backdropPathArg = arguments?.getString("backdropPath").toString()
        val languajeArg = arguments?.getString("originalLanguage").toString()
        val titleOriginalArg = arguments?.getString("originalTitle").toString()
        val overviewArg = arguments?.getString("overview").toString()
        val popularityArg = arguments?.getString("popularity").toString()
        val posterPathArg = arguments?.getString("posterPath").toString()
        val releaseArg = arguments?.getString("releaseDate").toString()
        val titleArg = arguments?.getString("title").toString()
        val videoArg = arguments?.getString("video").toString()
        val voteArg = arguments?.getString("voteAverage").toString()
        val voteCountArg = arguments?.getString("voteCount").toString()
        val ratingArg = arguments?.getString("rating").toString()

        //setGlideImage(ctx, BASE_IMAGE_PATH+backdropPathArg, backdropIView)

        setGlideImage(ctx, BASE_IMAGE_PATH+posterPathArg, posterIView)

        descriptionTView.text =
            Utils.getSpannedText(ctx.getString(R.string.details_movie, titleOriginalArg,  releaseArg, ratingArg, voteArg, languajeArg))

        movieOverviewTView.text =
            Utils.getSpannedText(ctx.getString(R.string.description, overviewArg))

    }
}