package fr.delcey.mvvm_clean_archi.view.model

data class PropertyUiModel(
    val id : Int, // Shared between Property and Address
    val type : String, // Coming from Propery
    val mainAddress : String? = null // Coming from Address
)