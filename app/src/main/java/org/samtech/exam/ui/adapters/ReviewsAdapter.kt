package org.samtech.exam.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.samtech.exam.R
import org.samtech.exam.database.entities.Reviews
import org.samtech.exam.utils.Constants
import org.samtech.exam.utils.Utils

class ReviewsAdapter() :
    ListAdapter<Reviews, ReviewsAdapter.ReviewsViewsHolder>(ReviewsComparator()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewsViewsHolder {
        return ReviewsViewsHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ReviewsViewsHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ReviewsViewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var userReviewIView: ImageView = itemView.findViewById(R.id.fr_movie_detail_icon_review)
        var titleReviewTView: TextView = itemView.findViewById(R.id.fr_movie_detail_title_review)
        var scoreReviewTView: TextView = itemView.findViewById(R.id.fr_movie_detail_review_score)
        var subtitleReviewTView: TextView =
            itemView.findViewById(R.id.fr_movie_detail_review_subtitlte_details)
        var detailReviewTView: TextView = itemView.findViewById(R.id.fr_movie_detail_review)

        fun bind(reviews: Reviews) {


            Utils.setGlideImage(itemView.context,
                Constants.BASE_IMAGE_PATH + reviews.avatar_path, userReviewIView)

            titleReviewTView.text = reviews.username
            scoreReviewTView.text = reviews.rating
            subtitleReviewTView.text = reviews.created_at
            detailReviewTView.text = reviews.content

        }

        companion object {
            fun create(parent: ViewGroup): ReviewsAdapter.ReviewsViewsHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.reviews_item, parent, false)
                return ReviewsAdapter.ReviewsViewsHolder(view)
            }
        }

    }


    class ReviewsComparator : DiffUtil.ItemCallback<Reviews>() {
        override fun areItemsTheSame(oldItem: Reviews, newItem: Reviews): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Reviews, newItem: Reviews): Boolean {
            return oldItem.id == newItem.id
        }
    }
}