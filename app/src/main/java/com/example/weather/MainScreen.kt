package com.example.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weather.ui.theme.MainList
import com.example.weather.ui.theme.data.City
import com.example.weather.ui.theme.data.WatherModel
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

var city ="London"
@Composable
fun MainCard(currentDay: MutableState<WatherModel>) {
    var gorod by remember{ mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(5.dp)

    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.Black.copy(alpha = 0.2f),
            elevation = 0.dp,
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                        text = currentDay.value.localtime,
                        style = TextStyle(fontSize = 15.sp),
                        color = Color.Black
                    )
                    AsyncImage(
                        model = "https:"+currentDay.value.icon,
                        contentDescription = "icon",
                        modifier = Modifier
                            .size(35.dp)
                            .padding(top = 3.dp, end = 8.dp)
                    )
                }
                    Text(
                        text = currentDay.value.city,
                        style = TextStyle(fontSize = 24.sp),
                        color = Color.Black
                    )
                    Text(
                        text = if(currentDay.value.currentTemp.isNotEmpty()) currentDay.value.currentTemp + "°C"
                        else "${currentDay.value.maxTemp} °C/ ${currentDay.value.minTemp} °C",
                        style = TextStyle(fontSize = 65.sp),
                        color = Color.Black
                    )
                    Text(
                        text = currentDay.value.condition ,
                        style = TextStyle(fontSize = 15.sp),
                        color = Color.Black
                    )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    //horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            getCity(gorod)
                            city = gorod
                            println(city)
                        })
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "im3"
                        )
                    }

                    BasicTextField(
                        value = gorod,
                        onValueChange = { newText -> gorod = newText },
                        singleLine = true,
                        modifier = Modifier
                            .scale(scaleX = 0.9F, scaleY = 0.9F)
                            .padding(start = 5.dp, top = 13.dp, bottom = 5.dp),
                        decorationBox = {
                            innerTextField ->  
                            Box(
                                Modifier
                                    .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(percent = 20))
                                    //.padding(5.dp)
                            )
                            {
                                if (gorod.isEmpty()) Text(text = "Choose City", modifier = Modifier.padding(start = 5.dp))
                                innerTextField()
                            }
                        }
                        )
                    Text(
                        text = currentDay.value.maxTemp + "°C/" + currentDay.value.minTemp + "°C",
                        style = TextStyle(fontSize = 15.sp),
                        color = Color.Black,
                        modifier = Modifier.padding(start = 5.dp)
                    )

                    IconButton(
                        onClick = {
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sync),
                            contentDescription = "im4",
                            modifier = Modifier.padding(start = 120.dp)
                        )
                    }
                }
            }
        }
    }

}
@OptIn(ExperimentalPagerApi::class)
@Composable
fun TablLayout(daysList: MutableState<List<WatherModel>>,currentDay: MutableState<WatherModel>) {
    val tabList = listOf("Часы", "Дни")
    val pagerState = rememberPagerState()
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .padding(start = 5.dp, end = 5.dp)
    ) {
        TabRow(
            selectedTabIndex = tabIndex,
            indicator = {pos ->
                        TabRowDefaults.Indicator(
                            Modifier.pagerTabIndicatorOffset(pagerState, pos)
                        )
            },
            backgroundColor = Color.Black.copy(alpha = 0.2f),
            contentColor = Color.Black
        ) {
            tabList.forEachIndexed { index, text ->
                Tab(
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(text = text)
                    }
                )
            }
        }
        HorizontalPager(
            count = tabList.size,
            state = pagerState,
            modifier = Modifier.weight(1.0f)
        ) {
            index ->
            val list = when(index){
                0 -> getWeatherByHours(currentDay.value.hours)
                1 -> daysList.value
                else -> daysList.value
            }
               MainList(list, currentDay)
        }
    }

}
private fun getWeatherByHours(hours: String): List<WatherModel>{
    if(hours.isEmpty()) return listOf()
    val hoursArray = JSONArray(hours)
    val list = ArrayList<WatherModel>()
    for(i in 0 until hoursArray.length()){
        val item = hoursArray[i] as JSONObject
        list.add(
            WatherModel(
                "",
                item.getString("time"),
                item.getString("temp_c") + "°C",
                item.getJSONObject("condition").getString("text"),
                item.getJSONObject("condition").getString("icon"),
                "",
                "",
                ""

            )
        )
    }
    return list
}
 fun getCity(city: String) : String {

        City(
            city
        )

    return city
}
