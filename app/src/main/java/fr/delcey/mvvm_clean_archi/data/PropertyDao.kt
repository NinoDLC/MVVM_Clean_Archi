package fr.delcey.mvvm_clean_archi.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PropertyDao {

    // "suspend" keyword here is super important : it tells that the implementation of this function
    // should be a "suspend" function, thus giving the result (a list of address) instead of a Livedata
    // to "wire up" and wait for the result to come thought this Livedata.
    @Query("SELECT * FROM Property")
    suspend fun getPropertiesAsSuspend(): List<Property>

    @Insert
    fun insertProperty(property: Property): Long

    @Update
    fun updateProperty(property: Property)
}