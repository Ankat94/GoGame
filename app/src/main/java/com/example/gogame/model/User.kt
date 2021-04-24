package com.example.gogame.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class User(

        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var first: String = "",
        var last: String = "",
        var thumbnail: Bitmap?,
        var favourite: Boolean = false,
        var phone: String = "",
) {
    constructor() : this(0,"","",null,false,"")
}
