package com.ensibuuko.android_dev_coding_assigment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ensibuuko.android_dev_coding_assigment.data.PostModel
import com.ensibuuko.android_dev_coding_assigment.databinding.FragmentFirstBinding
import com.ensibuuko.android_dev_coding_assigment.features.posts.PostAdapter
import com.ensibuuko.android_dev_coding_assigment.features.posts.PostClickListener
import com.ensibuuko.android_dev_coding_assigment.features.posts.PostsViewModel
import com.ensibuuko.android_dev_coding_assigment.util.Resource
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class FirstFragment : Fragment(), PostClickListener {

    private val viewModel : PostsViewModel by viewModels()

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val postAdapter = PostAdapter(this)

        binding.apply {
            postRecycler.apply {
                adapter = postAdapter
                layoutManager = LinearLayoutManager(requireActivity())

            }

            viewModel.posts.observe(requireActivity()){result->
                postAdapter.submitList(result.data)
                postProgressBar.isVisible = result is Resource.Loading && result.data.isNullOrEmpty()
                postErrorText.isVisible = result is Resource.Error && result.data.isNullOrEmpty()
                postErrorText.text = result.error?.localizedMessage
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPostClick(view: View, post: PostModel) {
        if(post.userId == 1){
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(post.id, true)
            findNavController().navigate(action)
        }else{
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(post.id, false)
            findNavController().navigate(action)
        }

    }

    override fun onViewProfileClick(view: View, post: PostModel) {
        val action = FirstFragmentDirections.actionFirstFragmentToNavProfile(post.userId)
        findNavController().navigate(action)
    }
}