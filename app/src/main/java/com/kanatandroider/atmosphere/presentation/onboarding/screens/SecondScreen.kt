package com.kanatandroider.atmosphere.presentation.onboarding.screens

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.kanatandroider.atmosphere.R
import com.kanatandroider.atmosphere.presentation.activities.LocationActivity


class SecondScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_second_screen, container, false)

        val viewPager =  activity?.findViewById<ViewPager2>(R.id.viewPager)

        view.findViewById<TextView>(R.id.nextButtonSecondScreen).setOnClickListener {
//            viewPager?.currentItem = 2
            val intent = Intent(activity, LocationActivity::class.java)
            startActivity(intent)
        }

        return view
    }


}