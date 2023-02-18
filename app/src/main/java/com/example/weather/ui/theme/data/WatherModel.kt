package com.example.weather.ui.theme.data


data class WatherModel(
    val city: String,
    val localtime: String,
    val currentTemp: String,
    val condition: String,
    val icon: String,
    val maxTemp: String,
    val minTemp: String,
    val hours: String,
)
