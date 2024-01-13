package com.kanatandroider.atmosphere.presentation.onboarding.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.kanatandroider.atmosphere.R
import com.kanatandroider.atmosphere.presentation.activities.MainActivity
import com.kanatandroider.atmosphere.presentation.activities.UserLocationActivity
import com.kanatandroider.atmosphere.presentation.utils.SharedPreferencesManager


class ThirdScreen : Fragment() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPreferencesManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third_screen, container, false)

        view.findViewById<TextView>(R.id.finishButton).setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)



//            val navController = findNavController()
//            navController.popBackStack()
        }

        return view
    }




}