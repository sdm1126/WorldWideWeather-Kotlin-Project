package kr.or.mrhi.openweather.Data

import java.io.Serializable

data class Minutely(
    val dt: Int,
    val precipitation: Int
) : Serializable