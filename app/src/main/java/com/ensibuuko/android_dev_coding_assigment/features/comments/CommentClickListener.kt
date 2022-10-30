package com.ensibuuko.android_dev_coding_assigment.features.comments

import android.view.View
import com.ensibuuko.android_dev_coding_assigment.data.CommentModel

interface CommentClickListener {
    fun onEditClick( view: View, comment : CommentModel)
    fun onDeleteClick( comment: CommentModel)
}