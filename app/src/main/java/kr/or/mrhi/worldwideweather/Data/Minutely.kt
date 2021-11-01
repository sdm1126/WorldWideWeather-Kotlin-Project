package kr.or.mrhi.worldwideweather.Data

import java.io.Serializable

data class Minutely(
    val dt: Int,
    val precipitation: Int
) : Serializable