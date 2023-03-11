package com.example.newsapp.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentLoginBinding
import com.example.newsapp.util.Constants.Companion.KEY_FIRST_TIME_TOGGLE
import com.example.newsapp.util.Constants.Companion.KEY_PASSWORD
import com.example.newsapp.util.Constants.Companion.KEY_USERNAME
import com.google.android.material.snackbar.Snackbar


class LoginFragment : Fragment(R.layout.fragment_login) {
private lateinit var sharedPref:SharedPreferences
 private var isFirstAppOpen=true
    lateinit var binding:FragmentLoginBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        sharedPref = requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        isFirstAppOpen = sharedPref.getBoolean(KEY_FIRST_TIME_TOGGLE, true)
        if(!isFirstAppOpen){
            val navOptions= NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment,true)
                .build()
            findNavController().navigate(R.id.action_loginFragment_to_breakingNewsFragment,savedInstanceState,navOptions)
        }

        binding.btnLogin.setOnClickListener {
            val success=writePersonalDataToSharedPref()
            if (success) {
                findNavController().navigate(R.id.action_loginFragment_to_breakingNewsFragment)
            }else{
                Snackbar.make(requireView(),"Please check your username & password,they should be max 6 symbols",
                    Snackbar.LENGTH_SHORT).show()
            }
        }



        findNavController().addOnDestinationChangedListener {_, destination, _ ->
            if (destination.id == R.id.breakingNewsFragment) {
                val username = sharedPref.getString(KEY_USERNAME, "") ?: ""
                if (activity != null) {
                    Toast.makeText(activity, "Welcome $username", Toast.LENGTH_LONG).show()
                }
            }
        }

    }
    private fun writePersonalDataToSharedPref():Boolean{
        val username=binding.etUserName.text.toString()
        val password=binding.etPassword.text.toString()
        if(username.isEmpty()||password.isEmpty())
        {
            return false
        }
        if (username.length>6 || password.length>6)
        {
            return false
        }

        sharedPref.edit()
            .putString(KEY_USERNAME,username)
            .putString(KEY_PASSWORD,password)
            .putBoolean(KEY_FIRST_TIME_TOGGLE,false)
            .apply()

        return true
    }

}