package org.samtech.exam.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import org.samtech.exam.R
import org.samtech.exam.Singleton

class UserFragment : Fragment() {

    private val opUserViewModel: UserViewModel by activityViewModels {
        UserViewModel.UserViewModelFactory(
            Singleton.instance!!.firebaseUserRepository
        )
    }

    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_user, container, false)
        recyclerView = root.findViewById(R.id.fr_user_recyclerview)
        val userIView: ImageView = root.findViewById(R.id.fr_user_image)
        val userInfoTView: TextView = root.findViewById(R.id.fr_user_info_tview)
        //opUserViewModel.downloadAndStoreUser()

        opUserViewModel.getUserFSValues().observe(viewLifecycleOwner) { user ->
            user.let {
                if (it.fsDocumentId == null) {
                    opUserViewModel.downloadAndStoreOrUpdateUser("")
                } else {
                    opUserViewModel.downloadAndStoreOrUpdateUser(it.fsDocumentId!!)
                }
            userInfoTView.text = it.username
                userIView
            //userIView.
        }
    }

    return root
}

}