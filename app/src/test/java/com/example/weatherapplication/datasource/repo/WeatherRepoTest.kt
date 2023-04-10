package com.example.weatherapplication.datasource.repo

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapplication.FakeRemoteDataSource
import com.example.weatherapplication.datasource.FakeLocalDataSource
import com.example.weatherapplication.datasource.database.LocalSourceInterface
import com.example.weatherapplication.datasource.network.RemoteSource
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.model.Current
import com.example.weatherapplication.model.OpenWeather
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class WeatherRepoTest {


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var repo : WeatherRepoInterface
    lateinit var localDataSource: LocalSourceInterface
    lateinit var remoteDataSource: RemoteSource
    lateinit var context: Context

    @Before
    fun setUp() {
        var current= Current(1355645,25,2552,255.00,5221.0,21522,2552,55225.0,252254.0,
            1420225,2654552,25225.0,2525,2145.00, listOf())
      var data1= OpenWeather(30,true,1234455.0,165552455.0, "cairo",1, current,
            listOf(), listOf(),null)
        var data2= OpenWeather(30,true,1234455.0,165552455.0, "cairo",1, current,
            listOf(), listOf(),null)
        var data3= OpenWeather(30,true,1234455.0,165552455.0, "cairo",1, current,
            listOf(), listOf(),null)
        var data4= OpenWeather(30,true,1234455.0,165552455.0, "cairo",1, current,
            listOf(), listOf(),null)
        val data = Alert(12323,13652,13654,30.25,32.25,"")
        val data5 =  Alert(12323,13652,13654,30.25,32.25,"")
        val data6 =  Alert(12323,13652,13654,30.25,32.25,"")
        val data7 =  Alert(12323,13652,13654,30.25,32.25,"")

        localDataSource=FakeLocalDataSource(listOf(data,data5,data6,data7)as MutableList<Alert>,
            listOf(data2,data3,data4) as MutableList<OpenWeather>,
           data1 as Response<OpenWeather>
        )
        remoteDataSource= FakeRemoteDataSource(data1)
        repo = WeatherRepo.getInstance(remoteDataSource,localDataSource,context)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun get_current_weather_return_openWeather()= runBlocking{
        //given
        var latitude=30.0
        var lon=25.0
        //when
        var response= repo.getCurrentTempData(latitude,lon,"eng","metric").first()
        //then

        MatcherAssert.assertThat(response.body()?.lat, Matchers.`is`(latitude))
        MatcherAssert.assertThat(response.body()?.lon, Matchers.notNullValue())
    }
    @Test
    fun get_current_alert_return_alert()= runBlocking{
        //given
        var latitude=30.25
        var lon=30.25
        //when
        var response= repo.getCurrentWeatherForAlert(latitude,lon)
        //then

        MatcherAssert.assertThat(response.body()?.lat, Matchers.`is`(latitude))
        MatcherAssert.assertThat(response.body()?.lon, Matchers.`is`(lon))
    }
}