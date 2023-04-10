package com.example.weatherapplication.FavoriteViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapplication.MainCoroutineRule
import com.example.weatherapplication.datasource.FakeLocalDataSource
import com.example.weatherapplication.datasource.FakeRemoteDataSource
import com.example.weatherapplication.datasource.database.LocalSourceInterface
import com.example.weatherapplication.datasource.network.RemoteSource
import com.example.weatherapplication.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.model.Current
import com.example.weatherapplication.model.OpenWeather
import com.example.weatherapplication.utility.ApiState
import com.example.weatherapplication.utility.ApiStateList
import com.example.weatherforecast.DataSource.FakeTestRepositary
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class FavoriteViewModelTest {
    // Subject under test
    private lateinit var favViewModel: FavoriteViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var repository: FakeTestRepositary

    // Executes each task synchronously using Architecture Components.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var localDataSource: LocalSourceInterface
    lateinit var remoteDataSource: RemoteSource
    lateinit var  welcomeList: MutableList<OpenWeather>
    lateinit var alertList:MutableList<Alert>
    lateinit var current1: OpenWeather
    lateinit var openWeather: Response<OpenWeather>
    @Before
    fun setupViewModel() {
        var current= Current(1355645,25,2552,255.00,5221.0,21522,2552,55225.0,252254.0,
            1420225,2654552,25225.0,2525,2145.00, listOf())
        current1= OpenWeather(30,true,1234455.0,165552455.0, "cairo",1, current,
            listOf(), listOf(),null)
        var data2= OpenWeather(30,true,1234455.0,165552455.0, "cairo",1, current,
            listOf(), listOf(),null)
        var data3= OpenWeather(30,true,1234455.0,165552455.0, "cairo",1, current,
            listOf(), listOf(),null)
        var data4=OpenWeather(30,true,1234455.0,165552455.0, "cairo",1, current,
            listOf(), listOf(),null)
        val data = Alert(12323,13652,13654,30.25,32.25,"")
        val data5 =  Alert(12323,13652,13654,30.25,32.25,"")
        val data6 =  Alert(12323,13652,13654,30.25,32.25,"")
        val data7 =  Alert(12323,13652,13654,30.25,32.25,"")
        welcomeList=listOf(data2,data3,data4) as MutableList<OpenWeather>
        alertList=listOf(data,data5,data6,data7)as MutableList<Alert>

        localDataSource= FakeLocalDataSource(alertList,
            welcomeList ,
            openWeather)
        remoteDataSource= FakeRemoteDataSource(openWeather)
        repository = FakeTestRepositary(localDataSource,remoteDataSource)
        favViewModel = FavoriteViewModel(repository)
    }
    @Test
    fun get_favorite_retur_list_favorites()= runBlocking{
        //given
        var favList=welcomeList
        //when
        favViewModel.getFavPlaces()
        var result=favViewModel.favWeather.value
        when (result) {

            is ApiStateList.Failure -> {}
            is ApiStateList.Loading -> {}
            is ApiStateList.Success -> {favList = result.data as MutableList<OpenWeather>}
        }
        //Then
        MatcherAssert.assertThat(favList, IsNull.notNullValue())
}
}