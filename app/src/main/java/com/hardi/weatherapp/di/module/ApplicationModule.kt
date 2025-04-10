package com.hardi.weatherapp.di.module

import com.hardi.weatherapp.data.api.ApiKeyInterceptor
import com.hardi.weatherapp.data.api.WeatherApi
import com.hardi.weatherapp.di.BaseUrl
import com.hardi.weatherapp.di.WeatherApiKey
import com.hardi.weatherapp.utils.AppConstant.API_KEY
import com.hardi.weatherapp.utils.AppConstant.BASE_URL
import com.hardi.weatherapp.utils.DefaultDispatcherProvider
import com.hardi.weatherapp.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @BaseUrl
    @Provides
    fun provideBaseUrl(): String = BASE_URL

    @WeatherApiKey
    @Provides
    fun provideApiKey(): String = API_KEY

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideDispatcher(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @Singleton
    fun provideRetrofitInstance(
        @BaseUrl baseUrl: String,
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) : WeatherApi{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(apiKeyInterceptor: ApiKeyInterceptor): OkHttpClient =
        OkHttpClient().newBuilder().addInterceptor(apiKeyInterceptor).build()

    @Provides
    @Singleton
    fun provideWeatherApiKey(@WeatherApiKey apiKey: String) : ApiKeyInterceptor =
        ApiKeyInterceptor(apiKey)
}