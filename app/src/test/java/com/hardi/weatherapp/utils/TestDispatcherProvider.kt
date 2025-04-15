package com.hardi.weatherapp.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@ExperimentalCoroutinesApi
class TestDispatcherProvider : DispatcherProvider {

    private val testDefaultDispatcherProvider = UnconfinedTestDispatcher()

    override val io: CoroutineDispatcher
        get() = testDefaultDispatcherProvider

    override val default: CoroutineDispatcher
        get() = testDefaultDispatcherProvider

    override val main: CoroutineDispatcher
        get() = testDefaultDispatcherProvider

}