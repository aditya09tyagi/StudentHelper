package com.example.projectsetup.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.projectsetup.ui.base.BaseActivity

class SplashActivity : BaseActivity() {

    companion object{
        fun newIntent(context: Context):Intent{
            val intent = Intent(context,SplashActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    private fun inject(){
//        val component = DaggerS
    }
}