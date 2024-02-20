package com.example.marketplace.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.marketplace.activity.ShoppingActivity
import com.example.marketplace.viewmodel.IntroductionViewModel
import com.example.marketplace.viewmodel.IntroductionViewModel.Companion.ACCOUNT_OPTION_FRAGMENT
import com.example.marketplace.viewmodel.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import com.example.newapptester1.R
import com.example.newapptester1.databinding.FragmentIntroductionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroductionFragment : Fragment(R.layout.fragment_introduction) {
    private lateinit var binding: FragmentIntroductionBinding
    private val viewModel by viewModels<IntroductionViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        lifecycleScope.launchWhenStarted {
//            viewModel.checkUserAndNavigate()
//            viewModel.navigate.collect {
//                when (it) {
//                    SHOPPING_ACTIVITY -> {
//                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                            startActivity(intent)
//                        }
//                    }
//                    ACCOUNT_OPTION_FRAGMENT -> {
//                        findNavController().navigate(it)
//                    }
//                    else -> Unit
//                }
//            }
//        }
        binding.startButton.setOnClickListener {

            findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)
        }
    }

}