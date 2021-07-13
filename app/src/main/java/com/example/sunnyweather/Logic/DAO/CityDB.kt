package com.example.sunnyweather.Logic.DAO

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sunnyweather.City

@Database(version=1,entities = [City::class],exportSchema = false)
abstract class CityDB:RoomDatabase() {
    abstract fun cityDao():CityDAO
    companion object{
        private var instance:CityDB?=null
        @Synchronized
        fun getDB(context:Context):CityDB{
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,CityDB::class.java,"cityDB").build().apply {
                instance=this
            }
        }
    }
}