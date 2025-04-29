package com.radmehr.roamto_radmehr.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "places")
data class Place(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val date: String,       // in yyyy‑MM‑dd form
    val address: String,    // store the original text
    val latitude: Double,
    val longitude: Double
) : Parcelable