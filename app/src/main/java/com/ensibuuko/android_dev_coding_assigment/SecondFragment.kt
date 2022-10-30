package com.ensibuuko.android_dev_coding_assigment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ensibuuko.android_dev_coding_assigment.data.PostModel
import com.ensibuuko.android_dev_coding_assigment.databinding.FragmentSecondBinding
import com.ensibuuko.android_dev_coding_assigment.features.comments.CommentAdapter
import com.ensibuuko.android_dev_coding_assigment.features.comments.CommentViewModel
import com.ensibuuko.android_dev_coding_assigment.util.Helpers
import com.ensibuuko.android_dev_coding_assigment.util.Helpers.Companion.isNetworkAvailable
import com.ensibuuko.android_dev_coding_assigment.util.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * This is the comments Fragment
 */
@AndroidEntryPoint
class SecondFragment : Fragment() {

    private val TAG = "SecondFragment"

    private val viewModel: CommentViewModel by viewModels()

    private var _binding: FragmentSecondBinding? = null

    private val args: SecondFragmentArgs by navArgs()
    private var myPostModel: PostModel? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val commentsAdapter = CommentAdapter()
        binding.apply {
            //Display Edit and Delete Buttons for User
            editPost.isVisible = args.isUser
            deletePost.isVisible = args.isUser

            commentsRecyclerView.apply {
                adapter = commentsAdapter
                layoutManager = LinearLayoutManager(requireActivity())
            }
            viewModel.fetchComments(args.postId)
            viewModel.commentsLiveData?.observe(requireActivity()) { result ->
                commentsAdapter.submitList(result.data)

                commentProgressBar.isVisible =
                    result is Resource.Loading && result.data.isNullOrEmpty()
                commentError.isVisible = result is Resource.Error && result.data.isNullOrEmpty()
                commentError.text = result.error?.localizedMessage
            }
            viewModel.fetchPostById(args.postId)
            viewModel.postLiveData?.observe(requireActivity()) { post ->
                myPostModel = post
                try {
                    postBody.text = post.body
                    postTitle.text = post.title
                } catch (e: Exception) {
                    Log.d(TAG, "onViewCreated: " + e.message)
                }

            }

            editPost.setOnClickListener {
                //to edit
                val action =
                    SecondFragmentDirections.actionSecondFragmentToEditPostFragment(args.postId)
                findNavController().navigate(action)
            }

            deletePost.setOnClickListener {
                if (isNetworkAvailable(requireContext())) {
                    //to delete
                    commentProgressBar.isVisible = true
                    try {
                        myPostModel?.let { it1 -> viewModel.deletePost(it1) }
                        viewModel.deletePostReply.observe(requireActivity()) {
                            if (it == 1) {
                                Snackbar.make(
                                    view,
                                    "Success: Post Deleted SuccessFully",
                                    Snackbar.LENGTH_LONG
                                ).setAction("Action", null).show()
                                findNavController().popBackStack()
                                commentProgressBar.isVisible = false
                            } else {
                                commentProgressBar.isVisible = false
                                Snackbar.make(view, "Error: Post Not Deleted", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show()
                            }
                        }

                    } catch (e: Exception) {
                        commentProgressBar.isVisible = false
                        Snackbar.make(view, "Error: " + e.message, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }

                }else{
                    Snackbar.make(view, "Error: No Network Connection Detected", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                }

                commentPost.setOnClickListener {
                    //to comment
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}