package com.apps.salesorder.ui.profil

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.salesorder.R
import com.apps.salesorder.data.constants.Constants
import com.apps.salesorder.data.preferences.Preferences
import com.apps.salesorder.databinding.FragmentHomeBinding
import com.apps.salesorder.databinding.FragmentProfilBinding
import com.apps.salesorder.ui.auth.LoginActivity
import com.apps.salesorder.ui.splash.SplashActivity

class ProfilFragment : Fragment() {

    private lateinit var binding: FragmentProfilBinding
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

        binding.btnChangeApi.setOnClickListener {
            pref.delete()
            val intent = Intent(requireActivity(), SplashActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        binding.icLogout.setOnClickListener {
            pref.remove(Constants.DEFAULT.USERNAME)
            pref.remove(Constants.DEFAULT.NAMA)
            pref.remove(Constants.DEFAULT.LAST_LOGIN)
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        setupViewModel()

    }

    private fun setupViewModel() {
        binding.etUsername.text = pref.getString(Constants.DEFAULT.USERNAME)
        binding.nama.text = pref.getString(Constants.DEFAULT.NAMA)
        binding.lastLogin.text = pref.getString(Constants.DEFAULT.LAST_LOGIN)
    }


}