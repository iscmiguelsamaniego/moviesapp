package org.samtech.exam.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.samtech.exam.R
import org.samtech.exam.Singleton
import org.samtech.exam.ui.adapters.MoviesAdapter
import org.samtech.exam.ui.viewmodels.UsersViewModel
import org.samtech.exam.utils.Constants.BASE_IMAGE_PATH
import org.samtech.exam.utils.Constants.PROFILE_PATH
import org.samtech.exam.utils.Constants.RATED_BY_ME
import org.samtech.exam.utils.Constants.RATED_PATH
import org.samtech.exam.utils.NetworkUtils.isOnline
import org.samtech.exam.utils.Utils.customToast
import org.samtech.exam.utils.Utils.getSpannedText
import org.samtech.exam.utils.Utils.setGlideImage


class UsersFragment : Fragment() {

    private val opUsersViewModel: UsersViewModel by activityViewModels {
        UsersViewModel.UserViewModelFactory(
            Singleton.instance!!.usersRepository,
            Singleton.instance!!.moviesRepository
        )
    }

    private lateinit var userInfoTView: TextView
    private lateinit var userIView: ImageView
    private lateinit var rvBestRated: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_user, container, false)
        rvBestRated = root.findViewById(R.id.fr_user_rated_recyclerview)
        userIView = root.findViewById(R.id.fr_user_image)
        userInfoTView = root.findViewById(R.id.fr_user_info_tview)

        if (isOnline(context)) {
            opUsersViewModel.downloadValuesBy(inflater.context, PROFILE_PATH, getString(R.string.no_value))
            opUsersViewModel.downloadValuesBy(inflater.context, RATED_PATH,RATED_BY_ME)
        } else {
            customToast(requireContext(), getString(R.string.no_internet))
        }

        setUpUserDetails(inflater.context)
        populateRatedList()
        return root
    }

    private fun setUpUserDetails(ctx: Context) {
        opUsersViewModel.allUsers.observe(viewLifecycleOwner) { users ->
            users.let {

                var urlAvatar = ""
                var adultValue: Boolean? = false
                var nameValue = ""
                var userNameValue = ""
                var idValue = ""
                var iso31661Value = ""
                var iso6391Value = ""

                for (user in it) { //TODO FIX
                    urlAvatar = BASE_IMAGE_PATH + user.avatarPath
                    adultValue = user.includeAdult
                    nameValue = user.name.toString()
                    userNameValue = user.username.toString()
                    idValue = user.id.toString()
                    iso31661Value = user.iso31661.toString()
                    iso6391Value = user.iso6391.toString()
                }

                setGlideImage(ctx, urlAvatar, userIView)


                val adultResponse =
                    if (adultValue == true)
                        getString(R.string.yes) else getString(R.string.no)

                val nameRespose =
                    if (nameValue.isNullOrBlank())
                        getString(R.string.no_registered) else nameValue

                val profileUser = getSpannedText(
                    getString(
                        R.string.user_values,
                        userNameValue,
                        idValue,
                        iso31661Value,
                        iso6391Value,
                        nameRespose,
                        adultResponse
                    )
                )
                userInfoTView.text = profileUser
            }
        }

    }

    private fun populateRatedList() {
        val adapter = MoviesAdapter()
        rvBestRated.adapter = adapter
        rvBestRated.layoutManager = GridLayoutManager(context, 2)

        opUsersViewModel.allRatedByMeMovies.observe(viewLifecycleOwner) { movies ->
            movies?.let {
                adapter.submitList(it)
            }
        }

        adapter.onDetailClick = { movies ->
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
            view?.findNavController()?.navigate(R.id.action_open_movie_detail, bundle)

        }

    }

}