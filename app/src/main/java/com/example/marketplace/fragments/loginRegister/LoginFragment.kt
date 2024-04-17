package com.example.marketplace.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.Snackbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.marketplace.activity.ShoppingActivity
import com.example.marketplace.dialog.setupBottomSheetDialog
import com.example.marketplace.util.Resource
import com.example.marketplace.viewmodel.LoginViewModel

import com.example.newapptester1.R
import com.example.newapptester1.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUpButton.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.apply {

            binding.LoginButton1.setOnClickListener {
                val email = loginEmail.text.toString()
                val password = passwordLogin.text.toString()
                viewModel.login(email, password)
            }

        }
        binding.tvForgotPassword.setOnClickListener {
            setupBottomSheetDialog { email->
                viewModel.resetPassword(email)

            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collect{
                when (it){
                    is Resource.Loading->{

                    }
                    is Resource.Success->{
                        Snackbar.make(requireView(), "Ссылка сброса пароля было отправлено на вашу почту",Snackbar.LENGTH_LONG).show()

                    }
                    is Resource.Error->{
                        Snackbar.make(requireView(),"Ошибка: ${it.message}", Snackbar.LENGTH_LONG).show()
                    }
                    else->Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.LoginButton1.startAnimation()
                    }
                    is Resource.Success->{
                        binding.LoginButton1.revertAnimation()
                        Intent(requireActivity(),ShoppingActivity::class.java).also{intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)

                        }

                    }
                    is Resource.Error->{
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                        binding.LoginButton1.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }
    }
}