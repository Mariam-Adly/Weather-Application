package com.example.weatherapplication.datasource.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ApiServiceTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var apiService: ApiService

    @Before
    fun setUp() {
      apiService = RetrofitHelper.getInstance().create(ApiService::class.java)
    }

    @Test
    fun getRoot_requestKey_Authorized() = runBlocking{
        //Given
        val longitude =30.0
        val latitude = 25.0
        val language="eng"
        val unit = "metric"
        val apiKey= "67a004872bca4e7a1a7edbed26715b28"

        //When
        val response= apiService.getCurrentTempData(
            latitude = latitude
            ,longitude= longitude
            , appid = apiKey
            , lang = language
            , units = unit

        )
        //Then
        MatcherAssert.assertThat(response.body()?.lat, Matchers.`is` (latitude))
        MatcherAssert.assertThat(response.body()?.current, Matchers.notNullValue())


    }

    @Test
    fun getRoot_requestKey_unAuthorized() = runBlocking{
        //Given
        val longitude =30.0
        val latitude = 25.0
        val language="eng"
        val unit = "metric"
        val apiKey= "67a004872bca4e7a1a7edbed26715b28"

        //When
        val response= apiService.getCurrentTempData(
            latitude = latitude
            ,longitude= longitude
            , appid = apiKey
            , lang = language
            , units = unit

        )
        //Then
        MatcherAssert.assertThat(response.code(), Matchers.`is` (401))
        MatcherAssert.assertThat(response.body(), Matchers.notNullValue())


    }

    @After
    fun tearDown() {
    }

    @Test
    fun getCurrentTempData() {
    }

    @Test
    fun getFavWeatherData() {
    }
}