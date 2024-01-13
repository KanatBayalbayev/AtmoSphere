package com.kanatandroider.atmosphere.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kanatandroider.atmosphere.R
import com.kanatandroider.atmosphere.presentation.activities.CurrentWeatherActivity
import com.kanatandroider.atmosphere.presentation.activities.MainActivity
import com.kanatandroider.atmosphere.presentation.utils.SharedPreferencesManager


class SplashFragment : Fragment() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPreferencesManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Handler().postDelayed({
            if (sharedPreferencesManager.getFinishedViewPagerContainerState(
                    "FinishedViewPager",
                    false
                )
            ) {
                val intent = Intent(activity, CurrentWeatherActivity::class.java)
                startActivity(intent)
//                findNavController().navigate(R.id.action_splashFragment_to_mainActivity2)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
            }

        }, 3000)

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }


}