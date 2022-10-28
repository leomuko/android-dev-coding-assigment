package com.ensibuuko.android_dev_coding_assigment.features.posts


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ensibuuko.android_dev_coding_assigment.data.PostModel
import com.ensibuuko.android_dev_coding_assigment.databinding.ItemPostBinding

class PostAdapter : ListAdapter<PostModel, PostAdapter.PostViewHolder>(PostComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = getItem(position)
        if(currentItem != null){
            holder.bind(currentItem)
        }
    }

    class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(postModel: PostModel){
            binding.apply {
                itemPostBody.text = postModel.body
                itemPostTitle.text = postModel.title
            }
        }
    }

    class PostComparator : DiffUtil.ItemCallback<PostModel>(){
        override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
            if (oldItem.body == newItem.body && oldItem.title == newItem.title){
                return true
            }

            return false
        }

        override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
            if (oldItem == newItem){
                return true
            }

            return false
        }

    }
}