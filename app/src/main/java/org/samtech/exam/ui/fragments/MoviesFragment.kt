package org.samtech.exam.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.samtech.exam.R
import org.samtech.exam.Singleton
import org.samtech.exam.ui.adapters.MoviesAdapter
import org.samtech.exam.ui.viewmodels.MoviesViewModel
import org.samtech.exam.utils.Constants.BEST_RATED_PATH
import org.samtech.exam.utils.Constants.BEST_RECOMMENDED_PATH
import org.samtech.exam.utils.Constants.POPULAR
import org.samtech.exam.utils.Constants.POPULAR_PATH
import org.samtech.exam.utils.Constants.RATED
import org.samtech.exam.utils.Constants.RECOMMENDED

class MoviesFragment : Fragment() {

    private val opMoviesViewModel: MoviesViewModel by activityViewModels {
        MoviesViewModel.MoviesModelFactory(
            Singleton.instance!!.moviesRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_movies, container, false)

        val titlePopular: TextView = root.findViewById(R.id.fr_movies_popular_tview)
        val titleRated: TextView = root.findViewById(R.id.fr_movies_rated_tview)
        val titleRecommended: TextView = root.findViewById(R.id.fr_movies_recommended_tview)
        val rvMovies: RecyclerView = root.findViewById(R.id.fr_movies_popular_rv)
        val rvRated: RecyclerView = root.findViewById(R.id.fr_movies_rated_rv)
        val rvRecommended: RecyclerView = root.findViewById(R.id.fr_movies_recommended_rv)
        val ctx = inflater.context

        opMoviesViewModel.downloadMovieBy(ctx, POPULAR_PATH, POPULAR)
        opMoviesViewModel.downloadMovieBy(ctx, BEST_RATED_PATH, RATED)
        opMoviesViewModel.downloadMovieBy(ctx, BEST_RECOMMENDED_PATH, RECOMMENDED)

        titlePopular.text = POPULAR
        titleRated.text = RATED
        titleRecommended.text = RECOMMENDED

        populateList(rvMovies, POPULAR)
        populateList(rvRated, RATED)
        populateList(rvRecommended, RECOMMENDED)

        return root
    }

    private fun populateList(paramRv: RecyclerView, paramType : String) {

        val adapter = MoviesAdapter()
        paramRv.adapter = adapter

        paramRv.layoutManager =
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )

        when (paramType) {
            POPULAR ->
                opMoviesViewModel.allPopularMovies.observe(viewLifecycleOwner) { popular ->
                    popular.let {
                        popular?.let {
                            adapter.submitList(it)
                        }
                    }
                }

            RATED ->
                opMoviesViewModel.allBestRatedMovies.observe(viewLifecycleOwner) { popular ->
                    popular.let {
                        popular?.let {
                            adapter.submitList(it)
                        }
                    }
                }

            RECOMMENDED ->
                opMoviesViewModel.allBestRecommendedMovies.observe(viewLifecycleOwner) { popular ->
                    popular.let {
                        popular?.let {
                            adapter.submitList(it)
                        }
                    }
                }
        }

        adapter.onDetailClick = {movies ->
            val bundle = bundleOf(
                getString(R.string.key_id) to movies.id.toString(),
                getString(R.string.key_adult) to movies.adult,
                getString(R.string.backdrop_path) to movies.backdropPath,
                getString(R.string.orig_languaje) to movies.originalLanguage,
                getString(R.string.orig_title) to movies.originalTitle,
                getString(R.string.overview) to movies.overview,
                getString(R.string.popularity) to movies.popularity,
                getString(R.string.posterpath) to movies.posterPath,
                getString(R.string.releasedate) to movies.releaseDate,
                getString(R.string.title) to movies.title,
                getString(R.string.video) to movies.video,
                getString(R.string.votes) to movies.voteAverage.toString(),
                getString(R.string.vote_count) to movies.voteCount.toString()
            )
            view?.findNavController()?.navigate(R.id.action_open_movie_detail_from_movies, bundle)
        }
    }
}