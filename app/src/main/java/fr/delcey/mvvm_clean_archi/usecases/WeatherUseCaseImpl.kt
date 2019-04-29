package fr.delcey.mvvm_clean_archi.usecases

import fr.delcey.mvvm_clean_archi.data.WeatherApiResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WeatherUseCaseImpl : WeatherUseCase {
    companion object {
        private const val APIKEY_NAME = "appid"
        private const val APIKEY_VALUE = "ef632f14705ca08eddf8cd7fc7a29d4f"
        private const val METRICS_UNITS_NAME = "units"
        private const val METRICS_UNITS_VALUE = "metric"

        private var service: WeatherApi

        init {
            // API Key interceptor
            val apikeyInterceptor = Interceptor { chain ->
                var request = chain.request()
                val url = request.url()
                    .newBuilder()
                    .addQueryParameter(APIKEY_NAME, APIKEY_VALUE)
                    .build()
                request = request.newBuilder().url(url).build()
                chain.proceed(request)
            }

            // Ask "metrics units only" interceptor
            val metricsUnitsInterceptor = Interceptor { chain ->
                var request = chain.request()
                val url = request.url()
                    .newBuilder()
                    .addQueryParameter(METRICS_UNITS_NAME, METRICS_UNITS_VALUE)
                    .build()
                request = request.newBuilder().url(url).build()
                chain.proceed(request)
            }

            // Logging interceptor (to see http call on logcat)
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            // Inject interceptors to client
            val client = OkHttpClient.Builder()
                .addInterceptor(apikeyInterceptor)
                .addInterceptor(metricsUnitsInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            service = retrofit.create(WeatherApi::class.java)
        }
    }

   override fun getWeather(name: String?): WeatherApiResponse? {
        return service.getWeatherForCity(name).execute().body()
    }
}