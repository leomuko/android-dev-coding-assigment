package com.ensibuuko.android_dev_coding_assigment.features.posts

import android.view.View
import com.ensibuuko.android_dev_coding_assigment.data.PostModel

interface PostClickListener {
    fun onPostClick(view: View, post : PostModel)
    fun onViewProfileClick(view: View, post: PostModel)
}