package com.kanatandroider.atmosphere.presentation.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kanatandroider.atmosphere.R
import com.kanatandroider.atmosphere.presentation.utils.SharedPreferencesManager

class WelcomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)
    }
}