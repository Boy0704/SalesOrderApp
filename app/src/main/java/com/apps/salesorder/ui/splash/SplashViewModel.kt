package com.apps.salesorder.ui.splash

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SplashViewModel : ViewModel(){

    val splashNotifier = MutableLiveData<Boolean>()

    private var isFinish = false
    private var isStarted = false
    private var isPause = false
    private var countDownTimer: CountDownTimer? = null

    /**
     * Delay dengan hitungan mundur untuk menampilkan page splash screen
     * @param timeInMillis waktu delay dalam millis
     */
    fun delayCountDown(timeInMillis: Long) {
        isStarted = true
        countDownTimer = object : CountDownTimer(timeInMillis, 1000) {

            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                isFinish = true
                if (!isPause) {
                    splashNotifier.postValue(true)
                }
            }
        }.start()
    }

}