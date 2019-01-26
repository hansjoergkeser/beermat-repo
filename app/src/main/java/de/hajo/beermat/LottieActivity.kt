package de.hajo.beermat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airbnb.lottie.LottieAnimationView

class LottieActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lottie)
    }

    override fun onPause() {
        super.onPause()
        val animation = findViewById<LottieAnimationView>(R.id.lottie_animation_view)
        animation.pauseAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        val animation = findViewById<LottieAnimationView>(R.id.lottie_animation_view)
        animation.cancelAnimation()
    }
}
