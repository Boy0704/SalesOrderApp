package com.apps.salesorder.ui.profil

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.apps.salesorder.R
import com.apps.salesorder.api.ApiService
import com.apps.salesorder.api.Resource
import com.apps.salesorder.data.constants.Constants
import com.apps.salesorder.data.model.ItemPrice
import com.apps.salesorder.data.preferences.Preferences
import com.apps.salesorder.databinding.FragmentHomeBinding
import com.apps.salesorder.databinding.FragmentProfilBinding
import com.apps.salesorder.helper.LoadingScreen
import com.apps.salesorder.ui.auth.LoginActivity
import com.apps.salesorder.ui.auth.LoginViewModel
import com.apps.salesorder.ui.auth.LoginViewModelFactory
import com.apps.salesorder.ui.home.HomeViewModel
import com.apps.salesorder.ui.home.HomeViewModelFactory
import com.apps.salesorder.ui.splash.SplashActivity
import com.wessam.library.NetworkChecker
import com.wessam.library.NoInternetLayout
import timber.log.Timber

class ProfilFragment : Fragment() {

    private lateinit var binding: FragmentProfilBinding
    private lateinit var viewModelFactory : LoginViewModelFactory
    private lateinit var viewModel : LoginViewModel
    private val api by lazy { ApiService.getClient(requireActivity()) }
    private val pref by lazy { Preferences(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = LoginViewModelFactory( api, requireActivity() )
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.btnChangeApi.setOnClickListener {
            pref.delete()
            val intent = Intent(requireActivity(), SplashActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        val pref = Preferences(requireContext())
        val androidId = pref.getString(Constants.DEFAULT.ANDROID_ID).toString()
        binding.icLogout.setOnClickListener {
            checkNetwork()
            viewModel.logoutProses(androidId)

        }
        setupViewModel()

    }

    private fun checkNetwork(){
        if (!NetworkChecker.isNetworkConnected(requireContext())){
            NoInternetLayout
                .Builder(requireActivity(), R.layout.activity_splash)

        }
    }

    private fun setupViewModel() {
        binding.etUsername.text = pref.getString(Constants.DEFAULT.USERNAME)
        binding.nama.text = pref.getString(Constants.DEFAULT.NAMA)
        binding.lastLogin.text = pref.getString(Constants.DEFAULT.LAST_LOGIN)
        Timber.e("CONSTANT: ${Constants.DEFAULT.ANDROID_ID}")

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Loading -> {
                    LoadingScreen.displayLoadingWithText(requireContext(),"Silahkan Tunggu..",false)
                }
                is Resource.Success -> {
                    pref.remove(Constants.DEFAULT.USERNAME)
                    pref.remove(Constants.DEFAULT.NAMA)
                    pref.remove(Constants.DEFAULT.LAST_LOGIN)
                    pref.remove(Constants.DEFAULT.TOKEN)
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                is Resource.Error -> {
                    LoadingScreen.hideLoading()
                    Toast.makeText(requireContext(), "gagal logout", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


}