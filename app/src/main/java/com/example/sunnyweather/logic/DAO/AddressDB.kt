package com.example.sunnyweather.logic.DAO

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sunnyweather.Address

@Database(version = 1, entities = [Address::class], exportSchema = false)
abstract class AddressDB : RoomDatabase() {
    abstract fun addressDAO(): AddressDAO

    companion object {
        private var instance: AddressDB? = null

        @Synchronized
        fun getAddressDB(context: Context): AddressDB {
            instance?.let { return it }
            return Room.databaseBuilder(
                context.applicationContext,
                AddressDB::class.java,
                "AddressDB"
            ).allowMainThreadQueries().build().apply { instance = this }
        }
    }
}