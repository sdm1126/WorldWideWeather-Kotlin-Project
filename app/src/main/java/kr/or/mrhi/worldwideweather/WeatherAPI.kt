package kr.or.mrhi.worldwideweather

import kr.or.mrhi.worldwideweather.Data.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

class WeatherAPI {
    companion object{
        const val BASE_URL = "https://api.openweathermap.org/"
        const val API_KEY = BuildConfig.OPENWEATHER_API_KEY
    }
}

interface WeatherApiService{
    @GET("data/2.5/onecall")
    suspend fun getWeatherAddress(
        @Query("lat") lat : String,
        @Query("lon") lon : String,
        @Query("exclude") exclude : String,
        @Query("lang") lang : String,
        @Query("appid") appid : String
    ): WeatherData
}