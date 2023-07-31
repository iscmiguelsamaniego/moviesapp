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
import org.samtech.exam.database.entities.Movies
import org.samtech.exam.utils.Constants.BASE_IMAGE_PATH
import org.samtech.exam.utils.Utils
import org.samtech.exam.utils.Utils.setGlideImage
import java.text.DecimalFormat


class MoviesAdapter : ListAdapter<Movies, MoviesAdapter.UserViewHolder>(UserComparator()) {
    var onDetailClick: ((Movies) -> Unit)? = null

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

        fun bind(movies: Movies) {
            setGlideImage(
                itemView.context,
                BASE_IMAGE_PATH + movies.posterPath,
                posterImageView
            )

            ratedTView.text = movies.voteAverage.toString()
            titleMovieTView.text = movies.title
            releaseDateTView.text =
                Utils.getSpannedText(
                    itemView.context.getString(
                        R.string.date_releasing,
                        movies.releaseDate
                    )
                )

            val df = DecimalFormat(itemView.context.getString(R.string.decimal_two_pos))
            val roundedNum = df.format(movies.voteAverage)
            ratedTView.text = roundedNum

            if (movies.voteAverage != null) {
                val voteAverage = movies.voteAverage!!

                val resource =
                    if (voteAverage >= 7)
                        R.drawable.custom_progress_green else R.drawable.custom_progress_orange

                setProgressDrawable(
                    itemView.context,
                    resource,
                    ratedProgress
                )

                ratedProgress.progress = voteAverage.toInt()
            }

        }


        fun setProgressDrawable(context: Context, paramSrc: Int, progress: ProgressBar) {
            progress.progressDrawable = ContextCompat.getDrawable(context, paramSrc)
        }

        companion object {
            fun create(parent: ViewGroup): UserViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.movies_item, parent, false)
                return UserViewHolder(view)
            }
        }
    }

    class UserComparator : DiffUtil.ItemCallback<Movies>() {
        override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            return oldItem.id == newItem.id
        }
    }
}