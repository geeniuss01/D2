package me.samen.d2.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.samen.d2.data.entities.Thing

@Dao
abstract class ThingDao  {
    @Query("SELECT * FROM things")
    abstract fun all(): androidx.paging.DataSource.Factory<Int, Thing>

    @Insert
    abstract suspend fun ins(vararg thing: Thing): List<Long>

    @Query("SELECT * FROM things")
    abstract fun _all() : List<Thing>
}