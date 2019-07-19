package fr.delcey.mvvm_clean_archi.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PropertyDao {

    @Query("SELECT * FROM Property")
    fun getProperties(): LiveData<List<Property>>

    @Insert
    fun insertProperty(property: Property): Long

    @Update
    fun updateProperty(property: Property)
}