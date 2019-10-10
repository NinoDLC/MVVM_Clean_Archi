package fr.delcey.mvvm_clean_archi.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.delcey.mvvm_clean_archi.MainApplication

@Database(entities = [Property::class, Address::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao
    abstract fun addressDao(): AddressDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            MainApplication.getApplication(),
                            AppDatabase::class.java, "Database.db"
                        ).build()
                    }
                }
            }

            return INSTANCE!!
        }
    }
}