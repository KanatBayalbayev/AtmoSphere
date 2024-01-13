package com.kanatandroider.atmosphere.presentation.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.kanatandroider.atmosphere.R
import com.kanatandroider.atmosphere.presentation.onboarding.screens.FirstScreen
import com.kanatandroider.atmosphere.presentation.onboarding.screens.SecondScreen
import com.kanatandroider.atmosphere.presentation.onboarding.screens.ThirdScreen


class ViewPagerFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )

        val viewPagerAdapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        view.findViewById<ViewPager2>(R.id.viewPager).adapter = viewPagerAdapter

        return view
    }

}