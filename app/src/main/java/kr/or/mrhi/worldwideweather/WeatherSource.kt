package kr.or.mrhi.worldwideweather

import kr.or.mrhi.worldwideweather.Data.Daily
import kr.or.mrhi.worldwideweather.Data.Hourly
import java.io.Serializable

data class WeatherSource(
  val timeZone : String,
  val currentWeather : String,
  val currentTemp : String,
  val hourly: List<Hourly>,
  val daily: List<Daily>
): Serializable