package com.ensibuuko.android_dev_coding_assigment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ensibuuko.android_dev_coding_assigment.databinding.FragmentAddPostBinding
import com.ensibuuko.android_dev_coding_assigment.features.posts.PostsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPostFragment : Fragment() {

    private  var _binding : FragmentAddPostBinding? = null
    private val viewModel : PostsViewModel by viewModels()
    private val TAG = "AddPostFragment"

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            postButton.setOnClickListener {
                addPostProgressBar.visibility = View.VISIBLE
                val body = postBody.text.toString()
                val title = postTitle.text.toString()

                if (title.isEmpty()){
                    addPostProgressBar.visibility = View.GONE
                    Snackbar.make(view, "Error: Title Field Is Empty", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                }else if (body.isEmpty()){
                    addPostProgressBar.visibility = View.GONE
                    Snackbar.make(view, "Error: Body Field Is Empty", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                }else{
                    //do post
                    //make posts to user with id 1
                    viewModel.makePost(title, body, 1)

                    viewModel.makePostReply.observe(requireActivity()){
                        Log.d(TAG, "onViewCreated: "+ it.body + " " + it.title)

                        viewModel.fetchPostById(it.id)
                        viewModel.postReply?.observe(requireActivity()){post ->
                            try{
                                Snackbar.make(view, "Post Made Successfully", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                                addPostProgressBar.visibility = View.GONE
                                val action = AddPostFragmentDirections.actionAddPostFragmentToNavPosts()
                                findNavController().navigate(action)
                            }catch (e : Exception){
                                Log.d(TAG, "onViewCreated: "+ e.message)
                            }

                        }

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}