package com.example.marketplace.fragments.loginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.newapptester1.R
import com.example.newapptester1.databinding.ActivityShoppingBinding
import com.example.newapptester1.databinding.FragmentAccountOptionsBinding
import com.example.newapptester1.databinding.FragmentIntroductionBinding

class AccountOptionsFragment: Fragment(R.layout.fragment_account_options) {
    private lateinit var binding: FragmentAccountOptionsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountOptionsBinding.inflate(inflater)
        binding.buttonRegisterAccountOption.setOnClickListener{
            findNavController().navigate(R.id.action_accountOptionsFragment_to_registerFragment)
        }
        binding.buttonLoginAccountOption.setOnClickListener{
            findNavController().navigate(R.id.action_accountOptionsFragment_to_loginFragment)
        }
        return binding.root
    }
}