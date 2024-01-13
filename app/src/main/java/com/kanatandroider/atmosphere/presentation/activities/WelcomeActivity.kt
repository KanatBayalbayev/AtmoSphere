package com.kanatandroider.atmosphere.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kanatandroider.atmosphere.R

class WelcomeActivity : AppCompatActivity() {



    private val component by lazy {
        (application as MyApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)
    }
}