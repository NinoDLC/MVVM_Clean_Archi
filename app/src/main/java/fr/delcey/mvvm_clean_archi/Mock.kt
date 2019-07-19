package fr.delcey.mvvm_clean_archi

import kotlin.random.Random

class Mock {
    companion object {
        private val addresses = listOf(
            "10880 Malibu Point",
            "10236 Charing Cross Rd, Los Angeles, CA 90024, USA",
            "221b Baker St, Marylebone, London NW1 6XE, UK",
            "Hogwarts Castle, Highlands, Scotland, Great Britain",
            "1600 Pennsylvania Avenue, Washington DC"
        )

        private val types = listOf(
            "FLAT",
            "MANSION",
            "HOUSE",
            "GARAGE",
            "BUILDING"
        )

        fun getAddress(): String {
            return addresses[Random.nextInt(addresses.size)]
        }

        fun getType(): String {
            return types[Random.nextInt(types.size)]
        }
    }
}