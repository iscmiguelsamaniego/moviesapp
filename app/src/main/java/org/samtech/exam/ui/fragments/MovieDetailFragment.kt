package org.samtech.exam.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.samtech.exam.ui.viewmodels.MovieDetailViewModel
import org.samtech.exam.R
import org.samtech.exam.Singleton
import org.samtech.exam.ui.adapters.ReviewsAdapter
import org.samtech.exam.utils.Constants.BASE_IMAGE_PATH
import org.samtech.exam.utils.NetworkUtils.isOnline
import org.samtech.exam.utils.Utils
import org.samtech.exam.utils.Utils.setGlideImage
import java.text.DecimalFormat

class MovieDetailFragment : Fragment() {

    private val opMoviewDetailsViewModel: MovieDetailViewModel by activityViewModels {
        MovieDetailViewModel.MoviesDetailViewModelFactory(
            Singleton.instance!!.reviewsRepository
        )
    }

    private lateinit var backdropIView: ImageView
    private lateinit var posterIView: ImageView
    private lateinit var movieOverviewTView: TextView
    private lateinit var descriptionTView: TextView
    private lateinit var rvReviews: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movie_detail, container, false)
        backdropIView = root.findViewById(R.id.fragment_item_movie_backdrop)
        posterIView = root.findViewById(R.id.rated_item_poster)
        descriptionTView = root.findViewById(R.id.fr_movie_in_details)
        movieOverviewTView = root.findViewById(R.id.fr_movie_detail_overview)
        rvReviews = root.findViewById(R.id.fr_user_reviews_recyclerview)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.tvalue_bar)
        setupValues(inflater.context)
        return root
    }

    fun setupValues(ctx: Context) {

        val idArg = arguments?.getString(getString(R.string.key_id) )
        val adultArg = arguments?.getString(getString(R.string.key_adult)).toString()
        val backdropPathArg = arguments?.getString(getString(R.string.backdrop_path)).toString()
        val languajeArg = arguments?.getString(getString(R.string.orig_languaje)).toString()
        val overviewArg = arguments?.getString(getString(R.string.overview)).toString()
        val popularityArg = arguments?.getString(getString(R.string.popularity)).toString()
        val posterPathArg = arguments?.getString(getString(R.string.posterpath)).toString()
        val releaseArg = arguments?.getString(getString(R.string.releasedate)).toString()
        val titleArg = arguments?.getString(getString(R.string.title)).toString()
        val videoArg = arguments?.getString(getString(R.string.video)).toString()
        val voteArg = arguments?.getString(getString(R.string.votes)).toString()
        val voteCountArg = arguments?.getString(getString(R.string.vote_count))
        val df = DecimalFormat(ctx.getString(R.string.decimal_two_pos))
        val roundedNum = df.format(voteArg.toDouble())

        backdropIView.setImageDrawable(
            AppCompatResources.getDrawable(
                ctx,
                R.drawable.gradient_backdrop
            )
        )

        setGlideImage(ctx, BASE_IMAGE_PATH + backdropPathArg, backdropIView)

        setGlideImage(ctx, BASE_IMAGE_PATH + posterPathArg, posterIView)

        descriptionTView.text =
            Utils.getSpannedText(
                ctx.getString(
                    R.string.details_movie,
                    titleArg,
                    releaseArg,
                    roundedNum,
                    voteCountArg,
                    languajeArg
                )
            )

        movieOverviewTView.text =
            Utils.getSpannedText(ctx.getString(R.string.description, overviewArg))

        if (isOnline(ctx)) {
            var isInsertOp = false
            opMoviewDetailsViewModel.reviewsCount.observe(viewLifecycleOwner) { reviews ->
                reviews?.let {
                    isInsertOp = it.get(0) == 0
                }
            }

            opMoviewDetailsViewModel.downloadReviewValues(ctx, idArg!!)
        }

        setupReviews(idArg)

    }

    private fun setupReviews(paramId: String?) {
        val adapter = ReviewsAdapter()
        rvReviews.adapter = adapter
        rvReviews.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        opMoviewDetailsViewModel.getReviewsBy(paramId!!).observe(viewLifecycleOwner) { reviews ->
            reviews?.let {
                adapter.submitList(it)
            }
        }
    }
}