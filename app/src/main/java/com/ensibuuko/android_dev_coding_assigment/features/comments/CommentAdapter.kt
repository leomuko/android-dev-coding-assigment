package com.ensibuuko.android_dev_coding_assigment.features.comments


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ensibuuko.android_dev_coding_assigment.data.CommentModel
import com.ensibuuko.android_dev_coding_assigment.databinding.ItemCommentBinding

class CommentAdapter(val commentClickListener: CommentClickListener) : ListAdapter<CommentModel, CommentAdapter.CommentViewHolder>(CommentComparator()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding, commentClickListener)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentItem = getItem(position)
        if(currentItem != null){
            holder.bind(currentItem)
        }
    }

    class CommentViewHolder(private val binding: ItemCommentBinding, private val commentClickListener: CommentClickListener) : RecyclerView.ViewHolder(binding.root){
        fun bind(commentModel: CommentModel){
            binding.apply {
                itemCommentBody.text = commentModel.body
                itemCommentEmail.text = commentModel.email
                itemCommentName.text = commentModel.name
                itemEditComment.setOnClickListener {
                    commentClickListener.onEditClick(it, commentModel)
                }
                itemDeleteComment.setOnClickListener {
                    commentClickListener.onDeleteClick( commentModel)
                }
            }
        }
    }

    class CommentComparator : DiffUtil.ItemCallback<CommentModel>(){
        override fun areItemsTheSame(oldItem: CommentModel, newItem: CommentModel): Boolean {
            if(oldItem.body == newItem.body && oldItem.name == newItem.name){
                return true
            }

            return false
        }

        override fun areContentsTheSame(oldItem: CommentModel, newItem: CommentModel): Boolean {
            if (oldItem == newItem){
                return true
            }
            return false
        }

    }

}