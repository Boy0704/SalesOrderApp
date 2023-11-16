package com.apps.salesorder.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.apps.salesorder.R
import com.apps.salesorder.databinding.ActivityHomeBinding
import com.apps.salesorder.ui.profil.ProfilFragment
import com.apps.salesorder.ui.so.list.ListSoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tapadoo.alerter.Alerter

class HomeActivity : AppCompatActivity(), OnFragmentInteractionListener {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    private val content: FrameLayout? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId) {
            R.id.menu_home -> {
                val fragment = HomeFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_transaksi -> {
                val fragment = ListSoFragment()
                //val fragment = HomeFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_profil -> {
                val fragment = ProfilFragment()
                //val fragment = HomeFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.content, fragment, fragment.javaClass.getSimpleName())
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val fragment = HomeFragment()
        addFragment(fragment)
    }

    override fun onBackPressed() {
        doExitApp()
    }

    private var exitTime: Long = 0

    fun doExitApp() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "Press again to exit app", Toast.LENGTH_SHORT).show()
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }

    override fun onRefreshActivity() {
        recreate()
    }

}