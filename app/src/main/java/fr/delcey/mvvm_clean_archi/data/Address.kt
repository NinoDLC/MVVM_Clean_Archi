package fr.delcey.mvvm_clean_archi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Address(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val path: String
)