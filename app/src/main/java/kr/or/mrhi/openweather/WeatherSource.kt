package kr.or.mrhi.openweather

import android.os.Parcelable
import kr.or.mrhi.openweather.Data.Daily
import kr.or.mrhi.openweather.Data.Hourly
import java.io.Serializable

data class WeatherSource(
  val timeZone : String,
  val currentWeather : String,
  val currentTemp : String,
  val hourly: List<Hourly>,
  val daily: List<Daily>
): Serializable