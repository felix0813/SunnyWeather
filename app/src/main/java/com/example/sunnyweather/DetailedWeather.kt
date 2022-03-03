package com.example.sunnyweather

class DetailedWeather(
    var temperature: Double,
    var apparentTemperature: Double,
    var skycon: String,
    var windSpeed: Double,
    var humidity: Double,
    var ultraviolet: String,
    var visibility: Double,
    var pressure: Double,
    var windDirection: String,
    var airQuality: String,
    var comfort: String,
    var arrHourly: Array<Weather?>,
    var arrDaily: Array<Weather?>
)