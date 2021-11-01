package kr.or.mrhi.worldwideweather.Data

import java.io.Serializable

data class FeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
) : Serializable