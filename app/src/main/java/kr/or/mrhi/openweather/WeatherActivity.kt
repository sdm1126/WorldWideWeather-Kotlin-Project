package kr.or.mrhi.openweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kr.or.mrhi.openweather.Adapter.WeatherListAdapter
import kr.or.mrhi.openweather.Adapter.WeatherRecyclerAdapter
import kr.or.mrhi.openweather.Data.Daily
import kr.or.mrhi.openweather.Data.Hourly
import kr.or.mrhi.openweather.databinding.ActivityWeatherBinding

class WeatherActivity : AppCompatActivity() {
    private val binding by lazy { ActivityWeatherBinding.inflate(layoutInflater) }
    private var arrayList = mutableListOf<String>()
    private lateinit var weatherSource : WeatherSource
    private lateinit var rvWeatherAdapter : WeatherRecyclerAdapter
    private lateinit var lvWeatherAdapter : WeatherListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.d("결과", "나옴")
        weatherSource = intent.getSerializableExtra("data") as WeatherSource

        binding.tvWeatherLocation.text = weatherSource.timeZone
        binding.tvWeatherCurrent.text = weatherSource.currentWeather
        binding.tvWeatherTemp.text = String.format("%.1f",weatherSource.currentTemp.toDouble() - 273.15)

        binding.rvWeather.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvWeather.setHasFixedSize(true)

        rvWeatherAdapter = WeatherRecyclerAdapter(this, weatherSource.hourly as MutableList<Hourly>)
        binding.rvWeather.adapter = rvWeatherAdapter

        lvWeatherAdapter = WeatherListAdapter(this, weatherSource.daily as MutableList<Daily>)
        binding.lvWeather.adapter = lvWeatherAdapter
    }
}