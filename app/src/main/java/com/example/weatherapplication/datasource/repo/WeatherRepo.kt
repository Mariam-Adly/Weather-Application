package com.example.weatherapplication.datasource.repo


import android.app.Activity
import android.content.Context
import com.example.weatherapplication.datasource.database.LocalSourceInterface
import com.example.weatherapplication.datasource.network.RemoteSource
import com.example.weatherapplication.model.Alert
import com.example.weatherapplication.model.FavoriteWeather
import com.example.weatherapplication.model.OpenWeather
import kotlinx.coroutines.Dispatchers
import retrofit2.Response
import kotlinx.coroutines.flow.*

class WeatherRepo (var remoteSource: RemoteSource,var localSource:LocalSourceInterface,var context: Context) : WeatherRepoInterface {

    val sharedPreferences = context.getSharedPreferences("language", Activity.MODE_PRIVATE)
   var lang = sharedPreferences.getString("myLang","")!!
    companion object {
        private var instance : WeatherRepo? = null
        fun getInstance(remoteResource: RemoteSource,localSource: LocalSourceInterface,context: Context):WeatherRepo{
            return instance?: synchronized(this){
                val temp = WeatherRepo(remoteResource,localSource,context)
                instance = temp
                temp
            }
        }
    }



   override suspend fun getCurrentTempData(
        lat: Double, long: Double, lang: String ,tempUnit: String
    ): Flow<Response<OpenWeather>> = flow{

      emit( remoteSource.getCurrentTempData(lat,long,lang,tempUnit))
   }.flowOn(Dispatchers.IO)

    override suspend fun selectAllStoredWeatherModel(): Flow<OpenWeather> {
        return localSource.selectAllStoredWeatherModel()
    }

    override suspend fun insertWeatherModel(openWeather: OpenWeather) {
        localSource.insertWeatherModel(openWeather)
    }

    override suspend fun getCurrentWeatherForAlert(lat: Double, lon: Double): Response<OpenWeather> {
        return remoteSource.getCurrentTempData(lat,lon,lang,"metric")
    }

    override suspend fun getAllFavoriteWeather(): Flow<List<FavoriteWeather>> {
        return localSource.getAllFavoriteWeather()
    }

    override suspend fun deleteFavoritePlace(favoriteWeather: FavoriteWeather) {
        localSource.deleteFavoritePlace(favoriteWeather)
    }

    override suspend fun insertFavoritePlace(favoriteWeather: FavoriteWeather) {
        localSource.insertFavoritePlace(favoriteWeather)
    }

    override suspend fun updateFavoritePlace(favoriteWeather: FavoriteWeather) {
        localSource.updateFavoritePlace(favoriteWeather)
    }

    override suspend fun getFavWeatherData(favWeather: FavoriteWeather): Response<OpenWeather> {
        return remoteSource.getFavWeatherData(favWeather)
    }

    override suspend fun getAlerts(): Flow<List<Alert>> {
        return localSource.getAlerts()
    }

    override suspend fun setAlarm(myAlert: Alert) {
        localSource.insertAlert(myAlert)
       // setOnTimeWorkManger(myAlert)
    }

//    private fun setOnTimeWorkManger(myAlert: Alert) {
//        val data = Data.Builder()
//            .putString("mariam", "reminder")
//            .build()
//        val constraints = Constraints.Builder()
//            .setRequiresBatteryNotLow(true)
//            .build()
//        val tag: String = "mariam"
//        val oneTimeWorkRequest =
//            OneTimeWorkRequest.Builder(MyPeriodicWorkJop::class.java).setInputData(data)
//                .setConstraints(constraints)
//                .setInitialDelay(1, TimeUnit.HOURS)
//                .addTag(tag)
//                .build()
//        WorkManager.getInstance()
//            .enqueueUniqueWork(tag, ExistingWorkPolicy.REPLACE, oneTimeWorkRequest)
//    }

    override suspend fun deleteAlerts(myAlert: Alert) {
        localSource.deleteAlerts(myAlert)
    }
//    override val allAlarmsList: Single<List<Alert>>?
//        get() = localSource.allAlarmsList


}