package com.kakao.mobility.ui.splash

import android.animation.Animator
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.kakao.mobility.R
import com.kakao.mobility.ui.main.MainViewModel
import com.kakao.mobility.ui.main.startMainActivity
import com.kakao.mobility.utils.createAnimatorEndListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModel()
    private val animatorEndListener by lazy { createAnimatorEndListener(::onAnimatorEnd) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel.status.observe(this, ::onStatusUpdated)
    }

    override fun onResume() {
        super.onResume()

        findViewById<LottieAnimationView>(R.id.splash_logo)?.let {
            it.removeAllAnimatorListeners()
            it.addAnimatorListener(
                    createAnimatorEndListener(::onAnimatorEnd)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        findViewById<LottieAnimationView>(R.id.splash_logo)?.removeAllAnimatorListeners()
    }

    private fun onAnimatorEnd() {
        takeIf { viewModel.isCompleted() }?.let {
            finish()
            startMainActivity()
        } ?: findViewById<LottieAnimationView>(R.id.splash_logo)?.playAnimation()
    }

    private fun onStatusUpdated(status: SplashStatus) = when (status) {
        is SplashStatus.Error -> finishWithAlert()
        else -> {
        } //DoNothing
    }

    private fun finishWithAlert() {
        Toast.makeText(this, R.string.failed_to_load_pokemon_data, Toast.LENGTH_LONG).show()
        finishAffinity()
    }
}