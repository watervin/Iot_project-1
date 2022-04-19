package com.example.today_room
import android.content.Intent
import android.os.Handler
import com.airbnb.lottie.LottieAnimationView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var lodingImage = findViewById(R.id.loading_image) as LottieAnimationView

        // 로딩화면 애니메이션 시작을 위한 코드
        lodingImage.playAnimation()

        val handler: Handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },3000)
    }
}