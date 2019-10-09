package fr.delcey.mvvm_clean_archi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fr.delcey.mvvm_clean_archi.data.WeatherApiResponse
import fr.delcey.mvvm_clean_archi.data.WeatherValues
import fr.delcey.mvvm_clean_archi.usecases.WeatherUseCase
import fr.delcey.mvvm_clean_archi.view.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class MainViewModelTest {

    // Instant update of the LiveDatas thanks to the InstantTaskExecutorRule (not really in this case because we switch
    // thread ourselves with *withContext(Dispatchers.Main)* but whatever this is usefull to know)
    // Note : A rule apply to every tests in this class
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    // "Rule" so that Coroutines use the same (and only one) Thread
    // This is an alternative way to set up a "rule". But it's more conventionnal
    // to extend a TestWatcher and use it as a Rule instead.
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    // @Before is called before every unit tests
    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    // @After is called after every unit tests
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `should print correct city name and temperature`() = runBlockingTest {
        // Given
        val mockedUseCase = object : WeatherUseCase {
            override fun getWeather(name: String?) = WeatherApiResponse(WeatherValues(25.0), "London")
        }
        val viewModel = MainViewModel(mockedUseCase)

        // When
        viewModel.queryWeather("London")

        // Then
        assertEquals("Dans la ville London, il fait 25.0°C", viewModel.weatherLiveData.value?.cityTemperature)
    }

    @Test
    fun `should print correct apostrophed city name and negative temperature`() = runBlockingTest {
        // Given
        val mockedUseCase = object : WeatherUseCase {
            override fun getWeather(name: String?) = WeatherApiResponse(WeatherValues(-8000.0), "Le village d'Astérix")
        }
        val viewModel = MainViewModel(mockedUseCase)

        // When
        viewModel.queryWeather("Le village d'Astérix")

        // Then
        assertEquals("Dans la ville Le village d'Astérix, il fait -8000.0°C", viewModel.weatherLiveData.value?.cityTemperature)
    }

    @Test
    fun `should print API corrected city name`() = runBlockingTest {
        // Given
        val mockedUseCase = object : WeatherUseCase {
            override fun getWeather(name: String?) = WeatherApiResponse(WeatherValues(-20.0), "Calais")
        }
        val viewModel = MainViewModel(mockedUseCase)

        // When
        viewModel.queryWeather("Le village des Ch'tis")

        // Then
        assertEquals("Dans la ville Calais, il fait -20.0°C", viewModel.weatherLiveData.value?.cityTemperature)
    }


    @Test
    fun `should print error with incorrect city name`() = runBlockingTest {
        // Given
        val mockedUseCase = object : WeatherUseCase {
            override fun getWeather(name: String?): WeatherApiResponse? = null
        }
        val viewModel = MainViewModel(mockedUseCase)

        // When
        viewModel.queryWeather("Mon chat s'est endormi sur le claaaaaaaaaaaavier")

        // Then
        assertEquals(
            "Mon chat s'est endormi sur le claaaaaaaaaaaavier est une ville inconnue au bataillon, entrez une vraie ville svp. Ou connectez-vous aux internets. ",
            viewModel.weatherLiveData.value?.cityTemperature
        )
    }
}