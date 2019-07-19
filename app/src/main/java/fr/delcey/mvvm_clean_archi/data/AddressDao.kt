package fr.delcey.mvvm_clean_archi.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AddressDao {

    @Query("SELECT * FROM Address")
    fun getAddresses(): LiveData<List<Address>>

    @Insert
    fun insertAddress(address: Address): Long
}