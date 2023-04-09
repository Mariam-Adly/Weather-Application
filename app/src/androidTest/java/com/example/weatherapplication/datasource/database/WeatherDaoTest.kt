package com.example.weatherapplication.datasource.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.model.Current
import com.example.weatherapplication.model.OpenWeather
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsNull
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var db : AppDatabase
    private lateinit var weatherDao: WeatherDao


    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java).allowMainThreadQueries().build()
           weatherDao = db.getWeatherDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun selectWeatherModel() {
    }

    @Test
    fun insertWeatherModel() {
    }

    @Test
    fun getAllFavoriteWeather() {
    }

    @Test
    fun insertFavoritePlace() {
    }

    @Test
    fun deleteFavoritePlace() {
    }

    @Test
    fun updateFavoritePlace() {
    }

    @Test
    suspend fun getAlerts() = runBlockingTest {
        val alertOne =  Alert(12323,13652,1245,13.65,12.21,"")
        val alertTwo = Alert(2323,13652,1245,13.65,12.21,"")
        val alertThree = Alert(1223,13652,1245,13.65,12.21,"")
        val alertFour = Alert(1232,13652,1245,13.65,12.21,"")

        weatherDao.insertAlert(alertOne)
        weatherDao.insertAlert(alertTwo)
        weatherDao.insertAlert(alertThree)
        weatherDao.insertAlert(alertFour)

        val result = weatherDao.getAlerts().first()
        MatcherAssert.assertThat(result.size,CoreMatchers. `is`(4))
    }

    @Test
    fun insertFavourite_insertOneItem_returnTheItem() = runBlockingTest {
        //Given
        val data = Alert(12323,13652,1245,13.65,12.21,"")

        //When
        weatherDao.insertAlert(data)

        //Then
        val result= weatherDao.getAlerts().first()
        MatcherAssert.assertThat(result[0], IsNull.notNullValue())

    }





    @Test
    fun insertAlert() {
    }

    @Test
    fun deleteAlerts() {
    }
}