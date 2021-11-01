package kr.or.mrhi.worldwideweather.Data

import java.io.Serializable

data class WeatherXX(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
) : Serializable