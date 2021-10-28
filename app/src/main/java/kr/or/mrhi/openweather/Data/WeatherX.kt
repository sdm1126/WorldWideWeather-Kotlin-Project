package kr.or.mrhi.openweather.Data

import java.io.Serializable

data class WeatherX(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
) : Serializable