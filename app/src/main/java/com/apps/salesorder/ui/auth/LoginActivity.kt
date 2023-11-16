package com.apps.salesorder.ui.auth

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.apps.salesorder.R
import com.apps.salesorder.api.ApiService
import com.apps.salesorder.api.Resource
import com.apps.salesorder.data.constants.Constants
import com.apps.salesorder.data.preferences.Preferences
import com.apps.salesorder.databinding.ActivityLoginBinding
import com.apps.salesorder.helper.LoadingScreen
import com.apps.salesorder.ui.home.HomeActivity
import com.wessam.library.NetworkChecker
import com.wessam.library.NoInternetLayout
import id.rizmaulana.sheenvalidator.lib.SheenValidator
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LoginActivity : AppCompatActivity() {

    private val api by lazy { ApiService.getClient(this) }
    private val pref by lazy { Preferences(this) }
    private lateinit var viewModelFactory : LoginViewModelFactory
    private lateinit var viewModel : LoginViewModel
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var sheenValidator: SheenValidator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (!NetworkChecker.isNetworkConnected(this)){
            NoInternetLayout
                .Builder(this, R.layout.activity_login)

        }

        setupViewModel()
        setupListener()
        setupObserver()

    }

    private fun setupViewModel() {
        viewModelFactory = LoginViewModelFactory( api, this )
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        sheenValidator = SheenValidator(this)
        sheenValidator.setOnValidatorListener {
            viewModel.loginProses(binding.etUsername.text.toString(),binding.etPassword.text.toString())
        }
        sheenValidator.registerAsRequired(binding.etUsername)
        sheenValidator.registerAsRequired(binding.etPassword)

    }

    private fun setupListener() {
        binding.etUsername.setText(pref.getString(Constants.DEFAULT.USERNAME))
        binding.btnLogin.setOnClickListener {
            sheenValidator.validate()

        }
    }

    private fun setupObserver() {
        viewModel.loginResponse.observe(this, Observer {
            when(it){
                is Resource.Loading -> {
                    LoadingScreen.displayLoadingWithText(this,"Silahkan Tunggu..",false)
                }
                is Resource.Success -> {
                    LoadingScreen.hideLoading()
                    Timber.e("TAG ${it.data!!.data}")
                    Toast.makeText(this, "Berhasil login", Toast.LENGTH_SHORT).show()
                    pref.put(Constants.DEFAULT.TOKEN, it.data.data[0].token)
                    pref.put(Constants.DEFAULT.USERNAME,binding.etUsername.text.trim().toString())
                    pref.put(Constants.DEFAULT.NAMA, it.data.data[0].nama_lengkap)

                    val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalDateTime.now()
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    val last_login = current.format(formatter)
                    pref.put(Constants.DEFAULT.LAST_LOGIN, last_login.toString())

                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                is Resource.Error -> {
                    LoadingScreen.hideLoading()
                    Toast.makeText(this, it.data!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}