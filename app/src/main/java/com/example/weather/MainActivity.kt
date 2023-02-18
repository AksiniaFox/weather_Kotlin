package com.example.weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weather.ui.theme.WeatherTheme
import com.example.weather.ui.theme.data.WatherModel
import org.json.JSONObject

const val API_KEY = "8f4c8653664e43d084c193052231402"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme {
                val daysList = remember {
                    mutableStateOf(listOf<WatherModel>())
                }
                val currentDay = remember {
                    mutableStateOf(WatherModel(
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                    ))
                }
                //getCity("London")
                getResult((getCity(city)),this, daysList, currentDay)
                Image(
                    painter = painterResource(id = R.drawable.zakat),
                    contentDescription = "im1",
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.7f),
                    contentScale = ContentScale.FillBounds
                )
                Column {
                    MainCard(currentDay)
                    TablLayout(daysList, currentDay)
                }
            }
        }
    }
}
private fun getResult(city: String, context: Context, dayslist: MutableState<List<WatherModel>>, currentDay: MutableState<WatherModel>){
    val url = "https://api.weatherapi.com/v1/forecast.json" +
            "?key=$API_KEY"+
            "&q=$city"+
            "&days="+
            "3"+
            "&aqi=no&alerts=no"
    val queue = Volley.newRequestQueue(context)
    val sRequest = StringRequest(
        com.android.volley.Request.Method.GET,
        url,
        {
            response ->
            val list = getWeatherByDays(response)
            currentDay.value = list[0]
            dayslist.value = list
        },
        {
            Log.d("MyLog", "VolleyError: $it")
        }
    )
    queue.add(sRequest)
}

private fun getWeatherByDays(response: String): List<WatherModel> {
    if (response.isEmpty()) return listOf()
    val list = ArrayList<WatherModel>()
    val mainObject = JSONObject(response)
    val city = mainObject.getJSONObject("location").getString("name")
    val days = mainObject.getJSONObject("forecast").getJSONArray("forecastday")

    for (i in 0 until days.length()) {
        val item = days[i] as JSONObject
        list.add(
            WatherModel(
                city,
                item.getString("date"),
                "",
                item.getJSONObject("day")
                    .getJSONObject("condition")
                    .getString("text"),
                item.getJSONObject("day")
                    .getJSONObject("condition")
                    .getString("icon"),
                item.getJSONObject("day").getString("maxtemp_c"),
                item.getJSONObject("day").getString("mintemp_c"),
                item.getJSONArray("hour").toString()
            )
        )
    }
    list[0] = list[0].copy(
        localtime = mainObject.getJSONObject("location").getString("localtime"),
        currentTemp = mainObject.getJSONObject("current").getString("temp_c")
    )
    return list
}