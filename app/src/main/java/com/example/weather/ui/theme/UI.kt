package com.example.weather.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weather.ui.theme.data.WatherModel

@Composable
fun MainList(list: List<WatherModel>, currentDays : MutableState<WatherModel>){
    LazyColumn(modifier = Modifier.fillMaxSize()){
        itemsIndexed (
            list

        ){
                _, item -> ListIten(item,currentDays)
        }

    }
}

@Composable
fun ListIten(item: WatherModel,currentDays: MutableState<WatherModel>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp)
            .clickable {
                if (item.hours.isEmpty()) return@clickable
                currentDays.value = item
            },
        backgroundColor = Color.Black.copy(alpha = 0.2f),
        elevation = 0.dp,
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(start = 8.dp, top = 5.dp, bottom = 5.dp)) {
                Text(text = item.localtime)
                Text(text = item.condition)
            }
            Text(
                text = item.currentTemp.ifEmpty { "${item.maxTemp}°C/${item.minTemp}°C" },
                style = TextStyle(fontSize = 25.sp)
            )
            AsyncImage(
                model = "https:${item.icon}",
                contentDescription = "icon2",
                modifier = Modifier
                    .size(35.dp)
                    .padding(end = 8.dp)
            )
        }
    }

}