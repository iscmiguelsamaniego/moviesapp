package org.samtech.exam.ui.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.samtech.exam.R
import org.samtech.exam.database.entities.Results
import org.samtech.exam.utils.Constants.BASE_IMAGE_PATH
import org.samtech.exam.utils.Utils
import org.samtech.exam.utils.Utils.customToast
import org.samtech.exam.utils.Utils.mayTapButton
import org.samtech.exam.utils.Utils.setGlideImage


class RatedAdapter() : ListAdapter<Results, RatedAdapter.UserViewHolder>(UserComparator()) {
    var onDetailClick: ((Results) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))

        holder.cardContainer.setOnClickListener {
            onDetailClick?.invoke(getItem(position))
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardContainer: CardView = itemView.findViewById(R.id.rated_item_card_contianer)
        private val posterImageView: ImageView = itemView.findViewById(R.id.rated_item_poster)
        private val ratedProgress: ProgressBar = itemView.findViewById(R.id.rated_item_progress)
        private val ratedTView: TextView = itemView.findViewById(R.id.rated_item_tv)
        private val titleMovieTView: TextView = itemView.findViewById(R.id.user_item_title)
        private val releaseDateTView: TextView = itemView.findViewById(R.id.user_item_release_date)

        fun bind(results: Results) {
            setGlideImage(
                itemView.context,
                BASE_IMAGE_PATH + results.posterPath,
                posterImageView
            )

            ratedTView.text = results.rating.toString()
            titleMovieTView.text = results.title
            releaseDateTView.text =
                Utils.getSpannedText(
                    itemView.context.getString(
                        R.string.date_releasing,
                        results.releaseDate
                    )
                )

            ratedTView.text = results.rating.toString()

            val rating = results.rating!!

            val resource =
                if (rating >= 7)
                R.drawable.custom_progress_green else R.drawable.custom_progress_orange

            setProgressDrawable(itemView.context,
                resource,
                ratedProgress)
            ratedProgress.progress = rating.times(10)

        }


        fun setProgressDrawable(context : Context, paramSrc : Int, progress : ProgressBar){
            progress.progressDrawable = ContextCompat.getDrawable(context, paramSrc)
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