package com.example.dicodingevent.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dicodingevent.data.local.entity.Event

@Dao
interface EventDao {
    @Query("SELECT * FROM event ORDER BY beginTime DESC")
    suspend fun getAllEvent(): List<Event>

    @Query("SELECT COUNT(*) FROM event WHERE id = :id")
    suspend fun isEventExist(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(event: Event)

    @Query("DELETE FROM event WHERE id = :id")
    suspend fun deleteEvent(id: Int)
}