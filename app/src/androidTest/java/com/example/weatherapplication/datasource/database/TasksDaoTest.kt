package com.example.weatherapplication.datasource.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherapplication.model.Current
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherDAOTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var weatherDAO: WeatherDao

    @Before
    fun setUp() {

        //initialize database
        db= Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
           AppDatabase::class.java
        ).allowMainThreadQueries().build()
        weatherDAO= db.getWeatherDao()
    }

    @After
    fun tearDown() {
        //close database
        db.close()
    }

        @Test
    fun insertweatherAndGetweather() = runBlockingTest{
        // GIVEN - insert a task
    val task = FavoriteWeather("Alexandria",30.23,30.25)

        db.getWeatherDao().insertFavoritePlace(task)

        // WHEN - Get the weather by id from the database
        val result = db.getWeatherDao().getAllFavoriteWeather().first()
        // THEN - The loaded data contains the expected values

        assertThat(result , notNullValue())
        assertThat(result.size, `is`(1))
    }
    @Test
    fun updateweatherAndGetweather() = runBlockingTest {
        // When inserting a weather
        val task = FavoriteWeather("Alexandria",30.23,30.25)
        val originalTask = task
        db.getWeatherDao().insertFavoritePlace(originalTask)

        // When the weather is updated
        val tasktwo = FavoriteWeather("Alexandria",30.23,30.25)
        db.getWeatherDao().updateFavoritePlace(tasktwo)

        // THEN - The loaded data contains the expected values
        var res =db.getWeatherDao().getAllFavoriteWeather().first()
            assertThat(res.size, `is`( 2))
    }

}