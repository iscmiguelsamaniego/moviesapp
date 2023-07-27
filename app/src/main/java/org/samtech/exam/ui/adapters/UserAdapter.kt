package org.samtech.exam.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.samtech.exam.R
import org.samtech.exam.network.pokos.UserPoko


class UserAdapter() :
    ListAdapter<UserPoko, UserAdapter.UserViewHolder>(UserComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: UserPoko) {

        }

        companion object {
            fun create(parent: ViewGroup): UserViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.user_item, parent, false)
                return UserViewHolder(view)
            }
        }
    }

    class UserComparator : DiffUtil.ItemCallback<UserPoko>() {
        override fun areItemsTheSame(oldItem: UserPoko, newItem: UserPoko): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: UserPoko, newItem: UserPoko): Boolean {
            return oldItem.id == newItem.id
        }
    }
}