package org.samtech.exam.ui.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.samtech.exam.R
import org.samtech.exam.Singleton
import org.samtech.exam.ui.adapters.RatedAdapter
import org.samtech.exam.utils.Constants.BASE_IMAGE_PATH
import org.samtech.exam.utils.Utils.getSpannedText
import org.samtech.exam.utils.Utils.setGlideImage

class UserFragment : Fragment() {

    private val opUserViewModel: UserViewModel by activityViewModels {
        UserViewModel.UserViewModelFactory(
            Singleton.instance!!.fireStoreUserRepository,
            Singleton.instance!!.resultsRepository
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

        setUpUserDetails(inflater.context)
        populateRatedList()
        return root
    }

    private fun setUpUserDetails(ctx : Context) {
        opUserViewModel.getUserFSValues().observe(viewLifecycleOwner) { user ->
            user.let {
                if (it.fsDocumentId == null) {
                    opUserViewModel.downloadAndStoreOrUpdateUser("")
                } else {
                    opUserViewModel.downloadAndStoreOrUpdateUser(it.fsDocumentId!!)
                }

                val urlImage = BASE_IMAGE_PATH + user.avatar!!.tmdb.avatarPath!!
                setGlideImage(ctx, urlImage, userIView)

                val adultResponse =
                if (it.includeAdult == true)
                    getString(R.string.yes) else getString(R.string.no)

                val nameRespose =
                    if(it.name.isNullOrBlank())
                        getString(R.string.no_registered) else it.name

                val x = getSpannedText(getString(R.string.user_values,
                    it.username,
                    it.id.toString(),
                    it.iso31661,
                    it.iso6391,
                    nameRespose,
                    adultResponse))
                userInfoTView.text = x
            }
        }

        opUserViewModel.getRatedValues()
    }

    private fun populateRatedList() {
        val adapter = RatedAdapter()
        rvBestRated.adapter = adapter
        rvBestRated.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        opUserViewModel.allResults.observe(viewLifecycleOwner, Observer { results ->
            results?.let{
                adapter.submitList(it)
            }
        })

        adapter.onDetailClick = {results ->
            val bundle = bundleOf(
                "id" to results.id,
                "adult" to results.adult,
                "backdropPath" to results.backdropPath,
                "originalLanguage" to results.originalLanguage,
                "originalTitle" to results.originalTitle,
                "overview" to results.overview,
                "popularity" to results.popularity,
                "posterPath" to results.posterPath,
                "releaseDate" to results.releaseDate,
                "title" to results.title,
                "video" to results.video,
                "voteAverage" to results.voteAverage,
                "voteCount" to results.voteCount,
                "rating" to results.rating)
            view?.findNavController()?.navigate(R.id.action_open_movie_detail, bundle)

        }

    }

}