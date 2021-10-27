package kr.or.mrhi.openweather

import kr.or.mrhi.openweather.Data.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

class WeatherAPI {
    companion object{
        const val BASE_URL = "https://api.openweathermap.org/"
        const val API_KEY = "b4c61753d28f494faa2c819e5f8b8e22"
    }
}

interface WeatherApiService{
    @GET("data/2.5/onecall")
    fun getWeatherAddress(
        @Query("lat") lat : String,
        @Query("lon") lon : String,
        @Query("exclude") exclude : String,
        @Query("appid") appid : String
    ): Call<WeatherData>
}