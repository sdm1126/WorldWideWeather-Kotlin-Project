package kr.or.mrhi.openweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kr.or.mrhi.openweather.Data.WeatherData
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val WeatherApi = WeatherClient.apiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        callWeatherKeyword("37.564648601090646", "127.02888760975634", "hourly,daily")
    }
    fun callWeatherKeyword(
        lat : String,
        lon : String,
        exclude : String
    ){
        val weather = MutableLiveData<WeatherData>()
        WeatherApi.getWeatherAddress(lat = lat, lon = lon, exclude = exclude ,WeatherAPI.API_KEY)
            .enqueue(object : retrofit2.Callback<WeatherData>{
                override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                    weather.value = response.body()
                    Log.d("weather", weather.value.toString())

                }

                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    Log.d("weather","전송 실패 ${t.printStackTrace()}")
                    t.printStackTrace()
                }
            })
    }
}

