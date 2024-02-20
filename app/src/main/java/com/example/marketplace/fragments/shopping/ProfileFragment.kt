package com.example.marketplace.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.newapptester1.R
import com.example.newapptester1.databinding.FragmentProfileBinding

class ProfileFragment:Fragment(R.layout.fragment_profile) {
    private lateinit var binding:FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        binding.addProdButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_productAdderFragment)
        }

        return binding.root

    }
}