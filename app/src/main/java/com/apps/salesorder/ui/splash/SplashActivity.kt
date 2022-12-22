package com.apps.salesorder.ui.splash

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.apps.salesorder.data.constants.Constants
import com.apps.salesorder.data.preferences.Preferences
import com.apps.salesorder.databinding.ActivitySplashBinding
import com.apps.salesorder.ui.auth.LoginActivity
import com.apps.salesorder.ui.auth.UrlActivity
import com.apps.salesorder.ui.home.HomeActivity
import io.sentry.Sentry
import timber.log.Timber

class SplashActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(SplashViewModel::class.java) }
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    lateinit var token: String
    lateinit var url: String
    private val pref by lazy { Preferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Sentry.captureMessage("testing SDK setup")
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        val pref = Preferences(this)
        token = pref.getString(Constants.DEFAULT.TOKEN).toString()
        url = pref.getString(Constants.DEFAULT.BASE_URL).toString()
        setupObserve()
    }

    private fun setupObserve() {
        viewModel.delayCountDown(2000)
        viewModel.splashNotifier.observe(this, Observer { finishSplash ->

            Timber.e("CekToken  : $token")
            Timber.e("Cek-URL  : $url")

            if (url == "null"){
                startActivity(
                    Intent(this, UrlActivity::class.java)
                )
                finish()
            } else if (token == "null") {
                startActivity(
                    Intent(this, LoginActivity::class.java)
                )
                finish()
            } else {
                pref.put(Constants.DEFAULT.LOGIN_NEW, "T")
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()

            }

        })
    }

}