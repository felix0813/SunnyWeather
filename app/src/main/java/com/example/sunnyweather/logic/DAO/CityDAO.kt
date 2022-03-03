package com.example.sunnyweather.logic.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sunnyweather.City

@Dao
interface CityDAO {
    @Insert
    fun insertCity(city: City): Long

    @Query("update City set id = :id where name = :name")
    fun changeID(id: Int, name: String): Int

    @Query("select id from City where name= :name")
    fun queryID(name: String): Int

    @Query("select name from City where id = :id")
    fun queryName(id: Int): String

    @Query("select count(*) from City")
    fun queryCount(): Int

    @Query("select name from City")
    fun queryAllName(): List<String>

    @Query("select * from City where name like '%' ||:name || '%'")
    fun queryName(name: String): List<City>


}