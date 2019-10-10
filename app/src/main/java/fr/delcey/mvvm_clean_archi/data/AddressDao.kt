package fr.delcey.mvvm_clean_archi.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AddressDao {

    // "suspend" keyword here is super important : it tells that the implementation of this function
    // should be a "suspend" function, thus giving the result (a list of address) instead of a Livedata
    // to "wire up" and wait for the result to come thought this Livedata.
    @Query("SELECT * FROM Address")
    suspend fun getAddressesAsSuspend(): List<Address>

    @Insert
    fun insertAddress(address: Address): Long
}