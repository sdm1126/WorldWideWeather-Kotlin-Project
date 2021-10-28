package kr.or.mrhi.openweather.Data

import java.io.Serializable

data class Temp(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double
) : Serializable