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
import com.ensibuuko.android_dev_coding_assigment.databinding.FragmentSecondBinding
import com.ensibuuko.android_dev_coding_assigment.features.comments.CommentAdapter
import com.ensibuuko.android_dev_coding_assigment.features.comments.CommentViewModel
import com.ensibuuko.android_dev_coding_assigment.util.Resource
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class SecondFragment : Fragment() {

    private val TAG = "SecondFragment"

    private val viewModel : CommentViewModel by viewModels()

    private var _binding: FragmentSecondBinding? = null

    private val args : SecondFragmentArgs by navArgs()

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
            commentsRecyclerView.apply {
                adapter = commentsAdapter
                layoutManager = LinearLayoutManager(requireActivity())
            }
            viewModel.fetchComments(args.postId)
            viewModel.commentsLiveData?.observe(requireActivity()){result ->
                commentsAdapter.submitList(result.data)

                commentProgressBar.isVisible = result is Resource.Loading && result.data.isNullOrEmpty()
                commentError.isVisible = result is Resource.Error && result.data.isNullOrEmpty()
                commentError.text = result.error?.localizedMessage
            }
            viewModel.fetchPostById(args.postId)
            viewModel.postLiveData?.observe(requireActivity()){post ->
                try{
                    postBody.text = post.body
                    postTitle.text = post.title
                }catch (e : Exception){
                    Log.d(TAG, "onViewCreated: "+ e.message)
                }

            }

            editPost.setOnClickListener {
                //to edit
                val action = SecondFragmentDirections.actionSecondFragmentToEditPostFragment(args.postId)
                findNavController().navigate(action)
            }

            deletePost.setOnClickListener {
                //to delete
            }

            commentPost.setOnClickListener {
              //to comment
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}