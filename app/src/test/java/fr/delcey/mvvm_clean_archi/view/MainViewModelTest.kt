package fr.delcey.mvvm_clean_archi.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import fr.delcey.mvvm_clean_archi.data.Address
import fr.delcey.mvvm_clean_archi.data.AddressDao
import fr.delcey.mvvm_clean_archi.data.Property
import fr.delcey.mvvm_clean_archi.data.PropertyDao
import fr.delcey.mvvm_clean_archi.view.model.PropertyUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.Matchers.*
import org.hamcrest.beans.HasPropertyWithValue
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

// Use JUnit4 for Unit Testing
@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class MainViewModelTest {

    // Check documentation but basically, with this rule : livedata.postValue() is the same as livedata.setValue()
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

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
    fun `should not expose data without refresh`() = runBlockingTest {
        // Given
        val addresses = listOf(Address(1, "101 rue des Dames"))
        val mockedAddressDao = mock<AddressDao> {
            onBlocking { getAddressesAsSuspend() } doReturn addresses
        }

        val properties = listOf(Property(0, "Business", 1))
        val mockedPropertyDao = mock<PropertyDao> {
            onBlocking { getPropertiesAsSuspend() } doReturn properties
        }

        val viewModel = MainViewModel(mockedPropertyDao, mockedAddressDao)

        // When
        val result = viewModel.uiPropertiesLiveData.value

        // Then
        assertNull(result)
    }

    @Test
    fun `should expose data with refresh`() = runBlockingTest {
        // Given
        val addresses = listOf(Address(1, "101 rue des Dames"))
        val mockedAddressDao = mock<AddressDao> {
            onBlocking { getAddressesAsSuspend() } doReturn addresses
        }

        val properties = listOf(Property(0, "Business", 1))
        val mockedPropertyDao = mock<PropertyDao> {
            onBlocking { getPropertiesAsSuspend() } doReturn properties
        }

        val viewModel = MainViewModel(mockedPropertyDao, mockedAddressDao)
        viewModel.doStuffOffMainThread()

        // When
        val result = viewModel.uiPropertiesLiveData.value

        // Then
        assertNotNull(result)
        assertEquals(1, result!!.size.toLong())
        // The most basic way to assert in a collection : get(0) after checking for size == 1
        assertEquals("101 rue des Dames", result[0].mainAddress)
        // The most basic way to assert in a collection : get(0) after checking for size == 1
        assertEquals("Business", result[0].type)
    }

    @Test
    fun `should expose 2 properties for one address`() = runBlockingTest {
        // Given
        val addresses = listOf(Address(1, "101 rue des Dames"))
        val mockedAddressDao = mock<AddressDao> {
            onBlocking { getAddressesAsSuspend() } doReturn addresses
        }

        val properties = listOf(
            Property(0, "Business", 1),
            Property(1, "Flat", 1)
        )
        val mockedPropertyDao = mock<PropertyDao> {
            onBlocking { getPropertiesAsSuspend() } doReturn properties
        }

        val viewModel = MainViewModel(mockedPropertyDao, mockedAddressDao)
        viewModel.doStuffOffMainThread()

        // When
        val result = viewModel.uiPropertiesLiveData.value

        // Then
        assertNotNull(result)
        assertEquals(2, result!!.size.toLong())
        // A more powerfull way to assert in a collection : it works with any order
        assertThat(
            result,
            containsInAnyOrder<Any>(
                hasProperty<Any>("type", `is`<String>("Flat")),
                hasProperty<Any>("type", `is`<String>("Business"))
            )
        )
        // Another more powerfull way to assert in a collection : every item has this property
        assertThat(
            result,
            everyItem(
                HasPropertyWithValue.hasProperty<PropertyUiModel>( // Need to explicit the generic type of this... Fuck Java.
                    "mainAddress",
                    `is`<String>("101 rue des Dames")
                )
            )
        )
    }

    @Test
    fun `should expose 2 properties for 2 addresses`() = runBlockingTest {
        // Given
        val addresses = listOf(
            Address(1, "101 rue des Dames"),
            Address(2, "31 rue Tronchet")
        )
        val mockedAddressDao = mock<AddressDao> {
            onBlocking { getAddressesAsSuspend() } doReturn addresses
        }

        val properties = listOf(
            Property(0, "Business", 1),
            Property(1, "Flat", 2)
        )
        val mockedPropertyDao = mock<PropertyDao> {
            onBlocking { getPropertiesAsSuspend() } doReturn properties
        }

        val viewModel = MainViewModel(mockedPropertyDao, mockedAddressDao)
        viewModel.doStuffOffMainThread()

        // When
        val result = viewModel.uiPropertiesLiveData.value

        // Then
        assertNotNull(result)
        assertEquals(2, result!!.size.toLong())
        // The most powerfull way to assert in a collection : it works with any order with complex items
        assertThat(
            result,
            containsInAnyOrder<Any>(
                allOf(
                    hasProperty("type", `is`("Flat")),
                    hasProperty("mainAddress", `is`("31 rue Tronchet"))
                ),
                allOf(
                    hasProperty("type", `is`("Business")),
                    hasProperty("mainAddress", `is`("101 rue des Dames"))
                )
            )
        )
    }
}