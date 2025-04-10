package com.hardi.weatherapp.utils

sealed interface Uistate<out T> {

    data class Success<T>(val result: T) : Uistate<T>

    data class Error<T>(val throwable: Throwable? = null,val message: String?= null) : Uistate<T>

    object Loading: Uistate<Nothing>
}