package com.ensibuuko.android_dev_coding_assigment.features.posts


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ensibuuko.android_dev_coding_assigment.data.PostModel
import com.ensibuuko.android_dev_coding_assigment.databinding.ItemPostBinding

class PostAdapter(val postClickListener: PostClickListener) : ListAdapter<PostModel, PostAdapter.PostViewHolder>(PostComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, postClickListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = getItem(position)
        if(currentItem != null){
            holder.bind(currentItem)
        }
    }

    class PostViewHolder(private val binding: ItemPostBinding, private val postClickListener: PostClickListener) : RecyclerView.ViewHolder(binding.root){
        fun bind(postModel: PostModel){
            binding.apply {
                itemPostBody.text = postModel.body
                itemPostTitle.text = postModel.title
                postComment.setOnClickListener {
                    postClickListener.onPostClick(it, postModel)
                }
                itemViewProfile.setOnClickListener {
                    postClickListener.onViewProfileClick(it, postModel)
                }
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