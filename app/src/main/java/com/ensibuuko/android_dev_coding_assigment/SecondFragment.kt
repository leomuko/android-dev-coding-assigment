package com.ensibuuko.android_dev_coding_assigment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ensibuuko.android_dev_coding_assigment.data.CommentModel
import com.ensibuuko.android_dev_coding_assigment.data.PostModel
import com.ensibuuko.android_dev_coding_assigment.databinding.FragmentSecondBinding
import com.ensibuuko.android_dev_coding_assigment.features.comments.CommentAdapter
import com.ensibuuko.android_dev_coding_assigment.features.comments.CommentClickListener
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
class SecondFragment : Fragment(), CommentClickListener {

    private val TAG = "SecondFragment"

    private val viewModel: CommentViewModel by viewModels()

    private var _binding: FragmentSecondBinding? = null

    private val args: SecondFragmentArgs by navArgs()
    private var myPostModel: PostModel? = null
    private var numberOfComments: Int = 0;

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

        val commentsAdapter = CommentAdapter(this)
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
                numberOfComments = result.data?.size!!

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

                } else {
                    Snackbar.make(
                        view,
                        "Error: No Network Connection Detected",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("Action", null).show()
                }


            }
            sendCommentBtn.setOnClickListener {
                //to comment
                commentProgressBar.isVisible = true
                if (isNetworkAvailable(requireContext())) {
                    val comment = commentTv.text.toString()
                    if (comment.isNullOrEmpty()) {
                        commentProgressBar.isVisible = false
                        Snackbar.make(view, "Comment Field Is Null", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    } else {
                        //Make Comment model with generic id, names and email
                        val commentModel = CommentModel(
                            args.postId,
                            (numberOfComments + 1),
                            "Local User",
                            "localuser@email.com",
                            comment
                        )
                        viewModel.insertCommentInDb(commentModel)
                        viewModel.saveCommentReply.observe(requireActivity()) {

                            if (it > 0) {
                                commentProgressBar.isVisible = false
                                Snackbar.make(
                                    view,
                                    "Success: Comment Saved",
                                    Snackbar.LENGTH_LONG
                                ).setAction("Action", null).show()
                                commentTv.setText("")
                            } else {
                                commentProgressBar.isVisible = false
                                Snackbar.make(
                                    view,
                                    "Error: " + it,
                                    Snackbar.LENGTH_LONG
                                ).setAction("Action", null).show()
                            }
                        }
                    }

                } else {
                    Snackbar.make(
                        view,
                        "Error: No Network Connection Detected",
                        Snackbar.LENGTH_LONG
                    ).setAction("Action", null).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEditClick(view: View, comment: CommentModel) {
        //to edit

        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.commentEditText)

        with(builder) {
            setTitle("Edit Comment")
            setPositiveButton("OK") { dialog, which ->
                val name = editText.text.toString()
                binding.apply {
                    if (isNetworkAvailable(requireContext())) {

                        if (myPostModel?.userId != 1) {
                            commentProgressBar.isVisible = false
                            Snackbar.make(
                                view,
                                "Info: User Can Only Edit Personal Comments",
                                Snackbar.LENGTH_LONG
                            )
                                .setAction("Action", null).show()
                        } else {
                            viewModel.insertCommentInDb(comment)
                            viewModel.saveCommentReply.observe(requireActivity()) {

                                if (it > 0) {
                                    commentProgressBar.isVisible = false
                                    Snackbar.make(
                                        view,
                                        "Success: Comment Updated",
                                        Snackbar.LENGTH_LONG
                                    ).setAction("Action", null).show()
                                    commentTv.setText("")
                                } else {
                                    commentProgressBar.isVisible = false
                                    Snackbar.make(
                                        view,
                                        "Error: " + it,
                                        Snackbar.LENGTH_LONG
                                    ).setAction("Action", null).show()
                                }
                            }
                        }

                    } else {
                        Snackbar.make(
                            view,
                            "Error: No Network Connection Detected",
                            Snackbar.LENGTH_LONG
                        ).setAction("Action", null).show()
                    }
                }
            }
            setNegativeButton("Cancel") { dialog, which ->

            }
            setView(dialogLayout)
            show()
        }

    }

    override fun onDeleteClick(comment: CommentModel) {
        //to delete
        viewModel.deleteComment(comment)
        viewModel.deleteCommentReply.observe(requireActivity()){
            if (it == 1){
                Snackbar.make(
                    requireView(),
                    "Comment Deleted Successfully",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null).show()
            }
        }
    }

}