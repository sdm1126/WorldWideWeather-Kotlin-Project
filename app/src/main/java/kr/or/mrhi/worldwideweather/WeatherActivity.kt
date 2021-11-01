package kr.or.mrhi.worldwideweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kr.or.mrhi.worldwideweather.Adapter.WeatherListAdapter
import kr.or.mrhi.worldwideweather.Adapter.WeatherRecyclerAdapter
import kr.or.mrhi.worldwideweather.Data.Daily
import kr.or.mrhi.worldwideweather.Data.Hourly
import kr.or.mrhi.worldwideweather.databinding.ActivityWeatherBinding

class WeatherActivity : AppCompatActivity() {
    private val binding by lazy { ActivityWeatherBinding.inflate(layoutInflater) }
    private lateinit var weatherSource : WeatherSource
    private lateinit var rvWeatherAdapter : WeatherRecyclerAdapter
    private lateinit var lvWeatherAdapter : WeatherListAdapter
    private lateinit var address : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.d("결과", "나옴")
        weatherSource = intent.getSerializableExtra("weatherSource") as WeatherSource
        address = intent.getStringExtra("address").toString()

        binding.tvWeatherLocation.text = address
        binding.tvWeatherCurrent.text = weatherSource.currentWeather
        when(weatherSource.currentWeather){
            "Clouds" -> binding.clWeather.setBackgroundResource(R.drawable.bg_cloudy)
            "Rain" -> binding.clWeather.setBackgroundResource(R.drawable.bg_rainy)
            "Snow" -> binding.clWeather.setBackgroundResource(R.drawable.bg_snowy)
            else -> binding.clWeather.setBackgroundResource(R.drawable.bg_sunny)
        }
        binding.tvWeatherTemp.text = String.format("%.1f",weatherSource.currentTemp.toDouble() - 273.15) + "℃"

        binding.rvWeather.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvWeather.setHasFixedSize(true)

        rvWeatherAdapter = WeatherRecyclerAdapter(this, weatherSource.hourly as MutableList<Hourly>)
        binding.rvWeather.adapter = rvWeatherAdapter

        lvWeatherAdapter = WeatherListAdapter(this, weatherSource.daily as MutableList<Daily>)
        binding.lvWeather.adapter = lvWeatherAdapter
    }
}