package com.ensibuuko.android_dev_coding_assigment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ensibuuko.android_dev_coding_assigment.databinding.FragmentEditPostBinding
import com.ensibuuko.android_dev_coding_assigment.features.comments.CommentViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

//This Fragment is For Editing Posts
@AndroidEntryPoint
class EditPostFragment : Fragment() {

    private val viewModel: CommentViewModel by viewModels()
    private var _binding: FragmentEditPostBinding? = null
    private val TAG = "EditPostFragment"

    private val args: EditPostFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditPostBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {


            viewModel.fetchPostById(args.postId)
            viewModel.postLiveData?.observe(requireActivity()) { post ->
                try {
                    postBody.setText(post.body)
                    postTitle.setText(post.title)
                } catch (e: Exception) {
                    Log.d(TAG, "onViewCreated: " + e.message)
                }

            }


            updatePost.setOnClickListener {
                val title = postTitle.text.toString()
                val body = postBody.text.toString()
                addPostProgressBar.visibility = View.VISIBLE
                if (title.isEmpty()){
                    addPostProgressBar.visibility = View.GONE
                    Snackbar.make(view, "Error: Title Field Is Empty", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                }else if (body.isEmpty()){
                    addPostProgressBar.visibility = View.GONE
                    Snackbar.make(view, "Error: Body Field Is Empty", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                }else{
                viewModel.updatePost(title, body, 1, args.postId)
                viewModel.updatePostReply.observe(requireActivity()) { post ->

                    viewModel.fetchPostById(post.id)
                    viewModel.postLiveData?.observe(requireActivity()) {
                        try {
                            Snackbar.make(view, "Post Updated Successfully", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
                            addPostProgressBar.visibility = View.GONE
                            val action = EditPostFragmentDirections.actionEditPostFragmentToSecondFragment(args.postId)
                            findNavController().navigate(action)
                        } catch (e: Exception) {
                            Log.d(TAG, "onViewCreated: " + e.message)
                        }

                    }
                }


        }}
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}