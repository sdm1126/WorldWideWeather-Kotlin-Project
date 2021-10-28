package kr.or.mrhi.openweather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kr.or.mrhi.openweather.Data.WeatherData
import kr.or.mrhi.openweather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val WeatherApi = WeatherClient.apiService
    private lateinit var weatherSource : WeatherSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            runBlocking {
                callWeatherKeyword("51.5072", "-0.1275", "minutely", "kr")
            }
            Intent(this@MainActivity, WeatherActivity::class.java).apply {
                putExtra("data", weatherSource)
            }.run {
                startActivity(this)
            }
        }
    }

    suspend fun callWeatherKeyword(
        lat: String,
        lon: String,
        exclude: String,
        lang: String
    ) {
        val weather = MutableLiveData<WeatherData>()
        weather.value = WeatherApi.getWeatherAddress(
            lat = lat,
            lon = lon,
            exclude = exclude,
            lang = lang,
            WeatherAPI.API_KEY
        )
        weatherSource = WeatherSource(
                weather.value?.timezone!!,
                weather.value?.current?.weather?.get(0)!!.main,
                weather.value?.current?.temp.toString(),
                weather.value?.hourly!!,
                weather.value?.daily!!
        )
    }
}

