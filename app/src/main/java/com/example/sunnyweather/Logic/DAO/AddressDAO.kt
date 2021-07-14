package com.example.sunnyweather.Logic.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sunnyweather.Address

@Dao
interface AddressDAO {

    @Insert
    fun insertAddress(address: Address):Long

    @Query("select * from Address")
    fun queryAll():List<Address>

    @Query("select * from Address where id=:id")
    fun queryById(id:Int):List<Address>

}