package kr.or.mrhi.worldwideweather

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherClient{
    private val retrofit: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(WeatherAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiService: WeatherApiService by lazy {
        retrofit
            .build()
            .create(WeatherApiService:: class.java)
    }
}
