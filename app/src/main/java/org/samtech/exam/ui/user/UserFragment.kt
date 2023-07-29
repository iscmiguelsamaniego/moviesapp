package org.samtech.exam.ui.user

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
import org.samtech.exam.ui.adapters.RatedAdapter
import org.samtech.exam.utils.Constants.BASE_IMAGE_PATH
import org.samtech.exam.utils.NetworkUtils.isOnline
import org.samtech.exam.utils.Utils.customToast
import org.samtech.exam.utils.Utils.getSpannedText
import org.samtech.exam.utils.Utils.setGlideImage


class UserFragment : Fragment() {

    private val opUserViewModel: UserViewModel by activityViewModels {
        UserViewModel.UserViewModelFactory(
            Singleton.instance!!.usersRepository,
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

        if(isOnline(context)) {
            opUserViewModel.downloadUserValues()
            opUserViewModel.downloadRatedValues()
        }else{
            customToast(requireContext(), getString(R.string.no_internet))
        }

        setUpUserDetails(inflater.context)
        populateRatedList()
        return root
    }

    private fun setUpUserDetails(ctx : Context) {
        opUserViewModel.allUsers.observe(viewLifecycleOwner){users ->
            users.let{

                var urlAvatar = ""
                var adultValue : Boolean? = false
                var nameValue = ""
                var userNameValue = ""
                var idValue = ""
                var iso31661Value = ""
                var iso6391Value = ""

                for (user in it){ //TODO FIX
                    urlAvatar = BASE_IMAGE_PATH+ user.avatarPath
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
                    if(nameValue.isNullOrBlank())
                        getString(R.string.no_registered) else nameValue

                val profileUser = getSpannedText(getString(R.string.user_values,
                    userNameValue,
                    idValue,
                    iso31661Value,
                    iso6391Value,
                    nameRespose,
                    adultResponse))
                userInfoTView.text = profileUser
            }
        }

    }

    private fun populateRatedList() {
        val adapter = RatedAdapter()
        rvBestRated.adapter = adapter
        //rvBestRated.layoutManager =
          //  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvBestRated.layoutManager = GridLayoutManager(context, 2)

        opUserViewModel.allResults.observe(viewLifecycleOwner) { results ->
            results?.let {
                adapter.submitList(it)
            }
        }

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