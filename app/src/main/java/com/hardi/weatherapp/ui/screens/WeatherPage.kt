package com.hardi.weatherapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.hardi.weatherapp.data.model.Weather
import com.hardi.weatherapp.utils.Uistate

@Composable
fun WeatherPage(
    viewModel: WeatherViewModel = hiltViewModel()
) {

    var location by remember {
        mutableStateOf("")
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState by viewModel.uistate.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = location,
                onValueChange = {
                    location = it
                },
                label = {
                    Text(text = "Search location here")
                }
            )
            IconButton(onClick = {
                viewModel.searchQuery(location)
                keyboardController?.hide()
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search location here"
                )
            }
        }
        FetchResult(uiState)
    }
}

@Composable
fun FetchResult(uiState: Uistate<Weather>) {
    when (uiState) {
        is Uistate.Error -> {
            ErrorMessageView(uiState.message.toString())
        }

        is Uistate.Loading -> {
            CircularProgressIndicator()
        }

        is Uistate.Success -> {
            WeatherDetails(uiState.result)
        }
    }
}

@Composable
fun ErrorMessageView(msg: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(
            text = msg,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(5.dp)
        )
    }
}

@Composable
fun WeatherDetails(data: Weather) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = data.location.name + ",",
                fontSize = 25.sp

            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = data.location.country,
                fontSize = 22.sp
            )
        }
        Spacer(modifier = Modifier.width(22.dp))
        Text(
            text = data.current.temp_c + "° c",
            fontSize = 35.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
            contentDescription = "Weather Condition Icon"
        )
        Text(
            text = data.current.condition.text,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(20.dp))
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyVal("Humidity", data.current.humidity + "%")
                    WeatherKeyVal("Feels Like", data.current.feelslike_c + "° c")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyVal("UV Index", data.current.uv)
                    WeatherKeyVal("Cloud", data.current.cloud + "%")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyVal("Wind", data.current.wind_mph + "mph")
                    WeatherKeyVal("Gusts", data.current.gust_mph + "mph")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyVal("Visibility", data.current.vis_miles + "mi")
                    WeatherKeyVal("Precipitation", data.current.precip_mm + "mm")
                }
            }
        }
    }

}

@Composable
fun WeatherKeyVal(key: String, value: String) {
    Column(
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = key,
            fontSize = 21.sp,
            fontWeight =
            FontWeight.Bold
        )
    }
}