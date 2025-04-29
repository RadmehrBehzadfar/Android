package com.example.radmehr_bookapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var author: String,
    var genre: String,
    var price: Double,
    var quantity: Int
)