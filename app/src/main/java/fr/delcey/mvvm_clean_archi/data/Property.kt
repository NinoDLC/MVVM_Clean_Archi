package fr.delcey.mvvm_clean_archi.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [ForeignKey(
        entity = Address::class,
        parentColumns = ["id"],
        childColumns = ["addressId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Property(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val addressId: Long
)