package com.jeremyrempel.android.matcher.features.search.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeremyrempel.android.matcher.R
import com.jeremyrempel.android.matcher.features.search.UserCard

class UserCardListAdapter(private val callback: (UserCard) -> Unit) :
    ListAdapter<UserCard, UserCardListAdapter.MyViewHolder>(
        TaskDiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.row_user_card, parent, false
        )

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class MyViewHolder(private val view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        private val usernameView: TextView = view.findViewById(R.id.txt_username)
        private val ageLocationView: TextView = view.findViewById(R.id.txt_agelocation)
        private val matchView: TextView = view.findViewById(R.id.txt_match)
        private val profileImageView: ImageView = view.findViewById(R.id.img_photo)

        init {
            view.setOnClickListener(this)
        }

        fun bind(row: UserCard) {
            usernameView.text = row.name
            ageLocationView.text = row.ageLocation
            matchView.text = view.context.getString(R.string.match, row.matchPercentage)

            Glide
                .with(view)
                .load(row.image)
                .centerCrop()
                .error(R.drawable.ic_error_outline_24dp)
                .into(profileImageView)

            val cardBgColor = if (row.isSelected) R.color.cardSelectedBg else R.color.cardUnselectedBg
            (view as CardView).setCardBackgroundColor(
                ContextCompat.getColor(
                    view.context,
                    cardBgColor
                )
            )
        }

        override fun onClick(v: View?) = callback(getItem(adapterPosition))
    }

    companion object TaskDiffCallback : DiffUtil.ItemCallback<UserCard>() {
        override fun areItemsTheSame(
            oldItem: UserCard,
            newItem: UserCard
        ) = oldItem.name == newItem.name

        override fun areContentsTheSame(
            oldItem: UserCard,
            newItem: UserCard
        ) = oldItem.name == newItem.name && oldItem.isSelected == newItem.isSelected
    }
}
