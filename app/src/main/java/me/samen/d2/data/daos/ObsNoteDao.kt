package me.samen.d2.data.daos

import androidx.room.Dao
import androidx.room.Query
import me.samen.d2.data.entities.ObsNote

@Dao
abstract class ObsNoteDao {
    @Query("SELECT * FROM ObsNote")
    abstract fun all(): androidx.paging.DataSource.Factory<Int, ObsNote>

    @Query("SELECT * FROM ObsNote WHERE `cont` like :q OR `fileName` like :q")
    abstract fun search(q: String): androidx.paging.DataSource.Factory<Int, ObsNote>
}