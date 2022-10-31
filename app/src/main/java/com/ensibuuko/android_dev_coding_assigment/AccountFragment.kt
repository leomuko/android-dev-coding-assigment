package com.ensibuuko.android_dev_coding_assigment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ensibuuko.android_dev_coding_assigment.databinding.FragmentAccountBinding
import com.ensibuuko.android_dev_coding_assigment.features.account.AccountViewModel
import com.ensibuuko.android_dev_coding_assigment.util.Helpers
import com.ensibuuko.android_dev_coding_assigment.util.Helpers.Companion.isNetworkAvailable
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private val viewModel : AccountViewModel by viewModels()

    private var _binding : FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args : AccountFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            if (isNetworkAvailable(requireContext())) {
            viewModel.getUserData(args.userId)
            viewModel.userLiveData.observe(requireActivity()){user->
                accountName.text = user.name
                accountCity.text = user.address.city
                accountEmail.text = user.email
                accountPhone.text = user.phone
                accountStreet.text = user.address.street
                accountUsername.text = user.username
            }
        }else{
                Snackbar.make(
                    view,
                    "Error: No Network Detected",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null).show()
        }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}