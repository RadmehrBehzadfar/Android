package com.radmehr.roamto_radmehr.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(place: Place): Long

    @Update
    fun update(place: Place): Int

    @Delete
    fun delete(place: Place): Int

    @Query("SELECT * FROM places ORDER BY id DESC")
    fun getAllPlaces(): LiveData<List<Place>>
}