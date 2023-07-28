package org.samtech.exam.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.samtech.exam.R
import org.samtech.exam.database.entities.Results
import org.samtech.exam.utils.Constants.BASE_IMAGE_PATH
import org.samtech.exam.utils.Utils.mayTapButton
import org.samtech.exam.utils.Utils.setGlideImage


class RatedDetailAdapter() :
    ListAdapter<Results, RatedDetailAdapter.UserViewHolder>(UserComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
/*
        private val backdropImageView: ImageView = itemView.findViewById(R.id.user_item_backdrop)
        private val posterImageView: ImageView = itemView.findViewById(R.id.rated_item_poster)
        private val titleMovieTView: TextView = itemView.findViewById(R.id.user_item_title)
        private val releaseDateTView: TextView = itemView.findViewById(R.id.user_item_release_date)
        private val ratingTView: TextView = itemView.findViewById(R.id.user_item_rating)
        private val votesTView: TextView = itemView.findViewById(R.id.user_item_vote_number)
        private val languajeTView: TextView = itemView.findViewById(R.id.user_item_original_languaje)
        private val overviewTView: TextView = itemView.findViewById(R.id.user_item_description)
        private val userIconImageView: ImageView = itemView.findViewById(R.id.user_item_icon_review) //todo set values
        private val titleReviewTView: TextView = itemView.findViewById(R.id.user_item_title_review) //todo set values
        private val scoreTView: TextView = itemView.findViewById(R.id.user_item_review_score) //Todo maybe not add
        private val detailsTView: TextView = itemView.findViewById(R.id.user_item_review_subtitlte_details)
        private val reviewTView: TextView = itemView.findViewById(R.id.user_item_review)
        private val viewAllBtn: Button = itemView.findViewById(R.id.user_item_view_all_reviews)*/

        fun bind(results: Results) {
            /*setGlideImage(itemView.context,
                BASE_IMAGE_PATH+results.backdropPath,
                backdropImageView)

            setGlideImage(itemView.context,
                BASE_IMAGE_PATH+results.posterPath,
                posterImageView)

            titleMovieTView.text = results.title
            releaseDateTView.text = results.releaseDate
            ratingTView.text = results.rating.toString() //todo add stars or circle design
            votesTView.text = results.voteCount.toString()
            languajeTView.text = results.originalLanguage
            overviewTView.text = results.overview
            viewAllBtn.setOnClickListener {
                //TODO open fragment for reviews details
                if(mayTapButton(System.currentTimeMillis())){

                }
            }*/
        }

        companion object {
            fun create(parent: ViewGroup): UserViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.rated_item, parent, false)
                return UserViewHolder(view)
            }
        }
    }

    class UserComparator : DiffUtil.ItemCallback<Results>() {
        override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem.id == newItem.id
        }
    }
}