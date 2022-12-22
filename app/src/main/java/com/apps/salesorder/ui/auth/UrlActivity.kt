package com.apps.salesorder.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.apps.salesorder.api.ApiService
import com.apps.salesorder.data.constants.Constants
import com.apps.salesorder.data.preferences.Preferences
import com.apps.salesorder.databinding.ActivityLoginBinding
import com.apps.salesorder.databinding.ActivityUrlBinding

class UrlActivity : AppCompatActivity() {

    private val binding by lazy { ActivityUrlBinding.inflate(layoutInflater) }
    private val pref by lazy { Preferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button.setOnClickListener {

            val url = binding.edtUrlApi.text.toString()
            if (url.isEmpty()){
                Toast.makeText(this, "Silahkan lengkapi url", Toast.LENGTH_SHORT).show()
            } else {
                pref.put(Constants.DEFAULT.BASE_URL, url)
                Toast.makeText(this, "URL diset : $url", Toast.LENGTH_SHORT).show()
                startActivity(
                    Intent(this, LoginActivity::class.java)
                )
                finish()
            }


        }



    }
}