package com.example.sunnyweather

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class City(var name: String?, _id: Int?) {
    @PrimaryKey
    var id=_id

    constructor():this(null,null)
}
