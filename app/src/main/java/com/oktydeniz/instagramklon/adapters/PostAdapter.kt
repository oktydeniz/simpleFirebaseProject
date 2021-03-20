package com.oktydeniz.instagramklon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oktydeniz.instagramklon.models.PostModel

import com.oktydeniz.instagramklon.databinding.RowFeedBinding


class PostAdapter(private var modelList: ArrayList<PostModel>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = modelList[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    class ViewHolder private constructor(private val binding: RowFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostModel) {
            binding.posts = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowFeedBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}
