package com.ensibuuko.android_dev_coding_assigment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ensibuuko.android_dev_coding_assigment.databinding.FragmentSecondBinding
import com.ensibuuko.android_dev_coding_assigment.features.comments.CommentAdapter
import com.ensibuuko.android_dev_coding_assigment.features.comments.CommentViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class SecondFragment : Fragment() {

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

//        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }


        val commentsAdapter = CommentAdapter()
        binding.apply {
            commentsRecyclerView.apply {
                adapter = commentsAdapter
                layoutManager = LinearLayoutManager(requireActivity())
            }
            viewModel.fetchComments(args.postId)
            viewModel.commentsLiveData.observe(requireActivity()){comments ->
                commentsAdapter.submitList(comments)
                commentProgressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}