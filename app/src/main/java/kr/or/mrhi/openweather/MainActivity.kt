package kr.or.mrhi.openweather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kr.or.mrhi.openweather.Data.WeatherData
import kr.or.mrhi.openweather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // view binding
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 생성자 없는 Handler는 Deprecated
        Handler(Looper.getMainLooper()).postDelayed({
            Intent(this@MainActivity, MapActivity::class.java).run {
                startActivity(this)
                finish();
            }
        }, 1000)
    }
}

