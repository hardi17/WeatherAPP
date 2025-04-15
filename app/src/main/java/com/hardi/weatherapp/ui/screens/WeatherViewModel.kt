package com.hardi.weatherapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hardi.weatherapp.data.model.Weather
import com.hardi.weatherapp.data.repository.WeatherRepository
import com.hardi.weatherapp.utils.DispatcherProvider
import com.hardi.weatherapp.utils.Uistate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<Uistate<Weather>>(Uistate.Error(null, "No data available"))

    val uistate: StateFlow<Uistate<Weather>> = _uiState

//    init {
//        searchQuery("current location")
//    }


    fun searchQuery(location: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiState.value = Uistate.Loading
            weatherRepository.getWeatherDetails(location)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = Uistate.Error(e)
                }.collect {
                    _uiState.value = Uistate.Success(it)
                }
        }
    }
}