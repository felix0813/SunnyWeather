package com.example.sunnyweather

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Address(_id: Int?) {
    @PrimaryKey
    var id = _id
    constructor():this(null)
}