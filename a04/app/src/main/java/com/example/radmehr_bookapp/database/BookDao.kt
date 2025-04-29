package com.example.radmehr_bookapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.radmehr_bookapp.Book

@Dao
interface BookDao {

    @Insert
    suspend fun insertBook(book: Book): Long
    // returns the new row ID

    @Update
    suspend fun updateBook(book: Book): Int
    // returns number of rows updated

    @Delete
    suspend fun deleteBook(book: Book): Int
    // returns number of rows deleted

    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<Book>
}