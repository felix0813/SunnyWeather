package com.example.sunnyweather

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Address(_id: Int?, var x: Float?, var y: Float?) {
    @PrimaryKey
    var id = _id

    constructor() : this(null, null, null)
}