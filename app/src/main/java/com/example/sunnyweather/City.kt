package com.example.sunnyweather

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class City(var name: String?, _id: Int?) : Serializable {
    @PrimaryKey
    var id = _id


    constructor() : this(null, null)
}
